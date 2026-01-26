package tim.iterative.instance.two_equal_properties;

import tim.Matcher;
import tim.iterative.instance.EquivalentStringFunctionAndXSharedPropertyInstanceMatcher;

public class EquivalentAltLabelAndTwoSharedStatementsInstanceMatcher extends EquivalentStringFunctionAndXSharedPropertyInstanceMatcher {

    public EquivalentAltLabelAndTwoSharedStatementsInstanceMatcher() {
        super(2, Matcher::getAltLabel);
    }
}
