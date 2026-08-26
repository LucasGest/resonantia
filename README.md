# Resonantia — Wiki du mod

*Mod Minecraft Java Edition — NeoForge 1.21.1*
*Par Lucanemone*

---

## Pourquoi ce mod ?

Resonantia est né d'une envie simple : créer un mod d'exploration/technologie avec **sa propre identité**, plutôt que de reproduire ce qui existe déjà. Beaucoup de mods tech (Create, Mekanism, Thermal Expansion, IndustrialCraft...) partagent au fond les mêmes mécaniques : minerai → lingot → machine → énergie générique. Ils sont d'excellentes références, mais l'objectif ici n'était pas de les copier avec d'autres noms — c'était de construire un système qui a sa propre logique interne, tout en restant familier et accessible pour n'importe quel joueur Minecraft.

Cette logique, c'est la **résonance**.

---

## Le concept central : la fréquence de résonance

Plutôt qu'un système d'énergie ou de progression générique, Resonantia s'organise autour d'une idée simple : **chaque minerai a une fréquence qui lui est propre**, et les machines du mod peuvent être **accordées** sur cette fréquence pour devenir plus efficaces.

Concrètement : une machine mal réglée fonctionne quand même (jamais de punition brutale), mais une machine bien accordée sur la fréquence de ce qu'elle traite va **deux fois plus vite**. Ça transforme un geste normalement anodin (miner du minerai, le jeter dans une machine) en une vraie petite couche de skill/optimisation, sans jamais bloquer un joueur qui ne s'y intéresse pas.

L'outil qui permet ça : la **Clé de Fréquence**, qui règle une machine par tranches de 10 (clic droit = +10, Shift+clic droit = -10, plage 0-100).

---

## Les minerais

| Minerai | Où le trouver | Identité | Fréquence idéale |
|---|---|---|---|
| **Chrysolithe** | Overworld, Y 0 à 80, partout | Minerai de base, stable et facile — pensé pour débuter sans complication | 50 *(accordé par défaut)* |
| **Ferrite Noire** | Overworld, Y -32 à 32, partout | Thème magnétisme — sert de base à l'automatisation | 30 |
| **Sylvanite** | Overworld, Y 0 à 32, **forêts et marais uniquement** | Rare et caché, façon "débris antiques" — camouflé dans la pierre, nécessite une pioche en fer minimum | 70 |
| **Cindrite** *(en cours)* | Nether, Y 10 à 100 | Thème thermique — résistant au feu et aux explosions, cohérent avec son environnement | *à définir* |

Chaque minerai suit la même chaîne de transformation : **minerai brut → four ou Concasseur → fragment fini**, avec un bloc de stockage compact (9 fragments ⇄ 1 bloc, comme le fer vanilla). Le minerai en dur peut aussi être fondu directement, ou récupéré intact avec Silk Touch.

---

## Les machines

### Concasseur Résonant
La première machine du mod. Transforme un minerai brut en **2 fragments** au lieu d'1 (comme au four), sans besoin de combustible — juste de l'énergie FE. Sensible à la fréquence : bien accordé, il double sa vitesse de traitement.

### Générateur Résonant
Brûle n'importe quel combustible de four classique (charbon, etc.) pour produire du FE. Peut charger directement une Batterie insérée dans son deuxième emplacement.

### Assembleur Résonant
La machine "craft avancé" : combine **jusqu'à 3 ingrédients différents** pour produire un résultat — la première fois que la progression demande de vraies recettes complexes plutôt qu'un simple 1-vers-1. C'est via cette machine qu'est fabriqué le premier vrai outil du mod.

---

## Le système énergétique

Resonantia utilise le **FE (Forge Energy)**, le standard d'interopérabilité énergie de l'écosystème NeoForge — ce qui garantit une compatibilité technique avec d'autres mods qui utilisent aussi le FE, même si l'identité visuelle et narrative du mod ("Flux Résonant") reste la sienne.

Deux façons de transporter l'énergie :
- **Batterie Résonante** — item portable, à charger sur un Générateur puis à transporter à la main jusqu'à une machine.
- **Câbles Résonants** — transport automatique, avec connexions visuelles dynamiques qui s'adaptent aux machines/câbles voisins (comme un vrai réseau de tuyaux).

---

## Premier objet de progression : la Pioche Résonante

Fabriquée dans l'Assembleur en combinant un fragment de **chacun des 3 minerais**, c'est la première vraie récompense tangible du mod — une pioche de niveau diamant, symbole que le joueur a exploré et maîtrisé les trois filières de minerais de base.

---

## La feuille de route — où le mod va

Le mod est pensé pour se terminer sur un vrai objectif final, pas juste s'arrêter arbitrairement :

1. **Cindrite** *(en cours)* — 4ᵉ minerai, thème Nether/thermique
2. **Nébulite** — minerai End, lié à la téléportation/l'espace
3. **La Faille** — dimension exclusive au mod, accessible via un portail construit en Nébulite
4. **Résonium** — minerai ultra-rare, uniquement dans la Faille, extraction nécessitant un vrai accordage précis de fréquence
5. **Cœur de Résonance Absolue** — l'objet qui clôt la progression du mod, combinant Résonium et fragments raffinés des minerais précédents

---

## Notes de développement

- Environnement : NeoForge 1.21.1, IntelliJ IDEA, Java 21
- Dépôt : GitHub privé/public selon les périodes, licence "Tous droits réservés"
- Direction artistique actuelle : textures placeholder générées procéduralement, à refaire en pixel-art définitif (Blockbench) une fois le mod fonctionnellement stable
