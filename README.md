# Into to Observability: OpenTelemetry in Java

This Spring Boot application is here for you to try out tracing.
It consists of a microservice that calls itself, so you can simulate
a whole microservice ecosystem with just one service!

## What to do

First, clone this repo and run the application.

IntelliJ: Open it in your IDE and right-click on the FibonacciApplication class and choose 'run'

Command line: `mvn spring-boot:run`

Then hit the home page at http://localhost:8080

## How to add tracing: 

### Autoinstrumentation
This will make tracing happen in the Spring app with no code changes!
You'll see the web requests coming in. They'll even nest inside each other when the service calls itself. You will not
see anything specific to this app, like the query parameter on the request.

To do the autoinstrumentation:

It takes 2 changes to how you run the app: some environment variables and an extra JVM argument.

The environment variables are:
```
SAMPLE_RATE=1
SERVICE_NAME=my-favorite-service
HONEYCOMB_API_KEY=replace-this-with-a-real-api-key
HONEYCOMB_DATASET=my-dataset
```

The extra JVM argument is a Java Agent:

`-javaagent:honeycomb-opentelemetry-javaagent-0.4.0-all.jar`

...which means you need that jar in your current directory. Download the jar [as instructed here](
https://github.com/honeycombio/honeycomb-opentelemetry-java#agent-usage),
 or ([from this direct link](https://github.com/honeycombio/honeycomb-opentelemetry-java/releases/download/v0.4.0/honeycomb-opentelemetry-javaagent-0.4.0-all.jar))

You can specify the JVM arguments either in your IntelliJ configuration, or...

mvn:spring-boot run` will read agents from the POM, so add this configuration to the spring-boot-maven-pluigin section:

```xml
 <plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
        <environmentVariables>
            <SAMPLE_RATE>1</SAMPLE_RATE>
            <SERVICE_NAME>fibonacci</SERVICE_NAME>
<!--                        <HONEYCOMB_API_KEY>define this in your runtime env vars</HONEYCOMB_API_KEY>-->
            <HONEYCOMB_DATASET>otel-java</HONEYCOMB_DATASET>
        </environmentVariables>
        <agents>
            <agent>
                honeycomb-opentelemetry-javaagent-0.4.0-all.jar
            </agent>
        </agents>
    </configuration>
</plugin>
```

Then define the HONEYCOMB_API_KEY separately (don't commit it to your repo).

