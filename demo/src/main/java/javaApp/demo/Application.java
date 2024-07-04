package javaApp.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {

  @RequestMapping("/")
  public String home() {
    return "Hello World Docker This Side";
    // return SpringApplication.run(Application.class, args)
   
  }
 

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

}