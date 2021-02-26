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
        def maxNumberToKeepBuilds = ctx.stepParams.getOptional('maxKeepBuilds', 10)
        def job = ctx.stepParams.getRequired("job")
        def environmentBuildCount = [:]
        try {
            def successfulJobRuns = getSuccessfulJobRuns(job)
            printOutput(successfulJobRuns)
            successfulJobRuns.each { build ->
//                def deployedEnvironment = []
//                build.getActions(BadgeAction.class).each {
//                    deployedEnvironment << it.id
//                    environmentBuildCount[it.id] = environmentBuildCount.get(it.id, 0) + 1
//                }
//
//                // each Build that should be kept will be stored in keepBuild map
//                def keepBuild = []
//                deployedEnvironment.each {
//                    if (environmentBuildCount[it] <= maxNumberToKeepBuilds) {
//                        keepBuild << it
//                    }
//                }
//
//                // print out reason of/not keeping the build
//                if (keepBuild) {
//                    ctx.info("Keeping build ${build} because of the following promotions: ${keepBuild.join(' ')}")
//                } else {
//                    ctx.info("Deleting build ${build}")
//                }
           }

        } catch (Exception e) {
            ctx.fail(e.printStackTrace())
        }
    }

    void printOutput(Object obj) {
        ctx.warn("Info: " + obj)
    }

    @NonCPS
    getSuccessfulJobRuns(String jobname) {
        return Jenkins.instance.getItemByFullName(jobname).getBuilds().findAll { it.isKeepLog() }
    }
}
