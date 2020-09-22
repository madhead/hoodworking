package me.madhead.hoodworking.repository

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.madhead.hoodworking.entity.chat.state.ChatState
import org.apache.logging.log4j.LogManager
import javax.sql.DataSource

class ChatStatesRepository(
        private val dataSource: DataSource
) {
    companion object {
        val logger = LogManager.getLogger(ChatStatesRepository::class.java)!!
        val json = Json { serializersModule = ChatState.serializers }
    }

    fun save(entity: ChatState) {
        dataSource
                .connection
                .use { connection ->
                    connection
                            .prepareStatement("""
                                INSERT INTO chat_states ("id", "state")
                                VALUES (?, ?::jsonb)
                                ON CONFLICT ("id")
                                    DO UPDATE SET "state" = EXCLUDED."state";
                            """.trimIndent())
                            .use { preparedStatement ->
                                preparedStatement.setLong(1, entity.id)
                                preparedStatement.setString(2, json.encodeToString(entity))
                                preparedStatement.executeUpdate()
                            }
                }
    }

    fun get(id: Long): ChatState? {
        dataSource.connection.use { connection ->
            connection
                    .prepareStatement("SELECT * FROM chat_states WHERE id = ?;")
                    .use { preparedStatement ->
                        preparedStatement.setLong(1, id)
                        preparedStatement.executeQuery().use { resultSet ->
                            return if (resultSet.next()) {
                                logger.debug("stored JSONB {}", resultSet.getString(2))

                                json.decodeFromString<ChatState>(resultSet.getString(2))
                            } else {
                                null
                            }
                        }
                    }
        }
    }

    fun delete(id: Long) {
        dataSource
                .connection
                .use { connection ->
                    connection
                            .prepareStatement("DELETE FROM chat_states WHERE id = ?;")
                            .use { preparedStatement ->
                                preparedStatement.setLong(1, id)
                                preparedStatement.executeUpdate()
                            }
                }
    }
}
