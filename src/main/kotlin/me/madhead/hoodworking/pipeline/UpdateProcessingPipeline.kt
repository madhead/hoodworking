package me.madhead.hoodworking.pipeline

import com.github.insanusmokrassar.TelegramBotAPI.types.update.abstracts.BaseMessageUpdate
import com.github.insanusmokrassar.TelegramBotAPI.types.update.abstracts.Update
import org.apache.logging.log4j.LogManager

class UpdateProcessingPipeline(
        private val processors: List<UpdateProcessor>,
) {
    companion object {
        val logger = LogManager.getLogger(UpdateProcessingPipeline::class.java)!!
    }

    suspend fun process(update: Update) {
        logger.debug("Processing update: {}", update)
        logger.debug("Chat ID: {}", update.chatId)

        val reactions = processors.mapNotNull { it.process(update) }

        when (reactions.size) {
            0 -> {
                logger.info("No suitable processors found")
            }
            1 -> {
                logger.info("Found single suitable processor")

                reactions.single().invoke()
            }
            else -> throw IllegalArgumentException("More than one processor wants to process the update!")
        }
    }
}

private val Update.chatId: Long
    get() = when (this) {
        is BaseMessageUpdate -> this.data.chat.id.chatId
        else -> throw IllegalArgumentException("Unknown update type")
    }
