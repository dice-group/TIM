package tim.bootstrap.equivalence.instance;

import tim.bootstrap.BootstrapMatcher;

import java.util.Set;

public class EquivalentLabelAndAltLabelInstanceMatcher extends BootstrapMatcher {
    @Override
    public void run() {
        findEquivalentLabelStream(Set.copyOf(state.getSourceInstancesUnmatched()), Set.copyOf(state.getTargetInstancesUnmatched())).forEach(this::createMatch);
    }
}
