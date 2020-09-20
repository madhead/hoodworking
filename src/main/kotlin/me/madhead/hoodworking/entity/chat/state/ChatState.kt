package me.madhead.hoodworking.entity.chat.state

import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

interface ChatState {
    companion object {
        val serializers = SerializersModule {
            polymorphic(ChatState::class) {
                subclass(Started::class)
                subclass(Helpfulness1::class)
                subclass(Helpfulness2::class)
                subclass(Helpfulness3::class)
            }
        }
    }

    val id: Long
}
