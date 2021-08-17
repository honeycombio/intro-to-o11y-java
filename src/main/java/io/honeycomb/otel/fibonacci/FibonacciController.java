package io.honeycomb.otel.fibonacci;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FibonacciController {

  @GetMapping("/fib")
  public FibonacciNumber calculate(@RequestParam(value="index", defaultValue="0") String index) {
    int i = Integer.parseInt(index);
    return new FibonacciNumber(i, 0);
  }
}
