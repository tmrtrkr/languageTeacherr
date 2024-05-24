package com.imaginai.languageteacher.Model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.assistant.AssistantRequest
import com.aallam.openai.api.http.Timeout
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.aallam.openai.client.OpenAIConfig
import com.imaginai.languageteacher.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import kotlinx.coroutines.runBlocking
import kotlinx.parcelize.Parcelize
import java.util.UUID
import kotlin.time.Duration.Companion.seconds

@Parcelize
@OptIn(BetaOpenAI::class)
@Entity
data class Teacher(

    @PrimaryKey
    var teacherID: String = UUID.randomUUID().toString(),
    val name: String,
    var language: String,
    var level: String,
    var threadID: String? = null,
) : Parcelable {

 //
     fun createAssistant() {

        val client = HttpClient(OkHttp) {
            install(HttpTimeout) {
                requestTimeoutMillis = 30000
                connectTimeoutMillis = 30000
                socketTimeoutMillis = 30000
            }
        }

        val openAIConfig = OpenAIConfig(
            token = BuildConfig.OPENAI_API_KEY,
            timeout = Timeout(socket = 30.seconds),

        )


        val openAI = OpenAI(openAIConfig)
        val GPTModel = "gpt-4"
        val instructions: String = "You are a personal $language teacher. Your name is $name, The person who wrote" +
                " the texts you received is a student who is learning $language and is at the $level level. " +
                "Chat with him/her. Learn his name at the beginning of the conversation and ask casual questions. " +
                "When the topic is finished or close to being finished, open a new topic and keep the conversation flowing. " +
                "Consider your language based on the level of the student."

        runBlocking {
            @OptIn(BetaOpenAI::class)
            val assistant = openAI.assistant(
                request = AssistantRequest(
                    name = name,
                    instructions = instructions,
                    model = ModelId(GPTModel)
                )
            )
            teacherID = assistant.id.id

            val thread = openAI.thread()
            threadID = thread.id.id

        }
    }


}
