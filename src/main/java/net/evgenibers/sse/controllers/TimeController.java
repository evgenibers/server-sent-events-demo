package net.evgenibers.sse.controllers;

import lombok.extern.log4j.Log4j2;
import net.evgenibers.sse.domain.TimeOutDto;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Time controller.<br>
 * Sends events with current server time.
 *
 * @author Evgeni Bokhanov
 */
@Log4j2
@RestController
@SuppressWarnings("unused")
@RequestMapping("/api/secured/time")
public class TimeController {

	/**
	 * Streams current server time with constant interval in one second.
	 *
	 * @return Reactive Stream
	 */
	@GetMapping("")
	public Flux<ServerSentEvent<TimeOutDto>> streamTime() {
		return Flux.interval(Duration.ofSeconds(1L))
				.map(e -> ServerSentEvent.<TimeOutDto>builder()
						.id(String.valueOf(e))
						.event("time-event")
						.data(new TimeOutDto(LocalDateTime.now().toString()))
						.build()
				);
	}
}
