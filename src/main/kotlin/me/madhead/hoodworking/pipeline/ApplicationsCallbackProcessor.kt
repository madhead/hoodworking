package me.madhead.hoodworking.pipeline

import com.github.insanusmokrassar.TelegramBotAPI.bot.RequestsExecutor
import com.github.insanusmokrassar.TelegramBotAPI.extensions.api.answers.answerCallbackQuery
import com.github.insanusmokrassar.TelegramBotAPI.extensions.api.deleteMessage
import com.github.insanusmokrassar.TelegramBotAPI.extensions.api.send.sendMessage
import com.github.insanusmokrassar.TelegramBotAPI.types.CallbackQuery.MessageDataCallbackQuery
import com.github.insanusmokrassar.TelegramBotAPI.types.CommonUser
import com.github.insanusmokrassar.TelegramBotAPI.types.ParseMode.MarkdownV2
import com.github.insanusmokrassar.TelegramBotAPI.types.update.CallbackQueryUpdate
import com.github.insanusmokrassar.TelegramBotAPI.types.update.abstracts.Update
import com.github.insanusmokrassar.TelegramBotAPI.utils.extensions.escapeMarkdownV2Common
import kotlinx.coroutines.delay
import me.madhead.hoodworking.entity.chat.state.ChatState
import me.madhead.hoodworking.entity.chat.state.Started
import me.madhead.hoodworking.i18n.I18N
import me.madhead.hoodworking.repository.ApplicationsRepository
import me.madhead.hoodworking.repository.ChatStatesRepository
import org.apache.logging.log4j.LogManager
import java.util.Locale

class ApplicationsCallbackProcessor(
        private val requestsExecutor: RequestsExecutor,
        private val chatStatesRepository: ChatStatesRepository,
        private val applicationsRepository: ApplicationsRepository,
) : UpdateProcessor {
    companion object {
        const val CALLBACK_DATA = "Applications"
        val logger = LogManager.getLogger(ApplicationsCallbackProcessor::class.java)!!
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
        val locale = Locale(user.languageCode)

        logger.debug("Processing callback for {}", user)

        return {
            requestsExecutor.answerCallbackQuery(
                    callbackQuery = callbackQuery,
            )
            requestsExecutor.deleteMessage(callbackQuery.message)
            chatStatesRepository.delete(callbackQuery.user.id.chatId)

            val applications = applicationsRepository.get(callbackQuery.user.id.chatId)

            if (applications.isEmpty()) {
                requestsExecutor.sendMessage(
                        chat = callbackQuery.message.chat,
                        text = I18N.messages(locale).actionApplicationsEmpty()
                )
            } else {
                for (application in applications) {
                    requestsExecutor.sendMessage(
                            chat = callbackQuery.message.chat,
                            text = """
                            *${application.helpfulness.escapeMarkdownV2Common()}*

                            ${application.userName.escapeMarkdownV2Common()} / ${application.contact.escapeMarkdownV2Common()}
                        """.trimIndent(),
                            parseMode = MarkdownV2
                    )
                    delay(100)
                }
            }
        }
    }
}
