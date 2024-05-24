package com.imaginai.languageteacher_snapshot_10.Service

import com.aallam.openai.api.BetaOpenAI
import com.aallam.openai.api.assistant.AssistantId
import com.aallam.openai.api.audio.SpeechRequest
import com.aallam.openai.api.audio.TranscriptionRequest
import com.aallam.openai.api.audio.Voice
import com.aallam.openai.api.core.Role
import com.aallam.openai.api.core.Status
import com.aallam.openai.api.file.FileSource
import com.aallam.openai.api.message.MessageContent
import com.aallam.openai.api.message.MessageRequest
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.api.run.RunRequest
import com.aallam.openai.api.thread.ThreadId
import com.aallam.openai.client.OpenAI
import com.imaginai.languageteacher.BuildConfig
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okio.FileSystem
import java.io.File
import okio.Path.Companion.toPath


class openAI_Service(

) {

    var openAI = OpenAI(BuildConfig.OPENAI_API_KEY)



    fun speechToText(audioSource: FileSource):String{

        var userText = ""

        runBlocking {


            val request = TranscriptionRequest(
                audio = audioSource,
                model = ModelId("whisper-1"),
            )
             userText = openAI.transcription(request).text

        }

        return userText

    }

    fun textToSpeech(textInput: String){

        runBlocking {



            val rawAudio = openAI.speech(
                request = SpeechRequest(
                    model = ModelId("tts-1"),
                    input = textInput,
                    voice = Voice.Alloy,
                )
            )

            val directory = File("AI_Audio")
            if (!directory.exists()) {
                directory.mkdir()
            }


            val audioFile = File(directory,"AISpeechOutput.wav")
            audioFile.writeBytes(rawAudio)

        }


    }


    @OptIn(BetaOpenAI::class)
    fun runRequest(threadId: ThreadId, AssistantId: AssistantId, userText: String):String{

        var AssistantTextResponse = ""

        runBlocking {

            openAI.message(
                threadId,
                request = MessageRequest(
                    role = Role.User,
                    content = userText
                )
            )

            val run = openAI.createRun(
                threadId,
                request = RunRequest(
                    assistantId = AssistantId,
                    instructions = "Please address the user as Jane Doe. The user has a premium account.",
                )
            )

            do {
                delay(1500)
                val retrievedRun = openAI.getRun(threadId = threadId, runId = run.id)
            } while (retrievedRun.status != Status.Completed)

            val assistantMessages = openAI.messages(threadId)
            println("\nThe assistant's last response:")
            val lastMessage = assistantMessages.last()
            val textContent = lastMessage.content.first() as? MessageContent.Text ?: error("Expected MessageContent.Text")
            println(textContent.text.value)

            AssistantTextResponse = textContent.text.value
        }

        return AssistantTextResponse


    }

    @OptIn(BetaOpenAI::class)
    fun conversationPipeLine(threadId: String?, AssistantId: String?){

        if (threadId == null || AssistantId == null) {
            throw IllegalArgumentException("Thread ID and Assistant ID must not be null")
        }

        val path = "raw/testUser.wav".toPath()

       // val audioSource = FileSystem.SYSTEM.source(path)

        val fileSource = FileSource(path = path, fileSystem = FileSystem.RESOURCES)


        val userText = speechToText(fileSource)


        val convertedThreadId = threadId.toThreadId()
        val convertedAssistantId = AssistantId.toAssistantId()

        val TeacherResponse = runRequest(convertedThreadId,convertedAssistantId,userText)

        textToSpeech(TeacherResponse)

        System.out.println("Done")



    }

    // String'den AssistantId'ye dönüştürme
    @OptIn(BetaOpenAI::class)
    fun String.toAssistantId(): AssistantId = AssistantId(this)

    // String'den ThreadId'ye dönüştürme
    @OptIn(BetaOpenAI::class)
    fun String.toThreadId(): ThreadId = ThreadId(this)




}