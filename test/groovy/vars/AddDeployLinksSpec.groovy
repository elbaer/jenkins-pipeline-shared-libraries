package groovy.vars

import com.homeaway.devtools.jenkins.testing.JenkinsPipelineSpecification

class AddDeployLinksSpec extends JenkinsPipelineSpecification {

    def addDeployLinks = loadPipelineScriptForTest('vars/addDeployLinks.groovy')

    def setup() {
        explicitlyMockPipelineStep('ansiColor')
    }

    def 'Does not pass jobname to function'() {
        when:
            addDeployLinks.call()
        then:
            1 * getPipelineMock("error").call('com.puzzleitc.jenkins.command.context.JenkinsPipelineContext: No job name passed. deployJob variable must be specified!')
    }

    def 'Does not find job passed to function'() {
        when:
            addDeployLinks.call(
                    somevar: "test",
                    deployJob: "my_job"
            )
        then:
            1 * getPipelineMock("error").call('com.puzzleitc.jenkins.command.context.JenkinsPipelineContext: Cannot find job my_job!')
    }

//    def 'addHtmlBadge successful added'() {
//        setup:
//            explicitlyMockPipelineStep("Jenkins.getItemByFullName")("my_job") >> {
//                return true
//            }
//            explicitlyMockPipelineStep("addHtmlBadge")({ it =~ /html*/ }) >> {
//                return "hallo"
//            }
//        when:
//            addDeployLinks.call(
//                    somevar: "test",
//                    deployJob: "my_job"
//            )
//        then:
//            1 * getPipelineMock("addHtmlBadge")({ it =~ /somethingidontknowrightnow/ })
//    }
}