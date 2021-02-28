package org.alberto97.ouilookup

import android.content.res.Resources
import androidx.annotation.RawRes

object Extensions {
    fun Resources.readRawTextFile(@RawRes id: Int): String =
        openRawResource(id).bufferedReader().use { it.readText() }
}