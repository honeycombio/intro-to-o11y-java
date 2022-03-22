# Intro to Observability: OpenTelemetry in Java

This Spring Boot application is here for you to try out tracing.
It consists of a microservice that calls itself, so you can simulate a whole microservice ecosystem with just one service!

## What to Do

Recommended:

[![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://github.com/honeycombio/intro-to-o11y-java)

Alternative:

Clone this repository. It expects that you have Java and Maven installed.

If you use [VSCode devcontainers](https://code.visualstudio.com/docs/remote/containers-tutorial), this repository is configured to be opened in a container.

### Start the App

`mvn spring-boot:run`

or with the script:

`./run`

### See the App

In GitPod: while it's running, click "Remote Explorer" on the left sidebar; then expand "ports" and look for a "preview" button.

Locally: [http://localhost:8080](http://localhost:8080)

Activate the sequence of numbers by pushing **Go**. After you see numbers, push **Stop**. Repeat this a few times.

### Stop the App

Push `Ctrl-C` in the terminal where the app is running.

## Configure Tracing: Connect to Honeycomb

If the app is running, stop it. Until...

You need two environment variables.

Get a Honeycomb API Key from your Team Settings in [Honeycomb](https://ui.honeycomb.io).
(Find this by clicking on your profile in the lower-left corner.)

**Recommended**: Let's put them in a file (and then not commit it).

Create a .env file:

`cp .env.example .env`

Now open `.env`, and populate the environment variables. (This file will be ignored by git, so you won't commit your API key.)

```
export HONEYCOMB_API_KEY=replace-this-with-a-real-api-key # important and unique to you
export HONEYCOMB_TRACES_DATASET=hello-observability # can be any name
export OTEL_SERVICE_NAME=fib-microsvc # can be any name
```

Alternative: set the variables at the command line before running the app.
(These will be overridden by .env if it exists.)

```
export HONEYCOMB_API_KEY=<your api key here>
export HONEYCOMB_TRACES_DATASET=hello-observability # or whatever you want to name it
export OTEL_SERVICE_NAME=fib-microsvc # or whatever you like

./run
```

Alternative: You can set these variables in `pom.xml` if you uncomment the HONEYCOMB_API_KEY line, but then don't commit that change to git.

Alternative 2 : If you use IntelliJ, add a run configuration for Maven target `spring-boot:run`. Configure its environment variables as below.

Alternative 3: In GitPod, you can set [variables](https://gitpod.io/variables) to be available in `*/*` workspaces.

### Run the App Again

Run the app:

`./run`

Once the Spring banner passes and the logs hold still, hit it at [http://localhost:8080](http://localhost:8080).

Activate the sequence of numbers by pushing **Go**. After you see numbers, push **Stop**. Try this a few times.

### See the Results

Go to [Honeycomb](https://ui.honeycomb.io) and choose the Dataset you configured.

Do you see data in the graphs? Success!

## Part 3 of the workshop: Customize a Span

Let's make it easier to see what the "index" query parameter is.

To do this, change the code using the OpenTelemetry API.

### Use the API in Your Code

In the `FibonacciController.java` file within the `getFibonacciNumber` method, add the index parameter to the current Span:

```java
import io.opentelemetry.api.trace.Span;

  Span span = Span.current();
  span.setAttribute("parameter.index", i);
```

Restart the app, make the sequence go, and find that field on the new spans.

Can you make the trace waterfall view show the index?
What pattern does it show?

## Advanced: Create a Custom Span

Make the calculation into its own span, to see how much of the time spent on this service is the meat: adding the fibonacci numbers.

Break out a method for creating the returned Fibonacci number, and add the magical `@WithSpan` attribute.

Something like:

```java
import io.opentelemetry.extension.annotations.WithSpan;

  @WithSpan
  private FibonacciNumber calculate(int index, FibonacciNumber previous, FibonacciNumber oneBeforeThat) {
    return new FibonacciNumber(index, previous.fibonacciNumber + oneBeforeThat.fibonacciNumber);
  }
```

After a restart, do your traces show this extra span? Do you see the name of your method?
What percentage of the service time is spend in it?

## Appendix: How to Add Auto-instrumentation to Your Own App

This magic happens through [instrumentation](https://docs.oracle.com/en/java/javase/11/docs/api/java.instrument/java/lang/instrument/Instrumentation.html) by a Java agent.
The agent gloms onto your Java app, recognizes Spring receiving HTTP requests, and emits events.

There's a general OpenTelemetry Java agent, and [Honeycomb wraps it into a version](https://github.com/honeycombio/honeycomb-opentelemetry-java#agent-usage) that's easier to configure. This app uses that one.

Find details (and the latest) in [Honeycomb's docs](https://docs.honeycomb.io/getting-data-in/java/opentelemetry-distro/).

### Get the Agent

This is a jar that runs alongside your program, and injects bytecode for tracing.

Download the Honeycomb OpenTelemetry Java agent into your project directory from [this direct download link](https://github.com/honeycombio/honeycomb-opentelemetry-java/releases/download/v0.8.1/honeycomb-opentelemetry-javaagent-0.8.1.jar) or from the [releases page](https://github.com/honeycombio/honeycomb-opentelemetry-java/releases).

or from a linux command line (including the devcontainer): `wget https://github.com/honeycombio/honeycomb-opentelemetry-java/releases/download/v0.8.1/honeycomb-opentelemetry-javaagent-0.8.1.jar`

### Attach the Agent

The goal is to add a JVM argument: `-javaagent:honeycomb-opentelemetry-javaagent-0.6.1-all.jar`

To add this to `mvn spring-boot:run`, open `pom.xml`, find the `plugin` block for `spring-boot-maven-plugin`, and add a `configuration` block like the one here:

```xml
 <plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
        <agents>
            <agent>
                honeycomb-opentelemetry-javaagent-0.8.1.jar
            </agent>
        </agents>
    </configuration>
</plugin>
```

### Bring in the OpenTelemetry API for Custom Tracing

Add these dependencies to `pom.xml`:

```xml
    <dependency>
        <groupId>io.opentelemetry</groupId>
        <artifactId>opentelemetry-api</artifactId>
        <version>1.5.0</version>
    </dependency>
    <dependency>
        <groupId>io.opentelemetry</groupId>
        <artifactId>opentelemetry-extension-annotations</artifactId>
        <version>1.5.0</version>
    </dependency>
```

## Updating this Repo

### Honeycomb Distro

Check the version of the agent in the [Releases](https://github.com/honeycombio/honeycomb-opentelemetry-java/releases) page of Honeycomb OpenTelemetry Distribution for Java.
Compare to the jar in the root of this repo.

If out of date:

- update the version in this file (many times, including the next line)
- download the new one: `wget https://github.com/honeycombio/honeycomb-opentelemetry-java/releases/download/v0.8.1/honeycomb-opentelemetry-javaagent-0.8.1.jar`
- delete the old one
- update the version in the `agent` section of `pom.xml`

### OTel Libraries

Check the [README](https://github.com/honeycombio/honeycomb-opentelemetry-java/blob/main/README.md) of the Honeycomb distro for the current version of OTel.

Make the `io.opentelemetry` dependencies in `pom.xml` match that.
Test the app.

## FAQ

**How do I open this project in IntelliJ IDEA?**

You're welcome to use the IDE you're comfortable with.

But setting up IntelliJ is out of scope for this document. It is possible, but strangely hard (in my experience).
