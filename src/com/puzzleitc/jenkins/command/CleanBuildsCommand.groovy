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
        ctx.info('-- CleanBuilds --')
        def maxNumberToKeepBuilds = ctx.stepParams.getOptional('maxKeepBuilds', 9)
        def job = ctx.stepParams.getRequired("job")
        def environmentBuildCount = [:]
        def successfulJobRuns = Jenkins.instance.getItemByFullName(job).getBuilds().findAll { it.isKeepLog() }
        String log = ""
        successfulJobRuns.each { build ->
            def deployedEnvironment = []
            build.getActions(BadgeAction.class).each {
                deployedEnvironment << it.id
                environmentBuildCount[it.id] = environmentBuildCount.get(it.id, 0) + 1
            }

            // each Build that should be kept will be stored in keepBuild map
            def keepBuild = []
            deployedEnvironment.each {
                if (environmentBuildCount[it] <= maxNumberToKeepBuilds) {
                    keepBuild << it
                }
            }

            // print out reason of/not keeping the build
            if (keepBuild) {
                keepBuild.join(' ')
                println("Keeping build ${build} because of the following promotions: ${keepBuild}")
                log = log + "<br>" + "Keeping build ${build} because of the following promotions: ${keepBuild}"
            } else {
                println("Deleting build ${build}")
                log = log + "<br>" + "Deleting build ${build}"
                build.delete()
            }
        }
        ctx.info(log)
    }
}
