<h1 align="center">VastUtils</h1>

<p align="center">一款加快你安卓开发的安卓工具集</p>

<p align="center">
<img src="https://img.shields.io/badge/compile--sdk--version-32-blue"/>
<img src="https://img.shields.io/badge/min%20sdk%20version-23-yellowgreen"/>
<img src="https://img.shields.io/badge/target--sdk--version-32-orange"/>
<img src="https://img.shields.io/badge/jdk%20version-11-%2300b894"/>
</p>

<p align="center">简体中文 | <a href="https://github.com/SakurajimaMaii/ToolsForAndroid/blob/master/README.md">English</a></p>

## 文档

**VastDocs**为你提供了详细的使用示例，点击[这里](https://sakurajimamaii.github.io/VastDocs/)即可查看详细文档

## 快速开始

- **VastNetStateLayout**

    ```groovy
    implementation 'io.github.sakurajimamaii:VastNetStateLayout:0.0.3'
    ```

- **VastTools**

    ```groovy
    implementation 'io.github.sakurajimamaii:VastTools:0.0.8'
    ```

- **VastAdapter**

    ```groovy
    implementation 'io.github.sakurajimamaii:VastAdapter:0.0.5'
    ```

## 打包Jar

因为只有在测试结束后才会在MavenCentral发布新版本，所以如果你想体验最新功能，可以按照下面的步骤打包jar。

在对应模块的build.gradle.kts中添加如下内容，以VastTools为例：

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

选择 `Android Studio Gradle` -> `libraries` -> `VastTools` -> `Tasks` -> `other` -> `makeJar` 。

<div align=center><img src="https://github.com/SakurajimaMaii/VastUtils/blob/release/mdResource/md_resource_1.png?raw=true" style="width:50%"/></div>

获取 `VastTools_0.0.9_Cancey.jar` 。

<div align=center><img src="https://github.com/SakurajimaMaii/VastUtils/blob/release/mdResource/md_resource_2.png?raw=true" style="width:50%"/></div>

## 依赖信息

由于项目使用**Composingbuilds module**进行项目管理，你可以在[PluginVersion](https://github.com/SakurajimaMaii/PluginVersion)获取项目依赖信息。

## 致谢

感谢下列开发者在开发过程中给予的技术支持

|**Avator**| **Name**  | **Website** |
|:--:| :--: | :--: | 
| <img src="https://avatars.githubusercontent.com/u/23447567?v=4" width=15%> | lt | [https://github.com/ltttttttttttt](https://github.com/ltttttttttttt) |
| <img src="https://avatars.githubusercontent.com/u/45026715?v=4" width=15%> | 夜里的向日葵 | [https://github.com/lookup-cat](https://github.com/lookup-cat) |