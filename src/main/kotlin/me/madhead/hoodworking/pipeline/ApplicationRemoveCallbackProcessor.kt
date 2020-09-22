package me.madhead.hoodworking.pipeline

import com.github.insanusmokrassar.TelegramBotAPI.bot.RequestsExecutor
import com.github.insanusmokrassar.TelegramBotAPI.extensions.api.answers.answerCallbackQuery
import com.github.insanusmokrassar.TelegramBotAPI.extensions.api.send.sendMessage
import com.github.insanusmokrassar.TelegramBotAPI.types.CallbackQuery.MessageDataCallbackQuery
import com.github.insanusmokrassar.TelegramBotAPI.types.CommonUser
import com.github.insanusmokrassar.TelegramBotAPI.types.buttons.InlineKeyboardButtons.CallbackDataInlineKeyboardButton
import com.github.insanusmokrassar.TelegramBotAPI.types.buttons.InlineKeyboardMarkup
import com.github.insanusmokrassar.TelegramBotAPI.types.update.CallbackQueryUpdate
import com.github.insanusmokrassar.TelegramBotAPI.types.update.abstracts.Update
import me.madhead.hoodworking.entity.chat.state.ChatState
import me.madhead.hoodworking.entity.chat.state.Helpfulness1
import me.madhead.hoodworking.entity.chat.state.RemovingApplication
import me.madhead.hoodworking.i18n.I18N
import me.madhead.hoodworking.repository.ChatStatesRepository
import org.apache.logging.log4j.LogManager
import java.util.Locale

class ApplicationRemoveCallbackProcessor(
        private val requestsExecutor: RequestsExecutor,
        private val chatStatesRepository: ChatStatesRepository,
) : UpdateProcessor {
    companion object {
        const val CALLBACK_DATA_PREFIX = "ApplicationRemove"
        val logger = LogManager.getLogger(ApplicationRemoveCallbackProcessor::class.java)!!
    }

    override suspend fun process(update: Update, chatState: ChatState?): UpdateReaction? {
        @Suppress("NAME_SHADOWING")
        val update = update as? CallbackQueryUpdate ?: return null
        val callbackQuery = update.data as? MessageDataCallbackQuery ?: return null

        if (!callbackQuery.data.startsWith(CALLBACK_DATA_PREFIX)) {
            return null
        }

        val application = callbackQuery.data.split(":")[1]
        val user = callbackQuery.user as? CommonUser ?: return null

        logger.debug("Processing callback for {}, application {}", user, application)

        val locale = Locale(user.languageCode)

        return {
            requestsExecutor.answerCallbackQuery(
                    callbackQuery = callbackQuery,
            )
            requestsExecutor.sendMessage(
                    chat = callbackQuery.message.chat,
                    text = I18N.messages(locale).actionApplicationsRemovePrompt(),
                    replyMarkup = InlineKeyboardMarkup(
                            keyboard = listOf(
                                    listOf(
                                            CallbackDataInlineKeyboardButton(
                                                    "✅",
                                                    "✅"
                                            ),
                                            CallbackDataInlineKeyboardButton(
                                                    "❌",
                                                    "❌"
                                            )
                                    )
                            )
                    )
            )
            chatStatesRepository.save(RemovingApplication(user.id.chatId, application))
        }
    }
}
