package com.example.data

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// 1. User Profile & Wallet Entity
@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val name: String = "Jahid Hasan",
    val username: String = "jahid_hasan",
    val email: String = "xrhasan05@gmail.com",
    val phone: String = "+8801712345678",
    val referralCode: String = "12568754",
    val level: Int = 7,
    val xp: Int = 3450,
    val totalBalance: Double = 1247.50,
    val todayEarnings: Double = 125.60,
    val weeklyEarnings: Double = 450.00,
    val monthlyEarnings: Double = 1850.00,
    val lifetimeEarnings: Double = 12450.00,
    val isMaintenanceMode: Boolean = false
)

// 2. Task Entity
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val reward: Double,
    val durationSeconds: Int,
    val category: String, // "VISIT", "WATCH", "SURVEY"
    val status: String = "NOT_STARTED", // "NOT_STARTED", "IN_PROGRESS", "COMPLETED"
    val iconName: String, // e.g. "mybl", "bkash", "nagad", "video", "survey"
    val externalUrl: String = "https://example.com"
)

// 3. Withdrawal Entity
@Entity(tableName = "withdrawals")
data class Withdrawal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Double,
    val account: String,
    val method: String, // "bKash", "Nagad", "Rocket", "Bank Transfer"
    val status: String = "PENDING", // "PENDING", "APPROVED", "FAILED"
    val timestamp: Long = System.currentTimeMillis()
)

// 4. Notification Entity
@Entity(tableName = "notifications")
data class Notification(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val message: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val type: String = "TASK" // "TASK", "WITHDRAWAL", "BONUS"
)

// 5. Referral Entity
@Entity(tableName = "referrals")
data class Referral(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val date: String,
    val amount: Double,
    val status: String = "Active" // "Active", "Pending"
)

// DAOs
@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    fun getUserProfile(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = 1 LIMIT 1")
    suspend fun getUserProfileSync(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUserProfile(profile: UserProfile)

    @Update
    suspend fun updateUserProfile(profile: UserProfile)

    @Query("UPDATE user_profile SET totalBalance = :balance, todayEarnings = :today WHERE id = 1")
    suspend fun updateBalance(balance: Double, today: Double)

    @Query("UPDATE user_profile SET isMaintenanceMode = :maintenance WHERE id = 1")
    suspend fun updateMaintenanceMode(maintenance: Boolean)
}

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE category = :category")
    fun getTasksByCategory(category: String): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :id LIMIT 1")
    suspend fun getTaskById(id: Int): Task?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<Task>)

    @Update
    suspend fun updateTask(task: Task)

    @Query("UPDATE tasks SET status = :status WHERE id = :id")
    suspend fun updateTaskStatus(id: Int, status: String)
}

@Dao
interface WithdrawalDao {
    @Query("SELECT * FROM withdrawals ORDER BY timestamp DESC")
    fun getAllWithdrawals(): Flow<List<Withdrawal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWithdrawal(withdrawal: Withdrawal)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<Notification>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: Notification)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllAsRead()
}

@Dao
interface ReferralDao {
    @Query("SELECT * FROM referrals ORDER BY id DESC")
    fun getAllReferrals(): Flow<List<Referral>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReferral(referral: Referral)
}

// Room Database
@Database(
    entities = [UserProfile::class, Task::class, Withdrawal::class, Notification::class, Referral::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun taskDao(): TaskDao
    abstract fun withdrawalDao(): WithdrawalDao
    abstract fun notificationDao(): NotificationDao
    abstract fun referralDao(): ReferralDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "income_task_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
