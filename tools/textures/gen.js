// Génère les textures de Resonantia à partir de grilles dessinées à la main (16×16).
// Usage : node tools/textures/gen.js <dossier des textures vanilla> [--preview <fichier.png>] [--out <dossier>]
// Le dossier vanilla contient stone.png, netherrack.png, end_stone.png (extraits du jar client, non versionnés).
const path = require("path");
const png = require("./png");
const designs = require("./designs");

const args = process.argv.slice(2);
const vanillaDir = args[0];
const opt = (k) => { const i = args.indexOf(k); return i >= 0 ? args[i + 1] : null; };
const outDir = opt("--out") || path.join(__dirname, "../../src/main/resources/assets/resonantia/textures");
const previewFile = opt("--preview");
if (!vanillaDir) { console.error("Dossier vanilla manquant"); process.exit(1); }

const hex = (h) => [parseInt(h.slice(1, 3), 16), parseInt(h.slice(3, 5), 16), parseInt(h.slice(5, 7), 16), h.length > 7 ? parseInt(h.slice(7, 9), 16) : 255];
const mix = (a, b, t) => a.map((v, i) => (i === 3 ? 255 : Math.round(v * (1 - t) + b[i] * t)));

function render(d) {
  const base = d.base ? png.read(path.join(vanillaDir, d.base + ".png")) : null;
  const px = new Uint8Array(16 * 16 * 4);
  d.grid.forEach((row, y) => {
    if (row.length !== 16) throw new Error(`${d.name} : ligne ${y} fait ${row.length} caractères`);
    [...row].forEach((c, x) => {
      const i = (y * 16 + x) * 4;
      const under = base ? [...base.px.subarray(i, i + 4)] : [0, 0, 0, 0];
      let col = under;
      const p = d.palette[c];
      if (p && p.tint) col = mix(under, hex(p.tint), p.amount);
      else if (p) col = hex(p);
      else if (c !== ".") throw new Error(`${d.name} : couleur « ${c} » inconnue`);
      px.set(col, i);
    });
  });
  return { w: 16, h: 16, px };
}

// Texture animée : une colonne de `frames` images 16×16, plus son fichier .mcmeta
function renderAnimated(d) {
  const px = new Uint8Array(16 * 16 * d.frames * 4);
  for (let f = 0; f < d.frames; f++)
    for (let y = 0; y < 16; y++)
      for (let x = 0; x < 16; x++) px.set(hex(d.pixel(x, y, f)), ((f * 16 + y) * 16 + x) * 4);
  return { w: 16, h: 16 * d.frames, px };
}

const fs = require("fs");
const done = [];
for (const d of designs) {
  const file = path.join(outDir, d.name + ".png");
  fs.mkdirSync(path.dirname(file), { recursive: true });
  const img = d.frames ? renderAnimated(d) : render(d);
  png.write(file, img);
  if (d.frames > 1) fs.writeFileSync(file + ".mcmeta", JSON.stringify({ animation: { frametime: d.frametime } }, null, 2) + "\n");
  done.push({ name: d.name, img });
}
console.log(`${done.length} textures écrites dans ${outDir}`);

// Planche d'aperçu : chaque texture agrandie ×8, sur fond gris
if (previewFile) {
  const S = 6, cell = 16 * S + 12, cols = Math.min(done.length, 8), rows = Math.ceil(done.length / cols);
  const W = cols * cell, H = rows * cell;
  const px = new Uint8Array(W * H * 4);
  for (let i = 0; i < W * H; i++) px.set([58, 60, 66, 255], i * 4);
  done.forEach(({ img }, n) => {
    const ox = (n % cols) * cell + 8, oy = Math.floor(n / cols) * cell + 8;
    for (let y = 0; y < 16 * S; y++) for (let x = 0; x < 16 * S; x++) {
      const si = (Math.floor(y / S) * 16 + Math.floor(x / S)) * 4;
      const a = img.px[si + 3] / 255;
      const di = ((oy + y) * W + ox + x) * 4;
      for (let k = 0; k < 3; k++) px[di + k] = Math.round(img.px[si + k] * a + px[di + k] * (1 - a));
    }
  });
  png.write(previewFile, { w: W, h: H, px });
  console.log("Aperçu : " + previewFile);
}
