package tim.bootstrap.percentage.property;

import tim.bootstrap.percentage.HighestCommonLabelOverlapMatcher;

import java.util.Set;

public class HighestCommonWordPercentageInRelationLabelMatcher extends HighestCommonLabelOverlapMatcher {

    @Override
    public void run() {
        run(Set.copyOf(state.getSourcePropertiesUnmatched()), Set.copyOf(state.getTargetPropertiesUnmatched()));
    }
}
