// Dessins 16×16. « . » = pixel de la texture de base (ou transparent pour un item).
// Une couleur { tint, amount } teinte le pixel de base au lieu de le remplacer.

// Palettes des minerais : o contour, d sombre, m moyen, l clair, h éclat, r onde (teinte sur la pierre)
const ORES = {
  chrysolithe: { o: "#1f5233", d: "#2d7445", m: "#3f9a57", l: "#7fdc8c", h: "#d8ffdc", r: { tint: "#7fdc8c", amount: 0.3 } },
  ferrite: { o: "#0e1014", d: "#22262e", m: "#363d4a", l: "#6d7f9c", h: "#b9d4ff", r: { tint: "#6d7f9c", amount: 0.25 } },
  sylvanite: { o: "#2b3a1f", d: "#45592c", m: "#6b7f3d", l: "#a7a35a", h: "#e0d58c", r: { tint: "#a7a35a", amount: 0.18 } },
  cindrite: { o: "#3a0d07", d: "#7a1e0c", m: "#c4421a", l: "#ff8a3d", h: "#ffe08a", r: { tint: "#ff8a3d", amount: 0.3 } },
  nebulite: { o: "#24103f", d: "#43207a", m: "#7442c4", l: "#b07cff", h: "#9ff6ff", r: { tint: "#b07cff", amount: 0.3 } },
};

// Boîtier des machines : K contour, S ombre, s caisse, L arête claire, R rivet, g vitre
const CASE = { K: "#1b1e24", S: "#2c3139", s: "#3a4049", L: "#565e6a", R: "#9aa3ae", g: "#0f1216" };
// Couleur de fonction de chaque machine : b sombre, a moyenne, A vive
const ACCENT = {
  concasseur: { b: "#7a4a12", a: "#d98a2b", A: "#ffcf73" },
  generator: { b: "#6e1f12", a: "#d4502a", A: "#ffa05a" },
  assembler: { b: "#155a63", a: "#2fb6c4", A: "#9ff3ff" },
};
// Objets : x contour, L acier clair, S acier sombre, s acier, B laiton, t flux, T flux vif, w manche, W manche sombre
const ITEM = { x: "#1b1e24", L: "#9aa3ae", S: "#565e6a", s: "#3a4049", B: "#c9a25a", t: "#3fcfa8", T: "#c8fff0", w: "#6b4a2b", W: "#3e2a17" };

const rep = (rows, n) => Array.from({ length: n }, (_, i) => rows[i % rows.length]);

// Chaque minerai a sa propre forme de gisement
const ORE_GRIDS = {
  // Cristaux nets, avec une onde autour du plus gros
  chrysolithe: [
    "................",
    "...rrrr.........",
    "..r.oo.r....oo..",
    ".r.omlo.r..omlo.",
    ".r.omhlor..ommo.",
    ".r..omo.r...oo..",
    "..r..o.r........",
    "...rrrr.........",
    ".........oo.....",
    "........omlo....",
    ".......omhlmo...",
    ".......ommmmo...",
    "..oo....omo...o.",
    ".omlo....o...omo",
    ".ommo.........o.",
    "..oo............",
  ],
  // Paillettes alignées en diagonale, comme des lignes de champ
  ferrite: [
    "..........r.....",
    ".oo.......r.....",
    "omlo.....r..oo..",
    ".ohlo....r.omlo.",
    "..omdo..r...ohlo",
    "...odo..r....omo",
    "....o..r......o.",
    "......r.........",
    ".....r....oo....",
    "....r....omlo...",
    "...r......ohlo..",
    "..r........omdo.",
    ".oo.........odo.",
    "omlo.........o..",
    ".ohlo...........",
    "..oo............",
  ],
  // Veines fines et discrètes, presque camouflées
  sylvanite: [
    "................",
    "................",
    "....r...........",
    "...rmd......r...",
    "....dlm....rdm..",
    ".....md.....ml..",
    "......d......d..",
    "................",
    "..........r.....",
    "..r......rmd....",
    ".rdm......dl....",
    "..lm.......md...",
    "...d........d...",
    "................",
    "......rmd.......",
    ".......dl.......",
  ],
  // Braises : cœur incandescent, bords noircis
  cindrite: [
    "................",
    "..oo.......ooo..",
    ".omdo.....omldo.",
    ".odho....omhhlo.",
    "..oo.....odlldo.",
    "..........oddo..",
    "...........oo...",
    "....ooo.........",
    "...omllo........",
    "..omhhlmo....oo.",
    "..odllldo...omdo",
    "...odddo....odho",
    "....ooo......oo.",
    "........oo......",
    ".......odho.....",
    "........oo......",
  ],
  // Éclats d'étoiles en croix, façon nébuleuse
  nebulite: [
    "................",
    "....r.......m...",
    "...rlr.....mhm..",
    "..rlhlr.....m...",
    "...rlr..........",
    "....r.....r.....",
    "..........l.....",
    "........rlhlr...",
    "..m.......l.....",
    ".mhm......r.....",
    "..m.............",
    ".......m.....r..",
    "......mhm...rlr.",
    ".......m...rlhlr",
    ".............rlr",
    "..............r.",
  ],
};

const ore = (name, base) => ({ name: `block/${name}_ore`, base, palette: ORES[name], grid: ORE_GRIDS[name] });

const storage = (name) => ({
  name: `block/${name}_block`,
  palette: ORES[name],
  grid: [
    "oooooooooooooooo",
    "ohllllllllllllmo",
    "olmmmdmmmmmmlmdo",
    "olmmmmmmmdmmmmdo",
    "olmdmmllllmmmmdo",
    "olmmmlmmmmlmmmdo",
    "olmmlmmhhmmlmddo",
    "olmmlmhmmhmlmmdo",
    "oldmlmhmmhmlmmdo",
    "olmmlmmhhmmlmmdo",
    "olmmmlmmmmlmmmdo",
    "olmmmmllllmmdmdo",
    "olmmmmmmmmmmmmdo",
    "olmmdmmmmmlmmmdo",
    "omdddddddddddddo",
    "oooooooooooooooo",
  ],
});

const fragment = (name) => ({
  name: `item/fragment_${name}`,
  palette: ORES[name],
  grid: [
    "................",
    "..........ooo...",
    ".........ohhlo..",
    "........ohllmo..",
    ".......ohllmmo..",
    "......ohllmmo...",
    ".....ohllmmo....",
    "....ohllmmo.....",
    "...olllmmo......",
    "...ollmmdo..ooo.",
    "..olmmmdo..ohlo.",
    "..olmmdo..ohmdo.",
    "..ommdo...omddo.",
    "...odo.....ooo..",
    "....o...........",
    "................",
  ],
});

const raw = (name) => ({
  name: `item/raw_${name}`,
  palette: ORES[name],
  grid: [
    "................",
    "................",
    "................",
    ".....ooo........",
    "....olhlo.oo....",
    "...olhllooldo...",
    "...ollmlolhlmo..",
    "..ollmmllllmmo..",
    "..olmmmllmmmdo..",
    "..ommmdmmmmdo...",
    "...odmmddmmo....",
    "....oddooddo....",
    ".....oo..oo.....",
    "................",
    "................",
    "................",
  ],
});

const WAVE = ["gggAAggggggAAggg", "ggAggAggggAggAgg", "gAggggAggAggggAg", "AggggggAAggggggA"];

const machineCommon = [
  {
    name: "block/machine_casing",
    palette: CASE,
    grid: [
      "ssssLssssssSssss",
      "sssssssssssssssS",
      "sSssssssLsssssss",
      "ssssssssssssssss",
      "sssssSsssssssLss",
      "ssLsssssssssssss",
      "ssssssssSsssssss",
      "ssssssssssssssss",
      "sssSssssssssLsss",
      "ssssssLsssssssss",
      "ssssssssssssSsss",
      "sLssssssssssssss",
      "sssssssSssssssss",
      "ssssssssssLsssSs",
      "ssSsssssssssssss",
      "ssssssssssssssss",
    ],
  },
  { name: "block/machine_trim", palette: CASE, grid: rep(["LSLSLSLSLSLSLSLS", "LSLSLSLSLSLSLSLS", "LSLSLSLSLSLSLSLS", "RKRKRKRKRKRKRKRK"], 16) },
];

const light = (name) => ({ name: `block/${name}_light`, palette: ACCENT[name], grid: rep(["aAAAAAAAAAAAAAAa"], 16) });
const screen = (name) => ({
  name: `block/${name}_screen`,
  palette: { ...CASE, ...ACCENT[name] },
  grid: [...rep(["gggggggggggggggg"], 6), ...WAVE, ...rep(["gggggggggggggggg"], 6)],
});

const machines = [
  ...machineCommon,
  screen("concasseur"),
  { name: "block/concasseur_rollers", palette: CASE, grid: rep(["gRgRgRgRgRgRgRgR", "RLRLRLRLRLRLRLRL", "SLSLSLSLSLSLSLSL", "gSgSgSgSgSgSgSgS"], 16) },
  light("concasseur"),
  {
    name: "block/generator_fire",
    palette: { ...CASE, ...ACCENT.generator },
    grid: [
      ...rep(["gggggggggggggggg"], 8),
      "KbKbKaKbKbKaKbKb",
      "KaKaKAKaKaKAKaKa",
      "KAKAKAKAKAKAKAKA",
      "KAKAKAKAKAKAKAKA",
      ...rep(["gggggggggggggggg"], 4),
    ],
  },
  light("generator"),
  screen("assembler"),
  light("assembler"),
];

// Câble : tube de verre de 4 px (de 6 à 9) avec un filet de flux de 2 px (7 et 8) à l'intérieur.
// Minecraft découpe la texture selon la position : la bande horizontale habille les bras est-ouest,
// la bande verticale les autres, et leur croisement le cœur.
const CABLE = { ...CASE, G: "#cfe9f0", _: "#00000000", f: "#1f8f74", t: ITEM.t, T: ITEM.T };
const GLASS = ["L", "_", "_", "_"]; // une seule arête, le reste en verre
const COLLAR = ["L", "R", "R", "S"]; // collier d'acier tous les 8 px
const band = (x, y, lo, hi) => {
  const inRow = y >= lo && y <= hi, inCol = x >= lo && x <= hi;
  if (inRow && inCol) return { core: true, k: Math.min(x - lo, y - lo, hi - x, hi - y) };
  if (inRow) return { k: y - lo, pos: x };
  if (inCol) return { k: x - lo, pos: y };
  return null;
};
const cableShell = {
  name: "block/cable_shell",
  frames: 1,
  pixel(x, y) {
    const b = band(x, y, 6, 9);
    if (!b) return CABLE._;
    if (b.core) return b.k === 0 ? CABLE.L : CABLE._;
    if (b.pos % 8 === 0) return CABLE[COLLAR[b.k]];
    if (b.k === 1 && b.pos % 5 === 2) return CABLE.G; // reflet sur le verre
    return CABLE[GLASS[b.k]];
  },
};
// Flux : circule le long du câble (8 images)
const cableFlux = {
  name: "block/cable_flux",
  frames: 8,
  frametime: 2,
  pixel(x, y, f) {
    const b = band(x, y, 7, 8);
    if (!b || b.core) return CABLE.T;
    const v = (b.pos + 8 - f) % 8;
    return CABLE[v < 2 ? "T" : v < 5 ? "t" : "f"];
  },
};

// Cadre de la Faille : pierre sombre incrustée de Nébulite
const failleFrame = {
  name: "block/faille_frame",
  palette: { k: "#0d0716", n: "#1a0f2e", m: ORES.nebulite.m, l: ORES.nebulite.l, h: ORES.nebulite.h },
  grid: [
    "kkkkkkkkkkkkkkkk",
    "knnnnnnnnnnnnnnk",
    "knkkkkkkkkkkkknk",
    "knknnnnnnnnnnknk",
    "knknnnmllmnnnknk",
    "knknnmnnnnmnnknk",
    "knknmnnhhnnmnknk",
    "knknlnhnnhnlnknk",
    "knknlnhnnhnlnknk",
    "knknmnnhhnnmnknk",
    "knknnmnnnnmnnknk",
    "knknnnmllmnnnknk",
    "knknnnnnnnnnnknk",
    "knkkkkkkkkkkkknk",
    "knnnnnnnnnnnnnnk",
    "kkkkkkkkkkkkkkkk",
  ],
};

// Portail de la Faille : remous violet et cyan, animé sur 16 images
const PORTAL_STOPS = ["#120626", "#2a0f55", "#5a2aa8", "#9a5cff", "#7fe9ff", "#e6fdff"];
const failsPortal = {
  name: "block/faille_portal",
  frames: 16,
  frametime: 2,
  pixel(x, y, f) {
    const cx = x - 7.5, cy = y - 7.5;
    const r = Math.hypot(cx, cy), a = Math.atan2(cy, cx);
    const t = (f / 16) * Math.PI * 2;
    const v = 0.5 + 0.28 * Math.sin(a * 3 + r * 0.9 - t) + 0.22 * Math.sin(r * 1.4 - t * 2) - r * 0.025;
    const k = Math.max(0, Math.min(PORTAL_STOPS.length - 1, Math.floor(v * PORTAL_STOPS.length)));
    return PORTAL_STOPS[k];
  },
};

const items = [
  {
    name: "item/resonant_pickaxe",
    palette: { ...ITEM, a: ORES.chrysolithe.l, A: ORES.chrysolithe.m, f: ORES.ferrite.l, F: ORES.ferrite.m, y: ORES.sylvanite.l, Y: ORES.sylvanite.m },
    grid: [
      "................",
      "....xxxxxxxx....",
      "..xxaaaffffyxx..",
      ".xaAAxxxxxxYyyx.",
      ".xAxx.xttx.xxYx.",
      ".xx..xtwx....xx.",
      ".....xwWx.......",
      "....xwWx........",
      "...xwWx.........",
      "..xwWx..........",
      ".xwWx...........",
      ".xWx............",
      "..x.............",
      "................",
      "................",
      "................",
    ],
  },
  {
    name: "item/battery",
    palette: ITEM,
    grid: [
      "................",
      "......xxxx......",
      "......xBBx......",
      "....xxxxxxxx....",
      "....xLLLLLSx....",
      "....xLxxxxSx....",
      "....xLxtTxSx....",
      "....xLxTtxSx....",
      "....xLxtTxSx....",
      "....xLxTtxSx....",
      "....xLxtTxSx....",
      "....xLxxxxSx....",
      "....xLssssSx....",
      "....xSSSSSSx....",
      "....xxxxxxxx....",
      "................",
    ],
  },
  {
    name: "item/frequency_key",
    palette: ITEM,
    grid: [
      "................",
      "....xxx..xxx....",
      "..t.xTx..xTx.t..",
      ".t..xLx..xLx..t.",
      "..t.xLx..xLx.t..",
      "....xLx..xLx....",
      "....xLxxxxLx....",
      ".....xLLLLx.....",
      "......xLLx......",
      "......xwwx......",
      "......xWwx......",
      "......xwWx......",
      "......xWwx......",
      "......xwwx......",
      "......xxxx......",
      "................",
    ],
  },
];

module.exports = [
  ...Object.keys(ORES).flatMap((n) => [
    ore(n, { chrysolithe: "stone", ferrite: "deepslate", sylvanite: "stone", cindrite: "netherrack", nebulite: "end_stone" }[n]),
    storage(n),
    fragment(n),
    raw(n),
  ]),
  ...machines,
  cableShell,
  cableFlux,
  failleFrame,
  failsPortal,
  ...items,
];
