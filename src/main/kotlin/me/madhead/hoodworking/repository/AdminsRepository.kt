package me.madhead.hoodworking.repository

import org.apache.logging.log4j.LogManager
import javax.sql.DataSource

class AdminsRepository(
        private val dataSource: DataSource
) {
    companion object {
        val logger = LogManager.getLogger(AdminsRepository::class.java)!!
    }

    fun getAllAdmins(): List<Long> {
        dataSource.connection.use { connection ->
            connection
                    .prepareStatement("SELECT * FROM admins;")
                    .use { preparedStatement ->
                        preparedStatement.executeQuery().use { resultSet ->
                            return resultSet.use {
                                generateSequence {
                                    if (resultSet.next()) resultSet.getLong(1) else null
                                }.toList()
                            }
                        }
                    }
        }
    }
}
