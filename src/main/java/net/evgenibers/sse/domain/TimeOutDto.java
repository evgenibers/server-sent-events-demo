package net.evgenibers.sse.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Server time output DTO.
 *
 * @author Evgeni Bokhanov
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeOutDto {

	private String time; // formatted time string
}
