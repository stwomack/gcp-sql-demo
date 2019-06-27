package com.womack.gcpsqldemo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Collection;

@SpringBootApplication
public class GcpSqlDemoApplication {

	private final Log log = LogFactory.getLog(getClass());

	private final JdbcTemplate template;

	private final RowMapper<Reservation> rowMapper =
			(rs, rowNum) -> new Reservation(rs.getLong("id"), rs.getString("name"));

	GcpSqlDemoApplication(JdbcTemplate template) {
		this.template = template;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void ready() {
		Collection<Reservation> reservations = this.template
				.query("select * from reservations", this.rowMapper);
		reservations.forEach(reservation -> log.info("reservation: " + reservation.toString()));
	}

	public static void main(String args[]) {
		SpringApplication.run(GcpSqlDemoApplication.class, args);
	}
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Reservation {
	private Long id;
	private String reservationName;
}