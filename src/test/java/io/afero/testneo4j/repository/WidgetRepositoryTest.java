package io.afero.testneo4j.repository;

import io.afero.testneo4j.configuration.Neo4jConfiguration;
import io.afero.testneo4j.model.EventValue;
import io.afero.testneo4j.model.Widget;
import io.afero.testneo4j.model.WidgetEvent;
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

@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Import(Neo4jConfiguration.class)
@DataNeo4jTest
public class WidgetRepositoryTest {

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
    private WidgetRepository repository;

    @AfterEach
    public void teardown() {
        repository.deleteAll().block();
    }

    @Test
    public void createWidgetHappyPath() {
        Widget widget = new Widget().setEvents(Arrays.asList(
                new WidgetEvent().setTag("one").setValues(Arrays.asList(
                        new EventValue().setValue("foo"),
                        new EventValue().setValue("bar")
                )),
                new WidgetEvent().setTag("two").setValues(Arrays.asList(
                        new EventValue().setValue("goo"),
                        new EventValue().setValue("car")
                ))
        ));

        StepVerifier.create(repository.save(widget))
                .assertNext(saved -> {
                    assertThat(saved.getEvents())
                            .hasSize(widget.getEvents().size());
                    assertThat(saved.getEvents().get(0).getValues())
                            .hasSize(widget.getEvents().get(0).getValues().size());
                    assertThat(saved.getEvents().get(1).getValues())
                            .hasSize(widget.getEvents().get(1).getValues().size());
                })
                .verifyComplete();

        StepVerifier.create(repository.findById(widget.getUuid()))
                .assertNext(actual -> {
                    assertThat(actual.getEvents())
                            .hasSize(widget.getEvents().size());
                    assertThat(actual.getEvents().get(0).getValues())
                            .hasSize(widget.getEvents().get(0).getValues().size());
                    assertThat(actual.getEvents().get(1).getValues())
                            .hasSize(widget.getEvents().get(1).getValues().size());
                })
                .verifyComplete();
    }
}