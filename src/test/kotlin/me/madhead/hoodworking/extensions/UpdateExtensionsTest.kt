package me.madhead.hoodworking.extensions

import com.github.insanusmokrassar.TelegramBotAPI.types.update.abstracts.Update
import com.github.insanusmokrassar.TelegramBotAPI.types.update.abstracts.UpdateDeserializationStrategy
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.streams.asStream

@TestInstance(Lifecycle.PER_CLASS)
class UpdateExtensionsTest {
    companion object {
        private val json = Json { ignoreUnknownKeys = true }
    }

    @ParameterizedTest
    @MethodSource("knownUpdates")
    fun `chatId should return correct value for known update types`(update: Update, chatId: Long) {
        Assertions.assertEquals(chatId, update.chatId)
    }

    @ParameterizedTest
    @MethodSource("unknownUpdates")
    fun `chatId should throw an exception for unknown update types`(update: Update) {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            update.chatId
        }
    }

    fun knownUpdates(): Stream<Arguments> {
        return sequenceOf<Pair<String, Long>>(
                "001" to 1,
                "002" to 2,
        ).map { (test, expectedChatId) ->
            Arguments.of(
                    json.decodeFromString(
                            UpdateDeserializationStrategy,
                            UpdateExtensionsTest::class.java.getResource("UpdateExtensionsTest/${test}.json").readText()
                    ),
                    expectedChatId,
            )
        }.asStream()
    }

    fun unknownUpdates(): Stream<Arguments> {
        return sequenceOf<Pair<String, Long>>(
                "001" to 1,
        ).map { (test, expectedChatId) ->
            Arguments.of(
                    json.decodeFromString(
                            UpdateDeserializationStrategy,
                            UpdateExtensionsTest::class.java.getResource("UpdateExtensionsTest/${test}.unknown.json").readText()
                    ),
                    expectedChatId,
            )
        }.asStream()
    }
}
