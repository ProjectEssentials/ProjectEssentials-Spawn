> ## Documentation for basically using Spawn module.

## 1. For playing and running minecraft:

#### 1.1 Download Spawn mod module.

Visit **Spawn** repository on github, visit **releases** tab and download `.jar` files of latest _pre-release_ / release (**recommended**)

Releases page: https://github.com/ProjectEssentials/ProjectEssentials-Spawn/releases

#### 1.2 Install Spawn modification.

The minecraft forge folder structure below will help you understand what is written below.

> ##### Important note: don't forget install mod dependencies!
  - core: https://github.com/ProjectEssentials/ProjectEssentials-Core/releases
  - permissions: https://github.com/ProjectEssentials/ProjectEssentials-Permissions/releases
  - essentials: https://github.com/ProjectEssentials/ProjectEssentials/releases


```
.
├── assets
├── config
├── libraries
├── mods (that's how it should be)
│   ├── Project Essentials Spawn-1.14.4-1.X.X.X.jar
│   ├── Project Essentials-1.14.4-1.X.X.X.jar.
│   ├── Project Essentials Core-MOD-1.14.4-1.X.X.X.jar.
│   └── Project Essentials Permissions-1.14.4-1.X.X.X.jar.
└── ...
```

Place your mods and Spawn mods according to the structure above.

#### 1.3 Verifying mod on the correct installation.

Run the game, check the number of mods, if the list of mods contains `Project Essentials Spawn` mod, then the mod has successfully passed the initialization of the modification.

After that, go into a single world, then try to write the `/spawn version` command, if you **get an error** that you do not have permissions, then the modification works as it should.

#### 1.4 Control spawn via minecraft commands.

We made the commands for you:

```
/spawn

- description: base command of spawn module; just teleport you on spawn point.

- permission: ess.spawn
```

```
/setspawn

- description: set new spawn point by player coordinates.

- permission: ess.spawn.set
```

```
/spawn reload

- description: reload world spawn configuration !!!without saving.

- permission: ess.spawn.reload
```

```
/spawn save

- description: save world spawn configuration.

- permission: ess.spawn.save
```

```
/spawn version

- description: send message to command sender with information about `spawn` module.

- permission: ess.spawn.version
```

### For all questions, be sure to write issues!