package me.madhead.hoodworking.pipeline

import com.github.insanusmokrassar.TelegramBotAPI.bot.RequestsExecutor
import com.github.insanusmokrassar.TelegramBotAPI.extensions.api.answers.answerCallbackQuery
import com.github.insanusmokrassar.TelegramBotAPI.extensions.api.send.sendMessage
import com.github.insanusmokrassar.TelegramBotAPI.types.CallbackQuery.MessageDataCallbackQuery
import com.github.insanusmokrassar.TelegramBotAPI.types.CommonUser
import com.github.insanusmokrassar.TelegramBotAPI.types.update.CallbackQueryUpdate
import com.github.insanusmokrassar.TelegramBotAPI.types.update.abstracts.Update
import me.madhead.hoodworking.entity.chat.state.ChatState
import me.madhead.hoodworking.entity.chat.state.RemovingApplication
import me.madhead.hoodworking.i18n.I18N
import me.madhead.hoodworking.repository.ApplicationsRepository
import me.madhead.hoodworking.repository.ChatStatesRepository
import org.apache.logging.log4j.LogManager
import java.util.Locale

class ApplicationRemoveConformationCallbackProcessor(
        private val requestsExecutor: RequestsExecutor,
        private val chatStatesRepository: ChatStatesRepository,
        private val applicationsRepository: ApplicationsRepository,
) : UpdateProcessor {
    companion object {
        val logger = LogManager.getLogger(ApplicationRemoveConformationCallbackProcessor::class.java)!!
    }

    override suspend fun process(update: Update, chatState: ChatState?): UpdateReaction? {
        @Suppress("NAME_SHADOWING")
        val update = update as? CallbackQueryUpdate ?: return null
        val callbackQuery = update.data as? MessageDataCallbackQuery ?: return null
        val user = callbackQuery.user as? CommonUser ?: return null

        if (chatState !is RemovingApplication) {
            return null
        }

        logger.debug("Processing callback for {}, application {}", user, chatState.applicationId)

        val locale = Locale(user.languageCode)

        return {
            requestsExecutor.answerCallbackQuery(
                    callbackQuery = callbackQuery,
            )
            if (callbackQuery.data == "✅") {
                applicationsRepository.delete(chatState.applicationId)
                requestsExecutor.sendMessage(
                        chat = callbackQuery.message.chat,
                        text = I18N.messages(locale).actionApplicationsRemoved(),
                )
            }
        }
    }
}
