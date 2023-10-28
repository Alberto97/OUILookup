package org.alberto97.ouilookup.tools

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build

object PackageManagerCompat {
    fun getPackageInfo(packageManager: PackageManager, packageName: String): PackageInfo? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            packageManager.getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(0))
        else
            getPackageInfoLegacy(packageManager, packageName)
    }

    private fun getPackageInfoLegacy(packageManager: PackageManager, packageName: String): PackageInfo? {
        return packageManager.getPackageInfo(packageName, 0)
    }
}
