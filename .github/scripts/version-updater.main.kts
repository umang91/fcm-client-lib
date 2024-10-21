#!/usr/bin/env kotlin

enum class UpgradeType {
    MAJOR, MINOR, PATCH
}

fun getUpdatedVersion(versionName: String, upgradeType: UpgradeType): String {
    val versionSplit = versionName.split(".")
    if (versionSplit.size != 3) throw IllegalStateException("Version not in the required semver format")
    val major = versionSplit[0].toInt()
    val minor = versionSplit[1].toInt()
    val patch = versionSplit[2].toInt()
    return when (upgradeType) {
        UpgradeType.MAJOR -> {
            "${major + 1}.$minor.$patch"
        }

        UpgradeType.MINOR -> {
            updateMinor(major, minor, patch, versionSplit)
        }

        UpgradeType.PATCH -> {
            val nextPatch = patch + 1
            if (isEligibleUpdate(nextPatch, versionSplit[2])) {
                "$major.$minor.$nextPatch"
            } else {
                updateMinor(major, minor, 0, versionSplit)
            }
        }
    }
}

fun isEligibleUpdate(nextVersion: Int, currentVersion: String): Boolean {
    return nextVersion.toString().length == currentVersion.length
}

fun updateMinor(major: Int, minor: Int, patch: Int, versionSplit: List<String>): String {
    val nextMinor = minor + 1
    return if (isEligibleUpdate(nextMinor, versionSplit[1])) {
        "$major.$nextMinor.$patch"
    } else {
        "${major + 1}.0.0"
    }
}