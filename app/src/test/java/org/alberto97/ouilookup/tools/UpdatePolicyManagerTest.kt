package org.alberto97.ouilookup.tools

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneOffset

class UpdatePolicyManagerTest {

    @Test
    fun testUpdateRemote() {
        val emptyDb = true
        val offline = false
        val time = LocalDateTime.now().minusDays(31).toInstant(ZoneOffset.UTC).toEpochMilli()

        val result = UpdatePolicyManager.getUpdatePolicy(offline, emptyDb, time)

        assertEquals(result, UpdatePolicy.REMOTE)
    }

    @Test
    fun testUpdateLocal() {
        val emptyDb = true
        val offline = true
        val time: Long = 0

        val result = UpdatePolicyManager.getUpdatePolicy(offline, emptyDb, time)

        assertEquals(result, UpdatePolicy.LOCAL)
    }

    @Test
    fun testNeedsUpdate() {
        val time = LocalDateTime.now().minusDays(31).toInstant(ZoneOffset.UTC).toEpochMilli()
        val result = UpdatePolicyManager.needsUpdate(false, time)
        assert(result)
    }

    @Test
    fun testUpToDate() {
        val time = LocalDateTime.now().minusDays(29).toInstant(ZoneOffset.UTC).toEpochMilli()
        val result = UpdatePolicyManager.needsUpdate(false, time)
        assert(!result)
    }
}