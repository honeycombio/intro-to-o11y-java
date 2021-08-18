package io.honeycomb.otel.fibonacci;

import io.opentelemetry.api.trace.Span;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class FibonacciController {

  @Autowired
  RestTemplate restTemplate;

  @GetMapping("/fib")
  public FibonacciNumber calculate(@RequestParam(value = "index", defaultValue = "0") String index) {
    int i = Integer.parseInt(index);
    Span span = Span.current();
    span.setAttribute("parameter.index", i);

    if (i == 0) {
      return new FibonacciNumber(i, 0);
    }
    if (i == 1) {
      return new FibonacciNumber(i, 1);
    }

    FibonacciNumber ultimate = restTemplate.getForObject("http://localhost:8080/fib?index=" + (i - 1), FibonacciNumber.class);
    FibonacciNumber penultimate = restTemplate.getForObject("http://localhost:8080/fib?index=" + (i - 2), FibonacciNumber.class);

    return new FibonacciNumber(i, penultimate.fibonacciNumber + ultimate.fibonacciNumber);

  }
}
