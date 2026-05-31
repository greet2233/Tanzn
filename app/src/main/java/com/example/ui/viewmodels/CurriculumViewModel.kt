package com.example.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.example.data.repository.CurriculumRepository
import com.example.domain.models.FormLevel
import com.example.domain.models.LearningPack
import com.example.domain.models.Subject
import com.example.domain.models.Topic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CurriculumState(
    val forms: List<FormLevel> = emptyList(),
    val selectedForm: FormLevel? = null,
    val subjects: List<Subject> = emptyList(),
    val topics: List<Topic> = emptyList(),
    val learningPacks: List<LearningPack> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class CurriculumViewModel : ViewModel() {
    private val repository = CurriculumRepository()
    
    private val _state = MutableStateFlow(CurriculumState())
    val state: StateFlow<CurriculumState> = _state.asStateFlow()

    init {
        loadForms()
    }

    private fun loadForms() {
        val forms = repository.getForms()
        val defaultForm = forms.firstOrNull()
        _state.update { it.copy(forms = forms, selectedForm = defaultForm) }
        defaultForm?.let { loadSubjects(it.id) }
    }

    fun selectForm(formId: String) {
        val form = _state.value.forms.find { it.id == formId } ?: return
        _state.update { it.copy(selectedForm = form, topics = emptyList(), learningPacks = emptyList()) }
        loadSubjects(form.id)
    }

    fun loadSubjects(formId: String) {
        val subjects = repository.getSubjectsByForm(formId)
        _state.update { it.copy(subjects = subjects) }
    }

    fun loadTopics(subjectId: String) {
        val formId = _state.value.selectedForm?.id ?: return
        val topics = repository.getTopicsBySubject(formId, subjectId)
        _state.update { it.copy(topics = topics) }
    }

    fun loadLearningPacks(topicId: String, subjectId: String) {
        val formId = _state.value.selectedForm?.id ?: return
        val packs = repository.getLearningPacksByTopic(formId, subjectId, topicId)
        _state.update { it.copy(learningPacks = packs) }
    }
}
