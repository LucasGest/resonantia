// Interfaces des machines (200×166 : fond 176×166 + sprites de progression à x = 176).
// Usage : node tools/textures/gui.js [--out <dossier>]
const path = require("path");
const png = require("./png");

const args = process.argv.slice(2);
const outIdx = args.indexOf("--out");
const OUT = outIdx >= 0 ? args[outIdx + 1] : path.join(__dirname, "../../src/main/resources/assets/resonantia/textures/gui/container");

const hex = (h) => [parseInt(h.slice(1, 3), 16), parseInt(h.slice(3, 5), 16), parseInt(h.slice(5, 7), 16), 255];
const C = {
  K: "#14171c", // contour
  S: "#262b33", // ombre
  s: "#343a43", // panneau
  p: "#2d323a", // panneau, trame
  L: "#4f5762", // arête claire
  R: "#9aa3ae", // rivet
  slot: "#1d2128",
  slotHi: "#5a636f",
  slotLo: "#101318",
  flux: ["#1d6b58", "#2fa587", "#3fcfa8", "#8ff0d6"],
};
const ACCENT = {
  concasseur: ["#7a4a12", "#d98a2b", "#ffcf73"],
  generator: ["#6e1f12", "#d4502a", "#ffa05a"],
  assembler: ["#155a63", "#2fb6c4", "#9ff3ff"],
};

function canvas(w, h) {
  const px = new Uint8Array(w * h * 4);
  const set = (x, y, c) => { if (x >= 0 && y >= 0 && x < w && y < h) px.set(typeof c === "string" ? hex(c) : c, (y * w + x) * 4); };
  const rect = (x, y, rw, rh, c) => { for (let j = 0; j < rh; j++) for (let i = 0; i < rw; i++) set(x + i, y + j, c); };
  return { w, h, px, set, rect };
}

// Creux biseauté : ombre en haut à gauche, lumière en bas à droite
function inset(g, x, y, w, h, fill) {
  g.rect(x, y, w, h, fill);
  g.rect(x, y, w - 1, 1, C.slotLo);
  g.rect(x, y, 1, h - 1, C.slotLo);
  g.rect(x + 1, y + h - 1, w - 1, 1, C.slotHi);
  g.rect(x + w - 1, y + 1, 1, h - 1, C.slotHi);
}
// Case d'objet : l'objet est dessiné en (sx, sy), la case fait 18×18 autour
const slot = (g, sx, sy) => inset(g, sx - 1, sy - 1, 18, 18, C.slot);
// Grande case de sortie, à la manière du four
function bigSlot(g, sx, sy, accent) {
  inset(g, sx - 5, sy - 5, 26, 26, C.slot);
  g.rect(sx - 4, sy - 4, 24, 1, accent[0]);
  g.rect(sx - 4, sy + 19, 24, 1, accent[0]);
}
const rivet = (g, x, y) => { g.set(x, y, C.R); g.set(x + 1, y + 1, C.S); };

function panel(g, accent) {
  const W = 176, H = 166;
  g.rect(0, 0, W, H, C.s);
  // trame légère, une ligne sur deux
  for (let y = 2; y < H - 2; y += 2) for (let x = 2; x < W - 2; x++) if ((x + y) % 4 === 0) g.set(x, y, C.p);
  // contour et biseau, coins arrondis
  g.rect(1, 0, W - 2, 1, C.K); g.rect(1, H - 1, W - 2, 1, C.K);
  g.rect(0, 1, 1, H - 2, C.K); g.rect(W - 1, 1, 1, H - 2, C.K);
  g.rect(1, 1, W - 2, 1, C.L); g.rect(1, 1, 1, H - 2, C.L);
  g.rect(2, H - 2, W - 3, 1, C.S); g.rect(W - 2, 2, 1, H - 3, C.S);
  for (const [x, y] of [[0, 0], [W - 1, 0], [0, H - 1], [W - 1, H - 1]]) g.set(x, y, [0, 0, 0, 0]);
  for (const [x, y] of [[3, 3], [W - 5, 3], [3, H - 5], [W - 5, H - 5]]) rivet(g, x, y);
  // filet de couleur sous le titre, plus vif au début
  g.rect(7, 15, 24, 1, accent[1]);
  g.rect(31, 15, 105, 1, accent[0]);
  // séparation avant l'inventaire
  g.rect(7, 79, W - 14, 1, C.S);
  g.rect(7, 80, W - 14, 1, C.L);
  // inventaire du joueur
  for (let r = 0; r < 3; r++) for (let c = 0; c < 9; c++) slot(g, 8 + c * 18, 84 + r * 18);
  for (let c = 0; c < 9; c++) slot(g, 8 + c * 18, 142);
}

// Jauge d'énergie : creux en (151,16) 16×54, remplissage 14×52 à (176,20)
function energy(g) {
  inset(g, 151, 16, 16, 54, C.slot);
  for (let y = 18; y < 68; y += 6) g.rect(152, y, 3, 1, C.slotHi); // graduations
  for (let y = 0; y < 52; y++) for (let x = 0; x < 14; x++) {
    const band = x === 0 ? 0 : x === 13 ? 0 : x < 4 ? 2 : x < 9 ? 1 : 2;
    const glint = (y + x) % 9 === 0 ? 3 : band;
    g.set(176 + x, 20 + y, C.flux[glint]);
  }
}

// Flèche de progression 24×17 : vide dans le panneau, pleine à (176,0)
const ARROW = [
  "...............##.......",
  "...............###......",
  "...............####.....",
  "...............#####....",
  "...............######...",
  "################.######.",
  "#################.######",
  "##################.#####",
  "###################.####",
  "##################.#####",
  "#################.######",
  "################.######.",
  "...............######...",
  "...............#####....",
  "...............####.....",
  "...............###......",
  "...............##.......",
];
function arrow(g, x, y, colorAt) {
  ARROW.forEach((row, j) => [...row].forEach((c, i) => { if (c === "#") g.set(x + i, y + j, colorAt(i, j)); }));
}

// Flamme 14×14 : vide dans le panneau, pleine à (176,0)
const FLAME = [
  "......##......",
  ".....####.....",
  ".....####.....",
  "....######....",
  "...#######....",
  "...########...",
  "..##########..",
  "..####..####..",
  ".####....####.",
  ".###......###.",
  "####..##..####",
  "####.####.####",
  ".############.",
  "..##########..",
];
function flame(g, x, y, colorAt) {
  FLAME.forEach((row, j) => [...row].forEach((c, i) => { if (c === "#") g.set(x + i, y + j, colorAt(i, j)); }));
}

const BUILD = {
  concasseur(g, a) {
    slot(g, 8, 17); // batterie
    slot(g, 56, 35);
    bigSlot(g, 116, 35, a);
    arrow(g, 79, 34, () => C.slotLo);
    arrow(g, 176, 0, (i) => (i < 8 ? a[0] : i < 16 ? a[1] : a[2]));
    energy(g);
  },
  generator(g, a) {
    slot(g, 56, 35); // combustible
    slot(g, 116, 35); // batterie à charger
    flame(g, 81, 36, () => C.slotLo);
    flame(g, 176, 0, (i, j) => (j < 5 ? a[2] : j < 10 ? a[1] : a[0]));
    energy(g);
  },
  assembler(g, a) {
    slot(g, 8, 17); // batterie
    for (const y of [17, 35, 53]) slot(g, 44, y);
    // accolade qui relie les trois ingrédients à la flèche
    g.rect(62, 25, 1, 37, a[0]);
    g.rect(62, 43, 16, 1, a[0]);
    bigSlot(g, 116, 35, a);
    arrow(g, 79, 34, () => C.slotLo);
    arrow(g, 176, 0, (i) => (i < 8 ? a[0] : i < 16 ? a[1] : a[2]));
    energy(g);
  },
};

for (const [name, build] of Object.entries(BUILD)) {
  const g = canvas(200, 166);
  panel(g, ACCENT[name]);
  build(g, ACCENT[name]);
  png.write(path.join(OUT, `${name}.png`), g);
  console.log(`gui/container/${name}.png`);
}
