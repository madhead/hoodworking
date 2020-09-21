package me.madhead.hoodworking.repository

import me.madhead.hoodworking.entity.Application
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.postgresql.ds.PGSimpleDataSource

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApplicationsRepositoryTest {
    lateinit var repository: ApplicationsRepository

    @BeforeAll
    fun setUp() {
        repository = ApplicationsRepository(
                PGSimpleDataSource().apply {
                    setUrl("jdbc:postgresql://${System.getenv("POSTGRES_HOST")!!}:${System.getenv("POSTGRES_PORT")!!}/${System.getenv("POSTGRES_DB")!!}")
                    user = System.getenv("POSTGRES_USER")!!
                    password = System.getenv("POSTGRES_PASSWORD")!!
                }
        )
    }

    @Test
    fun `save() should save application correctly`() {
        repository.save(Application("new", 3, "Din Djarin", "Bounty hunting", "mando"))

        Assertions.assertEquals(
                Application("new", 3, "Din Djarin", "Bounty hunting", "mando"),
                repository.get("new")
        )
    }

    @Test
    fun `get() should return correct application`() {
        Assertions.assertEquals(
                Application("test1", 1, "Darth Vader", "Force choking", "@annie_the_dustboy"),
                repository.get("test1")
        )
    }

    @Test
    fun `get() should return all user's applications`() {
        Assertions.assertEquals(
                listOf(
                        Application("test1", 1, "Darth Vader", "Force choking", "@annie_the_dustboy"),
                        Application("test2", 1, "Darth Vader", "Jedi hunting", "@annie_the_dustboy"),
                ),
                repository.get(1)
        )
    }

    @Test
    fun `get() should return empty list for user without applications`() {
        Assertions.assertEquals(
                emptyList<Application>(),
                repository.get(0)
        )
    }

    @Test
    fun `allApplications() should return all applications`() {
        Assertions.assertEquals(
                listOf(
                        Application("test1", 1, "Darth Vader", "Force choking", "@annie_the_dustboy"),
                        Application("test2", 1, "Darth Vader", "Jedi hunting", "@annie_the_dustboy"),
                        Application("test3", 2, "Lil Yo", "Protecting you from mudhorns and scam", "@YodaKid2019"),
                ),
                repository.allApplications()
        )
    }
}
