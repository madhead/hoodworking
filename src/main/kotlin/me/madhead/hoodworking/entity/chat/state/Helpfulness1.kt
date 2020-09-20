package me.madhead.hoodworking.entity.chat.state

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Helpfulness1")
data class Helpfulness1(
        override val id: Long,
) : ChatState
