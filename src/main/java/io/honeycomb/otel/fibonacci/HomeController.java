package io.honeycomb.otel.fibonacci;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
  
  private static final String head = "<head>" +
    "<title>Fibonacci Microservice</title>" +
"<script src=\"/sequence.js\" defer></script>"+
    "<link rel=\"stylesheet\" type=\"text/css\" href=\"styles.css\"/>" +
    "</head>";
  
private static final String bodyContent = 
  "<header><h1>A sequence of numbers:</h1></header>" +
"<main>" +
      "<button id=\"go-button\">Go</button>" +
      "<div id=\"put-numbers-here\" class=\"fibonacci-sequence\"> &nbsp;</div>" +
      "<button id=\"stop-button\">Stop</button>" +
"   </main>";

  @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
  @ResponseBody
  public String home() {
    return "<html>"+head+"<body>" + bodyContent + "</body></html>";
  }

}
