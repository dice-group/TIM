package tim.iterative.instance.two_equal_properties;

import tim.Matcher;
import tim.iterative.instance.EquivalentStringFunctionAndXSharedPropertyInstanceMatcher;
import org.apache.commons.lang3.tuple.Pair;

public class EquivalentCrossLabelAndAltLabelAndTwoSharedStatementsInstanceMatcher extends EquivalentStringFunctionAndXSharedPropertyInstanceMatcher {

    public EquivalentCrossLabelAndAltLabelAndTwoSharedStatementsInstanceMatcher() {
        super(2, Pair.of(Matcher::getLabelOrUriEndIfNoLabelPresent, Matcher::getAltLabel));
    }
}
