// Modèles du câble : gaine de verre (4 px) et filet de flux (1 px) visible seulement quand le câble est alimenté.
// Usage : node tools/models/cable.js
const fs = require("fs");
const path = require("path");

const ASSETS = path.join(__dirname, "../../src/main/resources/assets/resonantia");
const ALL = ["north", "south", "east", "west", "up", "down"];
const OPPOSITE = { north: "south", south: "north", east: "west", west: "east", up: "down", down: "up" };
// Le flux éclaire comme une source de lumière, quelle que soit l'ombre alentour
const GLOW = { block_light: 15, sky_light: 15, ambient_occlusion: false };

// Cube centré de largeur w, étendu jusqu'au bord dans la direction donnée (ou seulement le cœur)
function box(w, dir) {
  const lo = 8 - w / 2, hi = 8 + w / 2;
  const from = [lo, lo, lo], to = [hi, hi, hi];
  const axis = { east: 0, west: 0, up: 1, down: 1, south: 2, north: 2 }[dir];
  if (dir === "east" || dir === "up" || dir === "south") { from[axis] = hi; to[axis] = 16; }
  if (dir === "west" || dir === "down" || dir === "north") { from[axis] = 0; to[axis] = lo; }
  return { from, to };
}

function model(texture, w, dir, glow) {
  // Un bras n'a pas de face du côté du cœur, cachée de toute façon
  const faces = Object.fromEntries(ALL.filter((f) => !dir || f !== OPPOSITE[dir]).map((f) => [f, glow ? { texture: "#t", neoforge_data: GLOW } : { texture: "#t" }]));
  return {
    parent: "block/block",
    render_type: "minecraft:cutout",
    textures: { particle: "resonantia:block/cable_shell", t: `resonantia:block/${texture}` },
    elements: [{ ...box(w, dir), shade: !glow, faces }],
  };
}

const write = (rel, json) => {
  fs.writeFileSync(path.join(ASSETS, rel), JSON.stringify(json, null, 2) + "\n");
  console.log(rel);
};

const DIRS = ["north", "south", "east", "west", "up", "down"];
write("models/block/cable_core.json", model("cable_shell", 4));
write("models/block/cable_core_flux.json", model("cable_flux", 1, null, true));
for (const d of DIRS) {
  write(`models/block/cable_arm_${d}.json`, model("cable_shell", 4, d));
  write(`models/block/cable_arm_${d}_flux.json`, model("cable_flux", 1, d, true));
}

write("blockstates/cable.json", {
  multipart: [
    { apply: { model: "resonantia:block/cable_core" } },
    { when: { powered: "true" }, apply: { model: "resonantia:block/cable_core_flux" } },
    ...DIRS.flatMap((d) => [
      { when: { [d]: "true" }, apply: { model: `resonantia:block/cable_arm_${d}` } },
      { when: { [d]: "true", powered: "true" }, apply: { model: `resonantia:block/cable_arm_${d}_flux` } },
    ]),
  ],
});

// En main : un tronçon droit, alimenté
write("models/item/cable.json", {
  parent: "block/block",
  render_type: "minecraft:cutout",
  textures: { particle: "resonantia:block/cable_shell", shell: "resonantia:block/cable_shell", flux: "resonantia:block/cable_flux" },
  elements: [
    { from: [0, 6, 6], to: [16, 10, 10], faces: Object.fromEntries(ALL.map((f) => [f, { texture: "#shell" }])) },
    { from: [0, 7.5, 7.5], to: [16, 8.5, 8.5], shade: false, faces: Object.fromEntries(ALL.map((f) => [f, { texture: "#flux", neoforge_data: GLOW }])) },
  ],
});
