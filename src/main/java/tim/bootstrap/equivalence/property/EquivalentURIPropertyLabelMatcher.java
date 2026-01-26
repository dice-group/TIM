package tim.bootstrap.equivalence.property;

import org.apache.jena.rdf.model.Resource;
import tim.bootstrap.BootstrapMatcher;

import java.util.Set;

public class EquivalentURIPropertyLabelMatcher extends BootstrapMatcher {

    @Override
    public void run() {
        findPairsWithEqualStringFunction(Set.copyOf(state.getSourcePropertiesUnmatched()), Set.copyOf(state.getTargetPropertiesUnmatched()), Resource::getURI).forEach(this::createMatch);
    }
}
