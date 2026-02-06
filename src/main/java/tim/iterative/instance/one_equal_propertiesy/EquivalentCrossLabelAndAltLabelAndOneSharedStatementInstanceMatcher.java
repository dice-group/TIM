package tim.iterative.instance.one_equal_propertiesy;

import org.apache.commons.lang3.tuple.Pair;
import tim.Matcher;
import tim.iterative.instance.EquivalentStringFunctionAndXSharedPropertyInstanceMatcher;

public class EquivalentCrossLabelAndAltLabelAndOneSharedStatementInstanceMatcher extends EquivalentStringFunctionAndXSharedPropertyInstanceMatcher {

    public EquivalentCrossLabelAndAltLabelAndOneSharedStatementInstanceMatcher() {
        super(1, Pair.of(Matcher::getLabelOrUriEndIfNoLabelPresent, Matcher::getAltLabel));
    }
}
