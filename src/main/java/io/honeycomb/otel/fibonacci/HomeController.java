package io.honeycomb.otel.fibonacci;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

  @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
  @ResponseBody
  public String home() {
    String main = "<h1>Fibonacci Microservice</h1>" +
        "<p>This service calculates numbers in the <a href=\"https://en.wikipedia.org/wiki/Fibonacci_number\">Fibonacci Sequence</a>." +
        "You give it the index into the sequence, and it returns the Fibonacci number.</p>" +
        "<p>Click some of these links to call it:</p>" +
        "<ul>" +
        "<li><a href=\"/fib?index=0\">/fib?index=0</a></li>" +
        "<li><a href=\"/fib?index=1\">/fib?index=1</a></li>" +
        "<li><a href=\"/fib?index=2\">/fib?index=2</a></li>" +
        "<li><a href=\"/fib?index=3\">/fib?index=3</a></li>" +
        "<li><a href=\"/fib?index=4\">/fib?index=4</a></li>" +
        "<li><a href=\"/fib?index=5\">/fib?index=5</a></li>" +
        "</ul>";
    return "<html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\"/></head><body><main>" + main + "</main></body></html>";
  }

}
