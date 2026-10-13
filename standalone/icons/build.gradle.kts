plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.mavenPublish)
}

/**
 * Copies `NOTICE.md` into the artifact's Java resources, so the jar, the AAR and (below) the sources jars
 * carry the Lucide, Feather and Tabler notices the converted icons require. The path is unique to this
 * artifact, so it never collides with another library's notice when an application merges resources.
 */
val noticeResources = tasks.register<Sync>("noticeResources") {
    from(layout.projectDirectory.file("NOTICE.md")) {
        into("META-INF/dev.voir.areca/icons")
    }
    into(layout.buildDirectory.dir("generated/noticeResources"))
}

kotlin {
    jvmToolchain(21)
    explicitApi()

    jvm()
    android {
        namespace = "dev.voir.areca.icons"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain {
            resources.srcDir(noticeResources)
            dependencies {
                api(libs.compose.ui)
            }
        }

        jvmTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

// The sources jars carry the converted drawings as well, so they carry the notice too.
tasks.withType<org.gradle.jvm.tasks.Jar>()
    .matching { it.name == "sourcesJar" || it.name.endsWith("SourcesJar") }
    .configureEach { from(noticeResources) }

tasks.withType<Test>().configureEach {
    // The tests compare the compiled icons with the mapping they were generated from.
    systemProperty("areca.icons.mapping", layout.projectDirectory.file("icons.tsv").asFile.absolutePath)
    inputs.file(layout.projectDirectory.file("icons.tsv"))
}

mavenPublishing {
    pom {
        name.set("Areca Icons")
        description.set(
            "Vector icon assets for Compose Multiplatform, usable without the Areca theme.",
        )
        licenses {
            license {
                name.set("ISC License (Lucide icons)")
                url.set("https://github.com/lucide-icons/lucide/blob/main/LICENSE")
            }
            license {
                name.set("MIT License (Tabler Icons, Feather)")
                url.set("https://github.com/tabler/tabler-icons/blob/main/LICENSE")
            }
        }
    }
}

apply(from = "generate-icons.gradle.kts")
