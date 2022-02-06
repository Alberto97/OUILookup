package org.alberto97.ouilookup.tools

import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneOffset

class UpdatePolicyManagerTest {

    @Test
    fun testOutdated() {
        val emptyDb = false
        val time = LocalDateTime.now().minusDays(31).toInstant(ZoneOffset.UTC).toEpochMilli()

        val result = UpdatePolicyManager.isOutdated(emptyDb, time)

        assert(result)
    }

    @Test
    fun testSeeding() {
        val emptyDb = true
        val time: Long = 0

        val result = UpdatePolicyManager.isOutdated(emptyDb, time)

        assert(!result)
    }

    @Test
    fun testNeedsUpdate() {
        val time = LocalDateTime.now().minusDays(31).toInstant(ZoneOffset.UTC).toEpochMilli()
        val result = UpdatePolicyManager.needsUpdate(time)
        assert(result)
    }

    @Test
    fun testUpToDate() {
        val time = LocalDateTime.now().minusDays(29).toInstant(ZoneOffset.UTC).toEpochMilli()
        val result = UpdatePolicyManager.needsUpdate(time)
        assert(!result)
    }
}