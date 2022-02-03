package org.alberto97.ouilookup.tools

import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration

enum class UpdatePolicy {
    LOCAL,
    REMOTE
}

object UpdatePolicyManager {
    @OptIn(ExperimentalTime::class)
    fun getUpdatePolicy(offline: Boolean, emptyDb: Boolean, lastUpdateMillis: Long): UpdatePolicy? {
        // If offline at first boot use bundled data
        if (emptyDb && offline)
            return UpdatePolicy.LOCAL

        if (!offline && needsUpdate(emptyDb, lastUpdateMillis))
            return UpdatePolicy.REMOTE

        return null
    }

    @OptIn(ExperimentalTime::class)
    fun needsUpdate(emptyDb: Boolean, lastUpdateMillis: Long): Boolean {
        return !isUpToDate(lastUpdateMillis) || emptyDb
    }

    @ExperimentalTime
    private fun isUpToDate(lastUpdateMillis: Long) : Boolean {
        // Don't update until at least a month has passed since the last data fetch
        val duration = (System.currentTimeMillis() - lastUpdateMillis).toDuration(DurationUnit.MILLISECONDS)
        return duration.inWholeDays < 30
    }
}
