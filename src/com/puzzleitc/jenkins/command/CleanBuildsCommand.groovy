package com.puzzleitc.jenkins.command

import com.cloudbees.groovy.cps.NonCPS
import com.jenkinsci.plugins.badge.action.BadgeAction
import com.puzzleitc.jenkins.command.context.PipelineContext

class CleanBuildsCommand {
    private final PipelineContext ctx

    CleanBuildsCommand(PipelineContext ctx) {
        this.ctx = ctx
    }

    void execute() {
        def job = ctx.stepParams.getOptional("job")
        try {
            ctx.info("Keeping build: " + getSuccessfulBuilds(job))
        } catch (Exception e) {
            ctx.fail("Cannot find job: " + job)
        }
    }

    @NonCPS
    Object getSuccessfulBuilds(String jobname) {
        return Jenkins.instance.getItemByFullName(jobname).getBuilds().findAll { it.isKeepLog() }
    }
}
