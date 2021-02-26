package com.puzzleitc.jenkins.command

import com.jenkinsci.plugins.badge.action.BadgeAction
import com.puzzleitc.jenkins.command.context.PipelineContext

class CleanBuildsCommand {
    private final PipelineContext ctx

    CleanBuildsCommand(PipelineContext ctx) {
        this.ctx = ctx
    }

    void execute() {
        def maxNumberToKeepBuilds = ctx.stepParams.getOptional('maxKeepBuilds', 10) as Integer
        def environmentBuildCount = [:]
        def job = ctx.stepParams.getRequired("job")
        def getAllSuccessfulBuilds = Jenkins.instance.getItemByFullName(job)//.getBuilds().findAll { it.isKeepLog() }

        ctx.info("Keeping build ${getAllSuccessfulBuilds}")
    }
}
