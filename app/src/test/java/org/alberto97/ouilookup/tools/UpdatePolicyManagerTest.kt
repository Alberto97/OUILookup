package org.alberto97.ouilookup.tools


import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.test.Test
import kotlin.test.assertEquals

class UpdatePolicyManagerTest {

    @Test
    fun testLocalUpdate() {
        val bundledMillis = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()
        val lastMillis = LocalDateTime.now().minusDays(7).toInstant(ZoneOffset.UTC).toEpochMilli()

        val result = UpdatePolicyManager.getUpdatePolicy(bundledMillis, lastMillis)

        assertEquals(UpdatePolicy.Local, result)
    }

    @Test
    fun testRemoteUpdate() {
        val bundledMillis = LocalDateTime.now().minusDays(40).toInstant(ZoneOffset.UTC).toEpochMilli()
        val lastMillis = LocalDateTime.now().minusDays(31).toInstant(ZoneOffset.UTC).toEpochMilli()

        val result = UpdatePolicyManager.getUpdatePolicy(bundledMillis, lastMillis)

        assertEquals(UpdatePolicy.Remote, result)
    }

    @Test
    fun testBothUpdates() {
        val bundledMillis = LocalDateTime.now().minusDays(31).toInstant(ZoneOffset.UTC).toEpochMilli()
        val lastMillis = LocalDateTime.now().minusDays(40).toInstant(ZoneOffset.UTC).toEpochMilli()

        val result = UpdatePolicyManager.getUpdatePolicy(bundledMillis, lastMillis)

        assertEquals(UpdatePolicy.Both, result)
    }

    @Test
    fun testNoUpdateRequired() {
        val bundledMillis = LocalDateTime.now().minusDays(31).toInstant(ZoneOffset.UTC).toEpochMilli()
        val lastMillis = LocalDateTime.now().minusDays(7).toInstant(ZoneOffset.UTC).toEpochMilli()

        val result = UpdatePolicyManager.getUpdatePolicy(bundledMillis, lastMillis)

        assertEquals(UpdatePolicy.None, result)
    }

    @Test
    fun testOutdated() {
        val time = LocalDateTime.now().minusDays(31).toInstant(ZoneOffset.UTC).toEpochMilli()
        val result = UpdatePolicyManager.isOutdated(time)
        assert(result)
    }

    @Test
    fun testUpToDate() {
        val time = LocalDateTime.now().minusDays(29).toInstant(ZoneOffset.UTC).toEpochMilli()
        val result = UpdatePolicyManager.isOutdated(time)
        assert(!result)
    }
}