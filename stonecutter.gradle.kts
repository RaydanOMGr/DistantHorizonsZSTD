import java.util.Properties

plugins {
    id("dev.kikugie.stonecutter")
    id("io.github.pacifistmc.forgix") version "2.0.0" apply false
}
stonecutter active "1.20.1-fabric"

allprojects {
    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://maven.neoforged.net/releases")
        maven("https://api.modrinth.com/maven")
        maven("https://maven.fabricmc.net/")
        maven("https://maven.ladysnake.org/releases")
    }
}

val props = Properties()
file("gradle.properties").bufferedReader().use {
    props.load(it)
}

tasks.register("mergeJars", Jar::class) {
    group = "build"
    description = "Merge all produced jars into one"

    archiveBaseName = props["mod_id"] as String
    archiveVersion = "${props["mod_version"]}+fabric-neoforge"
    destinationDirectory = project.layout.buildDirectory.dir("merged/libs")

    // do not include forge, as it needs to bundle mixinextras which breaks neoforge
    val inputFiles = mapOf(
        "fabric" to project(":1.20.1-fabric").archiveFile(jarTask = "remapJar"),
        "neoforge" to project(":1.21.1-neoforge").archiveFile()
    )
    val outputFile = archiveFile

    inputs.files(inputFiles.map { it.value })
    outputs.file(outputFile)

    doFirst {
        outputFile.get().asFile.parentFile.mkdirs()
        outputFile.get().asFile.delete()
    }

    doLast {
        val output = outputFile.get().asFile

        val execOutput = providers.exec {
            val arguments = mutableListOf<String>()
            inputFiles.forEach { (name, file) ->
                val path = file.get().asFile.absolutePath
                arguments.add("--$name")
                arguments.add(path)
                println("Adding $name to loader list at $path")
            }

            workingDir = project.layout.projectDirectory.asFile
            commandLine(
                "java",
                "--add-exports=java.base/jdk.internal.access=ALL-UNNAMED",
                "-cp", "forgix/Forgix-2.0.0.jar",
                "io.github.pacifistmc.forgix.Forgix",
                "mergeJars",
                "--output", output.absolutePath,
                *arguments.toTypedArray()
            )


            isIgnoreExitValue = true

            println("CWD: $workingDir")
            println(commandLine.joinToString(" "))
        }

        val exitCode = execOutput.result.get().exitValue
        val stdout = execOutput.standardOutput.asText.get()
        val stderr = execOutput.standardError.asText.get()

        println("STDOUT: $stdout")
        println("STDERR: $stderr")
        println("Exit Code: $exitCode")

        if (exitCode != 0) {
            throw GradleException("Command failed with exit code $exitCode")
        }
    }
}

tasks.register("collectModArtifacts", Copy::class) {
    group = "build"
    description = "Collects all mod jars built"

    val inputFiles = files(
        tasks.named<org.gradle.jvm.tasks.Jar>("mergeJars").get().archiveFile,
        project(":1.20.1-forge").archiveFile()
    )
    val outputDir = project.layout.buildDirectory.dir("libs/mod")

    from(inputFiles)
    into(outputDir)
}

tasks.register("collectSourcesArtifacts", Copy::class) {
    group = "build"
    description = "Collects all sources jars built"

    val inputFiles = files(
        project(":1.20.1-fabric").archiveFile("sourcesJar"),
        project(":1.21.1-neoforge").archiveFile("sourcesJar"),
        project(":1.20.1-forge").archiveFile("sourcesJar")
    )
    val outputDir = project.layout.buildDirectory.dir("libs/sources")

    from(inputFiles)
    into(outputDir)
}

tasks.register("collectArtifacts") {
    group = "build"
    description = "Collects all important built jars"

    dependsOn(tasks.named("collectModArtifacts"))
    dependsOn(tasks.named("collectSourcesArtifacts"))
}

fun Project.libsDirectory(jarTask: String = "jar"): DirectoryProperty {
    return this.tasks.named<org.gradle.jvm.tasks.Jar>(jarTask).get().destinationDirectory
}

fun Project.archiveFile(jarTask: String = "jar"): Provider<RegularFile> {
    return this.tasks.named<org.gradle.jvm.tasks.Jar>(jarTask).get().archiveFile
}