package com.example.domain.models

data class User(
    val id: String,
    val phoneNumber: String
)

data class Profile(
    val userId: String,
    val name: String,
    val formLevel: String, // Form 1, Form 2, Form 3, Form 4
    val school: String?,
    val avatarUrl: String?,
    val totalXp: Int = 0,
    val currentStreak: Int = 0,
    val level: Int = 1
)

data class FormLevel(
    val id: String,
    val name: String,
    val order: Int,
    val isActive: Boolean = true
)

data class Subject(
    val id: String,
    val formId: String,
    val name: String,
    val description: String = "",
    val icon: String = "📚",
    val colorHex: String = "#3EE084",
    val order: Int = 0,
    val isCore: Boolean = false,
    val isActive: Boolean = true,
    val progress: Int = 0,
    val isPremiumLocked: Boolean = false
)

data class Topic(
    val id: String,
    val formId: String,
    val subjectId: String,
    val name: String,
    val description: String = "",
    val order: Int = 0,
    val difficulty: String = "Beginner", // Beginner, Intermediate, Advanced
    val isActive: Boolean = true,
    val isPremiumLocked: Boolean = false,
    val packCount: Int = 0,
    val progress: Int = 0
)

data class LearningPack(
    val id: String,
    val formId: String,
    val subjectId: String,
    val topicId: String,
    val title: String,
    val description: String = "",
    val order: Int = 0,
    val estimatedMinutes: Int = 15,
    val difficulty: String = "Beginner",
    val isPremiumLocked: Boolean = false,
    val isActive: Boolean = true,
    val completionXP: Int = 50
)

data class StudentProgress(
    val id: String,
    val userId: String,
    val formId: String,
    val subjectId: String,
    val topicId: String,
    val learningPackId: String,
    val progressPercent: Int,
    val status: String, // NotStarted, InProgress, Completed
    val xpEarned: Int = 0
)

enum class PackType { SUMMARY, MCQ, FIB, TF, HOQ }

enum class QuestionType { MCQ, FIB, TF }

data class Question(
    val id: String,
    val learningPackId: String,
    val type: QuestionType,
    val questionText: String,
    val options: List<McqOption> = emptyList(),
    val acceptedAnswers: List<String> = emptyList(),
    val correctAnswerTf: Boolean? = null,
    val explanation: String,
    val difficulty: String = "Beginner",
    val xpReward: Int = 10,
    val order: Int = 0
)

data class McqOption(
    val id: String,
    val text: String,
    val isCorrect: Boolean
)

data class QuizAttempt(
    val id: String = java.util.UUID.randomUUID().toString(),
    val userId: String = "demo_user",
    val learningPackId: String,
    val quizType: QuestionType,
    val totalQuestions: Int,
    var currentQuestionIndex: Int = 0,
    var correctCount: Int = 0,
    var wrongCount: Int = 0,
    var scorePercent: Int = 0,
    var xpEarned: Int = 0,
    var isComplete: Boolean = false
)

data class DailyUsage(
    val userId: String,
    val dateString: String,
    var mcqUsed: Int = 0,
    var fibUsed: Int = 0,
    var tfUsed: Int = 0
)
