package tim.iterative.instance.two_equal_properties;

import tim.Matcher;
import tim.iterative.instance.EquivalentStringFunctionAndXSharedPropertyInstanceMatcher;
import org.apache.commons.lang3.tuple.Pair;

public class EquivalentCrossAltLabelAndLabelAndTwoSharedStatementsInstanceMatcher extends EquivalentStringFunctionAndXSharedPropertyInstanceMatcher {

    public EquivalentCrossAltLabelAndLabelAndTwoSharedStatementsInstanceMatcher() {
        super(2, Pair.of(Matcher::getAltLabel, Matcher::getLabelOrUriEndIfNoLabelPresent));
    }
}
