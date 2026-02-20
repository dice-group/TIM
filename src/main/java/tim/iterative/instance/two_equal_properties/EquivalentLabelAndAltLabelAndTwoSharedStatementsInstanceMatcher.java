package tim.iterative.instance.two_equal_properties;

import tim.iterative.instance.EquivalentStringFunctionAndXSharedPropertyInstanceMatcher;

public class EquivalentLabelAndAltLabelAndTwoSharedStatementsInstanceMatcher extends EquivalentStringFunctionAndXSharedPropertyInstanceMatcher {

    public EquivalentLabelAndAltLabelAndTwoSharedStatementsInstanceMatcher() {
        super(2, individual -> getLabelOrUriEndIfNoLabelPresent(individual) + STRING_SEPARATOR + getAltLabel(individual));
    }
}
