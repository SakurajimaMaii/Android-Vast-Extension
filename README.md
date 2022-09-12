<h2 align="center">VastUtils</h2>

<p align="center">Easy Quick Android Utils for you to faster project development.</p>

<p align="center">
<img src="https://img.shields.io/badge/compile--sdk--version-32-blue"/>
<img src="https://img.shields.io/badge/min%20sdk%20version-23-yellowgreen"/>
<img src="https://img.shields.io/badge/target--sdk--version-32-orange"/>
<img src="https://img.shields.io/badge/jdk%20version-11-%2300b894"/>
</p>

<div align="center">English | <a href="https://github.com/SakurajimaMaii/VastUtils/blob/master/README_CN.md">简体中文</a></div>

## Documentation

The **VastDocs** provides you with detailed usage examples, click [here](https://sakurajimamaii.github.io/VastDocs/en/) to view the detailed documentation

## Quick start

- **VastNetStateLayout**


    A layout to set customized network state ui.

    ```groovy
    implementation 'io.github.sakurajimamaii:VastNetStateLayout:0.0.3'
    ```

- **VastTools**

    Easy Quick Android Tools for you to faster project development.

    ```groovy
    implementation 'io.github.sakurajimamaii:VastTools:0.0.8'
    ```

- **VastAdapter**

    Help you quickly build an Adapter suitable for RecyclerView.

    ```groovy
    implementation 'io.github.sakurajimamaii:VastAdapter:0.0.5'
    ```

## Package jar

Because the new version will be released in [MavenCentral](https://search.maven.org/search?q=g:io.github.sakurajimamaii) only after the test is over, so if you want to experience the latest features, you can follow the steps below to package the jar.

Add the following content to the build.gradle.kts of the corresponding module, taking VastTools as an example:

```kotlin
val JAR_PATH = "build/intermediates/runtime_library_classes_jar/release/"
val JAR_NAME = "classes.jar"
val DESTINATION_PATH = "libs"
val NEW_NAME = "VastTools_0.0.9_Cancey.jar"

tasks.register("makeJar",Copy::class){
    delete(DESTINATION_PATH + NEW_NAME)
    from(JAR_PATH + JAR_NAME)
    into(DESTINATION_PATH)
    rename(JAR_NAME, NEW_NAME)
}.dependsOn("build")
```

Select `Android Studio Gradle` -> `libraries` -> `VastTools` -> `Tasks` -> `other` -> `makeJar` .

<div align=center><img src="https://github.com/SakurajimaMaii/VastUtils/blob/release/mdResource/md_resource_1.png?raw=true" style="width:50%"/></div>

Get the `VastTools_0.0.9_Cancey.jar` .

<div align=center><img src="https://github.com/SakurajimaMaii/VastUtils/blob/release/mdResource/md_resource_2.png?raw=true" style="width:50%"/></div>

## Dependencies

Since the project uses **Composingbuilds module** for project management, you can get project dependency information at [PluginVersion](https://github.com/SakurajimaMaii/PluginVersion).

## Acknowledgements

Thanks to the following users for helping me with the development project.

|**Avator**| **Name**  | **Website**                                                |
|:--:| :--: | :--: | 
| <img src="https://avatars.githubusercontent.com/u/23447567?v=4" width=15%> | lt | [https://github.com/ltttttttttttt](https://github.com/ltttttttttttt) |
| <img src="https://avatars.githubusercontent.com/u/45026715?v=4" width=15%> | 夜里的向日葵 | [https://github.com/lookup-cat](https://github.com/lookup-cat) |