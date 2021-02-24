package groovy.vars

import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification

class CleanBuildsSpec extends JenkinsPipelineSpecification {

    def check = loadPipelineScriptForTest('vars/check.groovy')

    def 'It aborts the pipeline' () {
        setup:
        check.getBinding().setVariable('currentBuild', [:])
        when:
        check.mandatoryParameter('hallo')
        then:
        check.getBinding().getVariable('currentBuild').result == 'ABORTED'
        1 * getPipelineMock("error").call('missing parameter: hallo')
    }
}

