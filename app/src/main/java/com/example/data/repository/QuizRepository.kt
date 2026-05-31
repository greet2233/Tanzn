package com.example.data.repository

import com.example.domain.models.DailyUsage
import com.example.domain.models.McqOption
import com.example.domain.models.Question
import com.example.domain.models.QuestionType
import com.example.domain.models.QuizAttempt

class QuizRepository {

    // Mock usage data
    private var dailyUsage = DailyUsage(userId = "demo_user", dateString = "2023-10-25")
    
    // Limits
    private val limitMcq = 5
    private val limitFib = 3
    private val limitTf = 2
    
    fun getDailyUsage(): DailyUsage = dailyUsage

    fun checkDailyLimit(type: QuestionType, isPremium: Boolean = false): Boolean {
        if (isPremium) return true
        return when (type) {
            QuestionType.MCQ -> dailyUsage.mcqUsed < limitMcq
            QuestionType.FIB -> dailyUsage.fibUsed < limitFib
            QuestionType.TF -> dailyUsage.tfUsed < limitTf
        }
    }

    fun incrementUsage(type: QuestionType) {
        when (type) {
            QuestionType.MCQ -> dailyUsage.mcqUsed++
            QuestionType.FIB -> dailyUsage.fibUsed++
            QuestionType.TF -> dailyUsage.tfUsed++
        }
    }

    fun getQuestions(packId: String, type: QuestionType): List<Question> {
        // Return dummy questions based on type
        return when (type) {
            QuestionType.MCQ -> listOf(
                Question(
                    id = "q_mcq_1",
                    learningPackId = packId,
                    type = type,
                    questionText = "Solve: \\( 2x + 3 = 11 \\)",
                    options = listOf(
                        McqOption("1", "x = 4", true),
                        McqOption("2", "x = 5", false),
                        McqOption("3", "x = 8", false),
                        McqOption("4", "x = 14", false)
                    ),
                    explanation = "1. Subtract 3 from both sides: 2x = 8\n2. Divide by 2: x = 4"
                ),
                Question(
                    id = "q_mcq_2",
                    learningPackId = packId,
                    type = type,
                    questionText = "What is the powerhouse of the cell?",
                    options = listOf(
                        McqOption("1", "Nucleus", false),
                        McqOption("2", "Mitochondria", true),
                        McqOption("3", "Ribosome", false),
                        McqOption("4", "Cell Wall", false)
                    ),
                    explanation = "Mitochondria generates most of the chemical energy needed to power the cell's biochemical reactions."
                )
            )
            QuestionType.FIB -> listOf(
                Question(
                    id = "q_fib_1",
                    learningPackId = packId,
                    type = type,
                    questionText = "The capital city of Tanzania is _____.",
                    acceptedAnswers = listOf("Dodoma", "dodoma"),
                    explanation = "Dodoma was established as the national capital in 1974."
                ),
                Question(
                    id = "q_fib_2",
                    learningPackId = packId,
                    type = type,
                    questionText = "In physics, F = m * _____.",
                    acceptedAnswers = listOf("a", "A", "acceleration", "Acceleration"),
                    explanation = "Newton's second law of motion: Force equals mass times acceleration."
                )
            )
            QuestionType.TF -> listOf(
                Question(
                    id = "q_tf_1",
                    learningPackId = packId,
                    type = type,
                    questionText = "Photosynthesis requires oxygen to occur.",
                    correctAnswerTf = false,
                    explanation = "Photosynthesis requires carbon dioxide, water, and sunlight. It produces oxygen as a byproduct."
                ),
                Question(
                    id = "q_tf_2",
                    learningPackId = packId,
                    type = type,
                    questionText = "Mount Kilimanjaro is the highest mountain in Africa.",
                    correctAnswerTf = true,
                    explanation = "Mount Kilimanjaro is the highest peak in Africa at 5,895 meters above sea level."
                )
            )
        }
    }
}
