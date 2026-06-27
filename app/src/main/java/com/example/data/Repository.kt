package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class IncomeTaskRepository(private val db: AppDatabase) {

    val userProfile: Flow<UserProfile?> = db.userProfileDao().getUserProfile()
    val allTasks: Flow<List<Task>> = db.taskDao().getAllTasks()
    val allWithdrawals: Flow<List<Withdrawal>> = db.withdrawalDao().getAllWithdrawals()
    val allNotifications: Flow<List<Notification>> = db.notificationDao().getAllNotifications()
    val allReferrals: Flow<List<Referral>> = db.referralDao().getAllReferrals()

    suspend fun getTasksByCategory(category: String): Flow<List<Task>> {
        return if (category == "All" || category == "ALL") {
            db.taskDao().getAllTasks()
        } else {
            db.taskDao().getTasksByCategory(category)
        }
    }

    suspend fun completeTask(taskId: Int, reward: Double) {
        val currentProfile = db.userProfileDao().getUserProfileSync() ?: UserProfile()
        
        // Update task status
        db.taskDao().updateTaskStatus(taskId, "COMPLETED")

        // Increment earnings
        val newBalance = currentProfile.totalBalance + reward
        val newToday = currentProfile.todayEarnings + reward
        val newLifetime = currentProfile.lifetimeEarnings + reward
        val newXp = currentProfile.xp + 50
        val isLevelUp = newXp >= (currentProfile.level * 1000)
        val newLevel = if (isLevelUp) currentProfile.level + 1 else currentProfile.level

        val updatedProfile = currentProfile.copy(
            totalBalance = newBalance,
            todayEarnings = newToday,
            lifetimeEarnings = newLifetime,
            xp = newXp,
            level = newLevel
        )
        db.userProfileDao().insertOrUpdateUserProfile(updatedProfile)

        // Insert notification
        val taskName = db.taskDao().getTaskById(taskId)?.title ?: "Task"
        db.notificationDao().insertNotification(
            Notification(
                title = "Task Completed!",
                message = "You earned ৳ ${String.format("%.2f", reward)} from $taskName",
                type = "TASK"
            )
        )
    }

    suspend fun requestWithdrawal(amount: Double, account: String, method: String): Boolean {
        val currentProfile = db.userProfileDao().getUserProfileSync() ?: UserProfile()
        if (currentProfile.totalBalance < amount) return false

        // Deduct balance and insert pending withdrawal
        val updatedProfile = currentProfile.copy(
            totalBalance = currentProfile.totalBalance - amount
        )
        db.userProfileDao().insertOrUpdateUserProfile(updatedProfile)

        db.withdrawalDao().insertWithdrawal(
            Withdrawal(
                amount = amount,
                account = account,
                method = method,
                status = "PENDING"
            )
        )

        db.notificationDao().insertNotification(
            Notification(
                title = "Withdrawal Submitted",
                message = "Your withdrawal of ৳ ${String.format("%.2f", amount)} via $method is pending.",
                type = "WITHDRAWAL"
            )
        )
        return true
    }

    suspend fun claimDailyBonus(reward: Double): Boolean {
        val currentProfile = db.userProfileDao().getUserProfileSync() ?: UserProfile()
        val newBalance = currentProfile.totalBalance + reward
        val newToday = currentProfile.todayEarnings + reward
        
        db.userProfileDao().insertOrUpdateUserProfile(
            currentProfile.copy(totalBalance = newBalance, todayEarnings = newToday)
        )

        db.notificationDao().insertNotification(
            Notification(
                title = "Daily Bonus Claimed!",
                message = "You claimed your daily bonus of ৳ ${String.format("%.2f", reward)}",
                type = "BONUS"
            )
        )
        return true
    }

    suspend fun toggleMaintenanceMode(enabled: Boolean) {
        val currentProfile = db.userProfileDao().getUserProfileSync() ?: UserProfile()
        db.userProfileDao().insertOrUpdateUserProfile(
            currentProfile.copy(isMaintenanceMode = enabled)
        )
    }

    suspend fun resetProfileData() {
        val currentProfile = db.userProfileDao().getUserProfileSync() ?: UserProfile()
        db.userProfileDao().insertOrUpdateUserProfile(
            currentProfile.copy(
                totalBalance = 1247.50,
                todayEarnings = 125.60,
                xp = 3450,
                level = 7
            )
        )
    }

    suspend fun checkAndPrepopulate() {
        val currentProfile = db.userProfileDao().getUserProfileSync()
        if (currentProfile == null) {
            // Insert premium profile
            db.userProfileDao().insertOrUpdateUserProfile(UserProfile())
        }

        // Insert initial tasks if empty
        val existingTasks = db.taskDao().getAllTasks().firstOrNull() ?: emptyList()
        if (existingTasks.isEmpty()) {
            val initialTasks = listOf(
                Task(
                    title = "MyBL App Install",
                    description = "Install MyBL app and register to earn instant coins.",
                    reward = 25.0,
                    durationSeconds = 15,
                    category = "Visit",
                    iconName = "mybl",
                    externalUrl = "https://play.google.com/store/apps/details?id=com.banglalink.mybanglalink"
                ),
                Task(
                    title = "bKash Account Create",
                    description = "Create a verified bKash account and log in.",
                    reward = 30.0,
                    durationSeconds = 20,
                    category = "Visit",
                    iconName = "bkash",
                    externalUrl = "https://play.google.com/store/apps/details?id=com.bKash.customerapp"
                ),
                Task(
                    title = "Nagad Account Signup",
                    description = "Register as a customer on the official Nagad application.",
                    reward = 30.0,
                    durationSeconds = 20,
                    category = "Visit",
                    iconName = "nagad",
                    externalUrl = "https://play.google.com/store/apps/details?id=com.konasl.nagad"
                ),
                Task(
                    title = "Foodpanda App Install",
                    description = "Download foodpanda app, sign up and browse restaurants.",
                    reward = 20.0,
                    durationSeconds = 10,
                    category = "Visit",
                    iconName = "foodpanda",
                    externalUrl = "https://play.google.com/store/apps/details?id=com.global.foodpanda.android"
                ),
                Task(
                    title = "Watch Promotional Video 1",
                    description = "Watch an advertising video for 15 seconds to receive your coins.",
                    reward = 10.0,
                    durationSeconds = 15,
                    category = "Watch",
                    iconName = "video",
                    externalUrl = "https://www.w3schools.com/html/mov_bbb.mp4"
                ),
                Task(
                    title = "Watch Tech News Video",
                    description = "Watch this brief 10-second educational clip about mobile technology.",
                    reward = 5.0,
                    durationSeconds = 10,
                    category = "Watch",
                    iconName = "video_tech",
                    externalUrl = "https://media.w3.org/2010/05/sintel/trailer_hd.mp4"
                ),
                Task(
                    title = "Finance & Money Survey",
                    description = "Answer three simple multiple-choice questions about personal savings.",
                    reward = 15.0,
                    durationSeconds = 0,
                    category = "Survey",
                    iconName = "survey"
                ),
                Task(
                    title = "Daily Feedback Survey",
                    description = "Let us know your experience using this app to earn quick cash.",
                    reward = 5.0,
                    durationSeconds = 0,
                    category = "Survey",
                    iconName = "survey_feedback"
                )
            )
            db.taskDao().insertTasks(initialTasks)
        }

        // Prepopulate withdrawals if empty
        val existingWithdrawals = db.withdrawalDao().getAllWithdrawals().firstOrNull() ?: emptyList()
        if (existingWithdrawals.isEmpty()) {
            val sampleWithdrawals = listOf(
                Withdrawal(amount = 500.0, account = "01712345678", method = "bKash", status = "APPROVED", timestamp = System.currentTimeMillis() - 86400000 * 2),
                Withdrawal(amount = 800.0, account = "01812345678", method = "Nagad", status = "APPROVED", timestamp = System.currentTimeMillis() - 86400000 * 4),
                Withdrawal(amount = 300.0, account = "01912345678", method = "Rocket", status = "PENDING", timestamp = System.currentTimeMillis() - 3600000 * 5),
                Withdrawal(amount = 1000.0, account = "123-456-7890", method = "Bank Transfer", status = "APPROVED", timestamp = System.currentTimeMillis() - 86400000 * 10),
                Withdrawal(amount = 700.0, account = "01512345678", method = "bKash", status = "FAILED", timestamp = System.currentTimeMillis() - 86400000 * 15)
            )
            for (w in sampleWithdrawals) {
                db.withdrawalDao().insertWithdrawal(w)
            }
        }

        // Prepopulate notifications if empty
        val existingNotifications = db.notificationDao().getAllNotifications().firstOrNull() ?: emptyList()
        if (existingNotifications.isEmpty()) {
            val sampleNotifications = listOf(
                Notification(title = "Congratulations!", message = "You earned ৳ 25.00 from task", type = "TASK", timestamp = System.currentTimeMillis() - 120000),
                Notification(title = "Task Completed", message = "MyBL App Install completed successfully.", type = "TASK", timestamp = System.currentTimeMillis() - 600000),
                Notification(title = "Daily Bonus", message = "You claimed your daily bonus.", type = "BONUS", timestamp = System.currentTimeMillis() - 3600000),
                Notification(title = "New Referral", message = "Rakib Hasan joined via your link.", type = "BONUS", timestamp = System.currentTimeMillis() - 7200000),
                Notification(title = "Withdrawal Successful", message = "৳ 500 withdrawn successfully.", type = "WITHDRAWAL", timestamp = System.currentTimeMillis() - 86400000),
                Notification(title = "Task Pending", message = "bKash account task pending verification.", type = "TASK", timestamp = System.currentTimeMillis() - 172800000)
            )
            for (n in sampleNotifications) {
                db.notificationDao().insertNotification(n)
            }
        }

        // Prepopulate referrals if empty
        val existingReferrals = db.referralDao().getAllReferrals().firstOrNull() ?: emptyList()
        if (existingReferrals.isEmpty()) {
            val sampleReferrals = listOf(
                Referral(name = "Rakib Hasan", date = "May 20, 2026", amount = 20.0, status = "Active"),
                Referral(name = "Sakib Ahmed", date = "May 18, 2026", amount = 20.0, status = "Active"),
                Referral(name = "Tanvir Ahmed", date = "May 15, 2026", amount = 20.0, status = "Active")
            )
            for (r in sampleReferrals) {
                db.referralDao().insertReferral(r)
            }
        }
    }
}
