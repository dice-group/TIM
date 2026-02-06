import de.uni_mannheim.informatik.dws.melt.matching_data.TrackRepository;
import de.uni_mannheim.informatik.dws.melt.matching_eval.ExecutionResultSet;
import de.uni_mannheim.informatik.dws.melt.matching_eval.Executor;
import de.uni_mannheim.informatik.dws.melt.matching_eval.evaluator.EvaluatorCSV;
import tim.TIM_Matcher;

public class EvaluationPlayground {
    public static void main(String[] args) throws Exception {
        ExecutionResultSet result = Executor.run(TrackRepository.Knowledgegraph.V4,
                new TIM_Matcher()
        );
        EvaluatorCSV evaluatorCSV = new EvaluatorCSV(result);
        evaluatorCSV.writeToDirectory();
    }
}

