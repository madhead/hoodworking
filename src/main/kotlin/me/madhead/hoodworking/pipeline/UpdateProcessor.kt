package me.madhead.hoodworking.pipeline

import com.github.insanusmokrassar.TelegramBotAPI.types.update.abstracts.Update
import me.madhead.hoodworking.entity.chat.state.ChatState

interface UpdateProcessor {
    suspend fun process(update: Update, chatState: ChatState?): UpdateReaction?
}
