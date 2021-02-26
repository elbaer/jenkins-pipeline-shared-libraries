package com.puzzleitc.jenkins.command

import com.jenkinsci.plugins.badge.action.BadgeAction
import com.puzzleitc.jenkins.command.context.PipelineContext

class CleanBuildsCommand {
    private final PipelineContext ctx
    private int maxNumberToKeepBuilds
    private String job
    private List environmentBuildCount
    private List deployedEnvironment

    CleanBuildsCommand(PipelineContext ctx) {
        this.ctx = ctx
    }

    void execute() {
        this.maxNumberToKeepBuilds = ctx.stepParams.getOptional('maxKeepBuilds', 10) as Integer
        this.job = ctx.stepParams.getRequired("job")
        Jenkins.instance.getItemByFullName(this.job)
                .getBuilds()
                .findAll { it.isKeepLog() }
                .each { build ->
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
                        ctx.info("Keeping build ${build} because of the following promotions: ${keepBuild.join(' ')}")
                    } else {
                        ctx.info("Deleting build ${build}")
                        build.delete()
                    }
                }
    }
}
