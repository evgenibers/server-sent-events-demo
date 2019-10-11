package net.evgenibers.sse.services;

import lombok.extern.log4j.Log4j2;
import net.evgenibers.sse.domain.MetricOutDto;
import net.evgenibers.sse.domain.MetricValue;
import net.evgenibers.sse.domain.MetricValueInDto;
import net.evgenibers.sse.repositories.MetricValueRepository;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Metric service.
 *
 * @author Evgeni Bokhanov
 */
@Log4j2
@Service
public class MetricService {

	/**
	 * Thread-safe list of current SSE emitters.
	 */
	private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

	private final MetricValueRepository metricValueRepository;

	public MetricService(MetricValueRepository metricValueRepository) {
		this.metricValueRepository = metricValueRepository;
	}

	/**
	 * Get all metric values from database as reactive one-element stream.<br>
	 * Streams only data that is already stored in database.
	 *
	 * @return Mono-stream with all metric values
	 */
	public Mono<List<MetricOutDto>> getAllMetricValues() {
		log.info("getAllMetricValues: ");

		return metricValueRepository.findAll()
				.collectList() // get all stream data as list
				.map(l -> l
						.stream()
						.map(e -> new MetricOutDto(e.getId(), e.getDeviceId(), e.getValue()))
						.collect(Collectors.toList())
				);
	}

	/**
	 * Stream metric values from database as reactive stream.<br>
	 * Streams only data that is already stored in database.
	 *
	 * @return Stream with all metric values
	 */
	public Flux<ServerSentEvent<MetricOutDto>> streamMetricValues() {
		log.info("streamMetricValues: ");

		return metricValueRepository.findAll()
				.retry() // resubscribe to stream if needed
				.map(e -> ServerSentEvent.<MetricOutDto>builder()
						.id(e.getId())
						.event("metric-event")
						.data(new MetricOutDto(e.getId(), e.getDeviceId(), e.getValue()))
						.build()
				);
	}

	/**
	 * Stream metric values as reactive stream.<br>
	 * Streams only new data that is stored in database during stream.
	 *
	 * @return SSE emitter
	 */
	public SseEmitter sseMetricValues() {
		log.info("sseMetricValues: ");

		// create emitter
		SseEmitter emitter = new SseEmitter(1000000L); // TODO do something with timeout

		// callback if stream is complete
		emitter.onCompletion(() -> {
			log.info("Completed: {}", emitter);
			emitters.remove(emitter);
		});

		// callback if stream is finished with error
		emitter.onError(error -> {
			log.error(error);
			log.info("Error: {}", emitter);
			emitters.remove(emitter);
		});

		// callback if stream is timed out
		emitter.onTimeout(() -> {
			log.info("Timeout: {}", emitter);
			emitters.remove(emitter);
		});

		// add emitter to the global list
		emitters.add(emitter);

		// return created emitter
		return emitter;
	}

	/**
	 * Save new metric data to database and sent this data to all opened emitters.
	 *
	 * @param data Metric data to save
	 */
	public void addMetricValue(MetricValueInDto data) {
		log.info("addMetricValue: data = {}", data);

		// save to database
		Mono<MetricValue> mono = metricValueRepository.save(new MetricValue(null, data.getDeviceId(), data.getValue()));

		// subscribe callback to saved data
		mono.subscribe(c -> {
			log.info("emitters.size() = {}", emitters.size());

			// send saved data to all current SSE emitters
			for (Iterator<SseEmitter> iterator = emitters.iterator(); iterator.hasNext();) {
				try {
					iterator.next().send(ServerSentEvent.<MetricOutDto>builder()
							.id(c.getId())
							.event("metric-event")
							.data(new MetricOutDto(c.getId(), c.getDeviceId(), c.getValue()))
							.build().data()
					);
				} catch (IOException e) {
					log.error("addMetricValue", e);
				}
			}
		});
	}
}
