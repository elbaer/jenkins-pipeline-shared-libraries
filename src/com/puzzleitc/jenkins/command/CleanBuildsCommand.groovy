package com.puzzleitc.jenkins.command

import com.jenkinsci.plugins.badge.action.BadgeAction
import com.puzzleitc.jenkins.command.context.PipelineContext

class CleanBuildsCommand {
    private final PipelineContext ctx

    CleanBuildsCommand(PipelineContext ctx) {
        this.ctx = ctx
    }

    void execute() {
        def maxNumberToKeepBuilds = ctx.stepParams.getOptional('maxKeepBuilds', 10)

        def environmentBuildCount = [:]
        def job = ctx.stepParams.getRequired("job")
        try {
            Jenkins.instance.getItemByFullName(job)
                    .getBuilds()
                    .findAll { it.isKeepLog() }
                    .each { build ->
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
                            ctx.info("Keeping build ${build} because of the following promotions: ${keepBuild.join(' ')}")
                        } else {
                            ctx.info("Deleting build ${build}")
                            build.delete()
                        }
                    }
        } catch (Exception e) {
            ctx.fail("Exception: " + e.printStackTrace())
        }
    }
//    void execute() {
//        def maxNumberToKeepBuilds = ctx.stepParams.getOptional('maxKeepBuilds', 10) as Integer
//        def environmentBuildCount = [:]
//        def job = ctx.stepParams.getRequired("job")
//        Jenkins.instance.getItemByFullName(job).getBuilds().findAll { it.isKeepLog() }.each { build ->
//            def deployedEnvironment = []
//            build.getActions(BadgeAction.class).each {
//                deployedEnvironment << it.id
//                environmentBuildCount[it.id] = environmentBuildCount.get(it.id, 0) + 1
//            }
//
//            // each Build that should be kept will be stored in keepBuild map
//            def keepBuild = []
//            deployedEnvironment.each {
//                if (environmentBuildCount[it] <= maxNumberToKeepBuilds) {
//                    keepBuild << it
//                }
//            }
//
//            // print out reason of/not keeping the build
//            if (keepBuild) {
//                ctx.info("Keeping build ${build} because of the following promotions: ${keepBuild.join(' ')}")
//            } else {
//                ctx.info("Deleting build ${build}")
//                build.delete()
//            }
//        }
//    }
}
