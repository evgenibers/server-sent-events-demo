package net.evgenibers.sse.repositories;

import net.evgenibers.sse.domain.MetricValue;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * Metric value repository.
 *
 * @author Evgeni Bokhanov
 */
@Repository
public interface MetricValueRepository extends ReactiveMongoRepository<MetricValue, String> {

	Flux<MetricValue> findAllByDeviceId(String deviceId);
}
