// Modèles 3D des machines (façade au nord). Usage : node tools/models/machines.js
// Une face sans uv prend l'uv automatique de Minecraft (position du cube).
const fs = require("fs");
const path = require("path");

const OUT = path.join(__dirname, "../../src/main/resources/assets/resonantia/models/block");
const ALL = ["north", "south", "east", "west", "up", "down"];

// Cube dont toutes les faces prennent la même zone de texture
const flat = (name, from, to, tex, uv) => ({ name, from, to, tex, uv: Object.fromEntries(ALL.map((f) => [f, uv])) });

const posts = (top) => [
  { name: "montant_no", from: [0, 2, 0], to: [2, top, 2], tex: "trim" },
  { name: "montant_ne", from: [14, 2, 0], to: [16, top, 2], tex: "trim" },
  { name: "montant_so", from: [0, 2, 14], to: [2, top, 16], tex: "trim" },
  { name: "montant_se", from: [14, 2, 14], to: [16, top, 16], tex: "trim" },
];
const base = { name: "socle", from: [0, 0, 0], to: [16, 2, 16], tex: "casing" };

const MACHINES = {
  concasseur: {
    textures: { casing: "machine_casing", trim: "machine_trim", screen: "concasseur_screen", rollers: "concasseur_rollers", light: "concasseur_light" },
    elements: [
      base,
      { name: "corps", from: [1, 2, 1], to: [15, 13, 15], tex: "casing" },
      { name: "tremie_nord", from: [2, 13, 2], to: [14, 16, 3], tex: "casing" },
      { name: "tremie_sud", from: [2, 13, 13], to: [14, 16, 14], tex: "casing" },
      { name: "tremie_ouest", from: [2, 13, 3], to: [3, 16, 13], tex: "casing" },
      { name: "tremie_est", from: [13, 13, 3], to: [14, 16, 13], tex: "casing" },
      { name: "piston_est", from: [15, 4, 5], to: [16, 10, 11], tex: "casing" },
      { name: "piston_ouest", from: [0, 4, 5], to: [1, 10, 11], tex: "casing" },
      { name: "cadre_ecran", from: [2, 7, 0.5], to: [14, 13, 1], tex: "casing" },
      ...posts(14),
      { name: "ecran", from: [3, 8, 0.25], to: [13, 12, 0.5], tex: "screen",
        uv: { north: [3, 6, 13, 10], east: [0, 0, 1, 4], west: [0, 0, 1, 4], up: [0, 0, 10, 1], down: [0, 0, 10, 1] } },
      { name: "bouche", from: [3, 3, 0.75], to: [13, 6, 1], tex: "rollers",
        uv: { north: [3, 0, 13, 3], east: [0, 3, 1, 4], west: [0, 3, 1, 4], up: [0, 3, 10, 4], down: [0, 3, 10, 4] } },
      { name: "rouleaux", from: [3, 13, 3], to: [13, 13.5, 13], tex: "rollers" },
      flat("voyant", [5, 6.25, 0.5], [11, 6.75, 1], "light", [4, 4, 6, 6]),
      flat("voyant_est", [16, 6, 7], [16.25, 8, 9], "light", [4, 4, 6, 6]),
      flat("voyant_ouest", [-0.25, 6, 7], [0, 8, 9], "light", [4, 4, 6, 6]),
    ],
  },

  generator: {
    textures: { casing: "machine_casing", trim: "machine_trim", fire: "generator_fire", light: "generator_light" },
    elements: [
      base,
      { name: "corps", from: [1, 2, 1], to: [15, 12, 15], tex: "casing" },
      { name: "plateau", from: [2, 12, 2], to: [14, 13, 14], tex: "casing" },
      ...posts(13),
      { name: "cadre_foyer", from: [3, 3, 0.5], to: [13, 9, 1], tex: "casing" },
      { name: "foyer", from: [4, 4, 0.25], to: [12, 8, 0.5], tex: "fire",
        uv: { north: [4, 8, 12, 12], east: [0, 0, 1, 4], west: [0, 0, 1, 4], up: [0, 0, 8, 1], down: [0, 0, 8, 1] } },
      flat("jauge", [4, 10, 0.5], [12, 11, 1], "light", [4, 4, 6, 6]),
      { name: "cheminee", from: [9, 13, 9], to: [13, 16, 13], tex: "casing" },
      { name: "cheminee_bord", from: [8.5, 15, 8.5], to: [13.5, 16, 13.5], tex: "casing" },
      { name: "cheminee_fond", from: [9.5, 15.25, 9.5], to: [12.5, 16.25, 12.5], tex: "fire",
        uv: { up: [0, 0, 3, 3], north: [0, 0, 3, 1], south: [0, 0, 3, 1], east: [0, 0, 3, 1], west: [0, 0, 3, 1], down: [0, 0, 3, 3] } },
      { name: "bobine", from: [3, 13, 3], to: [7, 14, 7], tex: "casing" },
      flat("bobine_coeur", [4, 14, 4], [6, 16, 6], "light", [4, 4, 6, 6]),
      ...[3, 6, 9, 12].flatMap((z) => [
        { name: `ailette_est_${z}`, from: [15, 4, z], to: [16, 10, z + 1], tex: "trim" },
        { name: `ailette_ouest_${z}`, from: [0, 4, z], to: [1, 10, z + 1], tex: "trim" },
      ]),
    ],
  },

  assembler: {
    textures: { casing: "machine_casing", trim: "machine_trim", screen: "assembler_screen", light: "assembler_light" },
    elements: [
      base,
      { name: "corps", from: [1, 2, 1], to: [15, 9, 15], tex: "casing" },
      { name: "plan_de_travail", from: [1, 9, 1], to: [15, 10, 15], tex: "trim" },
      ...posts(10),
      { name: "cadre_ecran", from: [3, 3, 0.5], to: [13, 8, 1], tex: "casing" },
      { name: "ecran", from: [4, 4, 0.25], to: [12, 7, 0.5], tex: "screen",
        uv: { north: [4, 6, 12, 9], east: [0, 0, 1, 3], west: [0, 0, 1, 3], up: [0, 0, 8, 1], down: [0, 0, 8, 1] } },
      { name: "enclume", from: [5, 10, 5], to: [11, 11, 11], tex: "casing" },
      flat("enclume_coeur", [6, 11, 6], [10, 11.25, 10], "light", [4, 4, 8, 8]),
      { name: "pilier_ouest", from: [1, 10, 12], to: [3, 16, 14], tex: "trim" },
      { name: "pilier_est", from: [13, 10, 12], to: [15, 16, 14], tex: "trim" },
      { name: "traverse", from: [3, 14, 12], to: [13, 16, 14], tex: "casing" },
      { name: "bras", from: [7, 14.5, 6], to: [9, 15.5, 12], tex: "casing" },
      { name: "tete", from: [7, 12.5, 6], to: [9, 14.5, 8], tex: "casing" },
      flat("pointe", [7.5, 11.5, 6.5], [8.5, 12.5, 7.5], "light", [4, 4, 5, 5]),
    ],
  },
};

function toJson(m) {
  const textures = { particle: `resonantia:block/${m.textures.casing}` };
  for (const [k, v] of Object.entries(m.textures)) textures[k] = `resonantia:block/${v}`;
  const elements = m.elements.map((e) => ({
    name: e.name,
    from: e.from,
    to: e.to,
    faces: Object.fromEntries(ALL.map((f) => [f, e.uv?.[f] ? { uv: e.uv[f], texture: `#${e.tex}` } : { texture: `#${e.tex}` }])),
  }));
  return { parent: "block/block", textures, elements };
}

if (require.main === module) {
  for (const [name, m] of Object.entries(MACHINES)) {
    fs.writeFileSync(path.join(OUT, `${name}.json`), JSON.stringify(toJson(m), null, 2) + "\n");
    console.log(`models/block/${name}.json (${m.elements.length} cubes)`);
  }
}
module.exports = MACHINES;
