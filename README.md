# Into to Observability: OpenTelemetry in Java

This Spring Boot application is here for you to try out tracing.
It consists of a microservice that calls itself, so you can simulate
a whole microservice ecosystem with just one service!

## What to do

You can remix this app on Glitch or clone the repo and open it in IntelliJ.

In IntelliJ, add a run configuration for Maven target `spring-boot:run`. Then hit the home page at http://localhost:8080.

### Autoinstrument!

This will make tracing happen in the Spring app with no code changes!
You'll see the web requests coming in. They'll even nest inside each other when the service calls itself. You will not
see anything specific to this app, like the query parameter on the request.

This magic happens through [instrumentation](https://docs.oracle.com/en/java/javase/11/docs/api/java.instrument/java/lang/instrument/Instrumentation.html) by a Java agent.
The agent gloms onto your Java app, recognizes Spring receiving HTTP requests, and emits events.

There's a general OpenTelemetry Java agent, and [Honeycomb wraps it into a version]
https://github.com/honeycombio/honeycomb-opentelemetry-java#agent-usage) that's easier to configure. we'll use that one.

#### Get the agent

Download the agent jar [from this direct link](https://github.com/honeycombio/honeycomb-opentelemetry-java/releases/download/v0.4.0/honeycomb-opentelemetry-javaagent-0.4.0-all.jar).
In Glitch, click Tools, the Command Line, and then do this:

`wget https://github.com/honeycombio/honeycomb-opentelemetry-java/releases/download/v0.4.0/honeycomb-opentelemetry-javaagent-0.4.0-all.jar`

`sync` (this tells glitch to notice what you did at the command line)

#### Attach the agent

The goal is to add a JVM argument: `-javaagent:honeycomb-opentelemetry-javaagent-0.4.0-all.jar`

To add this to `mvn spring-boot:run`,
open `pom.xml`, find the `plugin` block for `spring-boot-maven-plugin`, and 
add a `configuration` block like the one here:

```xml
 <plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
        <agents>
            <agent>
                honeycomb-opentelemetry-javaagent-0.4.0-all.jar
            </agent>
        </agents>
    </configuration>
</plugin>
```

#### Configure the Agent

Finally, tell the agent how to send events to Honeycomb.
In `.env` in glitch or your run configuration in IntelliJ, add these
environment variables:

```
HONEYCOMB_API_KEY=replace-this-with-a-real-api-key
HONEYCOMB_DATASET=otel-java
SERVICE_NAME=fibonacci-microservice
SAMPLE_RATE=1
```

Get a Honeycomb API Key from your Team Settings in [Honeycomb](https://ui.honeycomb.io).
(find this by clicking on your profile in the lower-left corner.)

You can name the Honeycomb Dataset anything you want.

You can choose any Service Name you want.

The Sample Rate determines how many requests each saved trace represents; 1 means "keep all of them."" Right now you want all of them.
