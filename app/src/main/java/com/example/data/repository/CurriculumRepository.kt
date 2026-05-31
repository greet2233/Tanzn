package com.example.data.repository

import com.example.domain.models.FormLevel
import com.example.domain.models.LearningPack
import com.example.domain.models.StudentProgress
import com.example.domain.models.Subject
import com.example.domain.models.Topic

class CurriculumRepository {

    // Demo Data
    private val forms = listOf(
        FormLevel("f1", "Form 1", 1),
        FormLevel("f2", "Form 2", 2),
        FormLevel("f3", "Form 3", 3),
        FormLevel("f4", "Form 4", 4)
    )

    private val subjects = listOf(
        Subject("s1", "f1", "Mathematics", "Core principles of numbers and algebra", "🔢", isCore = true),
        Subject("s2", "f1", "English", "Grammar, reading, and writing skills", "📝", isCore = true),
        Subject("s3", "f1", "Biology", "Introduction to life sciences", "🧬"),
        Subject("s4", "f1", "Chemistry", "Matter and its properties", "🧪"),
        Subject("s5", "f1", "Physics", "Basics of mechanics and energy", "⚡"),
        Subject("s6", "f1", "Geography", "Physical and human geography", "🌍"),
        Subject("s7", "f1", "History", "Tanzanian and world history", "🏛️"),
        Subject("s8", "f2", "Mathematics", "Advanced algebra and geometry", "🔢", isCore = true, isPremiumLocked = true),
        Subject("s9", "f3", "Mathematics", "Trigonometry and matrices", "🔢", isCore = true),
        Subject("s10", "f4", "Mathematics", "Calculus and statistics", "🔢", isCore = true)
    )

    private val topics = listOf(
        Topic("t1", "f1", "s3", "Cell Structure", "Learn about the building blocks of life.", 1, packCount = 3),
        Topic("t2", "f1", "s3", "Reproduction", "How living organisms multiply.", 2, packCount = 2, difficulty = "Intermediate"),
        Topic("t3", "f1", "s3", "Genetics", "Understanding heredity and DNA.", 3, packCount = 4, difficulty = "Advanced", progress = 40),
        Topic("t4", "f1", "s1", "Numbers", "Fractions, decimals, and percentages.", 1, packCount = 5, progress = 80)
    )

    private val learningPacks = listOf(
        LearningPack("p1", "f1", "s3", "t1", "Introduction to Cells", "Discover what a cell is and its basic components.", 1, 10, "Beginner", completionXP = 50),
        LearningPack("p2", "f1", "s3", "t1", "Plant vs Animal Cells", "The key differences.", 2, 15, "Intermediate", completionXP = 60),
        LearningPack("p3", "f1", "s3", "t1", "Cell Division", "Mitosis and Meiosis basics.", 3, 20, "Advanced", isPremiumLocked = true, completionXP = 100)
    )

    fun getForms(): List<FormLevel> {
        return forms.sortedBy { it.order }
    }

    fun getSubjectsByForm(formId: String): List<Subject> {
        // Return default subjects if none specifically matched for demo purposes, 
        // to ensure UI always has data.
        val filtered = subjects.filter { it.formId == formId && it.isActive }.sortedBy { it.order }
        if (filtered.isNotEmpty()) return filtered
        
        // Fallback: copy Form 1 subjects
        return subjects.filter { it.formId == "f1" }.map { it.copy(id = it.id + "_copy", formId = formId) }
    }

    fun getTopicsBySubject(formId: String, subjectId: String): List<Topic> {
        val filtered = topics.filter { it.formId == formId && it.subjectId == subjectId && it.isActive }.sortedBy { it.order }
        if (filtered.isNotEmpty()) return filtered
        
        // Fallback demo data
        return listOf(
            Topic("demo_t1", formId, subjectId, "Introduction", "Basic concepts.", 1, packCount = 2),
            Topic("demo_t2", formId, subjectId, "Core Principles", "Deep dive.", 2, packCount = 3),
            Topic("demo_t3", formId, subjectId, "Advanced Topics", "Mastery level.", 3, packCount = 1, isPremiumLocked = true)
        )
    }

    fun getLearningPacksByTopic(formId: String, subjectId: String, topicId: String): List<LearningPack> {
        val filtered = learningPacks.filter { it.formId == formId && it.subjectId == subjectId && it.topicId == topicId && it.isActive }.sortedBy { it.order }
        if (filtered.isNotEmpty()) return filtered
        
        // Fallback demo data
        return listOf(
            LearningPack("demo_p1", formId, subjectId, topicId, "Basics 1", "Start here.", 1, 10, "Beginner", completionXP = 20),
            LearningPack("demo_p2", formId, subjectId, topicId, "Practice", "Apply your knowledge.", 2, 15, "Intermediate", completionXP = 40),
            LearningPack("demo_p3", formId, subjectId, topicId, "Exam Style", "NECTA format questions.", 3, 20, "Advanced", isPremiumLocked = true, completionXP = 80)
        )
    }
}
