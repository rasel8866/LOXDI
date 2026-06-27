package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

// --- REACTIVE STATE MANAGER SINGLETON ---

object AppStateManager {
    private lateinit var prefs: SharedPreferences

    // Global states
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    private val _currentUser = MutableStateFlow<UserProfile?>(null)
    val currentUser = _currentUser.asStateFlow()

    private val _isMaintenanceMode = MutableStateFlow(false)
    val isMaintenanceMode = _isMaintenanceMode.asStateFlow()

    private val _isSoundEnabled = MutableStateFlow(true)
    val isSoundEnabled = _isSoundEnabled.asStateFlow()

    private val _isVibrationEnabled = MutableStateFlow(true)
    val isVibrationEnabled = _isVibrationEnabled.asStateFlow()

    private val _language = MutableStateFlow("English")
    val language = _language.asStateFlow()

    // Financial balances
    private val _totalBalance = MutableStateFlow(1247.50)
    val totalBalance = _totalBalance.asStateFlow()

    private val _todayEarnings = MutableStateFlow(125.60)
    val todayEarnings = _todayEarnings.asStateFlow()

    // Data lists
    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks = _tasks.asStateFlow()

    private val _withdrawHistory = MutableStateFlow<List<Withdrawal>>(emptyList())
    val withdrawHistory = _withdrawHistory.asStateFlow()

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications = _notifications.asStateFlow()

    fun init(context: Context) {
        prefs = context.getSharedPreferences("IncomeTaskPrefs", Context.MODE_PRIVATE)
        
        // Load persistences
        _isLoggedIn.value = prefs.getBoolean("isLoggedIn", true) // Default login for instant preview!
        _isMaintenanceMode.value = prefs.getBoolean("isMaintenanceMode", false)
        _isSoundEnabled.value = prefs.getBoolean("isSoundEnabled", true)
        _isVibrationEnabled.value = prefs.getBoolean("isVibrationEnabled", true)
        _language.value = prefs.getString("language", "English") ?: "English"
        _totalBalance.value = prefs.getFloat("totalBalance", 1247.50f).toDouble()
        _todayEarnings.value = prefs.getFloat("todayEarnings", 125.60f).toDouble()

        // Seed current user
        _currentUser.value = UserProfile(
            name = prefs.getString("fullName", "Jahid Hasan") ?: "Jahid Hasan",
            username = prefs.getString("username", "jahid_hasan") ?: "jahid_hasan",
            email = prefs.getString("email", "xrhasan05@gmail.com") ?: "xrhasan05@gmail.com",
            phone = prefs.getString("phone", "+8801712345678") ?: "+8801712345678",
            totalBalance = _totalBalance.value,
            todayEarnings = _todayEarnings.value
        )

        // Seed tasks
        seedTasks()

        // Seed initial withdrawals
        seedWithdrawals()

        // Seed notifications
        seedNotifications()
    }

    private fun seedTasks() {
        _tasks.value = listOf(
            Task(1, "MyBL App Install & Register", "Install MyBL app and register to earn instant coins.", 25.0, 15, "Visit", "NOT_STARTED", "mybl", "https://play.google.com/store/apps/details?id=com.banglalink.mybanglalink"),
            Task(2, "bKash Account Verify", "Create a verified bKash account and login to claim.", 30.0, 20, "Visit", "NOT_STARTED", "bkash", "https://play.google.com/store/apps/details?id=com.bKash.customerapp"),
            Task(3, "Nagad App Signup", "Register as a customer on the official Nagad application.", 30.0, 20, "Visit", "NOT_STARTED", "nagad", "https://play.google.com/store/apps/details?id=com.konasl.nagad"),
            Task(4, "Foodpanda App Campaign", "Download foodpanda, register and browse restaurant listings.", 20.0, 10, "Visit", "NOT_STARTED", "foodpanda", "https://play.google.com/store/apps/details?id=com.global.foodpanda.android"),
            Task(5, "Watch Video Sponsor Ad 1", "Watch an advertising review video for 15s to claim free coins.", 10.0, 15, "Watch", "NOT_STARTED", "video", "https://www.w3schools.com/html/mov_bbb.mp4"),
            Task(6, "Watch Tech Review Clip", "Watch this short clip about tech advancements for 10s.", 5.0, 10, "Watch", "NOT_STARTED", "video_tech", "https://media.w3.org/2010/05/sintel/trailer_hd.mp4"),
            Task(7, "Personal Finance Survey", "Answer 3 quick multiple-choice questions about savings and payment preferences.", 15.0, 0, "Survey", "NOT_STARTED", "survey", ""),
            Task(8, "Daily App Experience Survey", "Provide short feedback to help us optimize the premium theme interface.", 5.0, 0, "Survey", "NOT_STARTED", "survey_feedback", "")
        )
    }

    private fun seedWithdrawals() {
        _withdrawHistory.value = listOf(
            Withdrawal(1, 500.0, "01712345678", "bKash", "Approved", System.currentTimeMillis() - 172800000),
            Withdrawal(2, 800.0, "01812345678", "Nagad", "Approved", System.currentTimeMillis() - 86400000),
            Withdrawal(3, 300.0, "01912345678", "Rocket", "PENDING", System.currentTimeMillis() - 18000000)
        )
    }

    private fun seedNotifications() {
        _notifications.value = listOf(
            Notification(1, "Congratulations! 🎉", "You claimed your ৳ 15.00 daily login bonus successfully.", System.currentTimeMillis(), false, "BONUS"),
            Notification(2, "Task Approved! ✔", "Sponsor verified your 'MyBL App Install' campaign.", System.currentTimeMillis() - 3600000, false, "TASK"),
            Notification(3, "Withdrawal Dispatched", "Your Nagad cashout request of ৳ 800.00 is successful.", System.currentTimeMillis() - 14400000, true, "WITHDRAWAL")
        )
    }

    // --- ACTIONS ---

    fun login(email: String, pass: String): Boolean {
        if (email.isNotBlank() && pass.isNotBlank()) {
            _isLoggedIn.value = true
            prefs.edit().putBoolean("isLoggedIn", true).apply()
            return true
        }
        return false
    }

    fun register(name: String, user: String, mail: String, phone: String, code: String): Boolean {
        if (name.isNotBlank() && user.isNotBlank() && mail.isNotBlank()) {
            _currentUser.value = UserProfile(
                name = name,
                username = user,
                email = mail,
                phone = phone,
                referralCode = if (code.isBlank()) "JAHID123" else code,
                totalBalance = _totalBalance.value,
                todayEarnings = _todayEarnings.value
            )
            _isLoggedIn.value = true
            prefs.edit().apply {
                putBoolean("isLoggedIn", true)
                putString("fullName", name)
                putString("username", user)
                putString("email", mail)
                putString("phone", phone)
            }.apply()
            return true
        }
        return false
    }

    fun logout() {
        _isLoggedIn.value = false
        prefs.edit().putBoolean("isLoggedIn", false).apply()
    }

    fun completeTask(taskId: Int) {
        val currentList = _tasks.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == taskId }
        if (index != -1) {
            val task = currentList[index]
            if (task.status != "COMPLETED") {
                // Update task state
                val updatedTask = task.copy(status = "COMPLETED")
                currentList[index] = updatedTask
                _tasks.value = currentList

                // Increment balance
                addBalance(task.reward)
                _todayEarnings.value += task.reward

                // Save to shared preference
                prefs.edit().putFloat("totalBalance", _totalBalance.value.toFloat()).apply()
                prefs.edit().putFloat("todayEarnings", _todayEarnings.value.toFloat()).apply()

                // Add notification
                val newNotif = Notification(
                    id = _notifications.value.size + 1,
                    title = "Task Completed! ৳ ${task.reward}",
                    message = "Earned BDT ৳${task.reward} from ${task.title}.",
                    timestamp = System.currentTimeMillis()
                )
                _notifications.value = listOf(newNotif) + _notifications.value
            }
        }
    }

    fun submitWithdraw(method: String, amount: Double, account: String): Boolean {
        if (_totalBalance.value >= amount) {
            _totalBalance.value -= amount
            prefs.edit().putFloat("totalBalance", _totalBalance.value.toFloat()).apply()

            val newReq = Withdrawal(
                id = _withdrawHistory.value.size + 1,
                method = method,
                amount = amount,
                account = account,
                status = "Pending",
                timestamp = System.currentTimeMillis()
            )
            _withdrawHistory.value = listOf(newReq) + _withdrawHistory.value

            // Add notification
            val newNotif = Notification(
                id = _notifications.value.size + 1,
                title = "Cashout Pending",
                message = "Withdraw request of ৳$amount via $method is pending approval.",
                timestamp = System.currentTimeMillis()
            )
            _notifications.value = listOf(newNotif) + _notifications.value
            return true
        }
        return false
    }

    fun adminApproveWithdraw(requestId: Int) {
        val list = _withdrawHistory.value.toMutableList()
        val index = list.indexOfFirst { it.id == requestId }
        if (index != -1) {
            val updated = list[index].copy(status = "Approved")
            list[index] = updated
            _withdrawHistory.value = list

            // Add notification
            val newNotif = Notification(
                id = _notifications.value.size + 1,
                title = "Cashout Approved! 💸",
                message = "Withdraw of ৳${list[index].amount} via ${list[index].method} is completed.",
                timestamp = System.currentTimeMillis()
            )
            _notifications.value = listOf(newNotif) + _notifications.value
        }
    }

    fun adminRejectWithdraw(requestId: Int) {
        val list = _withdrawHistory.value.toMutableList()
        val index = list.indexOfFirst { it.id == requestId }
        if (index != -1) {
            val updated = list[index].copy(status = "Rejected")
            list[index] = updated
            _withdrawHistory.value = list

            // Return balance
            addBalance(list[index].amount)

            // Add notification
            val newNotif = Notification(
                id = _notifications.value.size + 1,
                title = "Cashout Rejected ✖",
                message = "Withdraw of ৳${list[index].amount} was rejected. Balance returned.",
                timestamp = System.currentTimeMillis()
            )
            _notifications.value = listOf(newNotif) + _notifications.value
        }
    }

    fun addBalance(amount: Double) {
        _totalBalance.value += amount
        prefs.edit().putFloat("totalBalance", _totalBalance.value.toFloat()).apply()
    }

    fun claimDailyBonus(amount: Double) {
        addBalance(amount)
        _todayEarnings.value += amount
        prefs.edit().putFloat("todayEarnings", _todayEarnings.value.toFloat()).apply()

        val newNotif = Notification(
            id = _notifications.value.size + 1,
            title = "Daily Bonus Claimed! 🎉",
            message = "Claimed daily rewards of BDT ৳$amount successfully.",
            timestamp = System.currentTimeMillis()
        )
        _notifications.value = listOf(newNotif) + _notifications.value
    }

    fun checkAdminPin(pin: String): Boolean {
        return pin == "7788"
    }

    fun setMaintenanceMode(enabled: Boolean) {
        _isMaintenanceMode.value = enabled
        prefs.edit().putBoolean("isMaintenanceMode", enabled).apply()
    }

    fun setSoundEnabled(enabled: Boolean) {
        _isSoundEnabled.value = enabled
        prefs.edit().putBoolean("isSoundEnabled", enabled).apply()
    }

    fun setVibrationEnabled(enabled: Boolean) {
        _isVibrationEnabled.value = enabled
        prefs.edit().putBoolean("isVibrationEnabled", enabled).apply()
    }

    fun setLanguage(lang: String) {
        _language.value = lang
        prefs.edit().putString("language", lang).apply()
    }

    fun markAllNotificationsRead() {
        val list = _notifications.value.map { it.copy(isRead = true) }
        _notifications.value = list
    }
}
