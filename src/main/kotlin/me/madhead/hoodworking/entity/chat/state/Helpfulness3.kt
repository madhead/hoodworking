package me.madhead.hoodworking.entity.chat.state

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("Helpfulness3")
data class Helpfulness3(
        override val id: Long,
        val name: String,
        val description: String,
) : ChatState
