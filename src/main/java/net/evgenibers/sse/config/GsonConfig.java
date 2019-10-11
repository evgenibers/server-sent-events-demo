package net.evgenibers.sse.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Gson configuration.
 *
 * @author Evgeni Bokhanov
 */
@Configuration
public class GsonConfig {

	@Bean
	public Gson gson() {
		return new GsonBuilder().serializeNulls().create();
	}
}
