package me.madhead.hoodworking.pipeline

import com.github.insanusmokrassar.TelegramBotAPI.types.update.abstracts.Update
import me.madhead.hoodworking.extensions.chatId
import me.madhead.hoodworking.repository.ChatStatesRepository
import org.apache.logging.log4j.LogManager

class UpdateProcessingPipeline(
        private val processors: List<UpdateProcessor>,
        private val chatStatesRepository: ChatStatesRepository
) {
    companion object {
        val logger = LogManager.getLogger(UpdateProcessingPipeline::class.java)!!
    }

    suspend fun process(update: Update) {
        logger.debug("Processing update: {}", update)
        logger.debug("Chat ID: {}", update.chatId)

        val chatState = chatStatesRepository.get(update.chatId)

        logger.debug("Chat state: {}", chatState)

        processors.mapNotNull { it.process(update, chatState) }.firstOrNull()?.let { reaction ->
            logger.info("Found single suitable processor")

            reaction.invoke()
        } ?: run {
            logger.info("No suitable processors found")
        }
    }
}
