package tim.iterative.instance.one_equal_propertiesy;

import tim.Matcher;
import tim.iterative.instance.EquivalentStringFunctionAndXSharedPropertyInstanceMatcher;

public class EquivalentLabelAndOneSharedStatementInstanceMatcher extends EquivalentStringFunctionAndXSharedPropertyInstanceMatcher {

    public EquivalentLabelAndOneSharedStatementInstanceMatcher() {
        super(2, Matcher::getLabelOrUriEndIfNoLabelPresent);
    }
}
