package me.madhead.hoodworking.repository

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.postgresql.ds.PGSimpleDataSource
import java.lang.System.getenv as env

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AdminsRepositoryTest {
    lateinit var repository: AdminsRepository

    @BeforeAll
    fun setUp() {
        repository = AdminsRepository(
                PGSimpleDataSource().apply {
                    setUrl("jdbc:postgresql://${env("POSTGRES_HOST")!!}:${env("POSTGRES_PORT")!!}/${env("POSTGRES_DB")!!}")
                    user = env("POSTGRES_USER")!!
                    password = env("POSTGRES_PASSWORD")!!
                }
        )
    }

    @Test
    fun `allAdmins() should return a list of admins`() {
        Assertions.assertEquals(
                listOf<Long>(1, 2, 3),
                repository.allAdmins()
        )
    }
}
