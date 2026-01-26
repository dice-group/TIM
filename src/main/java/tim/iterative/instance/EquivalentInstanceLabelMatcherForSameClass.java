package tim.iterative.instance;

import tim.iterative.templates.IterativeClassMatchListenerMatcher;
import tim.util.JenaStreamUtils;
import org.apache.jena.ontology.OntClass;

public class EquivalentInstanceLabelMatcherForSameClass extends IterativeClassMatchListenerMatcher {


    @Override
    public void runIteration() {
        getClassesMatchedSinceLastIteration().stream().parallel().forEach(matchedClasses -> {
            OntClass sourceClass = matchedClasses.getLeft();
            OntClass targetClass = matchedClasses.getRight();
            findEquivalentLabelStream(JenaStreamUtils.toStream(sourceClass.listInstances()), JenaStreamUtils.toStream(targetClass.listInstances()))
                    .forEach(this::createMatch);
        });
    }

}
