package me.madhead.hoodworking.pipeline

import com.github.insanusmokrassar.TelegramBotAPI.types.update.abstracts.UpdateDeserializationStrategy
import io.mockk.called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import me.madhead.hoodworking.repository.ChatStatesRepository
import org.junit.jupiter.api.Test

class UpdateProcessingPipelineTest {
    companion object {
        private val json = Json { ignoreUnknownKeys = true }
    }

    @Test
    fun `process() should not fail with empty list of processors`() = runBlocking {
        val chatStatesRepository = mockk<ChatStatesRepository>()

        every { chatStatesRepository.get(1) } returns null

        val pipeline = UpdateProcessingPipeline(emptyList(), chatStatesRepository)

        pipeline.process(
                json.decodeFromString(
                        UpdateDeserializationStrategy,
                        UpdateProcessingPipelineTest::class.java.getResource("UpdateProcessingPipelineTest/update.json").readText()
                )
        )

        verify {
            chatStatesRepository.get(1)
        }
        confirmVerified(chatStatesRepository)
    }

    @Test
    fun `process() should call available processor`() = runBlocking {
        val processor = mockk<UpdateProcessor>()
        val chatStatesRepository = mockk<ChatStatesRepository>()
        val reaction = mockk<UpdateReaction>()

        coEvery { processor.process(any(), any()) } returns reaction
        every { chatStatesRepository.get(1) } returns null
        coEvery { reaction.invoke() } just runs

        val pipeline = UpdateProcessingPipeline(listOf(processor), chatStatesRepository)

        pipeline.process(
                json.decodeFromString(
                        UpdateDeserializationStrategy,
                        UpdateProcessingPipelineTest::class.java.getResource("UpdateProcessingPipelineTest/update.json").readText()
                )
        )

        coVerify {
            chatStatesRepository.get(1)
            reaction.invoke()
        }
        confirmVerified(chatStatesRepository)
        confirmVerified(reaction)
    }

    @Test
    fun `process() should call first available processor`() = runBlocking {
        val processor1 = mockk<UpdateProcessor>()
        val processor2 = mockk<UpdateProcessor>()
        val chatStatesRepository = mockk<ChatStatesRepository>()
        val reaction1 = mockk<UpdateReaction>()
        val reaction2 = mockk<UpdateReaction>()

        coEvery { processor1.process(any(), any()) } returns reaction1
        coEvery { processor2.process(any(), any()) } returns reaction2
        every { chatStatesRepository.get(1) } returns null
        coEvery { reaction1.invoke() } just runs

        val pipeline = UpdateProcessingPipeline(listOf(processor1, processor2), chatStatesRepository)

        pipeline.process(
                json.decodeFromString(
                        UpdateDeserializationStrategy,
                        UpdateProcessingPipelineTest::class.java.getResource("UpdateProcessingPipelineTest/update.json").readText()
                )
        )

        coVerify {
            chatStatesRepository.get(1)
            reaction1.invoke()
            reaction2 wasNot called
        }
        confirmVerified(chatStatesRepository)
        confirmVerified(reaction1)
        confirmVerified(reaction2)
    }
}
