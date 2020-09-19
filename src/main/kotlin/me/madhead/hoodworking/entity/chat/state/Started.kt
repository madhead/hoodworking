package me.madhead.hoodworking.entity.chat.state

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Started")
data class Started(
        override val id: Long
) : ChatState
