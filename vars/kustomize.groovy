import com.puzzleitc.jenkins.command.JenkinsPipelineContext
import com.puzzleitc.jenkins.command.KustomizeCommand

def call(String resource) {
    KustomizeCommand command = new KustomizeCommand(resource, new JenkinsPipelineContext())
    return command.execute()
}