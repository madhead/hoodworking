package me.madhead.hoodworking.entity.chat.state

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("RemovingApplication")
data class RemovingApplication(
        override val id: Long,
        val applicationId: String
) : ChatState
