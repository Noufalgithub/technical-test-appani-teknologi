package com.example.technicaltest.util

import com.example.technicaltest.model.User

object UserUtils {

    /**
     * Soal 1: Mendapatkan nama user yang aktif dan berusia minimal 18 tahun,
     * kemudian diurutkan berdasarkan nama secara ascending.
     *
     * Expected Result: ["Andi", "Deni"]
     */
    fun getActiveAdultUserNames(users: List<User>): List<String> {
        return users
            .filter { it.isActive && it.age >= 18 }
            .sortedBy { it.name }
            .map { it.name }
    }

    /**
     * Alternatif optimasi memory menggunakan Sequence untuk dataset masif (> 10.000 item).
     */
    fun getActiveAdultUserNamesOptimized(users: List<User>): List<String> {
        return users.asSequence()
            .filter { it.isActive && it.age >= 18 }
            .sortedBy { it.name }
            .map { it.name }
            .toList()
    }

    /**
     * Soal 5: Mencari user berdasarkan name:
     * - Case-insensitive
     * - Jika keyword kosong/blank, kembalikan seluruh data
     * - Hasil diurutkan berdasarkan nama
     */
    fun searchUsers(
        users: List<User>,
        keyword: String
    ): List<User> {
        val trimmed = keyword.trim()
        val filtered = if (trimmed.isEmpty()) {
            users
        } else {
            users.filter { it.name.contains(trimmed, ignoreCase = true) }
        }
        return filtered.sortedBy { it.name }
    }
}
