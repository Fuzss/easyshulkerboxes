plugins {
    id("fuzs.multiloader.multiloader-convention-plugins-neoforge")
}

dependencies {
    modCompileOnly(libs.puzzleslib.common)
    modApi(libs.puzzleslib.neoforge)
    modCompileOnly(libs.iteminteractions.common)
    modApi(libs.iteminteractions.neoforge)
    include(libs.iteminteractions.neoforge)
}
