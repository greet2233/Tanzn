package com.example.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.ui.screens.onboarding.*
import com.example.ui.screens.main.*
import com.example.ui.screens.study.*
import com.example.ui.screens.admin.*
import com.example.ui.viewmodels.CurriculumViewModel
import kotlinx.serialization.Serializable

@Serializable data class PackListRoute(val topicId: String, val topicName: String, val subjectId: String)
@Serializable data class PackDetailRoute(val packId: String, val packName: String)

@Serializable data class QuizIntroRoute(val packId: String, val quizType: String)
@Serializable data class QuizQuestionRoute(val attemptId: String)
@Serializable data class QuizResultRoute(val attemptId: String)
@Serializable object AdminQuizGeneratorRoute

@Serializable object DailyMissionsRoute
@Serializable object RewardsRoute

@Serializable object SubscriptionRoute
@Serializable data class PaymentMethodRoute(val planId: String)

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    // Shared ViewModel for curriculum browsing
    val curriculumViewModel: CurriculumViewModel = viewModel()
    val quizViewModel: com.example.ui.viewmodels.QuizViewModel = viewModel()
    val gamificationViewModel: com.example.ui.viewmodels.GamificationViewModel = viewModel()
    var showPremiumModal by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = SplashRoute,
            modifier = Modifier.fillMaxSize()
        ) {
        composable<SplashRoute> {
            SplashScreen(onNavigateNext = {
                navController.navigate(WelcomeRoute) {
                    popUpTo<SplashRoute> { inclusive = true }
                }
            })
        }
        
        composable<WelcomeRoute> {
            WelcomeScreen(onNavigateNext = {
                navController.navigate(OtpLoginRoute)
            })
        }
        
        composable<OtpLoginRoute> {
            OtpLoginScreen(onNavigateNext = { phone ->
                navController.navigate(OtpVerificationRoute(phone))
            })
        }
        
        composable<OtpVerificationRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<OtpVerificationRoute>()
            OtpVerificationScreen(onNavigateNext = {
                navController.navigate(CreateProfileRoute) {
                    popUpTo<WelcomeRoute> { inclusive = true }
                }
            })
        }
        
        composable<CreateProfileRoute> {
            CreateProfileScreen(onNavigateNext = {
                navController.navigate(SubjectSelectionRoute)
            })
        }
        
        composable<SubjectSelectionRoute> {
            SubjectSelectionScreen(onNavigateNext = {
                navController.navigate(MainNavRoute) {
                    popUpTo<SubjectSelectionRoute> { inclusive = true }
                }
            })
        }
        
        composable<MainNavRoute> {
            MainCoordinatorScreen(
                curriculumViewModel = curriculumViewModel,
                gamificationViewModel = gamificationViewModel,
                onNavigateToTopic = { id, name -> navController.navigate(TopicRoute(id, name)) },
                onNavigateToSettings = { navController.navigate(SettingsRoute) },
                onNavigateToSubscription = { navController.navigate(SubscriptionRoute) },
                onNavigateToMissions = { navController.navigate(DailyMissionsRoute) },
                onNavigateToRewards = { navController.navigate(RewardsRoute) }
            )
        }
        
        composable<TopicRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<TopicRoute>()
            TopicScreen(
                subjectId = route.subjectId,
                subjectName = route.subjectName,
                viewModel = curriculumViewModel,
                onNavigateBack = { navController.navigateUp() },
                onNavigateToPackList = { topicId, topicName -> 
                    navController.navigate(PackListRoute(topicId, topicName, route.subjectId)) 
                }
            )
        }
        
        composable<PackListRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<PackListRoute>()
            PackListScreen(
                topicId = route.topicId,
                topicName = route.topicName,
                subjectId = route.subjectId,
                viewModel = curriculumViewModel,
                onNavigateBack = { navController.navigateUp() },
                onNavigateToDetail = { packId, packName ->
                    navController.navigate(PackDetailRoute(packId, packName))
                }
            )
        }
        
        composable<PackDetailRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<PackDetailRoute>()
            LearningPackScreen(
                packId = route.packId,
                packName = route.packName,
                viewModel = curriculumViewModel,
                onNavigateBack = { navController.navigateUp() },
                onNavigateToQuizIntro = { pId, qType -> navController.navigate(QuizIntroRoute(pId, qType)) },
                onShowPremiumModal = { showPremiumModal = true }
            )
        }

        composable<QuizIntroRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<QuizIntroRoute>()
            val type = com.example.domain.models.QuestionType.valueOf(route.quizType)
            QuizIntroScreen(
                packId = route.packId,
                quizType = type,
                viewModel = quizViewModel,
                onNavigateBack = { navController.navigateUp() },
                onStartQuiz = { 
                    navController.navigate(QuizQuestionRoute("current")) {
                        popUpTo<PackDetailRoute> { inclusive = false }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable<QuizQuestionRoute> {
            QuizQuestionScreen(
                viewModel = quizViewModel,
                onNavigateToResult = {
                    navController.navigate(QuizResultRoute("current")) {
                        popUpTo<PackDetailRoute> { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable<QuizResultRoute> {
            val state by quizViewModel.state.collectAsState()
            androidx.compose.runtime.LaunchedEffect(Unit) {
                state.attempt?.let {
                    gamificationViewModel.awardXP(it.xpEarned)
                    gamificationViewModel.awardCoins(it.correctCount * 2)
                    gamificationViewModel.addMissionProgress("questions_answered", it.totalQuestions)
                }
            }
            QuizResultScreen(
                viewModel = quizViewModel,
                onNavigateBack = { 
                    navController.popBackStack<MainNavRoute>(inclusive = false)
                },
                onNavigateNext = {
                    navController.popBackStack<MainNavRoute>(inclusive = false)
                }
            )
        }
        
        composable<PackCompletionRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<PackCompletionRoute>()
            androidx.compose.runtime.LaunchedEffect(Unit) {
                gamificationViewModel.awardXP(100)
                gamificationViewModel.awardCoins(20)
                gamificationViewModel.addMissionProgress("pack_complete", 1)
            }
            PackCompletionScreen(
                score = route.score,
                maxScore = route.maxScore,
                onFinish = {
                    navController.popBackStack<MainNavRoute>(inclusive = false)
                }
            )
        }
        
        composable<DailyMissionsRoute> {
            com.example.ui.screens.gamification.DailyMissionsScreen(
                viewModel = gamificationViewModel,
                onNavigateBack = { navController.navigateUp() }
            )
        }
        
        composable<RewardsRoute> {
            com.example.ui.screens.gamification.RewardsScreen(
                viewModel = gamificationViewModel,
                onNavigateBack = { navController.navigateUp() }
            )
        }
        
        composable<SettingsRoute> {
            SettingsScreen(
                onNavigateBack = { navController.navigateUp() },
                onNavigateToAdmin = { navController.navigate(AdminQuizGeneratorRoute) }
            )
        }
        composable<SubscriptionRoute> {
            com.example.ui.screens.subscription.SubscriptionScreen(
                onNavigateBack = { navController.navigateUp() },
                onNavigateToPayment = { planId ->
                    navController.navigate(PaymentMethodRoute(planId))
                }
            )
        }

        composable<PaymentMethodRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<PaymentMethodRoute>()
            com.example.ui.screens.subscription.PaymentMethodScreen(
                planId = route.planId,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        composable<AdminQuizGeneratorRoute> {
            AdminQuizGeneratorScreen(
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }

    if (showPremiumModal) {
        com.example.ui.screens.subscription.PremiumUpgradeModal(
            onDismiss = { showPremiumModal = false },
            onNavigateToSubscription = {
                showPremiumModal = false
                navController.navigate(SubscriptionRoute)
            }
        )
    }
    }
}

