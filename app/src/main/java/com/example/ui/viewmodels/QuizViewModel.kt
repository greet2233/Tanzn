package com.example.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.data.repository.QuizRepository
import com.example.domain.models.Question
import com.example.domain.models.QuestionType
import com.example.domain.models.QuizAttempt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class QuizState(
    val attempt: QuizAttempt? = null,
    val questions: List<Question> = emptyList(),
    val currentQuestion: Question? = null,
    val isFeedbackVisible: Boolean = false,
    val feedbackIsCorrect: Boolean = false,
    val feedbackExplanation: String = "",
    val feedbackCorrectAnswer: String = "",
    val earnedXpThisQuestion: Int = 0,
    val isShowingLimitModal: Boolean = false,
    val wrongQuestions: List<Question> = emptyList()
)

class QuizViewModel : ViewModel() {
    private val repository = QuizRepository()
    private val _state = MutableStateFlow(QuizState())
    val state: StateFlow<QuizState> = _state.asStateFlow()

    fun checkAccess(type: QuestionType): Boolean {
        // Mock free user check
        val hasAccess = repository.checkDailyLimit(type, isPremium = false)
        if (!hasAccess) {
            _state.update { it.copy(isShowingLimitModal = true) }
        }
        return hasAccess
    }

    fun dismissLimitModal() {
        _state.update { it.copy(isShowingLimitModal = false) }
    }

    fun startQuiz(packId: String, type: QuestionType) {
        val questions = repository.getQuestions(packId, type)
        val attempt = QuizAttempt(
            learningPackId = packId,
            quizType = type,
            totalQuestions = questions.size
        )
        _state.update { 
            QuizState(
                attempt = attempt,
                questions = questions,
                currentQuestion = questions.firstOrNull(),
                wrongQuestions = emptyList()
            )
        }
    }

    fun checkAnswerMcq(optionId: String) {
        val q = _state.value.currentQuestion ?: return
        val option = q.options.find { it.id == optionId }
        val isCorrect = option?.isCorrect == true
        val correctOpt = q.options.find { it.isCorrect }?.text ?: ""
        showFeedback(isCorrect, q, correctOpt)
    }

    fun checkAnswerFib(text: String) {
        val q = _state.value.currentQuestion ?: return
        val isCorrect = q.acceptedAnswers.any { it.equals(text.trim(), ignoreCase = true) }
        val correctAns = q.acceptedAnswers.firstOrNull() ?: ""
        showFeedback(isCorrect, q, correctAns)
    }

    fun checkAnswerTf(isTrue: Boolean) {
        val q = _state.value.currentQuestion ?: return
        val isCorrect = q.correctAnswerTf == isTrue
        val correctAns = if (q.correctAnswerTf == true) "True" else "False"
        showFeedback(isCorrect, q, correctAns)
    }

    private fun showFeedback(isCorrect: Boolean, q: Question, correctAnswerStr: String) {
        val attempt = _state.value.attempt ?: return
        
        var xp = 0
        val updatedWrongQuestions = if (isCorrect) {
            _state.value.wrongQuestions
        } else {
            _state.value.wrongQuestions + q
        }

        if (isCorrect) {
            attempt.correctCount++
            xp = q.xpReward
            attempt.xpEarned += xp
        } else {
            attempt.wrongCount++
        }
        
        // Track usage
        repository.incrementUsage(q.type)

        _state.update { 
            it.copy(
                isFeedbackVisible = true,
                feedbackIsCorrect = isCorrect,
                feedbackExplanation = q.explanation,
                feedbackCorrectAnswer = correctAnswerStr,
                earnedXpThisQuestion = xp,
                attempt = attempt,
                wrongQuestions = updatedWrongQuestions
            )
        }
    }

    fun nextQuestion(onComplete: () -> Unit = {}) {
        val attempt = _state.value.attempt ?: return
        var nextIdx = attempt.currentQuestionIndex + 1
        
        if (nextIdx >= attempt.totalQuestions) {
            // Finish
            attempt.isComplete = true
            attempt.scorePercent = if (attempt.totalQuestions > 0) 
                (attempt.correctCount * 100) / attempt.totalQuestions else 0
                
            _state.update { 
                it.copy(
                    isFeedbackVisible = false,
                    attempt = attempt
                )
            }
            onComplete()
        } else {
            attempt.currentQuestionIndex = nextIdx
            val nextQ = _state.value.questions.getOrNull(nextIdx)
            _state.update {
                it.copy(
                    attempt = attempt,
                    currentQuestion = nextQ,
                    isFeedbackVisible = false,
                    feedbackExplanation = "",
                    feedbackCorrectAnswer = "",
                    earnedXpThisQuestion = 0
                )
            }
        }
    }
}
