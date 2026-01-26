package tim.iterative.instance.two_equal_properties;

import tim.Matcher;
import tim.iterative.instance.EquivalentStringFunctionAndXSharedPropertyInstanceMatcher;

public class EquivalentLabelAndTwoSharedStatementsInstanceMatcher extends EquivalentStringFunctionAndXSharedPropertyInstanceMatcher {

    public EquivalentLabelAndTwoSharedStatementsInstanceMatcher() {
        super(2, Matcher::getLabel);
    }
}
