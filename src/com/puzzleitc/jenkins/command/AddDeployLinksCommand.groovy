package com.puzzleitc.jenkins.command

import jenkins.model.Jenkins
import com.puzzleitc.jenkins.command.context.PipelineContext

class AddDeployLinksCommand {

    private final PipelineContext ctx

    AddDeployLinksCommand(PipelineContext ctx) {
        this.ctx = ctx
    }

    void execute() {
        ctx.info('-- AddDeployLinks --')
        String Jobname = ctx.stepParams.getOptional('deployJob').toString()
        if (Jobname == "null") {
            error(ctx.getClass().getName() + ': No job name passed. deployJob variable must be specified!')
        } else {
            ctx.echo("deployJob: " + Jobname)
            try {
                def deploymentJob = Jenkins.getItemByFullName(Jobname)
                ctx.addHtmlBadge html: "<a href=\"/${deploymentJob.getUrl()}parambuild?delay=0sec&built_name=${ctx.getEnv('JOB_NAME')}&built_number=${ctx.getEnv('BUILD_NUMBER')}\">Deploy</a> "
            } catch (Exception e) {
                error(ctx.getClass().getName() + ": Cannot find job ${Jobname}!")
            }
        }
    }
}