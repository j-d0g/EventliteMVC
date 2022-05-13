package uk.ac.man.cs.eventlite;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;


import uk.ac.man.cs.eventlite.config.Security;
import uk.ac.man.cs.eventlite.controllers.EventsController;
import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EventsController.class)
@Import(Security.class)
public class EventTest {
	@Autowired
	private MockMvc mvc;

	@Mock
	private Event event;

	@Mock
	private Venue venue;

	@MockBean
	private EventService eventService;

	@MockBean
	private VenueService venueService;
	@BeforeEach
	public void setup() {
		Venue v1 = new Venue();
		v1.setId(1);
		v1.setName("Kilburn G23");
		v1.setCapacity(120);
		v1.setAddress("Oxford Road, M13 9PL");
		v1.setLongitude(12.55);
		v1.setLatitude(55.66);
		event = new Event();
		event.setName("COMP23412 Showcase, group F");
		event.setDescription("Test Description");
		event.setTime(LocalTime.of(16, 0));
		event.setDate(LocalDate.of(2022, 05, 17));
		event.setVenue(v1);
		
	}
	@Test
	public void testGetName() throws Exception {
		assertThat(event.getName(), is(equalTo("COMP23412 Showcase, group F")));
	}
	@Test
	public void testGetDate() throws Exception {
		assertThat(event.getDate(), is(equalTo(LocalDate.of(2022, 05, 17))));

	}
	@Test
	public void testGetTime() {
		assertThat(event.getTime(), is(equalTo(LocalTime.of(16, 0))));
		}
	
	@Test
	public void testGetDescription() {
		assertThat(event.getDescription(), is(equalTo("Test Description")));
		}
	
	
	@Test
	public void testGetvName() {
		assertThat(event.getVenue().getName(), is(equalTo("Kilburn G23")));
		}
		
	@Test
	public void testGetvCapacity() {
		assertThat(event.getVenue().getCapacity(), is(equalTo(120)));
		}
	
	
	
}
