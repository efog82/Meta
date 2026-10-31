package com.meta.assistant
import android.app.Application
import androidx.room.Room
import com.meta.assistant.data.AppDatabase
import com.meta.assistant.core.MediaPipeLocalEngine

class MetaApp : Application() {
    lateinit var db: AppDatabase
        private set
    lateinit var localEngine: MediaPipeLocalEngine
        private set

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(this, AppDatabase::class.java, "meta.db").build()
        localEngine = MediaPipeLocalEngine(this)
        instance = this
    }
    companion object { lateinit var instance: MetaApp; private set }
}
