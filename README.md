About BlockOut
--------------
[![Build Status](https://teamcity.minecolonies.com/app/rest/builds/buildType:LetSDevTogether_BlockOut_Alpha_Publish/statusIcon)](http://teamcity.minecolonies.com/)

Welcome to the new BlockOut.
This Minecraft Mod/Library aims to provide other mods and users with the abilities and tools to enjoy making UIs for Minecraft.
To achieve this we use several techniques to not only reduce the workload of making and maintaining a UI, but also take care of almost all synchronisation of information required to display the GUI.

### Current state
This redesign is not finished, however we feel that BlockOut is ready to be used by the general public, by default we provide support of the most common elements required to make nice and well functioning UIs.
This said however, things might break or are still broken, we are humans. So if you find a bug or an issue, do not hesitate and create an issue.

### How to use
#### Players
Under normal conditions you as player do not need to download BlockOut. However it might be that you use a mod that requires BlockOut for its guis.
In that case you can download the latest releases of any kind (alpha, beta or release) directly from [CurseForge](https://minecraft.curseforge.com/projects/blockout).

#### Modders
##### Installation
Add the following Maven repo as a repository to your buildscript:

```groovy
repositories {
    maven {
        name 'LDTTeam Artifactory'
        url 'https://ldtteam.jfrog.io/ldtteam/modding/'
    }
}
```

Visit our [Artifactory](https://ldtteam.jfrog.io/ldtteam/webapp/#/artifacts/browse/tree/General/modding) and look for the correct BlockOut version you want to use.
If you do not care about a specific version you can always just get the latest version, by using a + as version number.

Now that you have a version, add BlockOut as a dependency:
```groovy
dependencies {
    //Mod dependencies
    deobfCompile('com.ldtteam:blockout:<insert version or + here>')
}
```

##### Examples
The best examples can be found of course in mods using BlockOut, however to test our systems we also distribute a TestMod, which exists as a separate SourceSet within this Repository.
You can find examples of several techniques used by BlockOut there:
 - GUIs: [Image only test](https://github.com/ldtteam/BlockOut/blob/version/1.12/src/testmod/resources/assets/blockout_test/gui/image_only_test.json)
 - Styles: [Style definitions](https://github.com/ldtteam/BlockOut/tree/version/1.12/src/testmod/resources/assets/blockout_test/styles)
 - Resources: [Image resource definitions](https://github.com/ldtteam/BlockOut/tree/version/1.12/src/testmod/resources/assets/blockout_test/styles/test/image)

### More information can be found here:
#### Our wiki:
Our wiki will be growing in the next few weeks with more and more informational posts, on how to use and modify BlockOut: [Wiki](https://github.com/ldtteam/BlockOut/wiki).
We are always looking for people who are willing to write a piece for our wiki, using the usual github wiki processes.
#### Our discord:
Our development discord is as of now closed to the public, however our primary mod, [Minecolonies](https://github.com/ldtteam/minecolonies), is open for public.
Use the following join link: [Discord](https://discord.gg/nAYMNWZ). 