package tim.iterative.instance.one_equal_propertiesy;

import org.apache.commons.lang3.tuple.Pair;
import tim.Matcher;
import tim.iterative.instance.EquivalentStringFunctionAndXSharedPropertyInstanceMatcher;

public class EquivalentCrossAltLabelAndLabelAndOneSharedStatementInstanceMatcher extends EquivalentStringFunctionAndXSharedPropertyInstanceMatcher {

    public EquivalentCrossAltLabelAndLabelAndOneSharedStatementInstanceMatcher() {
        super(1, Pair.of(Matcher::getAltLabel, Matcher::getLabel));
    }
}
