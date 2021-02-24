package groovy.vars

import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification

class AddDeployLinksSpec extends JenkinsPipelineSpecification {

    def adddeploylink = loadPipelineScriptForTest('vars/addDeployLinks.groovy')

    def setup() {
        explicitlyMockPipelineStep('ansiColor')
    }

    def 'Does not pass jobname to function'() {
        when:
        adddeploylink.call()
        then:
        thrown IllegalStateException
        1 * getPipelineMock("error").call('com.puzzleitc.jenkins.command.context.JenkinsPipelineContext: No deploymentJob found. Must be specified!')
    }

    def 'Does not find job passed to function'() {
        when:
        adddeploylink.call(
            deployJob: "my_job"
        )
        then:
        thrown IllegalStateException
        1 * getPipelineMock("error").call('com.puzzleitc.jenkins.command.context.JenkinsPipelineContext: Cannot find job my_job!')
    }
}