package com.example.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.IncomeTaskRepository
import com.example.data.Notification
import com.example.data.Referral
import com.example.data.Task
import com.example.data.UserProfile
import com.example.data.Withdrawal
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Screen {
    Splash,
    Welcome,
    Login,
    Register,
    MainContainer,
    ReferAndEarn,
    TaskDetailVisit,
    TaskDetailWatch,
    TaskDetailSurvey,
    History,
    WithdrawHistory,
    Notifications,
    Settings,
    AdminPanel
}

enum class BottomTab {
    Home,
    Tasks,
    Wallet,
    Profile
}

class IncomeTaskViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: IncomeTaskRepository

    // Database states
    val userProfile: StateFlow<UserProfile?>
    val tasks: StateFlow<List<Task>>
    val withdrawals: StateFlow<List<Withdrawal>>
    val notifications: StateFlow<List<Notification>>
    val referrals: StateFlow<List<Referral>>

    // Navigation states
    var currentScreen by mutableStateOf(Screen.Splash)
    var currentTab by mutableStateOf(BottomTab.Home)

    // Form inputs
    var loginEmail by mutableStateOf("xrhasan05@gmail.com")
    var loginPassword by mutableStateOf("••••••••")
    
    var regFullName by mutableStateOf("")
    var regUsername by mutableStateOf("")
    var regEmail by mutableStateOf("")
    var regPhone by mutableStateOf("")
    var regPassword by mutableStateOf("")
    var regConfirmPassword by mutableStateOf("")
    var regReferralCode by mutableStateOf("")

    var withdrawAmount by mutableStateOf("")
    var withdrawAccount by mutableStateOf("")
    var withdrawMethod by mutableStateOf("bKash") // bKash, Nagad, Rocket, Bank Transfer

    // Dynamic notification badge count
    var notificationBadgeCount by mutableIntStateOf(5)

    // Active Task state
    var activeTask by mutableStateOf<Task?>(null)
    var taskTimeRemaining by mutableIntStateOf(0)
    var isTaskTimerRunning by mutableStateOf(false)
    var taskProgress by mutableStateOf(0f)
    var isTaskRewardClaimed by mutableStateOf(false)
    private var timerJob: Job? = null

    // Multi-choice Survey fields
    var selectedSurveyAnswers by mutableStateOf(mutableMapOf<Int, String>())
    var isSurveySubmitted by mutableStateOf(false)

    // UI Feedback Dialog state
    var showNotificationDialog by mutableStateOf(false)
    var notificationTitle by mutableStateOf("")
    var notificationMessage by mutableStateOf("")
    var isSuccessNotification by mutableStateOf(true)

    // Hidden Admin PIN System
    var showAdminPinDialog by mutableStateOf(false)
    var adminPinInput by mutableStateOf("")
    var adminPinError by mutableStateOf("")

    // Simulated Lottie / Shimmer Loading states
    var isPullToRefreshing by mutableStateOf(false)
    var isShimmerLoading by mutableStateOf(false)

    // Animation assistance
    var animateBalanceTrigger by mutableStateOf(false)

    init {
        val database = AppDatabase.getDatabase(application)
        repository = IncomeTaskRepository(database)

        userProfile = repository.userProfile.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

        tasks = repository.allTasks.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        withdrawals = repository.allWithdrawals.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        notifications = repository.allNotifications.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        referrals = repository.allReferrals.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        // Initialize and seed database
        viewModelScope.launch {
            repository.checkAndPrepopulate()
        }

        // Auto-redirect from Splash
        viewModelScope.launch {
            delay(2800)
            currentScreen = Screen.Welcome
        }
    }

    // Auth functions
    fun login() {
        if (loginEmail.isBlank() || loginPassword.isBlank()) {
            showNotification(
                title = "Authentication Failed",
                message = "Please enter both Email/Phone and Password.",
                isSuccess = false
            )
            return
        }
        isShimmerLoading = true
        viewModelScope.launch {
            delay(1200) // Beautiful mock delay
            isShimmerLoading = false
            currentScreen = Screen.MainContainer
            currentTab = BottomTab.Home
            showNotification(
                title = "Welcome Back!",
                message = "Logged in successfully to your Premium Dashboard.",
                isSuccess = true
            )
        }
    }

    fun register() {
        if (regFullName.isBlank() || regUsername.isBlank() || regEmail.isBlank() || regPhone.isBlank() || regPassword.isBlank()) {
            showNotification(
                title = "Registration Failed",
                message = "Please fill out all required fields.",
                isSuccess = false
            )
            return
        }
        if (regPassword != regConfirmPassword) {
            showNotification(
                title = "Registration Failed",
                message = "Passwords do not match.",
                isSuccess = false
            )
            return
        }

        isShimmerLoading = true
        viewModelScope.launch {
            delay(1500)
            // Update user profile
            val db = AppDatabase.getDatabase(getApplication())
            val current = repository.userProfile.stateIn(viewModelScope).value ?: UserProfile()
            db.userProfileDao().insertOrUpdateUserProfile(
                current.copy(
                    name = regFullName,
                    username = regUsername,
                    email = regEmail,
                    phone = regPhone
                )
            )
            isShimmerLoading = false
            currentScreen = Screen.MainContainer
            currentTab = BottomTab.Home
            showNotification(
                title = "Registration Success!",
                message = "Welcome to Income Task! Your premium account is now active.",
                isSuccess = true
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.resetProfileData() // Reset profile values to standard
            currentScreen = Screen.Welcome
            showNotification(
                title = "Logged Out",
                message = "You have logged out of your account.",
                isSuccess = true
            )
        }
    }

    // Refresh function
    fun refreshHome() {
        viewModelScope.launch {
            isPullToRefreshing = true
            delay(1500)
            isPullToRefreshing = false
            animateBalanceTrigger = !animateBalanceTrigger
        }
    }

    // Trigger Active Task
    fun startTask(task: Task) {
        activeTask = task
        isTaskRewardClaimed = false
        taskTimeRemaining = task.durationSeconds
        taskProgress = 1.0f
        
        when (task.category) {
            "Visit" -> {
                currentScreen = Screen.TaskDetailVisit
                startTimer()
            }
            "Watch" -> {
                currentScreen = Screen.TaskDetailWatch
                startTimer()
            }
            "Survey" -> {
                selectedSurveyAnswers = mutableMapOf()
                isSurveySubmitted = false
                currentScreen = Screen.TaskDetailSurvey
            }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        isTaskTimerRunning = true
        val total = activeTask?.durationSeconds ?: 1
        timerJob = viewModelScope.launch {
            while (taskTimeRemaining > 0) {
                delay(1000)
                taskTimeRemaining--
                taskProgress = taskTimeRemaining.toFloat() / total.toFloat()
            }
            isTaskTimerRunning = false
        }
    }

    fun stopTaskTimer() {
        timerJob?.cancel()
        isTaskTimerRunning = false
    }

    fun claimTaskReward() {
        val task = activeTask ?: return
        if (isTaskRewardClaimed) return

        viewModelScope.launch {
            repository.completeTask(task.id, task.reward)
            isTaskRewardClaimed = true
            showNotification(
                title = "Coins Added! ৳${String.format("%.2f", task.reward)}",
                message = "You have successfully completed \"${task.title}\"!",
                isSuccess = true
            )
            // Deduct notification badge count just as interactive element
            if (notificationBadgeCount > 0) {
                notificationBadgeCount--
            }
        }
    }

    // Withdrawals
    fun submitWithdrawal() {
        val amount = withdrawAmount.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            showNotification(
                title = "Withdrawal Rejected",
                message = "Please enter a valid withdraw amount.",
                isSuccess = false
            )
            return
        }

        val limit = if (withdrawMethod == "Bank Transfer") 500.0 else 100000.0 // check limit
        val minLimit = if (withdrawMethod == "Bank Transfer") 500.0 else 100.0
        if (amount < minLimit) {
            showNotification(
                title = "Minimum Limit",
                message = "Minimum withdrawal via $withdrawMethod is ৳ ${String.format("%.2f", minLimit)}",
                isSuccess = false
            )
            return
        }

        if (withdrawAccount.isBlank()) {
            showNotification(
                title = "Information Required",
                message = "Please enter your Account/Card number.",
                isSuccess = false
            )
            return
        }

        viewModelScope.launch {
            val success = repository.requestWithdrawal(amount, withdrawAccount, withdrawMethod)
            if (success) {
                withdrawAmount = ""
                withdrawAccount = ""
                showNotification(
                    title = "Submitted Successfully",
                    message = "৳ ${String.format("%.2f", amount)} withdrawal request submitted via $withdrawMethod.",
                    isSuccess = true
                )
            } else {
                showNotification(
                    title = "Insufficient Balance",
                    message = "Your total balance is too low for this request.",
                    isSuccess = false
                )
            }
        }
    }

    // Daily Bonus click
    fun triggerDailyBonus() {
        viewModelScope.launch {
            val bonusAmount = 15.00
            repository.claimDailyBonus(bonusAmount)
            showNotification(
                title = "Daily Bonus Claimed!",
                message = "৳ ${String.format("%.2f", bonusAmount)} credited to your balance.",
                isSuccess = true
            )
        }
    }

    // Settings Toggle Functions
    fun setMaintenanceMode(enabled: Boolean) {
        viewModelScope.launch {
            repository.toggleMaintenanceMode(enabled)
        }
    }

    // Secret Admin Panel System
    fun verifyAdminPin() {
        if (adminPinInput == "7788" || adminPinInput == "1234") {
            adminPinInput = ""
            adminPinError = ""
            showAdminPinDialog = false
            currentScreen = Screen.AdminPanel
            showNotification(
                title = "Access Granted",
                message = "Welcome to the Secret Admin Web Console.",
                isSuccess = true
            )
        } else {
            adminPinError = "Incorrect PIN! Access Denied."
        }
    }

    // Notifications Utilities
    fun markAllNotificationsRead() {
        viewModelScope.launch {
            val db = AppDatabase.getDatabase(getApplication())
            db.notificationDao().markAllAsRead()
            notificationBadgeCount = 0
            showNotification(
                title = "Notifications Marked",
                message = "All system alerts set to read.",
                isSuccess = true
            )
        }
    }

    // Dynamic visual dialog triggers
    fun showNotification(title: String, message: String, isSuccess: Boolean) {
        notificationTitle = title
        notificationMessage = message
        isSuccessNotification = isSuccess
        showNotificationDialog = true
    }
}
