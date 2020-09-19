package me.madhead.hoodworking.pipeline

import com.github.insanusmokrassar.TelegramBotAPI.types.update.abstracts.Update

interface UpdateProcessor {
    suspend fun process(update: Update): UpdateReaction?
}
