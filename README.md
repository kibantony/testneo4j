# TestNeo4j
This is a simple test project to reproduce [DATAGRAPH-1431](https://jira.spring.io/browse/DATAGRAPH-1431)
## Usage
Run the tests via `./gradlew test`
Notice it fails.
Change the Spring Boot version in `build.gradle` to `2.4.0-M4`
Run the tests again, and notice that it now succeeds.  