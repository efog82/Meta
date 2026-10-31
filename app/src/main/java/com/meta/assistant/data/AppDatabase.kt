package com.meta.assistant.data
import androidx.room.*
@Entity data class ChatMessage(@PrimaryKey(autoGenerate=true) val id:Long=0,val role:String,val content:String,val timestamp:Long=System.currentTimeMillis(),val mode:String)
@Entity data class AgendaItem(@PrimaryKey(autoGenerate=true) val id:Long=0,val title:String,val done:Boolean=false,val createdAt:Long=System.currentTimeMillis())
@Entity data class Note(@PrimaryKey(autoGenerate=true) val id:Long=0,val text:String,val createdAt:Long=System.currentTimeMillis())
@Entity data class StudyItem(@PrimaryKey(autoGenerate=true) val id:Long=0,val topic:String,val summary:String)
@Entity(indices=[Index(value=["key"],unique=true)]) data class Fact(@PrimaryKey(autoGenerate=true) val id:Long=0,val key:String,val value:String,val source:String="user")
@Dao interface ChatDao {
@Query("SELECT * FROM ChatMessage ORDER BY timestamp ASC, id ASC") suspend fun getAll():List<ChatMessage>
@Insert suspend fun insert(msg:ChatMessage)
@Query("DELETE FROM ChatMessage") suspend fun clear()
}
@Dao interface AgendaDao {
@Query("SELECT * FROM AgendaItem ORDER BY createdAt DESC") suspend fun getAll():List<AgendaItem>
@Insert suspend fun insert(item:AgendaItem)
@Update suspend fun update(item:AgendaItem)
@Delete suspend fun delete(item:AgendaItem)
}
@Dao interface NoteDao {
@Query("SELECT * FROM Note ORDER BY createdAt DESC") suspend fun getAll():List<Note>
@Insert suspend fun insert(note:Note)
@Delete suspend fun delete(note:Note)
}
@Dao interface StudyDao {
@Query("SELECT * FROM StudyItem") suspend fun getAll():List<StudyItem>
@Insert suspend fun insert(item:StudyItem)
}
@Dao interface FactDao {
@Query("SELECT * FROM Fact WHERE key LIKE '%' || :query || '%' OR value LIKE '%' || :query || '%'") suspend fun search(query:String):List<Fact>
@Insert(onConflict=OnConflictStrategy.REPLACE) suspend fun upsert(fact:Fact)
@Query("SELECT * FROM Fact") suspend fun getAll():List<Fact>
}
@Database(entities=[ChatMessage::class,AgendaItem::class,Note::class,StudyItem::class,Fact::class],version=1,exportSchema=false)
abstract class AppDatabase:RoomDatabase(){
abstract fun chatDao():ChatDao
abstract fun agendaDao():AgendaDao
abstract fun noteDao():NoteDao
abstract fun studyDao():StudyDao
abstract fun factDao():FactDao
}
