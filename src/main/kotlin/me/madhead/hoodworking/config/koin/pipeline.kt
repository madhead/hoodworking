package me.madhead.hoodworking.config.koin

import me.madhead.hoodworking.pipeline.StartCommandProcessor
import me.madhead.hoodworking.pipeline.UpdateProcessingPipeline
import org.koin.dsl.module

val pipelineModule = module {
    single {
        StartCommandProcessor(
                get(),
                get()
        )
    }
    single {
        UpdateProcessingPipeline(
                listOf(
                        get<StartCommandProcessor>(),
                )
        )
    }
}
