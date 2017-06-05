package web;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.boot.builder.*;
import org.springframework.boot.web.support.*;

@SpringBootApplication
public class App extends SpringBootServletInitializer {
	public static void main(String[] s) {
		SpringApplication.run(App.class, s);
	}

	@Override
        protected SpringApplicationBuilder configure(SpringApplicationBuilder app) {
		return app.sources(App.class);
	}
}