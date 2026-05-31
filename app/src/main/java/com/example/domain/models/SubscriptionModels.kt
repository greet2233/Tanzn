package com.example.domain.models

data class SubscriptionPlan(
    val id: String,
    val name: String,
    val type: String, // "single" | "family"
    val priceTsh: Int,
    val maxProfiles: Int,
    val features: List<String>,
    val isActive: Boolean = true
)

data class Subscription(
    val id: String,
    val planId: String,
    val type: String,
    val status: String, // "free" | "active" | "expired"
    val maxProfiles: Int
)

object SubscriptionMockData {
    val plans = listOf(
        SubscriptionPlan(
            id = "plan_single",
            name = "Single User",
            type = "single",
            priceTsh = 5000,
            maxProfiles = 1,
            features = listOf("Unlimited MCQ, FIB, TF", "Topic Summaries", "Higher Order Questions", "Exam Mode", "Full Past Papers", "AI Tutor Ready")
        ),
        SubscriptionPlan(
            id = "plan_family",
            name = "Family Pack",
            type = "family",
            priceTsh = 8000,
            maxProfiles = 4,
            features = listOf("Up to 4 Profiles", "Unlimited everything", "Independent Progress")
        )
    )

    val currentSubscription = Subscription(
        id = "sub_1",
        planId = "",
        type = "free",
        status = "free",
        maxProfiles = 1
    )

    val dailyUsage = DailyUsage(
        userId = "user_1",
        dateString = "today",
        mcqUsed = 1,
        fibUsed = 0,
        tfUsed = 0
    )
    
    val limits = mapOf(
        "mcq" to 5,
        "fib" to 3,
        "tf" to 2
    )
}
