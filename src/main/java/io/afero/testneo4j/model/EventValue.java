package io.afero.testneo4j.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.UUID;

@Node
@Accessors(chain = true)
@Getter @Setter @ToString
public class EventValue {

    @Id
    @GeneratedValue
    private UUID uuid;

    private String value;
}
