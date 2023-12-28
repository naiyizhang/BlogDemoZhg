pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
}

rootProject.name = "BlogDemoZhg"
include(":app")
include(":myapplication")
include(":mylibrary")
include(":wechatdemo")
include(":callphone")
include(":callmenow")
