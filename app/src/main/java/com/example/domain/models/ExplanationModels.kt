package com.example.domain.models

data class AiExplanation(
    val id: String,
    val questionId: String,
    val quizType: String,
    val subjectId: String,
    val formId: String,
    val explanationText: String,
    val whyCorrect: String,
    val whyWrong: String,
    val examTip: String,
    val memoryTrick: String,
    val stepByStep: List<String> = emptyList(),
    val latexBlocks: List<String> = emptyList(),
    val qualityScore: Double = 5.0,
    val aiProvider: String = "Gemini 3.5 Flash",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

data class AiPromptTemplate(
    val id: String,
    val name: String,
    val subject: String,
    val quizType: String,
    val templateText: String,
    val isActive: Boolean = true,
    val version: Int = 1,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

data class AiQualityLog(
    val id: String = java.util.UUID.randomUUID().toString(),
    val explanationId: String,
    val issueType: String,
    val severity: String,
    val message: String,
    val createdAt: Long = System.currentTimeMillis()
)

data class ExplanationFeedback(
    val id: String = java.util.UUID.randomUUID().toString(),
    val userId: String = "demo_user",
    val questionId: String,
    val explanationId: String,
    val rating: String, // "helpful", "confusing", "mistake"
    val comment: String = "",
    val createdAt: Long = System.currentTimeMillis()
)
