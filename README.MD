# Metal Revolution

1. [简体中文](#简介)
2. [English](#overview)

## 简介

Metal Revolution 是 [魔法金属](https://www.curseforge.com/minecraft/mc-mods/manametal) 的 Minecraft 1.7.10 附属模组。它让魔法金属的设备更容易接入漏斗和移物水晶等物流系统，并尽可能保留原有的合成和使用方式。

### 主要功能

- **设备自动化**。大多数单方块设备支持从任意面输入和抽取物品，并尽可能将材料放入对应的槽位。装备强化桌、武器卷轴强化桌和质量改造桌支持自动供料，高炉和黑暗钢高炉只允许物流设备抽取产物。
- **特殊设备**。向魔力灌注核心输入物品会立即尝试开始合成。黄金屋核心支持自动输入金块，引力井支持从输出槽抽取物品，输入时需要自行安排槽位。魔法合成台可自动输入合成材料，魔法能请通过魔法能分享石碑供给。
- **金属能量**。金属分离器内置金属能量缓存，通常以中型金属能量输出。相邻的金属融合台和金属还原台可以直接使用缓存中的能量。搭配 Trivial Thoughts 时，金属能量储罐也可从任意一面为这两种设备供能。
- **NEI 配方**。查看果汁机等机器的用途即可打开对应的配方界面。魔法合成台、金属融合台、纺车、引力井和织布机支持一键摆放配方，进阶烹饪提供部分支持。
- **背包快速卸货**。手持魔法金属背包潜行右键容器，可尝试转入所有物品。安装 Applied Energistics 2 时，也可通过 ME 接口将物品存入网络。

### 安装与升级

- 使用 Minecraft 1.7.10 Forge，安装魔法金属 8.0.2 或以上版本及 UniMixins 0.1.23 或以上版本。客户端的配方功能使用 Not Enough Items（NEI）。
- Metal Revolution 运行时需要 `fastutil`。请安装提供该库的 [GTNH External Lib](https://www.curseforge.com/minecraft/mc-mods/gtnh-external-lib)。如果整合包已经提供了兼容的 `fastutil`，无需额外安装。
- 从旧存档升级时，请先备份存档，再挖下并重新放置已有的黄金屋核心、魔力灌注核心、魔力石柱、金属分离器和奶酪机，使这些方块应用新的自动化行为。

---

## Overview

Metal Revolution is a Minecraft 1.7.10 addon for [ManaMetal](https://www.curseforge.com/minecraft/mc-mods/manametal). It makes ManaMetal machines easier to automate with hoppers and transfer crystals while keeping their original recipes and operation wherever possible.

### Features

- **Machine automation.** Most single-block machines accept input and output on any side and route ingredients to the appropriate slots where possible. Equipment Enhancement, Weapon Scroll Enhancement, and Quality Modification Tables support automated input. Blast Furnaces and Dark Steel Blast Furnaces protect their input slots from automated extraction.
- **Special machines.** Inserting an item into a Mana Injection Core immediately attempts to start crafting. Golden House Cores accept gold blocks from automation. Gravity Wells allow extraction from the output slot and require manual slot planning for automated input. The Magic Crafting Table accepts automated ingredient input and receives mana from a Mana Energy Sharing Stele.
- **Metal energy.** Metal Separators buffer metal energy internally and usually output it as Medium Metal Energy. Adjacent Metal Fusion and Metal Reduction Tables can draw from the buffer directly. With Trivial Thoughts installed, Metal Energy Tanks can also power these tables from any side.
- **NEI recipes.** Viewing the uses of the Juicer and other machines opens their recipe screens. One-click recipe transfer works for the Magic Crafting Table, Metal Fusion Table, Spinning Wheel, Gravity Well, and Loom, with partial support for Advanced Cooking.
- **Quick backpack unloading.** Sneak-right-click a container while holding a ManaMetal backpack to transfer as many items as it can accept. With Applied Energistics 2 installed, an ME Interface can send items into the network.

### Installation and upgrades

- Use Minecraft 1.7.10 Forge with ManaMetal 8.0.2 or newer and UniMixins 0.1.23 or newer. The client-side recipe features use Not Enough Items (NEI).
- Metal Revolution requires `fastutil` at runtime. Install [GTNH External Lib](https://www.curseforge.com/minecraft/mc-mods/gtnh-external-lib) to provide it if your modpack does not already include a compatible version of `fastutil`.
- When upgrading an existing world, back it up and break and replace any existing Golden House Cores, Mana Injection Cores, Mana Crystal Pillars, Metal Separators, and Cheese Makers to apply the new automation behavior.
