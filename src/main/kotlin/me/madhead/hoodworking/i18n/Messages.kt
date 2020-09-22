package me.madhead.hoodworking.i18n

interface Messages {
    fun chooseAnAction(): String

    fun actionHelpfulness1(): String
    fun actionHelpfulness1Prompt(): String
    fun actionHelpfulness2Prompt(): String
    fun actionHelpfulness3Prompt(): String
    fun actionHelpfulnessFinish(): String

    fun actionApplications(): String
    fun actionApplicationsEmpty(): String
    fun actionApplicationsRemove(): String
    fun actionApplicationsRemovePrompt(): String
    fun actionApplicationsRemoved(): String

    fun actionAdminApplications(): String
}
