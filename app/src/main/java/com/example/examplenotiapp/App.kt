package com.example.examplenotiapp

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

import com.example.examplenotiapp.database.NewsDatabase

class App: Application() {
    companion object{
        lateinit var db: NewsDatabase
    }

    override fun onCreate() {
        val MIGRATION_1_2: Migration = object : Migration(1,2){
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE Noticias ADD COLUMN link TEXT")
                db.execSQL("ALTER TABLE Noticias ADD COLUMN imageAuthor TEXT")
                db.execSQL("ALTER TABLE Noticias RENAME COLUMN userAuthor TO pubDate")
            }
        }

        val MIGRATION_2_3: Migration = object : Migration(2,3){
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE Noticias ADD COLUMN articleId TEXT")
            }
        }

        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            NewsDatabase::class.java,
            "MyDatabase")
            .addMigrations(MIGRATION_2_3)
            .build()
    }

}
