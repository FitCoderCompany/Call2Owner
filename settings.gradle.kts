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
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        repositories {
            maven {
                url = uri("https://maven.google.com")
            }
            maven {
                url = uri("https://jitpack.io")
            }
            flatDir {
                dirs("libs")
            }

        }


    }
}

rootProject.name = "Call 2 Owner"
include(":app")
 