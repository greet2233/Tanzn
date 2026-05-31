package com.example.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.domain.models.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class GamificationState(
    val profile: GamificationProfile = GamificationProfile(
        totalXP = 150,
        level = 2,
        coins = 20,
        currentStreak = 4
    ),
    val badges: List<Badge> = listOf(
        Badge("1", "First Quiz", "Complete your first quiz", "📝", "Quiz", true),
        Badge("2", "Fast Learner", "Score 100% in under 2 mins", "⚡", "Speed", false),
        Badge("3", "3 Day Streak", "Maintain a 3 day streak", "🔥", "Streak", true)
    ),
    val missions: List<DailyMission> = listOf(
        DailyMission("m1", "Daily Scholar", "Answer 5 questions", 5, 2, 50, 10),
        DailyMission("m2", "Quiz Master", "Complete 1 full quiz", 1, 0, 100, 20),
        DailyMission("m3", "Top Performer", "Earn 50 XP today", 50, 25, 50, 10)
    )
)

class GamificationViewModel : ViewModel() {
    private val _state = MutableStateFlow(GamificationState())
    val state: StateFlow<GamificationState> = _state.asStateFlow()

    fun awardXP(amount: Int) {
        _state.update { curr ->
            val newXp = curr.profile.totalXP + amount
            val newLevel = calculateLevel(newXp)
            curr.copy(
                profile = curr.profile.copy(
                    totalXP = newXp,
                    level = newLevel
                )
            )
        }
    }

    private fun calculateLevel(xp: Int): Int {
        var level = 1
        while (GamificationMock.getLevelParams(level).second <= xp) {
            level++
        }
        return level
    }

    fun awardCoins(amount: Int) {
        _state.update { curr ->
            curr.copy(
                profile = curr.profile.copy(
                    coins = curr.profile.coins + amount
                )
            )
        }
    }

    fun claimMission(missionId: String) {
        _state.update { curr ->
            val mission = curr.missions.find { it.id == missionId }
            if (mission != null && mission.isCompleted && !mission.isClaimed) {
                // Award XP and coins
                val newXp = curr.profile.totalXP + mission.xpReward
                val newLevel = calculateLevel(newXp)
                
                curr.copy(
                    profile = curr.profile.copy(
                        totalXP = newXp,
                        level = newLevel,
                        coins = curr.profile.coins + mission.coinReward
                    ),
                    missions = curr.missions.map {
                        if (it.id == missionId) it.copy(isClaimed = true) else it
                    }
                )
            } else {
                curr
            }
        }
    }

    // Mock progress update for demo
    fun addMissionProgress(type: String, amount: Int) {
        // E.g., type "questions_answered"
        _state.update { curr ->
            val updated = curr.missions.map {
                if (it.title == "Daily Scholar" && !it.isCompleted) {
                    val newVal = (it.currentValue + amount).coerceAtMost(it.targetValue)
                    it.copy(currentValue = newVal, isCompleted = newVal >= it.targetValue)
                } else {
                    it
                }
            }
            curr.copy(missions = updated)
        }
    }
}
