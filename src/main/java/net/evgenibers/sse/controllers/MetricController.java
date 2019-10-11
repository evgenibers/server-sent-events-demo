package net.evgenibers.sse.controllers;

import lombok.extern.log4j.Log4j2;
import net.evgenibers.sse.domain.MetricValueInDto;
import net.evgenibers.sse.domain.MetricOutDto;
import net.evgenibers.sse.services.MetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Metric controller.<br>
 * Sends events with generic metric values saved to database.
 *
 * @author Evgeni Bokhanov
 */
@Log4j2
@Validated
@RestController
@SuppressWarnings("unused")
@RequestMapping("/api/secured/metric")
public class MetricController {

	private final MetricService metricService;

	@Autowired
	public MetricController(final MetricService metricService) {
		this.metricService = metricService;
	}

	/**
	 * Get all metric values from database as reactive one-element stream.<br>
	 * Streams only data that is already stored in database.
	 *
	 * @return Mono-stream with all metric values
	 */
	@GetMapping("/all")
	public Mono<List<MetricOutDto>> getAllMetricValues() {
		log.info("getAllMetricValues: ");
		return metricService.getAllMetricValues();
	}

	/**
	 * Stream metric values from database as reactive stream.<br>
	 * Streams only data that is already stored in database.
	 *
	 * @return Stream with all metric values
	 */
	@GetMapping("/stream")
	public Flux<ServerSentEvent<MetricOutDto>> streamMetricValues() {
		log.info("streamMetricValues: ");
		return metricService.streamMetricValues();
	}

	/**
	 * Stream metric values as reactive stream.<br>
	 * Streams only new data that is stored in database during stream.
	 *
	 * @return SSE emitter
	 */
	@GetMapping("/sse")
	public SseEmitter sseMetricValues() {
		log.info("sseMetricValues: ");
		return metricService.sseMetricValues();
	}

	/**
	 * Save new metric data to database and sent this data to all opened emitters.
	 *
	 * @param data Metric data to save
	 */
	@PostMapping("")
	public void addMetricValue(@RequestBody @NotNull @Valid MetricValueInDto data) {
		log.info("addMetricValue: data = {}", data);
		metricService.addMetricValue(data);
	}
}
