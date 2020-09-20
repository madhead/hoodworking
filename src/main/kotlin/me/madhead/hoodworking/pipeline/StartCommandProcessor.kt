package me.madhead.hoodworking.pipeline

import com.github.insanusmokrassar.TelegramBotAPI.bot.RequestsExecutor
import com.github.insanusmokrassar.TelegramBotAPI.extensions.api.send.sendMessage
import com.github.insanusmokrassar.TelegramBotAPI.types.CommonUser
import com.github.insanusmokrassar.TelegramBotAPI.types.MessageEntity.textsources.BotCommandTextSource
import com.github.insanusmokrassar.TelegramBotAPI.types.buttons.InlineKeyboardButtons.CallbackDataInlineKeyboardButton
import com.github.insanusmokrassar.TelegramBotAPI.types.buttons.InlineKeyboardMarkup
import com.github.insanusmokrassar.TelegramBotAPI.types.message.CommonMessageImpl
import com.github.insanusmokrassar.TelegramBotAPI.types.message.content.TextContent
import com.github.insanusmokrassar.TelegramBotAPI.types.update.MessageUpdate
import com.github.insanusmokrassar.TelegramBotAPI.types.update.abstracts.Update
import me.madhead.hoodworking.entity.chat.state.Started
import me.madhead.hoodworking.i18n.I18N
import me.madhead.hoodworking.repository.AdminsRepository
import me.madhead.hoodworking.repository.ChatStatesRepository
import java.util.Locale

class StartCommandProcessor(
        private val requestsExecutor: RequestsExecutor,
        private val adminsRepository: AdminsRepository,
        private val chatStatesRepository: ChatStatesRepository,
) : UpdateProcessor {
    companion object {
        val logger = LogManager.getLogger(StartCommandProcessor::class.java)!!
    }

    override suspend fun process(update: Update, chatState: ChatState?): UpdateReaction? {
        @Suppress("NAME_SHADOWING")
        val update = update as? MessageUpdate ?: return null
        val message = update.data as? CommonMessageImpl<*> ?: return null
        val content = (message as? CommonMessageImpl<*>)?.content as? TextContent ?: return null

        content.entities.singleOrNull { "start" == (it.source as? BotCommandTextSource)?.command } ?: return null

        val user = message.user as? CommonUser ?: return null
        val locale = Locale(user.languageCode)
        val keyboard = mutableListOf(
                listOf(
                        CallbackDataInlineKeyboardButton(
                                text = I18N.messages(locale).actionICouldHelp(),
                                callbackData = "ICouldHelp"
                        )
                )
        )

        if (user.id.chatId in adminsRepository.allAdmins()) {
            keyboard += listOf(
                    CallbackDataInlineKeyboardButton(
                            text = I18N.messages(locale).actionAdminApplications(),
                            callbackData = "AdminApplications"
                    )
            )
        }

        return {
            requestsExecutor.sendMessage(
                    chat = message.chat,
                    text = I18N.messages(locale).chooseAnAction(),
                    replyMarkup = InlineKeyboardMarkup(
                            keyboard = keyboard
                    )
            )
            chatStatesRepository.save(Started(user.id.chatId))
        }
    }
}
