package com.puzzleitc.jenkins.command

import com.cloudbees.groovy.cps.NonCPS
import com.jenkinsci.plugins.badge.action.BadgeAction
import com.puzzleitc.jenkins.command.context.JenkinsPipelineContext

class CleanBuildsCommand {
    private final JenkinsPipelineContext ctx

    CleanBuildsCommand(JenkinsPipelineContext ctx) {
        this.ctx = ctx
    }

    void execute() {
        def maxNumberToKeepBuilds = ctx.stepParams.getOptional('maxKeepBuilds', 10)
        def job = ctx.stepParams.getRequired("job")
        def environmentBuildCount = [:]
        def successfulJobRuns = getSuccessfulJobRuns(job)
        successfulJobRuns.each { build ->
            printThis("hallo welt")
        }
    }

    void printThis(String argument) {
        println(argument)
    }

    @NonCPS
    getSuccessfulJobRuns(String jobname) {
        return Jenkins.instance.getItemByFullName(jobname).getBuilds().findAll { it.isKeepLog() }
    }
}
