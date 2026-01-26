package tim.bootstrap.percentage.clazz;

import tim.bootstrap.percentage.HighestCommonLabelOverlapMatcher;

import java.util.*;

public class HighestCommonWordPercentageInClassLabelMatcher extends HighestCommonLabelOverlapMatcher {

    @Override
    public void run() {
        run(Set.copyOf(state.getSourceClassesUnmatched()), Set.copyOf(state.getTargetClassesUnmatched()));
    }
}
