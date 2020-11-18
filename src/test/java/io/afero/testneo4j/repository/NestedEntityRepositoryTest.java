package io.afero.testneo4j.repository;

import io.afero.testneo4j.configuration.Neo4jConfiguration;
import io.afero.testneo4j.model.NestedEntity;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Import(Neo4jConfiguration.class)
@DataNeo4jTest
public class NestedEntityRepositoryTest {

    private static Neo4j embeddedDatabaseServer;

    @BeforeAll
    static void initializeNeo4j() {
        embeddedDatabaseServer = Neo4jBuilders.newInProcessBuilder()
                .withDisabledServer()
                .build();
    }

    @AfterAll
    static void stopNeo4j() {
        embeddedDatabaseServer.close();
    }

    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.neo4j.uri", embeddedDatabaseServer::boltURI);
        registry.add("spring.neo4j.authentication.username", () -> "neo4j");
        registry.add("spring.neo4j.authentication.password", () -> null);
    }

    @Autowired
    private NestedEntityRepository repository;

    @AfterEach
    public void teardown() {
        repository.deleteAll().block();
    }

    @Test
    public void testNestedEntity() {
        NestedEntity expected = new NestedEntity()
                .setChildren(Arrays.asList(
                        new NestedEntity(),
                        new NestedEntity()
                ));

        StepVerifier.create(repository.save(expected))
                .expectNextCount(1)
                .verifyComplete();

        StepVerifier.create(repository.findById(expected.getUuid()))
                .assertNext(actual -> {
                    assertThat(actual.getChildren())
                            .hasSize(expected.getChildren().size());
                })
                .verifyComplete();
    }
}