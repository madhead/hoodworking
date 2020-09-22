package me.madhead.hoodworking.repository

import me.madhead.hoodworking.entity.chat.state.Helpfulness1
import me.madhead.hoodworking.entity.chat.state.Helpfulness2
import me.madhead.hoodworking.entity.chat.state.Helpfulness3
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
    fun `save() should save Started state correctly`() {
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

    @Test
    fun `save() should save Helpfulness1 state correctly`() {
        repository.save(Helpfulness1(-2))

        Assertions.assertEquals(
                Helpfulness1(-2),
                repository.get(-2)
        )
    }

    @Test
    fun `get() should return correct Helpfulness1 state`() {
        Assertions.assertEquals(
                Helpfulness1(2),
                repository.get(2)
        )
    }

    @Test
    fun `save() should save Helpfulness2 state correctly`() {
        repository.save(Helpfulness2(-3, "Darth Vader"))

        Assertions.assertEquals(
                Helpfulness2(-3, "Darth Vader"),
                repository.get(-3)
        )
    }

    @Test
    fun `get() should return correct Helpfulness2 state`() {
        Assertions.assertEquals(
                Helpfulness2(3, "Darth Vader"),
                repository.get(3)
        )
    }

    @Test
    fun `save() should save Helpfulness3 state correctly`() {
        repository.save(Helpfulness3(-4, "Darth Vader", "Force choking"))

        Assertions.assertEquals(
                Helpfulness3(-4, "Darth Vader", "Force choking"),
                repository.get(-4)
        )
    }

    @Test
    fun `get() should return correct Helpfulness3 state`() {
        Assertions.assertEquals(
                Helpfulness3(4, "Darth Vader", "Force choking"),
                repository.get(4)
        )
    }

    @Test
    fun `delete() should delete states`() {
        repository.save(Started(0))

        Assertions.assertEquals(
                Started(0),
                repository.get(0)
        )

        repository.delete(0)

        Assertions.assertNull(repository.get(0))
    }
}
