package io.afero.testneo4j.repository;

import io.afero.testneo4j.model.NestedEntity;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;

import java.util.UUID;

public interface NestedEntityRepository extends ReactiveNeo4jRepository<NestedEntity, UUID> {
}
