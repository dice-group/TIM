import de.uni_mannheim.informatik.dws.melt.matching_base.external.docker.MatcherDockerFile;
import de.uni_mannheim.informatik.dws.melt.matching_base.external.http.MatcherHTTPCall;
import de.uni_mannheim.informatik.dws.melt.matching_data.TrackRepository;
import de.uni_mannheim.informatik.dws.melt.matching_eval.ExecutionResultSet;
import de.uni_mannheim.informatik.dws.melt.matching_eval.Executor;
import de.uni_mannheim.informatik.dws.melt.matching_eval.evaluator.EvaluatorCSV;
import tim.TIM_Matcher;

import java.io.File;
import java.net.URI;

public class EvaluationDockerMatcher {
    public static void main(String[] args) throws Exception {
        URI matcherServiceUri = new URI("http://127.0.0.1:8080/match");
        MatcherHTTPCall matcher = new MatcherHTTPCall(matcherServiceUri, true);

        // let's run the matcher
        ExecutionResultSet ers = Executor.run(TrackRepository.Knowledgegraph.V4.getFirstTestCase(), matcher);

        // evaluating our system
        EvaluatorCSV evaluatorCSV = new EvaluatorCSV(ers);

        evaluatorCSV.writeToDirectory();
    }
}

