package tim.bootstrap.equivalence.property;

import tim.bootstrap.BootstrapMatcher;

import java.util.Set;

public class EquivalentPropertyLabelAndAltLabelMatcher extends BootstrapMatcher {

    @Override
    public void run() {
        findEquivalentLabelStream(Set.copyOf(state.getSourcePropertiesUnmatched()), Set.copyOf(state.getTargetPropertiesUnmatched())).forEach(this::createMatch);
    }
}
