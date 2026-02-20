package tim;

import tim.bootstrap.BootstrapMatcher;
import tim.bootstrap.equivalence.clazz.EquivalentClassLabelMatcher;
import tim.bootstrap.equivalence.instance.EquivalentLabelAndAltLabelInstanceMatcher;
import tim.bootstrap.equivalence.instance.UniqueWordInAnyLiteralInstanceMatcher;
import tim.bootstrap.equivalence.property.EquivalentPropertyLabelAndAltLabelMatcher;
import tim.bootstrap.equivalence.property.EquivalentURIPropertyLabelMatcher;
import tim.bootstrap.percentage.clazz.HighestCommonWordPercentageInClassLabelMatcher;
import tim.bootstrap.equivalence.instance.UniqueSharedStatementInstanceMatcher;
import tim.bootstrap.percentage.property.HighestCommonWordPercentageInRelationLabelMatcher;
import tim.combiner.IterativeTieredMatcherWIthBootstrapMatchersWhenNothingIsFound;
import tim.combiner.SequentialMatcherCombiner;
import tim.iterative.clazz.NewClassMatcherBasedOnMatchedIndividuals;
import tim.iterative.instance.EquivalentInstanceLabelMatcherForSameClass;
import tim.iterative.instance.InstancesWithCommonUniqueStatementMatcher;
import tim.iterative.instance.RelatedToExistingMatchAndEquivalentLabelInstanceMatcher;
import tim.iterative.instance.one_equal_propertiesy.*;
import tim.iterative.instance.two_equal_properties.*;
import tim.iterative.property.MatchPropertiesBasedOnUniqueSubjectObjectCombinations;
import tim.util.MeltMatcherWrapper;

import java.util.List;

public class TIM_Matcher extends MeltMatcherWrapper {
    public TIM_Matcher() {
        super(
                new SequentialMatcherCombiner(new IMatcher[]{
                        new IterativeTieredMatcherWIthBootstrapMatchersWhenNothingIsFound(
                                List.of(
                                        new EquivalentInstanceLabelMatcherForSameClass()
                                        , new RelatedToExistingMatchAndEquivalentLabelInstanceMatcher()

                                        , new EquivalentLabelAndAltLabelAndTwoSharedStatementsInstanceMatcher()
                                        , new EquivalentLabelAndTwoSharedStatementsInstanceMatcher()
                                        , new EquivalentAltLabelAndTwoSharedStatementsInstanceMatcher()
                                        , new EquivalentCrossLabelAndAltLabelAndTwoSharedStatementsInstanceMatcher()
                                        , new EquivalentCrossAltLabelAndLabelAndTwoSharedStatementsInstanceMatcher()

                                        , new EquivalentLabelAndAltLabelAndOneSharedStatementInstanceMatcher()
                                        , new EquivalentLabelAndOneSharedStatementInstanceMatcher()
                                        , new EquivalentAltLabelAndOneSharedStatementInstanceMatcher()
                                        , new EquivalentCrossLabelAndAltLabelAndOneSharedStatementInstanceMatcher()
                                        , new EquivalentCrossAltLabelAndLabelAndOneSharedStatementInstanceMatcher()

                                        , new MatchPropertiesBasedOnUniqueSubjectObjectCombinations()
                                        , new InstancesWithCommonUniqueStatementMatcher()
                                        , new NewClassMatcherBasedOnMatchedIndividuals()
                                ),
                                List.of(
                                        new SequentialMatcherCombiner(
                                                new EquivalentURIPropertyLabelMatcher()
                                                , new EquivalentClassLabelMatcher()
                                                , new EquivalentPropertyLabelAndAltLabelMatcher()
                                        )
                                        , new HighestCommonWordPercentageInClassLabelMatcher()
                                        , new HighestCommonWordPercentageInRelationLabelMatcher()
                                        , new EquivalentLabelAndAltLabelInstanceMatcher()
                                        , new UniqueSharedStatementInstanceMatcher()
                                        , new UniqueWordInAnyLiteralInstanceMatcher()

                                )
                        )
                        ,
                        new BootstrapMatcher() { //Remove w3.org matches that should not be matched in OAEI tracks
                            @Override
                            public void run() {
                                state.getMatchedProperties().stream().filter(ontPropertyOntPropertyPair -> ontPropertyOntPropertyPair.getLeft().getURI().startsWith("http://www.w3.org/") ||
                                ontPropertyOntPropertyPair.getRight().getURI().startsWith("http://www.w3.org/")).forEach(ontPropertyOntPropertyPair -> {
                                    state.getAlignment().removeCorrespondencesSource(ontPropertyOntPropertyPair.getLeft().getURI());
                                    state.getAlignment().removeCorrespondencesTarget(ontPropertyOntPropertyPair.getRight().getURI());
                                });
                            }
                        }
                })
        );
    }
}
