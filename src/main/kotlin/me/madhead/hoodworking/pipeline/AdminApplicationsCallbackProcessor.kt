package me.madhead.hoodworking.pipeline

import com.github.insanusmokrassar.TelegramBotAPI.bot.RequestsExecutor
import com.github.insanusmokrassar.TelegramBotAPI.extensions.api.answers.answerCallbackQuery
import com.github.insanusmokrassar.TelegramBotAPI.extensions.api.deleteMessage
import com.github.insanusmokrassar.TelegramBotAPI.extensions.api.send.media.sendDocument
import com.github.insanusmokrassar.TelegramBotAPI.extensions.api.send.sendActionUploadDocument
import com.github.insanusmokrassar.TelegramBotAPI.requests.abstracts.MultipartFile
import com.github.insanusmokrassar.TelegramBotAPI.types.CallbackQuery.MessageDataCallbackQuery
import com.github.insanusmokrassar.TelegramBotAPI.types.update.CallbackQueryUpdate
import com.github.insanusmokrassar.TelegramBotAPI.types.update.abstracts.Update
import com.github.insanusmokrassar.TelegramBotAPI.utils.StorageFile
import com.github.insanusmokrassar.TelegramBotAPI.utils.StorageFileInfo
import io.ktor.utils.io.streams.asInput
import me.madhead.hoodworking.entity.chat.state.ChatState
import me.madhead.hoodworking.entity.chat.state.Started
import me.madhead.hoodworking.repository.AdminsRepository
import me.madhead.hoodworking.repository.ApplicationsRepository
import me.madhead.hoodworking.repository.ChatStatesRepository
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import org.apache.logging.log4j.LogManager
import java.io.ByteArrayOutputStream
import java.io.PrintWriter

class AdminApplicationsCallbackProcessor(
        private val requestsExecutor: RequestsExecutor,
        private val adminsRepository: AdminsRepository,
        private val chatStatesRepository: ChatStatesRepository,
        private val applicationsRepository: ApplicationsRepository,
) : UpdateProcessor {
    companion object {
        const val CALLBACK_DATA = "AdminApplications"
        val logger = LogManager.getLogger(AdminApplicationsCallbackProcessor::class.java)!!
    }

    override suspend fun process(update: Update, chatState: ChatState?): UpdateReaction? {
        @Suppress("NAME_SHADOWING")
        val update = update as? CallbackQueryUpdate ?: return null
        val callbackQuery = update.data as? MessageDataCallbackQuery ?: return null

        if (callbackQuery.data != CALLBACK_DATA) {
            return null
        }
        if (callbackQuery.user.id.chatId !in adminsRepository.allAdmins()) {
            return null
        }
        if (chatState !is Started) {
            return null
        }

        logger.debug("Processing callback for {}", callbackQuery.user)

        return {
            requestsExecutor.answerCallbackQuery(
                    callbackQuery = callbackQuery,
            )
            requestsExecutor.deleteMessage(callbackQuery.message)
            chatStatesRepository.delete(callbackQuery.user.id.chatId)
            requestsExecutor.sendActionUploadDocument(callbackQuery.user.id)

            val out = ByteArrayOutputStream()
            val format = CSVFormat.POSTGRESQL_CSV.withHeader("Name", "Description", "Contact")

            CSVPrinter(PrintWriter(out), format).use { csv ->
                for (application in applicationsRepository.allApplications()) {
                    csv.printRecord(application.userName, application.helpfulness, application.contact)
                }
            }

            val storageFile = StorageFile(StorageFileInfo("text/csv", "applications.csv")) {
                out.toByteArray().inputStream().asInput()
            }

            requestsExecutor.sendDocument(
                    chatId = callbackQuery.user.id,
                    document = MultipartFile(storageFile)
            )
        }
    }
}
