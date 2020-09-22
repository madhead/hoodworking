package me.madhead.hoodworking.entity.chat.state

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Helpfulness2")
data class Helpfulness2(
        override val id: Long,
        val name: String,
) : ChatState
