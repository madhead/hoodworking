package me.madhead.hoodworking.repository

import me.madhead.hoodworking.entity.Application
import org.apache.logging.log4j.LogManager
import javax.sql.DataSource

class ApplicationsRepository(
        private val dataSource: DataSource
) {
    companion object {
        val logger = LogManager.getLogger(ApplicationsRepository::class.java)!!
    }

    fun save(entity: Application) {
        dataSource
                .connection
                .use { connection ->
                    connection
                            .prepareStatement("""
                                INSERT INTO applications ("id", "user_id", "user_name", "helpfulness", "contact")
                                VALUES (?, ?, ?, ?, ?);
                            """.trimIndent())
                            .use { preparedStatement ->
                                preparedStatement.setString(1, entity.id)
                                preparedStatement.setLong(2, entity.userId)
                                preparedStatement.setString(3, entity.userName)
                                preparedStatement.setString(4, entity.helpfulness)
                                preparedStatement.setString(5, entity.contact)
                                preparedStatement.executeUpdate()
                            }
                }
    }

    fun get(id: String): Application? {
        dataSource.connection.use { connection ->
            connection
                    .prepareStatement("SELECT * FROM applications WHERE id = ?;")
                    .use { preparedStatement ->
                        preparedStatement.setString(1, id)
                        preparedStatement.executeQuery().use { resultSet ->
                            return if (resultSet.next()) {
                                Application(
                                        id = resultSet.getString(1),
                                        userId = resultSet.getLong(2),
                                        userName = resultSet.getString(3),
                                        helpfulness = resultSet.getString(4),
                                        contact = resultSet.getString(5)
                                )
                            } else {
                                null
                            }
                        }
                    }
        }
    }
}
