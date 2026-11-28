// Lecture et écriture PNG minimales (RGBA 8 bits), sans dépendance
const fs = require("fs");
const zlib = require("zlib");

const CRC_TABLE = Array.from({ length: 256 }, (_, n) => {
  let c = n;
  for (let k = 0; k < 8; k++) c = c & 1 ? 0xedb88320 ^ (c >>> 1) : c >>> 1;
  return c >>> 0;
});
function crc32(buf) {
  let c = 0xffffffff;
  for (const b of buf) c = CRC_TABLE[(c ^ b) & 0xff] ^ (c >>> 8);
  return (c ^ 0xffffffff) >>> 0;
}
function chunk(type, data) {
  const len = Buffer.alloc(4);
  len.writeUInt32BE(data.length);
  const td = Buffer.concat([Buffer.from(type), data]);
  const crc = Buffer.alloc(4);
  crc.writeUInt32BE(crc32(td));
  return Buffer.concat([len, td, crc]);
}

// img = { w, h, px: Uint8Array(w*h*4) }
function write(file, img) {
  const { w, h, px } = img;
  const raw = Buffer.alloc((w * 4 + 1) * h);
  for (let y = 0; y < h; y++) {
    raw[y * (w * 4 + 1)] = 0;
    Buffer.from(px.buffer, px.byteOffset + y * w * 4, w * 4).copy(raw, y * (w * 4 + 1) + 1);
  }
  const ihdr = Buffer.alloc(13);
  ihdr.writeUInt32BE(w, 0);
  ihdr.writeUInt32BE(h, 4);
  ihdr[8] = 8;
  ihdr[9] = 6;
  fs.writeFileSync(file, Buffer.concat([
    Buffer.from([0x89, 0x50, 0x4e, 0x47, 0x0d, 0x0a, 0x1a, 0x0a]),
    chunk("IHDR", ihdr),
    chunk("IDAT", zlib.deflateSync(raw, { level: 9 })),
    chunk("IEND", Buffer.alloc(0)),
  ]));
}

function read(file) {
  const buf = fs.readFileSync(file);
  let pos = 8, w, h, depth, type, palette, trns;
  const idat = [];
  while (pos < buf.length) {
    const len = buf.readUInt32BE(pos);
    const t = buf.toString("ascii", pos + 4, pos + 8);
    const d = buf.subarray(pos + 8, pos + 8 + len);
    if (t === "IHDR") { w = d.readUInt32BE(0); h = d.readUInt32BE(4); depth = d[8]; type = d[9]; }
    else if (t === "PLTE") palette = d;
    else if (t === "tRNS") trns = d;
    else if (t === "IDAT") idat.push(d);
    pos += 12 + len;
  }
  const ch0 = { 0: 1, 2: 3, 3: 1, 4: 2, 6: 4 }[type];
  if (depth !== 8 && !(depth < 8 && ch0 === 1)) throw new Error(`${file} : profondeur ${depth} non gérée`);
  const stride = Math.ceil((w * ch0 * depth) / 8);
  const ch = Math.max(1, (ch0 * depth) / 8); // octets par pixel pour les filtres
  const data = zlib.inflateSync(Buffer.concat(idat));
  const out = Buffer.alloc(stride * h);
  for (let y = 0; y < h; y++) {
    const f = data[y * (stride + 1)];
    for (let x = 0; x < stride; x++) {
      const v = data[y * (stride + 1) + 1 + x];
      const a = x >= ch ? out[y * stride + x - ch] : 0;
      const b = y > 0 ? out[(y - 1) * stride + x] : 0;
      const c = x >= ch && y > 0 ? out[(y - 1) * stride + x - ch] : 0;
      let p = v;
      if (f === 1) p = v + a;
      else if (f === 2) p = v + b;
      else if (f === 3) p = v + ((a + b) >> 1);
      else if (f === 4) {
        const q = a + b - c, pa = Math.abs(q - a), pb = Math.abs(q - b), pc = Math.abs(q - c);
        p = v + (pa <= pb && pa <= pc ? a : pb <= pc ? b : c);
      }
      out[y * stride + x] = p & 0xff;
    }
  }
  // Profondeur < 8 : on déplie les échantillons en un octet chacun
  let rawOut = out;
  if (depth < 8) {
    rawOut = Buffer.alloc(w * h);
    const max = (1 << depth) - 1;
    for (let y = 0; y < h; y++)
      for (let x = 0; x < w; x++) {
        const bit = x * depth;
        const v = (out[y * stride + (bit >> 3)] >> (8 - depth - (bit & 7))) & max;
        rawOut[y * w + x] = type === 0 ? Math.round((v * 255) / max) : v;
      }
  }
  const px = new Uint8Array(w * h * 4);
  for (let i = 0; i < w * h; i++) {
    const out = rawOut;
    let r, g, b, a = 255;
    if (type === 6) [r, g, b, a] = out.subarray(i * 4, i * 4 + 4);
    else if (type === 2) [r, g, b] = out.subarray(i * 3, i * 3 + 3);
    else if (type === 0) r = g = b = out[i];
    else if (type === 4) { r = g = b = out[i * 2]; a = out[i * 2 + 1]; }
    else { const k = out[i]; [r, g, b] = palette.subarray(k * 3, k * 3 + 3); a = trns && k < trns.length ? trns[k] : 255; }
    px.set([r, g, b, a], i * 4);
  }
  return { w, h, px };
}

module.exports = { read, write };
