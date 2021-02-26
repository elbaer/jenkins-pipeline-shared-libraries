import com.puzzleitc.jenkins.command.CleanBuildsCommand
import com.puzzleitc.jenkins.command.context.JenkinsPipelineContext

def call(Map params = [:]) {
    CleanBuildsCommand command = new CleanBuildsCommand(new JenkinsPipelineContext(this, params))
    command.execute()
}
