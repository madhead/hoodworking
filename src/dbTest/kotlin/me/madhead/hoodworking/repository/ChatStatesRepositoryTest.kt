package me.madhead.hoodworking.repository

import me.madhead.hoodworking.entity.chat.state.Started
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.postgresql.ds.PGSimpleDataSource
import java.lang.System.getenv as env

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChatStatesRepositoryTest {
    lateinit var repository: ChatStatesRepository

    @BeforeAll
    fun setUp() {
        repository = ChatStatesRepository(
                PGSimpleDataSource().apply {
                    setUrl("jdbc:postgresql://${env("POSTGRES_HOST")!!}:${env("POSTGRES_PORT")!!}/${env("POSTGRES_DB")!!}")
                    user = env("POSTGRES_USER")!!
                    password = env("POSTGRES_PASSWORD")!!
                }
        )
    }

    @Test
    fun `save() should return Started state correctly`() {
        repository.save(Started(-1))

        Assertions.assertEquals(
                Started(-1),
                repository.get(-1)
        )
    }

    @Test
    fun `get() should return correct Started state`() {
        Assertions.assertEquals(
                Started(1),
                repository.get(1)
        )
    }
}
