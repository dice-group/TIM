package tim.iterative.instance.one_equal_propertiesy;

import tim.iterative.instance.EquivalentStringFunctionAndXSharedPropertyInstanceMatcher;

public class EquivalentLabelAndAltLabelAndOneSharedStatementInstanceMatcher extends EquivalentStringFunctionAndXSharedPropertyInstanceMatcher {

    public EquivalentLabelAndAltLabelAndOneSharedStatementInstanceMatcher() {
        super(1, individual -> getLabelOrUriEndIfNoLabelPresent(individual) + STRING_SEPARATOR + getAltLabel(individual));
    }
}
