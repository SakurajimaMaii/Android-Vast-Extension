include(":app",":appjava",":appcompose")
includeBuild("../PluginVersion")

rootProject.name = "VastUtils"

// =======
// = Lib =
// =======

val libs = arrayOf(
        "VastAdapter",
        "VastTools",
        "VastNetStateLayout",
)

libs.forEach {
     include(":libraries:$it")
}