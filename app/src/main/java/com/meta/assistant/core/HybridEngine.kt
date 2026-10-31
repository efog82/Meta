package com.meta.assistant.core
import com.meta.assistant.data.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.emitAll
class HybridEngine(private val db:AppDatabase,private val genAi:LocalGenAiWrapper?=null,private val onlineApi:OnlineApi?=null){
suspend fun retrieveFacts(query:String):String=db.factDao().search(query).take(5).joinToString("\n"){"[Memória local (não verificada): ${it.key} = ${it.value}]"}
fun generateResponse(prompt:String,mode:AssistantMode,useOnline:Boolean):Flow<String> = flow {
val context=retrieveFacts(prompt)
val fullPrompt=buildString{
append(mode.systemPrompt);append("\n\n")
if(context.isNotBlank())append("Notas fornecidas pelo utilizador:\n$context\n\n")
append("Pergunta: $prompt")
}
if(useOnline){
if(onlineApi==null){emit("Modo online ainda não configurado.");return@flow}
try{emit(onlineApi.complete(fullPrompt,mode.temperature));return@flow}
catch(e:Exception){if(genAi==null){emit("Falha online e nenhum modelo local configurado.");return@flow}}
}
if(genAi==null)emit("Modelo offline ainda não configurado.")
else emitAll(genAi.generate(fullPrompt,mode.temperature))
}
}
interface LocalGenAiWrapper{fun generate(prompt:String,temp:Float):Flow<String>}
interface OnlineApi{suspend fun complete(prompt:String,temp:Float):String}
