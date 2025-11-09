# BetterCompass
 [![CodeFactor](https://www.codefactor.io/repository/github/vewaclub/bettercompass/badge)](https://www.codefactor.io/repository/github/vewaclub/bettercompass)
 [![Modrinth downloads](https://img.shields.io/modrinth/dt/bettercompass)](https://modrinth.com/plugin/bettercompass)

 
[![Modrinth](https://raw.githubusercontent.com/gist/jenchanws/842eee8428e1e0aec20de4594878156a/raw/0dbefc2fcbec362d14f1689acb807183ceffdbe1/modrinth.svg)](https://modrinth.com/plugin/bettercompass)


BetterCompass is a lightweight, configurable Spigot/Paper plugin that hides coordinates from the F3 debug screen and instead shows them neatly in the action bar when the player holds a compass — in either hand.

![compass shows coords in actionbar](https://cdn.modrinth.com/data/mLdAjUef/images/6ba289bda31593824d1585aad12de404884273b5.png)
---

## ✨ Features

- Hides coordinates from the F3 debug menu (`gamerule reducedDebugInfo`)
- Displays coordinates (`X: Y: Z:`) in the **action bar** when holding a compass (main or off-hand)
- Fully customizable action bar text format and colors
- Commands to reload configuration and toggle F3 coordinates globally
- Admin-only commands with configurable permissions
- Lightweight and optimized for all modern Spigot/Paper versions (1.16+)

---

## 🔧 Commands

| Command                 | Alias        | Description                                                |
| ----------------------- | ------------ | ---------------------------------------------------------- |
| `/bettercompass reload` | `/bc reload` | Reloads the config file  |
| `/bettercompass on`     | `/bc on`     | Hides F3 coordinates globally  |
| `/bettercompass off`    | `/bc off`    | Shows F3 coordinates globally  |


---
## 🧾 Default Configuration (`config.yml`)


```yml
# automatically hide coordinates on server start
auto-hide-f3: true

actionbar:
 enabled: true

 # how often do you want coordinate updates (20 ticks = 1 second)
 interval-ticks: 5

 # actionbar message format (color codes support)
 format: "&eX: &f{x}  &aY: &f{y}  &bZ: &f{z}"

```



## bStats
[![bStats Graph Data](https://bstats.org/signatures/bukkit/BetterCompass.svg)](https://bstats.org/plugin/bukkit/BetterCompass/27925)
