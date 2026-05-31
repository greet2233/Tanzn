package com.example.domain.services

import com.example.domain.models.Question
import com.example.domain.models.QuestionType

class PromptTemplateService {

    fun getPromptTemplate(subjectName: String): String {
        return """
            You are an expert Tanzania O-Level and A-Level secondary school teacher. 
            Your goal is to explain quiz answers with extreme clarity, care, and pedagogic brilliance so that any student can understand.
            
            You must output the response STRICTLY as a single valid JSON object containing exactly the following keys, with no markdown wrapping (i.e. do NOT include ```json ... ```):
            {
               "explanationText": "A simple, friendly, easy-to-understand explanation of the core concept addressed in the question.",
               "whyCorrect": "A clear description of why the correct answer is correct.",
               "whyWrong": "An explanation of why the wrong choices are wrong or what mistakes a student might have made to choose them.",
               "examTip": "A strategic exam tip or gold standard advice specifically for answering similar questions in national NECTA exams.",
               "memoryTrick": "A fun mnemonic, visual description, or memory hook to remember this concept forever.",
               "stepByStep": [
                   "Step 1: description of step 1",
                   "Step 2: description of step 2"
               ],
               "latexBlocks": [
                   "Formula or equation 1",
                   "Formula or equation 2"
               ]
            }
            
            Important Rules:
            1. Use simple, direct, student-friendly O-level English. Avoid overly dense language.
            2. If the subject is Mathematics or Physics, the "stepByStep" list must detail each step mathematically. 
            3. Formulas and mathematical equations MUST use clean LaTeX notation with double backslashes for escaping. Example: `\\[2x + 3 = 11\\]` or inline standard equations.
            4. Make sure that all options are explained clearly.
            5. Return ONLY the raw JSON string. DO NOT include any other text, greeting, or wrapping codes.
        """.trimIndent()
    }

    fun buildExplanationPrompt(question: Question, subjectName: String, formName: String): String {
        val optionsStr = when (question.type) {
            QuestionType.MCQ -> question.options.joinToString("\n") { "Option ${it.id}: ${it.text} (Correct: ${it.isCorrect})" }
            QuestionType.FIB -> "FIB: Allowed Answers: ${question.acceptedAnswers.joinToString(", ")}"
            QuestionType.TF -> "True or False question. Correct answer is: ${question.correctAnswerTf}"
        }

        val baseTemplate = getPromptTemplate(subjectName)

        return """
            $baseTemplate
            
            Student Context:
            - School Level: Form $formName
            - Subject: $subjectName
            
            Question Details:
            - Question Text: ${question.questionText}
            - Question Type: ${question.type}
            - Options/Answers:
            $optionsStr
            
            Now generate the explanation in JSON format.
        """.trimIndent()
    }
}
