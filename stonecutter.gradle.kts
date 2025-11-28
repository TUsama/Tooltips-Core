plugins {
    id("dev.kikugie.stonecutter")
}
stonecutter active "1.21.1-neoforge"

allprojects {
    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://maven.neoforged.net/releases")
        maven("https://maven.fabricmc.net/")
        maven("https://api.modrinth.com/maven")
        maven("https://maven.fzzyhmstrs.me/")
        maven("https://thedarkcolour.github.io/KotlinForForge/")
        maven("https://cursemaven.com")
        maven("https://maven.theillusivec4.top/")
    }
}