package com.example.data.remote.gemini

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class GeminiService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    private val json = Json { ignoreUnknownKeys = true }

    suspend fun generateExplanation(prompt: String): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            Log.e("GeminiService", "API key is missing or is placeholder")
            return@withContext ""
        }

        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

        val requestPayload = buildJsonObject {
            putJsonArray("contents") {
                addJsonObject {
                    putJsonArray("parts") {
                        addJsonObject {
                            put("text", prompt)
                        }
                    }
                }
            }
        }.toString()

        val requestBody = requestPayload.toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errBody = response.body?.string() ?: ""
                    Log.e("GeminiService", "Error response: $errBody")
                    return@withContext ""
                }
                val responseBody = response.body?.string()
                if (responseBody != null) {
                    val root = json.parseToJsonElement(responseBody).jsonObject
                    val text = root["candidates"]?.jsonArray
                        ?.getOrNull(0)?.jsonObject
                        ?.get("content")?.jsonObject
                        ?.get("parts")?.jsonArray
                        ?.getOrNull(0)?.jsonObject
                        ?.get("text")?.jsonPrimitive?.content
                    return@withContext text ?: ""
                }
            }
        } catch (e: IOException) {
            Log.e("GeminiService", "Network error calling Gemini", e)
        } catch (e: Exception) {
            Log.e("GeminiService", "Uncaught exception in Gemini call", e)
        }
        return@withContext ""
    }
}
