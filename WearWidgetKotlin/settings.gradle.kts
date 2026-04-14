pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
//        maven {
//            url = uri("https://androidx.dev/snapshots/builds/14666938/artifacts/repository")
//        }
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Commented out as tagged releases are now available on Google Maven.
        // maven {
        //     url = uri("https://androidx.dev/snapshots/builds/14978996/artifacts/repository")
        // }
    }
}

rootProject.name = "WearWidgetKotlin"
include(":app")
