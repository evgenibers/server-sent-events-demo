package net.evgenibers.sse.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Metric value output DTO.
 *
 * @author Evgeni Bokhanov
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricOutDto {

	private String id;       // metric value ID
	private String deviceId; // device ID
	private Double value;    // metric value
}
