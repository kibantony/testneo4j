package io.afero.testneo4j.configuration;

import org.neo4j.driver.Driver;
import org.neo4j.driver.internal.shaded.reactor.core.publisher.Flux;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.core.ReactiveDatabaseSelectionProvider;
import org.springframework.data.neo4j.core.transaction.ReactiveNeo4jTransactionManager;
import org.springframework.data.neo4j.repository.config.ReactiveNeo4jRepositoryConfigurationExtension;
import org.springframework.transaction.ReactiveTransactionManager;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass({
        ReactiveNeo4jTransactionManager.class,
        ReactiveTransactionManager.class,
        Flux.class
})
public class Neo4jConfiguration {

    @Bean(ReactiveNeo4jRepositoryConfigurationExtension.DEFAULT_TRANSACTION_MANAGER_BEAN_NAME)
    @ConditionalOnMissingBean(ReactiveTransactionManager.class)
    public ReactiveTransactionManager reactiveTransactionManager(
            Driver driver,
            ReactiveDatabaseSelectionProvider databaseNameProvider
    ) {
        return new ReactiveNeo4jTransactionManager(driver, databaseNameProvider);
    }

}
