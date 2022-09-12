include(":app",":appjava",":appcompose")
includeBuild("../PluginVersion")

rootProject.name = "VastUtils"

// =======
// = Lib =
// =======

val libs = arrayOf(
        "VastAdapter",
        "VastMenuFab",
        "VastNetStateLayout",
        "VastSkin",
        "VastSwipeRecyclerView",
        "VastTools",
        "VastUtils"
)

libs.forEach {
        include(":libraries:$it")
}

// =======
// = Skin =
// =======

val skin = arrayOf(
        "darkskin"
)

skin.forEach {
        include(":skin:$it")
}