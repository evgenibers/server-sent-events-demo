package net.evgenibers.sse.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Metric value input DTO.
 *
 * @author Evgeni Bokhanov
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricValueInDto {

	@NotNull
	@Size(min = 1, max = 256)
	private String deviceId; // device ID
	@NotNull
	private Double value;    // metric value ID
}
