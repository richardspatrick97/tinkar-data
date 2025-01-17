package dev.ikm.tinkar;

import dev.ikm.tinkar.common.id.IntIds;
import dev.ikm.tinkar.common.id.PublicIds;
import dev.ikm.tinkar.common.service.CachingService;
import dev.ikm.tinkar.common.service.PrimitiveData;
import dev.ikm.tinkar.common.service.ServiceKeys;
import dev.ikm.tinkar.common.service.ServiceProperties;
import dev.ikm.tinkar.composer.Composer;
import dev.ikm.tinkar.composer.Session;
import dev.ikm.tinkar.composer.assembler.ConceptAssembler;
import dev.ikm.tinkar.composer.assembler.PatternAssembler;
import dev.ikm.tinkar.composer.assembler.SemanticAssembler;
import dev.ikm.tinkar.composer.template.Definition;
import dev.ikm.tinkar.composer.template.FullyQualifiedName;
import dev.ikm.tinkar.composer.template.Identifier;
import dev.ikm.tinkar.composer.template.StatedAxiom;
import dev.ikm.tinkar.composer.template.StatedNavigation;
import dev.ikm.tinkar.composer.template.Synonym;
import dev.ikm.tinkar.composer.template.USDialect;
import dev.ikm.tinkar.entity.EntityService;
import dev.ikm.tinkar.entity.export.ExportEntitiesController;
import dev.ikm.tinkar.terms.EntityProxy;
import dev.ikm.tinkar.terms.State;
import org.eclipse.collections.api.factory.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static dev.ikm.tinkar.terms.TinkarTerm.BOOLEAN_FIELD;
import static dev.ikm.tinkar.terms.TinkarTerm.COMPONENT_FIELD;
import static dev.ikm.tinkar.terms.TinkarTerm.COMPONENT_ID_LIST_FIELD;
import static dev.ikm.tinkar.terms.TinkarTerm.COMPONENT_ID_SET_FIELD;
import static dev.ikm.tinkar.terms.TinkarTerm.DESCRIPTION_NOT_CASE_SENSITIVE;
import static dev.ikm.tinkar.terms.TinkarTerm.DEVELOPMENT_MODULE;
import static dev.ikm.tinkar.terms.TinkarTerm.DEVELOPMENT_PATH;
import static dev.ikm.tinkar.terms.TinkarTerm.ENGLISH_LANGUAGE;
import static dev.ikm.tinkar.terms.TinkarTerm.FLOAT_FIELD;
import static dev.ikm.tinkar.terms.TinkarTerm.INTEGER_FIELD;
import static dev.ikm.tinkar.terms.TinkarTerm.PREFERRED;
import static dev.ikm.tinkar.terms.TinkarTerm.STRING;
import static dev.ikm.tinkar.terms.TinkarTerm.TINKAR_MODEL_CONCEPT;
import static dev.ikm.tinkar.terms.TinkarTerm.UNIVERSALLY_UNIQUE_IDENTIFIER;
import static dev.ikm.tinkar.terms.TinkarTerm.USER;

public class TinkarExampleData {
    private static final Logger LOG = LoggerFactory.getLogger(TinkarExampleData.class.getSimpleName());

    private final File exportFile;
    private final File datastore;

    private Session session;
    private EntityProxy.Concept SAMPLE_TINKAR_DATA;

    public TinkarExampleData(String[] args) {
        datastore = new File(args[0]);
        exportFile = new File(args[1]);
    }

    private void init() {
        LOG.info("Starting database");
        LOG.info("Loading data from {}", datastore.getAbsolutePath());
        CachingService.clearAll();
        ServiceProperties.set(ServiceKeys.DATA_STORE_ROOT, datastore);
        PrimitiveData.selectControllerByName("Open SpinedArrayStore");
        PrimitiveData.start();
    }

    public void transform() {
        EntityService.get().beginLoadPhase();
        try {
            Composer composer = new Composer("Tinkar Example Data Composer");
            session = composer.open(
                    State.ACTIVE,
                    System.currentTimeMillis(),
                    USER,
                    DEVELOPMENT_MODULE,
                    DEVELOPMENT_PATH);

            createData();

            composer.commitSession(session);
        } finally {
            EntityService.get().endLoadPhase();
        }
    }

    private void createData() {
        SAMPLE_TINKAR_DATA = createConcept("Tinkar Sample Data", "67af3d9d-2d32-4036-9861-c052f3174134", TINKAR_MODEL_CONCEPT);

        createPatternOne();
        createPatternTwo();
    }

    private void createPatternOne() {
        EntityProxy.Pattern EXAMPLE_PATTERN_ONE = EntityProxy.Pattern.make("Tinkar Semantic Test Pattern 1", UUID.fromString("6604faf6-e914-49ca-a354-126f619f31ca"));
        EntityProxy.Concept EXAMPLE_MEANING = createConcept("A test pattern for primitive data types", "ad6f4fdd-fee8-45db-a207-111dc4c939a9");
        EntityProxy.Concept STRING_FIELD_MEANING = createConcept("An example String field", "c39286ba-55ed-4009-b7e1-48519fbd0e0a");
        EntityProxy.Concept INTEGER_FIELD_MEANING = createConcept("An example Integer field", "38bcb9c6-cdce-4b02-a1bd-d976e3065b8a");
        EntityProxy.Concept FLOAT_FIELD_MEANING = createConcept("An example Float field", "4276d7a6-2ae7-4ab4-8797-cffad22bf140");
        EntityProxy.Concept BOOLEAN_FIELD_MEANING = createConcept("An example Boolean field", "87fc11f4-401d-47b2-9d64-82826f09524f");

        session.compose((PatternAssembler patternAssembler) -> patternAssembler.pattern(EXAMPLE_PATTERN_ONE)
                        .meaning(EXAMPLE_MEANING)
                        .purpose(EXAMPLE_MEANING)
                        .fieldDefinition(
                                STRING_FIELD_MEANING,
                                STRING_FIELD_MEANING,
                                STRING)
                        .fieldDefinition(
                                INTEGER_FIELD_MEANING,
                                INTEGER_FIELD_MEANING,
                                INTEGER_FIELD)
                        .fieldDefinition(
                                FLOAT_FIELD_MEANING,
                                FLOAT_FIELD_MEANING,
                                FLOAT_FIELD)
                        .fieldDefinition(
                                BOOLEAN_FIELD_MEANING,
                                BOOLEAN_FIELD_MEANING,
                                BOOLEAN_FIELD))
                .attach((FullyQualifiedName fqn) -> fqn
                        .text(EXAMPLE_PATTERN_ONE.description())
                        .language(ENGLISH_LANGUAGE)
                        .caseSignificance(DESCRIPTION_NOT_CASE_SENSITIVE))
                .attach((Synonym synonym) -> synonym
                        .text(EXAMPLE_PATTERN_ONE.description())
                        .language(ENGLISH_LANGUAGE)
                        .caseSignificance(DESCRIPTION_NOT_CASE_SENSITIVE))
                .attach((Definition definition) -> definition
                        .text("An example pattern for Tinkar String, Integer, Float, and Boolean data types")
                        .language(ENGLISH_LANGUAGE)
                        .caseSignificance(DESCRIPTION_NOT_CASE_SENSITIVE));

        EntityProxy.Concept CONCEPT_FOR_SEMANTIC_1 = createConcept("First Semantic for Sample Pattern 1", "ad7e09a2-a492-4293-a68a-eb3018e5f23b");
        session.compose((SemanticAssembler semanticAssembler) -> semanticAssembler
                .pattern(EXAMPLE_PATTERN_ONE)
                .reference(CONCEPT_FOR_SEMANTIC_1)
                .fieldValues(objects -> objects.addAll(Lists.mutable.of("This is a test String", 1, 0.5f, true))));

        EntityProxy.Concept CONCEPT_FOR_SEMANTIC_2 = createConcept("Second Semantic for Sample Pattern 1", "a25ed810-68bf-4c92-b010-a9492106484e");
        session.compose((SemanticAssembler semanticAssembler) -> semanticAssembler
                .pattern(EXAMPLE_PATTERN_ONE)
                .reference(CONCEPT_FOR_SEMANTIC_2)
                .fieldValues(objects -> objects.addAll(Lists.mutable.of("ThIs iS a DiFfeRenT tEsT StRiNg", 10, 7.5f, false))));

    }

    private void createPatternTwo() {
        EntityProxy.Pattern EXAMPLE_PATTERN_TWO = EntityProxy.Pattern.make("Tinkar Semantic Test Pattern 2", UUID.fromString("7222d538-9641-474a-94ce-72c5bf6462b3"));
        EntityProxy.Concept EXAMPLE_MEANING = createConcept("A test pattern for component data types", "577ca159-5034-4c3b-8817-24a9de0d9b5c");
        EntityProxy.Concept COMPONENT_FIELD_MEANING = createConcept("An example Component field", "3cd97362-ff6f-4337-b3f9-fb76d2ca4338");
        EntityProxy.Concept COMPONENT_SET_FIELD_MEANING = createConcept("An example Component Set field", "990e5a92-cdc2-4e23-a68d-1f01345b8759");
        EntityProxy.Concept COMPONENT_LIST_FIELD_MEANING = createConcept("An example Component List field", "f0847cd3-2034-43f5-b25f-2bd6e923d228");

        session.compose((PatternAssembler patternAssembler) -> patternAssembler.pattern(EXAMPLE_PATTERN_TWO)
                        .meaning(EXAMPLE_MEANING)
                        .purpose(EXAMPLE_MEANING)
                        .fieldDefinition(
                                COMPONENT_FIELD_MEANING,
                                COMPONENT_FIELD_MEANING,
                                COMPONENT_FIELD)
                        .fieldDefinition(
                                COMPONENT_SET_FIELD_MEANING,
                                COMPONENT_SET_FIELD_MEANING,
                                COMPONENT_ID_SET_FIELD)
                        .fieldDefinition(
                                COMPONENT_LIST_FIELD_MEANING,
                                COMPONENT_LIST_FIELD_MEANING,
                                COMPONENT_ID_LIST_FIELD))
                .attach((FullyQualifiedName fqn) -> fqn
                        .text(EXAMPLE_PATTERN_TWO.description())
                        .language(ENGLISH_LANGUAGE)
                        .caseSignificance(DESCRIPTION_NOT_CASE_SENSITIVE))
                .attach((Synonym synonym) -> synonym
                        .text(EXAMPLE_PATTERN_TWO.description())
                        .language(ENGLISH_LANGUAGE)
                        .caseSignificance(DESCRIPTION_NOT_CASE_SENSITIVE))
                .attach((Definition definition) -> definition
                        .text("An example pattern for Tinkar Component, Component Set, and Component List data types")
                        .language(ENGLISH_LANGUAGE)
                        .caseSignificance(DESCRIPTION_NOT_CASE_SENSITIVE));

        EntityProxy.Concept CONCEPT_FOR_SEMANTIC_1 = createConcept("First Semantic for Sample Pattern 2", "016de8cc-b93c-4dab-a1d8-a2cc720f9351");
        session.compose((SemanticAssembler semanticAssembler) -> semanticAssembler
                .pattern(EXAMPLE_PATTERN_TWO)
                .reference(CONCEPT_FOR_SEMANTIC_1)
                .fieldValues(objects -> objects.addAll(
                        Lists.mutable.of(
                                EntityProxy.make("", PublicIds.of("845274b5-9644-3799-94c6-e0ea37e7d1a4")),
                                IntIds.set.of(
                                        EntityProxy.make("", PublicIds.of("700546a3-09c7-3fc2-9eb9-53d318659a09")).nid(),
                                        EntityProxy.make("", PublicIds.of("b42c1948-7645-5da8-a888-de6ec020ab98")).nid(),
                                        EntityProxy.make("", PublicIds.of("03004053-c23e-5206-8514-fb551dd328f4")).nid(),
                                        EntityProxy.make("", PublicIds.of("b17bde5d-98ed-5416-97cf-2d837d75159d")).nid(),
                                        EntityProxy.make("", PublicIds.of("b17bde5d-98ed-5416-97cf-2d837d75159d")).nid()
                                ),
                                IntIds.list.of(
                                        EntityProxy.make("", PublicIds.of("700546a3-09c7-3fc2-9eb9-53d318659a09")).nid(),
                                        EntityProxy.make("", PublicIds.of("351955ff-30f4-5806-a0a5-5dda79756377")).nid(),
                                        EntityProxy.make("", PublicIds.of("5a2e7786-3e41-11dc-8314-0800200c9a66")).nid()
                                )
                        )
                )));

        EntityProxy.Concept CONCEPT_FOR_SEMANTIC_2 = createConcept("Second Semantic for Sample Pattern 2", "dde159ca-415e-4947-9174-cae7e8e7202d");
        session.compose((SemanticAssembler semanticAssembler) -> semanticAssembler
                .pattern(EXAMPLE_PATTERN_TWO)
                .reference(CONCEPT_FOR_SEMANTIC_2)
                .fieldValues(objects -> objects.addAll(
                        Lists.mutable.of(
                                EntityProxy.make("", PublicIds.of("700546a3-09c7-3fc2-9eb9-53d318659a09")),
                                IntIds.set.of(
                                        EntityProxy.make("", PublicIds.of("1f200ca6-960e-11e5-8994-feff819cdc9f")).nid(),
                                        EntityProxy.make("", PublicIds.of("e95b6718-f824-5540-817b-8e79544eb97a")).nid(),
                                        EntityProxy.make("", PublicIds.of("80710ea6-983c-5fa0-8908-e479f1f03ea9")).nid()
                                ),
                                IntIds.list.of(
                                        EntityProxy.make("", PublicIds.of("5c9b5844-1434-5111-83d5-cb7cb0be12d9")).nid(),
                                        EntityProxy.make("", PublicIds.of("65af466b-360c-58b2-8b7d-2854150029a8")).nid(),
                                        EntityProxy.make("", PublicIds.of("c1baba19-e918-5d2c-8fa4-b0ad93e03186")).nid(),
                                        EntityProxy.make("", PublicIds.of("6f96e8cf-5568-5e49-8a90-aa6c65125ee9")).nid(),
                                        EntityProxy.make("", PublicIds.of("6dfacbd5-8344-5794-9fda-bec95b2aa6c9")).nid()
                                )
                        )
                )));
    }

    private EntityProxy.Concept createConcept(String description, String uuidStr) {
        return createConcept(description, uuidStr, SAMPLE_TINKAR_DATA);
    }

    private EntityProxy.Concept createConcept(String description, String uuidStr, EntityProxy.Concept parent) {
        EntityProxy.Concept concept = EntityProxy.Concept.make(description, UUID.fromString(uuidStr));
        session.compose((ConceptAssembler conceptAssembler) -> conceptAssembler.concept(concept))
                .attach((FullyQualifiedName fqn) -> fqn
                        .text(concept.description())
                        .language(ENGLISH_LANGUAGE)
                        .caseSignificance(DESCRIPTION_NOT_CASE_SENSITIVE)
                        .attach(usDialect()))
                .attach((Synonym synonym) -> synonym
                        .text(concept.description())
                        .caseSignificance(DESCRIPTION_NOT_CASE_SENSITIVE)
                        .language(ENGLISH_LANGUAGE)
                        .attach(usDialect()))
                .attach((Definition definition) -> definition
                        .text(concept.description())
                        .caseSignificance(DESCRIPTION_NOT_CASE_SENSITIVE)
                        .language(ENGLISH_LANGUAGE)
                        .attach(usDialect()))
                .attach((Identifier identifier) -> identifier
                        .source(UNIVERSALLY_UNIQUE_IDENTIFIER)
                        .identifier(concept.asUuidArray()[0].toString()))
                .attach(new StatedNavigation()
                        .parents(parent))
                .attach(new StatedAxiom()
                        .isA(parent));
        return concept;
    }

    private USDialect usDialect() {
        return new USDialect().acceptability(PREFERRED);
    }

    private void exportToProtoBuf() {
        try {
            new ExportEntitiesController().export(exportFile).get();
        } catch (ExecutionException | InterruptedException e) {
            LOG.error("Error while exporting.", e);
        }
    }

    private void cleanup() {
        PrimitiveData.stop();
    }

    public void execute() {
        init();
        transform();
        exportToProtoBuf();
        cleanup();
    }

    public static void main(String[] args) {
        TinkarExampleData exampleData = new TinkarExampleData(args);
        exampleData.execute();
    }
}