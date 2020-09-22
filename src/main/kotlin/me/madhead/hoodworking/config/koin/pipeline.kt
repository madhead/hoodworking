package me.madhead.hoodworking.config.koin

import me.madhead.hoodworking.pipeline.AdminApplicationsCallbackProcessor
import me.madhead.hoodworking.pipeline.ApplicationRemoveCallbackProcessor
import me.madhead.hoodworking.pipeline.ApplicationRemoveConformationCallbackProcessor
import me.madhead.hoodworking.pipeline.ApplicationsCallbackProcessor
import me.madhead.hoodworking.pipeline.ContactMessageProcessor
import me.madhead.hoodworking.pipeline.HelpfulnessMessageProcessor
import me.madhead.hoodworking.pipeline.ICouldHelpCallbackProcessor
import me.madhead.hoodworking.pipeline.NameMessageProcessor
import me.madhead.hoodworking.pipeline.StartCommandProcessor
import me.madhead.hoodworking.pipeline.UpdateProcessingPipeline
import org.koin.dsl.module

val pipelineModule = module {
    single {
        StartCommandProcessor(
                get(),
                get(),
                get(),
        )
    }
    single {
        ICouldHelpCallbackProcessor(
                get(),
                get(),
        )
    }
    single {
        NameMessageProcessor(
                get(),
                get(),
        )
    }
    single {
        HelpfulnessMessageProcessor(
                get(),
                get(),
        )
    }
    single {
        ContactMessageProcessor(
                get(),
                get(),
                get(),
        )
    }
    single {
        ApplicationsCallbackProcessor(
                get(),
                get(),
                get(),
        )
    }
    single {
        AdminApplicationsCallbackProcessor(
                get(),
                get(),
                get(),
                get(),
        )
    }
    single {
        ApplicationRemoveCallbackProcessor(
                get(),
                get(),
        )
    }
    single {
        ApplicationRemoveConformationCallbackProcessor(
                get(),
                get(),
                get(),
        )
    }
    single {
        UpdateProcessingPipeline(
                listOf(
                        get<StartCommandProcessor>(),
                        get<ICouldHelpCallbackProcessor>(),
                        get<ApplicationsCallbackProcessor>(),
                        get<AdminApplicationsCallbackProcessor>(),
                        get<ApplicationRemoveCallbackProcessor>(),
                        get<ApplicationRemoveConformationCallbackProcessor>(),
                        get<NameMessageProcessor>(),
                        get<HelpfulnessMessageProcessor>(),
                        get<ContactMessageProcessor>(),
                ),
                get(),
        )
    }
}
