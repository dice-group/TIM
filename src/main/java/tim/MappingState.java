package tim;

import de.uni_mannheim.informatik.dws.melt.yet_another_alignment_api.Alignment;
import org.apache.jena.rdf.model.RDFNode;
import tim.util.ConsoleColors;
import tim.util.JenaStreamUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Statement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

import static tim.Matcher.*;


public class MappingState {

    private final OntModel sourceOntology;
    private final OntModel targetOntology;
    private final Alignment alignment;
    private final Set<OntClass> sourceClassesUnmatched;
    private final Set<OntClass> targetClassesUnmatched;
    private final Set<OntProperty> sourcePropertiesUnmatched;
    private final Set<OntProperty> targetPropertiesUnmatched;
    private final Set<OntResource> sourceInstancesUnmatched;
    private final Set<OntResource> targetInstancesUnmatched;

    private final Set<Pair<OntClass, OntClass>> matchedClasses;
    private final Set<Pair<OntProperty, OntProperty>> matchedProperties;
    private final Set<Pair<OntResource, OntResource>> matchedInstances;

    private final Map<OntResource, OntClass> instancesMappedToTheirClass;
    private final Map<String, Set<String>> sourceToTargetTranslation;

    private final Map<OntResource, Set<String>> statementRepresentationsRTforTargetInstances;
    private final Map<String, Set<Statement>> statementRepresentationsHTforTargetTriples;
    private final Map<OntProperty, Set<Statement>> statementsForSourceProperty;
    private final Map<OntResource, Set<Statement>> statementsWithSourceInstanceAsObjectProperty;

    private boolean fullyInitialized = false;

    public MappingState(OntModel sourceOntology, OntModel targetOntology, Alignment alignment) {
        this.sourceOntology = sourceOntology;
        this.targetOntology = targetOntology;
        this.alignment = alignment;

        //This has to be run before initializing allStatementRepresentationsForTargetEntity, statementsForSourceProperty, statementsWithSourceAsObjectProperty
        createOntProperties();
        this.sourcePropertiesUnmatched = new HashSet<>(sourceOntology.listAllOntProperties().toSet());
        this.targetPropertiesUnmatched = new HashSet<>(targetOntology.listAllOntProperties().toSet());
        this.sourceClassesUnmatched = new HashSet<>(sourceOntology.listClasses().toSet());
        this.targetClassesUnmatched = new HashSet<>(targetOntology.listClasses().toSet());
        this.sourceInstancesUnmatched = new HashSet<>(sourceOntology.listIndividuals().toSet());
        this.targetInstancesUnmatched = new HashSet<>(targetOntology.listIndividuals().toSet());

        this.matchedClasses = new HashSet<>();
        this.matchedProperties = new HashSet<>();
        this.matchedInstances = new HashSet<>();

        this.sourceToTargetTranslation = new HashMap<>();

        this.instancesMappedToTheirClass = new HashMap<>();

        statementRepresentationsRTforTargetInstances = new HashMap<>();
        statementRepresentationsHTforTargetTriples = new HashMap<>();
        statementsForSourceProperty = new HashMap<>();
        statementsWithSourceInstanceAsObjectProperty = new HashMap<>();

        Matcher.runAsyncAndWaitForCompletion(
                () -> {
                    sourceOntology.listClasses().forEach(clazz -> {
                        clazz.listInstances().forEach(instance -> {
                            instancesMappedToTheirClass.put(instance, clazz);
                        });
                    });
                    targetOntology.listClasses().forEach(clazz -> {
                        clazz.listInstances().forEach(instance -> {
                            instancesMappedToTheirClass.put(instance, clazz);
                        });
                    });
                },
                () -> JenaStreamUtils.toStream(sourceOntology.listIndividuals())
                        .forEach(individual -> {
                            JenaStreamUtils.toStream(individual.listProperties())
                                    .filter(statement -> !isLabelOrAltLabel(statement.getPredicate()))
                                    .forEach(statement -> {
                                        if (statement.getObject().isResource()) {
                                            statementsWithSourceInstanceAsObjectProperty.computeIfAbsent(sourceOntology.getOntResource(statement.getObject().asResource()), k -> new HashSet<>()).add(statement);
                                        }
                                    });
                        }),
                () -> JenaStreamUtils.toStream(sourceOntology.listIndividuals())
                        .forEach(individual -> {
                            JenaStreamUtils.toStream(individual.listProperties())
                                    .filter(statement -> !isLabelOrAltLabel(statement.getPredicate()))
                                    .forEach(statement -> {
                                        statementsForSourceProperty.computeIfAbsent(sourceOntology.getOntProperty(statement.getPredicate().getURI()), k -> new HashSet<>()).add(statement);
                                    });
                        }),
                () -> JenaStreamUtils.toStream(targetOntology.listIndividuals())
                        .forEach(individual -> {
                            Set<String> statementRepresentationsForThisIndividual = statementRepresentationsRTforTargetInstances.computeIfAbsent(individual, k -> new HashSet<>());
                            JenaStreamUtils.toStream(individual.listProperties())
                                    .filter(statement -> !isLabelOrAltLabel(statement.getPredicate()))
                                    .forEach(statement -> {
                                        statementRepresentationsForThisIndividual.add(computeStringRepresentationBasedOnPredicateObject(statement));
                                    });
                        }),
                () -> JenaStreamUtils.toStream(targetOntology.listStatements())
                        .forEach(statement -> {
                            statementRepresentationsHTforTargetTriples.computeIfAbsent(
                                    computeStatementRepresentationBasedOnSubjectObject(statement), k -> new HashSet<>()).add(statement);
                        })




        );

        fullyInitialized = true;
        System.out.println(ConsoleColors.PURPLE_BOLD_BRIGHT + "MappingState got initialized successfully!" + ConsoleColors.RESET);
    }

    private void createOntProperties() {
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        CompletableFuture<Void> sourceFuture = CompletableFuture.runAsync(() -> {
            Set<String> propertiesToCreate = JenaStreamUtils.toStream(sourceOntology.listStatements())
                    .parallel()
                    .map(statement -> statement.getPredicate().getURI())
                    .filter(propertyUri -> sourceOntology.getOntProperty(propertyUri) == null)
                    .collect(Collectors.toSet());
            propertiesToCreate.forEach(sourceOntology::createOntProperty);
        }, forkJoinPool);
        CompletableFuture<Void> targetFuture = CompletableFuture.runAsync(() -> {
            Set<String> propertiesToCreate = JenaStreamUtils.toStream(targetOntology.listStatements())
                    .parallel()
                    .map(statement -> statement.getPredicate().getURI())
                    .filter(propertyUri -> targetOntology.getOntProperty(propertyUri) == null)
                    .collect(Collectors.toSet());
            propertiesToCreate.forEach(targetOntology::createOntProperty);
        }, forkJoinPool);
        CompletableFuture.allOf(sourceFuture, targetFuture).join();
    }


    public String computeStringRepresentationBasedOnPredicateObject(Statement statement) {
        if (statement.getObject().isResource()) {
            return translateIfPossibleElseReturnOriginal(statement.getPredicate().getURI()) + STRING_SEPARATOR + translateIfPossibleElseReturnOriginal(statement.getObject().asResource().getURI());
        }
        return translateIfPossibleElseReturnOriginal(statement.getPredicate().getURI()) + STRING_SEPARATOR + statement.getObject().asLiteral().getLexicalForm();
    }

    public String computeStatementRepresentationBasedOnSubjectObject(Statement statement) {
        if (statement.getObject().isResource()) {
            return translateIfPossibleElseReturnOriginal(statement.getSubject().getURI()) + STRING_SEPARATOR + translateIfPossibleElseReturnOriginal(statement.getObject().asResource().getURI());
        }
        return translateIfPossibleElseReturnOriginal(statement.getSubject().getURI()) + STRING_SEPARATOR + statement.getObject().asLiteral().getLexicalForm();
    }

    public OntClass getClass(OntResource instance) {
        return instancesMappedToTheirClass.get(instance);
    }

    public void matchClasses(OntClass sourceClass, OntClass targetClass) {
        addToAlignment(sourceClass.getURI(), targetClass.getURI());
        sourceClassesUnmatched.remove(sourceClass);
        targetClassesUnmatched.remove(targetClass);
        matchedClasses.add(Pair.of(sourceClass, targetClass));
        sourceToTargetTranslation.computeIfAbsent(sourceClass.getURI(), k -> new HashSet<>()).add(targetClass.getURI());
        //System.out.println("Match Classes: " + sourceClass.getURI() + " -> " + targetClass.getURI());
    }

    public void matchProperties(OntProperty sourceProperty, OntProperty targetProperty) {
        addToAlignment(sourceProperty.getURI(), targetProperty.getURI());
        sourcePropertiesUnmatched.remove(sourceProperty);
        targetPropertiesUnmatched.remove(targetProperty);
        matchedProperties.add(Pair.of(sourceProperty, targetProperty));
        sourceToTargetTranslation.computeIfAbsent(sourceProperty.getURI(), k -> new HashSet<>()).add(targetProperty.getURI());
        //System.out.println("Match Properties: " + sourceProperty.getURI() + " -> " + targetProperty.getURI());
    }

    public void matchIndividuals(OntResource sourceIndividual, OntResource targetIndividual) {
        addToAlignment(sourceIndividual.getURI(), targetIndividual.getURI());
        sourceInstancesUnmatched.remove(sourceIndividual);
        targetInstancesUnmatched.remove(targetIndividual);
        matchedInstances.add(Pair.of(sourceIndividual, targetIndividual));
        sourceToTargetTranslation.computeIfAbsent(sourceIndividual.getURI(), k -> new HashSet<>()).add(targetIndividual.getURI());
        //System.out.println("Match Instances: " + sourceIndividual.getURI() + " -> " + targetIndividual.getURI());
    }

    private void addToAlignment(String uri1, String uri2) {
//        if (fullyInitialized) {
//            if (FPChecker.isFalsePositive(uri1, uri2)) {
//                //throw new RuntimeException("FALSE POSITIVE: " + uri1 + " and " + uri2);
//                System.out.println("FP: " + uri1 + " -> " + uri2);
//                String correctMappingForSourceElement = FPChecker.getCorrectMappingSource(uri1);
//                System.out.println("Source correct match: " + uri1 + " -> " + correctMappingForSourceElement);
//                String correctMappingForTargetElement = FPChecker.getCorrectMappingTarget(uri2);
//                System.out.println("Target correct match: " + uri2 + " -> " + correctMappingForTargetElement);
//                Set<Statement> r1Properties = sourceOntology.getResource(uri1).listProperties().toSet();
//                Set<Statement> r2Properties = targetOntology.getResource(uri2).listProperties().toSet();
//                Set<Statement> goldTargetEntityForSource = correctMappingForSourceElement != null ? targetOntology.getResource(correctMappingForSourceElement).listProperties().toSet() : null;
//                Set<Statement> goldSourceEntityForTarget = correctMappingForTargetElement != null ? sourceOntology.getResource(correctMappingForTargetElement).listProperties().toSet() : null;
//                System.out.println("--------");
//            }
//        }
        this.alignment.add(uri1, uri2);
    }

    public String translateIfPossibleElseReturnOriginal(String uri) {
        return sourceToTargetTranslation.getOrDefault(uri, Set.of()).stream().findFirst().orElse(uri);
    }

    public Set<Pair<OntClass, OntClass>> getMatchedClasses() {
        return matchedClasses;
    }

    public Set<Pair<OntResource, OntResource>> getMatchedInstances() {
        return matchedInstances;
    }

    public Set<Pair<OntProperty, OntProperty>> getMatchedProperties() {
        return matchedProperties;
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public Set<OntClass> getSourceClassesUnmatched() {
        return sourceClassesUnmatched;
    }

    public Set<OntClass> getTargetClassesUnmatched() {
        return targetClassesUnmatched;
    }

    public Set<OntProperty> getSourcePropertiesUnmatched() {
        return sourcePropertiesUnmatched;
    }

    public Set<OntProperty> getTargetPropertiesUnmatched() {
        return targetPropertiesUnmatched;
    }

    public Set<OntResource> getSourceInstancesUnmatched() {
        return sourceInstancesUnmatched;
    }

    public Set<OntResource> getTargetInstancesUnmatched() {
        return targetInstancesUnmatched;
    }

    public OntModel getSourceOntology() {
        return sourceOntology;
    }

    public OntModel getTargetOntology() {
        return targetOntology;
    }

    public boolean isSourceClassStillUnmatched(OntClass sourceClass) {
        return sourceClassesUnmatched.contains(sourceClass);
    }

    public boolean isTargetClassStillUnmatched(OntClass targetClass) {
        return targetClassesUnmatched.contains(targetClass);
    }

    public boolean isSourceInstanceStillUnmatched(OntResource sourceInstance) {
        return sourceInstancesUnmatched.contains(sourceInstance);
    }

    public boolean isSourceObjectInstanceAndStillUnmatched(RDFNode object) {
        return object.isResource() && isSourceInstanceStillUnmatched(object.asResource().as(OntResource.class));
    }

    public boolean isTargetInstanceStillUnmatched(OntResource targetInstance) {
        return targetInstancesUnmatched.contains(targetInstance);
    }

    public boolean isTargetObjectInstanceAndStillUnmatched(RDFNode object) {
        return object.isResource() && isTargetInstanceStillUnmatched(object.asResource().as(OntResource.class));
    }
    public boolean isSourcePropertyStillUnmatched(OntProperty sourceProperty) {
        return sourcePropertiesUnmatched.contains(sourceProperty);
    }

    public boolean isTargetPropertyStillUnmatched(OntProperty targetProperty) {
        return targetPropertiesUnmatched.contains(targetProperty);
    }

    public boolean isSourcePropertyStillUnmatched(Property property) {
        return isSourcePropertyStillUnmatched(sourceOntology.getOntProperty(property.getURI()));
    }

    public boolean isTargetPropertyStillUnmatched(Property property) {
        return isTargetPropertyStillUnmatched(targetOntology.getOntProperty(property.getURI()));
    }

    private static boolean isLabelOrAltLabel(Property predicate) {
        return predicate.getURI().equals(URI_LABEL) || predicate.getURI().equals(URI_ALT_LABEL);
    }

    public Map<OntResource, Set<String>> getStatementRepresentationsRTforTargetInstancesMap() {
        return statementRepresentationsRTforTargetInstances;
    }

    public Map<String, Set<Statement>> getStatementRepresentationsHTforTargetTriplesMap() {
        return statementRepresentationsHTforTargetTriples;
    }

    public Map<OntProperty, Set<Statement>> getStatementsForSourcePropertyMap() {
        return statementsForSourceProperty;
    }

    public Map<OntResource, Set<Statement>> getStatementsWithSourceInstanceAsObjectPropertyMap() {
        return statementsWithSourceInstanceAsObjectProperty;
    }
}
