package tim.iterative.instance.one_equal_propertiesy;

import tim.Matcher;
import tim.iterative.instance.EquivalentStringFunctionAndXSharedPropertyInstanceMatcher;

public class EquivalentAltLabelAndOneSharedStatementInstanceMatcher extends EquivalentStringFunctionAndXSharedPropertyInstanceMatcher {

    public EquivalentAltLabelAndOneSharedStatementInstanceMatcher() {
        super(1, Matcher::getAltLabel);
    }
}
