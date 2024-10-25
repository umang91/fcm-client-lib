#!/usr/bin/env kotlin

@file:Import("version-updater.main.kts")
@file:Import("utils.main.kts")

import java.util.Calendar


val upgradeInput = args[0]

val updatedVersion = updateLibraryVersion(upgradeInput)
val calendar = Calendar.getInstance()
val date =
    "${calendar[Calendar.DAY_OF_MONTH]}-${calendar[Calendar.MONTH]+1}-${calendar[Calendar.YEAR]}"
replaceTextInFile("CHANGELOG.md", "Next Release", "$updatedVersion($date)")
// build project
executeCommandOnShell("./gradlew assemble")
// publish to central portal
executeCommandOnShell("./gradlew fcm-client:publishToMavenRepository -PlogLevel=4")

// commit changes
executeCommandOnShell("git add .")
executeCommandOnShell("git commit -m \"release $updatedVersion\"")
executeCommandOnShell("git push origin master")
executeCommandOnShell("git tag -a v-$updatedVersion -m \"Release $updatedVersion\"")
executeCommandOnShell("git push origin --tags")

