package com.example.technicaltest

import com.example.technicaltest.model.User
import com.example.technicaltest.util.UserUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class UserUtilsTest {

    private val sampleUsers = listOf(
        User(1, "Andi", 25, true),
        User(2, "Budi", 17, true),
        User(3, "Citra", 30, false),
        User(4, "Deni", 22, true)
    )

    // ==========================================
    // PENGUJIAN SOAL 1: getActiveAdultUserNames
    // ==========================================

    @Test
    fun getActiveAdultUserNames_returnsOnlyActiveAdultsSortedByName() {
        val result = UserUtils.getActiveAdultUserNames(sampleUsers)
        
        // Expected: Andi (25, active) dan Deni (22, active)
        // Budi (17, active) -> tidak lolos karena age < 18
        // Citra (30, inactive) -> tidak lolos karena isActive == false
        assertEquals(listOf("Andi", "Deni"), result)
    }

    @Test
    fun getActiveAdultUserNamesOptimized_producesSameResultAsStandard() {
        val standardResult = UserUtils.getActiveAdultUserNames(sampleUsers)
        val sequenceResult = UserUtils.getActiveAdultUserNamesOptimized(sampleUsers)
        
        assertEquals(standardResult, sequenceResult)
    }

    @Test
    fun getActiveAdultUserNames_emptyList_returnsEmptyList() {
        val result = UserUtils.getActiveAdultUserNames(emptyList())
        assertTrue(result.isEmpty())
    }

    @Test
    fun getActiveAdultUserNames_noActiveUsers_returnsEmptyList() {
        val inactiveUsers = listOf(
            User(1, "Andi", 25, false),
            User(2, "Budi", 30, false)
        )
        val result = UserUtils.getActiveAdultUserNames(inactiveUsers)
        assertTrue(result.isEmpty())
    }

    // ==========================================
    // PENGUJIAN SOAL 5: searchUsers
    // ==========================================

    @Test
    fun searchUsers_exactMatch_returnsMatchingUser() {
        val result = UserUtils.searchUsers(sampleUsers, "Andi")
        assertEquals(1, result.size)
        assertEquals("Andi", result.first().name)
    }

    @Test
    fun searchUsers_caseInsensitive_returnsMatchingUser() {
        val resultUpper = UserUtils.searchUsers(sampleUsers, "BUDI")
        val resultLower = UserUtils.searchUsers(sampleUsers, "budi")
        val resultMixed = UserUtils.searchUsers(sampleUsers, "bUdI")

        assertEquals(1, resultUpper.size)
        assertEquals("Budi", resultUpper.first().name)
        assertEquals(resultUpper, resultLower)
        assertEquals(resultUpper, resultMixed)
    }

    @Test
    fun searchUsers_emptyKeyword_returnsAllUsersSortedByName() {
        val result = UserUtils.searchUsers(sampleUsers, "")
        
        // Seluruh 4 user diurutkan berdasarkan nama (Andi, Budi, Citra, Deni)
        assertEquals(4, result.size)
        assertEquals(listOf("Andi", "Budi", "Citra", "Deni"), result.map { it.name })
    }

    @Test
    fun searchUsers_blankWhitespaceKeyword_returnsAllUsersSortedByName() {
        val result = UserUtils.searchUsers(sampleUsers, "   ")
        
        assertEquals(4, result.size)
        assertEquals(listOf("Andi", "Budi", "Citra", "Deni"), result.map { it.name })
    }

    @Test
    fun searchUsers_partialSubstring_returnsSortedMatchingUsers() {
        // 'i' terdapat pada Andi, Budi, Citra, Deni
        val result = UserUtils.searchUsers(sampleUsers, "i")
        assertEquals(4, result.size)
        assertEquals(listOf("Andi", "Budi", "Citra", "Deni"), result.map { it.name })
    }

    @Test
    fun searchUsers_noMatch_returnsEmptyList() {
        val result = UserUtils.searchUsers(sampleUsers, "Zul")
        assertTrue(result.isEmpty())
    }
}
