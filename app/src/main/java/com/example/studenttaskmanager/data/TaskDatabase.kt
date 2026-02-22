package com.example.studenttaskmanager.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Вказуємо сутності, версію БД та вимикаємо експорт схеми для простоти
@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {

    // Абстрактний метод для отримання DAO
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getDatabase(context: Context): TaskDatabase {
            // Використовуємо Singleton, щоб не створювати кілька екземплярів БД
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "student_task_database"
                )
                    /* * Оновлено: використовуємо параметр dropAllTables = true,
                     * щоб уникнути помилки Deprecation.
                     * Це дозволить перестворити базу, якщо ви зміните структуру таблиці Task.
                     */
                    .fallbackToDestructiveMigration(true)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}