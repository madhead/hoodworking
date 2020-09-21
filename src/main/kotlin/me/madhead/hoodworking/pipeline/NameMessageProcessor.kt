package me.madhead.hoodworking.pipeline

import com.github.insanusmokrassar.TelegramBotAPI.bot.RequestsExecutor
import com.github.insanusmokrassar.TelegramBotAPI.extensions.api.send.sendMessage
import com.github.insanusmokrassar.TelegramBotAPI.types.CommonUser
import com.github.insanusmokrassar.TelegramBotAPI.types.message.CommonMessageImpl
import com.github.insanusmokrassar.TelegramBotAPI.types.message.content.TextContent
import com.github.insanusmokrassar.TelegramBotAPI.types.update.MessageUpdate
import com.github.insanusmokrassar.TelegramBotAPI.types.update.abstracts.Update
import me.madhead.hoodworking.entity.chat.state.ChatState
import me.madhead.hoodworking.entity.chat.state.Helpfulness1
import me.madhead.hoodworking.entity.chat.state.Helpfulness2
import me.madhead.hoodworking.i18n.I18N
import me.madhead.hoodworking.repository.ChatStatesRepository
import org.apache.logging.log4j.LogManager
import java.util.Locale

class NameMessageProcessor(
        private val requestsExecutor: RequestsExecutor,
        private val chatStatesRepository: ChatStatesRepository,
) : UpdateProcessor {
    companion object {
        val logger = LogManager.getLogger(NameMessageProcessor::class.java)!!
    }

    override suspend fun process(update: Update, chatState: ChatState?): UpdateReaction? {
        if (chatState is Helpfulness1) {
            @Suppress("NAME_SHADOWING")
            val update = update as? MessageUpdate ?: return null
            val message = update.data as? CommonMessageImpl<*> ?: return null
            val content = (message as? CommonMessageImpl<*>)?.content as? TextContent ?: return null
            val user = message.user as? CommonUser ?: return null
            val locale = Locale(user.languageCode)

            logger.debug("Processing message for {}", user)
            logger.info("User {} provided name: {}", user, content.text)

            return {
                requestsExecutor.sendMessage(
                        chat = message.chat,
                        text = I18N.messages(locale).actionHelpfulness2Prompt()
                )
                chatStatesRepository.save(Helpfulness2(user.id.chatId, content.text))
            }
        } else {
            return null
        }
    }
}
