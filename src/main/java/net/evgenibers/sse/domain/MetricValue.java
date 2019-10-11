package net.evgenibers.sse.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Metric value entity.
 *
 * @author Evgeni Bokhanov
 */
@Document("metricValue")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricValue {

	@Id
	private String id;       // metric value ID
	private String deviceId; // device ID
	private Double value;    // metric value
}
