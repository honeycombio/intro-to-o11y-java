# Intro to Observability: OpenTelemetry in Java

This Spring Boot application is here for you to try out tracing.
It consists of a microservice that calls itself, so you can simulate
a whole microservice ecosystem with just one service!

## What to do

You can remix [this app on Glitch](https://glitch.com/~intro-to-o11y-java) or run locally for speed.

### Running locally 

(I recommend cloning the repo. Java in Glitch is verrrrry slooooow. This app on your computer is fast.)

[clone the repo](https://github.com/jessitron/otel-java) 

Download the Honeycomb OpenTelemetry Java agent into your project directory:

`wget https://github.com/honeycombio/honeycomb-opentelemetry-java/releases/download/v0.6.1/honeycomb-opentelemetry-javaagent-0.6.1-all.jar`

If you use IntelliJ, add a run configuration for Maven target `spring-boot:run`. Then hit the home page at http://localhost:8080.

Or at the command line:

set up the environment:
```sh
export HONEYCOMB_API_KEY=<your API key here>
export HONEYCOMB_TRACES_DATASET=otel-java # or whatever you like
```

Run the app with `mvn spring-boot:run`

### 1. Autoinstrument!

This will make tracing happen in the Spring app with no code changes!
You'll see the web requests coming in. They'll even nest inside each other when the service calls itself. You will not
see anything specific to this app, like the query parameter on the request.

This magic happens through [instrumentation](https://docs.oracle.com/en/java/javase/11/docs/api/java.instrument/java/lang/instrument/Instrumentation.html) by a Java agent.
The agent gloms onto your Java app, recognizes Spring receiving HTTP requests, and emits events.

There's a general OpenTelemetry Java agent, and [Honeycomb wraps it into a version]
https://github.com/honeycombio/honeycomb-opentelemetry-java#agent-usage) that's easier to configure. This app uses that one.

#### Configure tracing

Finally, tell the agent how to send events to Honeycomb.
In `.env` in glitch, add these
environment variables:

```
HONEYCOMB_API_KEY=replace-this-with-a-real-api-key
HONEYCOMB_TRACES_DATASET=otel-java
```

Get a Honeycomb API Key from your Team Settings in [Honeycomb](https://ui.honeycomb.io).
(find this by clicking on your profile in the lower-left corner.)

You can name the Honeycomb Dataset anything you want.

#### See the results

Run the app. Activate the sequence of numbers.
Go to [Honeycomb](https://ui.honeycomb.io) and choose the Dataset you configured.

How many traces are there?

How many spans are in the traces?

Why are there so many??

Which trace has the most, and why is it different?

## 2. Customize a span

Let's make it easier to see what the "index" query parameter is.

To do this, change the code using the OpenTelemetry API.

### Use the API in your code

In `FibonacciController.java`, in the `getFibonacciNumber` method, add the index parameter to the current Span:

```java
import io.opentelemetry.api.trace.Span;

  Span span = Span.current();
  span.setAttribute("parameter.index", i);
```

Restart the app, make the sequence go, and find that field on the new spans.

Can you make the trace waterfall view show the index? What pattern does it show?

## 3. Create a custom span

Make the calculation into its own span, to see how much of the time spent on
this service is the meat: adding the fibonacci numbers.

Break out a method for creating the returned Fibonacci number, and add the
magical `@WithSpan` attribute.

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

# Appendix: how to add autoinstrumentation to your own app

Find details (and the latest) in [Honeycomb's docs](https://docs.honeycomb.io/getting-data-in/java/opentelemetry-distro/).

#### Get the agent

Download the agent jar [from this direct link](https://github.com/honeycombio/honeycomb-opentelemetry-java/releases/download/v0.1.1/honeycomb-opentelemetry-javaagent-0.6.1-all.jar).
In Glitch, click Tools, the Command Line, and then do this:

`wget https://github.com/honeycombio/honeycomb-opentelemetry-java/releases/download/v0.6.1/honeycomb-opentelemetry-javaagent-0.6.1-all.jar`

`sync` (this tells glitch to notice what you did at the command line)

#### Attach the agent

The goal is to add a JVM argument: `-javaagent:honeycomb-opentelemetry-javaagent-0.6.1-all.jar`

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
                honeycomb-opentelemetry-javaagent-0.6.1-all.jar
            </agent>
        </agents>
    </configuration>
</plugin>
```

### Bring in the OpenTelemetry API for custom tracing

Add these dependencies to add to `pom.xml`.

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
