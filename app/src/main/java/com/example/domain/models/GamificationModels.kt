package com.example.domain.models

data class GamificationProfile(
    val id: String = "profile_1",
    val userId: String = "user_1",
    val totalXP: Int = 0,
    val level: Int = 1,
    val coins: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val nationalRank: Int = 500,
    val schoolRank: Int = 15
)

data class Badge(
    val id: String,
    val name: String,
    val description: String,
    val icon: String,
    val category: String,
    val isUnlocked: Boolean = false
)

data class DailyMission(
    val id: String,
    val title: String,
    val description: String,
    val targetValue: Int,
    val currentValue: Int = 0,
    val xpReward: Int,
    val coinReward: Int,
    val isCompleted: Boolean = false,
    val isClaimed: Boolean = false
)

data class LeaderboardUser(
    val id: String,
    val name: String,
    val xp: Int,
    val rank: Int,
    val avatar: String = "👤"
)

object GamificationMock {
    fun getLevelParams(level: Int): Pair<Int, Int> {
        val currThreshold = getLevelRequiredXp(level)
        val nextThreshold = getLevelRequiredXp(level + 1)
        return Pair(currThreshold, nextThreshold)
    }

    private fun getLevelRequiredXp(level: Int): Int {
        return when (level) {
            1 -> 0
            2 -> 100
            3 -> 250
            4 -> 500
            5 -> 900
            else -> 900 + (level - 5) * 500
        }
    }
}
