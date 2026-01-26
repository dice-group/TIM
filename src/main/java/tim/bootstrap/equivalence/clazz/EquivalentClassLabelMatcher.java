package tim.bootstrap.equivalence.clazz;

import tim.bootstrap.BootstrapMatcher;

import java.util.Set;

public class EquivalentClassLabelMatcher extends BootstrapMatcher {

    @Override
    public void run() {
        findEquivalentLabelStream(Set.copyOf(state.getSourceClassesUnmatched()), Set.copyOf(state.getTargetClassesUnmatched())).forEach(this::createMatch);
    }
}
