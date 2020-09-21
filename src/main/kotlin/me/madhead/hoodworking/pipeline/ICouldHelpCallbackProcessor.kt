package me.madhead.hoodworking.pipeline

import com.github.insanusmokrassar.TelegramBotAPI.bot.RequestsExecutor
import com.github.insanusmokrassar.TelegramBotAPI.extensions.api.answers.answerCallbackQuery
import com.github.insanusmokrassar.TelegramBotAPI.extensions.api.deleteMessage
import com.github.insanusmokrassar.TelegramBotAPI.extensions.api.send.sendMessage
import com.github.insanusmokrassar.TelegramBotAPI.types.CallbackQuery.MessageDataCallbackQuery
import com.github.insanusmokrassar.TelegramBotAPI.types.CommonUser
import com.github.insanusmokrassar.TelegramBotAPI.types.update.CallbackQueryUpdate
import com.github.insanusmokrassar.TelegramBotAPI.types.update.abstracts.Update
import me.madhead.hoodworking.entity.chat.state.ChatState
import me.madhead.hoodworking.entity.chat.state.Helpfulness1
import me.madhead.hoodworking.entity.chat.state.Started
import me.madhead.hoodworking.i18n.I18N
import me.madhead.hoodworking.repository.ChatStatesRepository
import org.apache.logging.log4j.LogManager
import java.util.Locale

class ICouldHelpCallbackProcessor(
        private val requestsExecutor: RequestsExecutor,
        private val chatStatesRepository: ChatStatesRepository,
) : UpdateProcessor {
    companion object {
        const val CALLBACK_DATA = "ICouldHelp"
        val logger = LogManager.getLogger(Helpfulness1::class.java)!!
    }

    override suspend fun process(update: Update, chatState: ChatState?): UpdateReaction? {
        @Suppress("NAME_SHADOWING")
        val update = update as? CallbackQueryUpdate ?: return null
        val callbackQuery = update.data as? MessageDataCallbackQuery ?: return null

        if (callbackQuery.data != CALLBACK_DATA) {
            return null
        }
        if (chatState !is Started) {
            return null
        }

        val user = callbackQuery.user as? CommonUser ?: return null

        logger.debug("Processing callback for {}", user)

        val locale = Locale(user.languageCode)

        return {
            requestsExecutor.answerCallbackQuery(
                    callbackQuery = callbackQuery,
            )
            requestsExecutor.deleteMessage(callbackQuery.message)
            requestsExecutor.sendMessage(
                    chat = callbackQuery.message.chat,
                    text = I18N.messages(locale).actionHelpfulness1Prompt()
            )
            chatStatesRepository.save(Helpfulness1(user.id.chatId))
        }
    }
}
