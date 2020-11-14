package io.afero.testneo4j.repository;

import io.afero.testneo4j.model.Widget;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;

import java.util.UUID;

public interface WidgetRepository extends ReactiveNeo4jRepository<Widget, UUID> {
}
