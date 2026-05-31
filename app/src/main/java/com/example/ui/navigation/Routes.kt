package com.example.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object SplashRoute

@Serializable
object WelcomeRoute

@Serializable
object OtpLoginRoute

@Serializable
data class OtpVerificationRoute(val phoneNumber: String)

@Serializable
object CreateProfileRoute

@Serializable
object SubjectSelectionRoute

@Serializable
object MainNavRoute // Base route for the bottom navigation, handles Home, Subjects, etc. internally

@Serializable
data class TopicRoute(val subjectId: String, val subjectName: String)

@Serializable
data class LearningPackRoute(val topicId: String, val packName: String)

@Serializable
data class PackCompletionRoute(val score: Int, val maxScore: Int)

@Serializable
object SettingsRoute
