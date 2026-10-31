package com.meta.assistant
import android.app.Application
import androidx.room.Room
import com.meta.assistant.data.AppDatabase
class MetaApp : Application() {
lateinit var db: AppDatabase
private set
override fun onCreate() {
super.onCreate()
db = Room.databaseBuilder(this, AppDatabase::class.java, "meta.db").build()
instance = this
}
companion object { lateinit var instance: MetaApp; private set }
}
