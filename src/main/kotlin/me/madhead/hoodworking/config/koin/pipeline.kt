package me.madhead.hoodworking.config.koin

import me.madhead.hoodworking.pipeline.UpdateProcessingPipeline
import org.koin.dsl.module

val pipelineModule = module {
    single {
        UpdateProcessingPipeline(
                emptyList()
        )
    }
}
