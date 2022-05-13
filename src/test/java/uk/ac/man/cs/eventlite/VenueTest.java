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
public class VenueTest{
	@Autowired
	private MockMvc mvc;


	@Mock
	private Venue venue;

	@MockBean
	private EventService eventService;

	@MockBean
	private VenueService venueService;
	
	@BeforeEach
	public void setup() {
		venue = new Venue();
		venue.setId(1);
		venue.setName("Kilburn G23");
		venue.setCapacity(120);
		venue.setAddress("Oxford Road, M13 9PL");
		venue.setLongitude(12.55);
		venue.setLatitude(55.66);
		
		
	}
	@Test
	public void testGetName() throws Exception {
		assertThat(venue.getName(), is(equalTo("Kilburn G23")));
	}
	@Test
	public void testGetCapacity() throws Exception {
		assertThat(venue.getCapacity(), is(equalTo(120)));

	}
	@Test
	public void testGetAddress() {
		assertThat(venue.getAddress(), is(equalTo("Oxford Road, M13 9PL") ));
		}
	
	@Test
	public void testGetLongitude() {
		assertThat(venue.getLongitude(), is(equalTo(12.55)));
		}
	
	
	@Test
	public void testGetLatitude() {
		assertThat(venue.getLatitude(), is(equalTo(55.66)));
		}
		
	
	
	
	
}