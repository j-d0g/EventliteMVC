package uk.ac.man.cs.eventlite.config.data;

import java.time.LocalDate;
import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.*;

@Configuration
@Profile("default")
public class InitialDataLoader {

	private final static Logger log = LoggerFactory.getLogger(InitialDataLoader.class);

	@Autowired
	private EventService eventService;

	@Autowired
	private VenueService venueService;

	@Bean
	CommandLineRunner initDatabase() {
		return args -> {
			if (venueService.count() > 0) {
				log.info("Database already populated with venues. Skipping venue initialization.");
			} else {
				// Build and save initial venues here.
				Venue v1 = new Venue();
				v1.setId(1);
				v1.setName("Kilburn G23");
				v1.setCapacity(120);
				venueService.save(v1);
				
				Venue v2 = new Venue();
				v2.setId(2);
				v2.setName("Online");
				v2.setCapacity(10000);
				venueService.save(v2);
			}

			if (eventService.count() > 0) {
				log.info("Database already populated with events. Skipping event initialization.");
			} else {
				// Build and save initial events here.
				Venue v1 = new Venue();
				v1.setId(1);
				v1.setName("Kilburn G23");
				v1.setCapacity(120);
				venueService.save(v1);
				
				Venue v2 = new Venue();
				v2.setId(2);
				v2.setName("Online");
				v2.setCapacity(10000);
				venueService.save(v2);
				 
				Event event = new Event();
				event.setId(1);
				event.setName("event");
				event.setTime(LocalTime.now());
				event.setDate(LocalDate.now());
				event.setVenue(v1);
				event.setVenue(v1);
				eventService.save(event);			
			}
		};
	} 
}
