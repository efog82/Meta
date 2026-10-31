package com.meta.assistant.core

import android.content.Context
import android.net.Uri
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.io.File

/**
 * MediaPipe model loader. Only MediaPipe-compatible model bundles are supported.
 * No arbitrary GGUF files are accepted.
 */
class MediaPipeLocalEngine(private val context: Context) : LocalGenAiWrapper {
    private var inference: LlmInference? = null
    private val modelFile = File(context.filesDir, "local_model.task")

    val isLoaded: Boolean get() = inference != null

    suspend fun importModel(uri: Uri) = withContext(Dispatchers.IO) {
        val name = context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val index = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            if (index >= 0 && cursor.moveToFirst()) cursor.getString(index) else null
        } ?: ""
        require(name.lowercase().endsWith(".task")) {
            "Escolhe um modelo .task compatível com MediaPipe; ficheiros GGUF não são suportados."
        }
        val staging = File(context.filesDir, "local_model_import.tmp")
        try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                staging.outputStream().use { output -> input.copyTo(output) }
            } ?: error("Não foi possível ler o ficheiro.")
            require(staging.length() > 0L) { "O modelo está vazio." }
            synchronized(this@MediaPipeLocalEngine) {
                inference?.close()
                inference = null
                if (modelFile.exists()) modelFile.delete()
                check(staging.renameTo(modelFile)) { "Não foi possível guardar o modelo." }
            }
            loadModel()
        } finally {
            if (staging.exists()) staging.delete()
        }
    }

    suspend fun loadSavedModel(): Boolean = withContext(Dispatchers.Default) {
        if (!modelFile.exists()) return@withContext false
        loadModel()
        true
    }

    private suspend fun loadModel() = withContext(Dispatchers.Default) {
        synchronized(this@MediaPipeLocalEngine) {
            inference?.close()
            inference = LlmInference.createFromOptions(
                context,
                LlmInference.LlmInferenceOptions.builder()
                    .setModelPath(modelFile.absolutePath)
                    .setMaxTokens(1024)
                    .build()
            )
        }
    }

    override fun generate(prompt: String, temp: Float): Flow<String> = flow {
        val result = withContext(Dispatchers.Default) {
            synchronized(this@MediaPipeLocalEngine) {
                val model = inference ?: error("Ainda não carregaste um modelo local.")
                model.generateResponse(prompt)
            }
        }
        emit(result)
    }
}
