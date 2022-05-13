package uk.ac.man.cs.eventlite.controllers;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import uk.ac.man.cs.eventlite.config.Security;
import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(EventsController.class)
@Import(Security.class)
public class EventsControllerTest {

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

	@Test
	public void getIndexWhenNoEvents() throws Exception {
		when(eventService.findAll()).thenReturn(Collections.<Event>emptyList());


		mvc.perform(get("/events").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(view().name("events/index")).andExpect(handler().methodName("getAllEvents"));

		verify(eventService).findAll();
		
		verifyNoInteractions(event);
		
	}

	@Test
	public void getIndexWithEvents() throws Exception {
		when(venue.getName()).thenReturn("Kilburn Building");
		when(venueService.findAll()).thenReturn(Collections.<Venue>singletonList(venue));

		when(event.getVenue()).thenReturn(venue);
		when(eventService.findAll()).thenReturn(Collections.<Event>singletonList(event));

		mvc.perform(get("/events").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(view().name("events/index")).andExpect(handler().methodName("getAllEvents"));

		verify(eventService).findAll();
		
	}

	@Test
	public void getEventNotFound() throws Exception {
		mvc.perform(get("/events/99").accept(MediaType.TEXT_HTML)).andExpect(status().isNotFound())
				.andExpect(view().name("events/not_found")).andExpect(handler().methodName("getEvent"));
	}
	private CsrfToken csrfToken;
	private final String TOKEN_ATTR_NAME =
		"org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";

	@BeforeEach
	public void initCsrfToken(){
		HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
		csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());
	}
	@Test
	@WithMockUser(roles = "ADMINISTRATOR")
	public void accessAddEventPageAuthenticated() throws Exception{
		when(venueService.findAll()).thenReturn(Collections.singletonList(new Venue()));

		mvc.perform(get("/events/add-event").accept(MediaType.TEXT_HTML)
			.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
			.param(csrfToken.getParameterName(), csrfToken.getToken()))
			.andExpect(status().isOk())
			.andExpect(view().name("events/add-event"))
			.andExpect(handler().methodName("addEvent"));

		verify(venueService).findAll();
	}
	@Test
	public void accessaddeventNotAuthenticated() throws Exception{
		when(venueService.findAll()).thenReturn(Collections.singletonList(new Venue()));

		mvc.perform(get("/events/add-event").accept(MediaType.TEXT_HTML)
			.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
			.param(csrfToken.getParameterName(), csrfToken.getToken()))
			.andExpect(status().isFound())
			.andExpect(redirectedUrlPattern("**/sign-in"));

		verifyNoInteractions(venueService);
	}
	@Test
	public void createaneventwithoutpermissions() throws Exception{
		mvc.perform(post("/events").accept(MediaType.TEXT_HTML)
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.param("_csrf", csrfToken.getToken())
			.param("name", "Event")
			.param("venue", "0")
			.param("date", "2021-05-17")
			.param("time", "22:17")
			.param("description", "desc")
			.sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken()))
			.andExpect(status().isFound())
			.andExpect(redirectedUrlPattern("**/sign-in"));

		//check no venue was looked up
		verifyNoInteractions(venueService);
		//check persisting object wasn't attempted
		verifyNoInteractions(eventService);
	}
	@Test
	@WithMockUser(roles = "ADMINISTRATOR")
	public void createNewValidEventWithPermission() throws Exception{
		mvc.perform(post("/events").accept(MediaType.TEXT_HTML)
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.param("_csrf", csrfToken.getToken())
			.param("name", "testEvent")
			.param("venue.id", "1")
			.param("date", "2021-05-17")
			.param("time", "22:17")
			.param("description", "desc")
			.sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken()))
			.andExpect(status().isFound())
			.andExpect(redirectedUrl("/events"));

		//check validation successful by making sure event persisted after binding
		verify(eventService).save(any());
	}

	

	@Test
	@WithMockUser(roles = "ADMINISTRATOR")
	public void createNewEventNoData() throws Exception{
		mvc.perform(post("/events").accept(MediaType.TEXT_HTML)
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.param("_csrf", csrfToken.getToken())
			.sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken()))
			//verify invalid inputs handled without issues in the same view
			.andExpect(status().isOk())
			.andExpect(view().name("events/add-event"));

		//check persisting object wasn't attempted via service
		verifyNoInteractions(eventService);
	}

	

	@Test
	public void accessUpdateEventViewWithoutAuthority() throws Exception{
		mvc.perform(post("/events/0").accept(MediaType.TEXT_HTML)
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
			.param(csrfToken.getParameterName(), csrfToken.getToken()))
			//finds redirect and points user to sign in page
			.andExpect(status().isFound())
			.andExpect(redirectedUrlPattern("**/sign-in"));

		verifyNoInteractions(eventService);
	}

	@Test
	@WithMockUser(roles = "ADMINISTRATOR")
	public void updateEventWithValidData() throws Exception{
		mvc.perform(post("/events/updateEvent").accept(MediaType.TEXT_HTML)
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.sessionAttr(TOKEN_ATTR_NAME, csrfToken)
			.param(csrfToken.getParameterName(), csrfToken.getToken())
			.param("_csrf", csrfToken.getToken())
			.param("name", "testEvent")
			.param("venue.id", "0")
			.param("date", "2021-05-17")
			.param("time", "22:17")
			.param("description", "desc"))
			.andExpect(handler().methodName("updateEvent"))
			//expect redirect to events
			.andExpect(status().isFound())
			.andExpect(redirectedUrl("/events"));

		//verify update called on our event
		verify(eventService).save(any());
	}

	

	

	@Test
	public void deleteExistingEventWithoutAuthority() throws Exception {
		mvc.perform(delete("/events/search").accept(MediaType.TEXT_HTML)
			.sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken()))
			//finds redirect and points user to sign in page
			.andExpect(status().isFound())
			.andExpect(redirectedUrlPattern("**/sign-in"));

		//confirm does not interact with event service
		verifyNoInteractions(eventService);
	}

	@Test
	@WithMockUser(roles = "ADMINISTRATOR")
	public void deleteExistingEventWithAuthority() throws Exception {
		when(eventService.findById(0)).thenReturn(Optional.of(new Event()));

		mvc.perform(delete("/events/0").accept(MediaType.TEXT_HTML)
			.sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken()))
			.andExpect(handler().methodName("deleteEvent"))
			//expect redirect to venues
			.andExpect(status().isFound())
			.andExpect(redirectedUrl("/events"))
			//expect redirect to contain no error messages
			.andExpect(flash().attribute("deleteEventErrorMsg", org.hamcrest.Matchers.nullValue()));

		//verify event id was checked to be valid
		verify(eventService).findById(0L);
		//verify delete was called
		verify(eventService).deleteById(0L);
		//confirm does not interact with event service
		verifyNoMoreInteractions(eventService);
	}

	@Test
	@WithMockUser(roles = "ADMINISTRATOR")
	public void deleteNonExistentEventWithAuthority() throws Exception {
		when(eventService.findById(0)).thenReturn(Optional.empty());

		mvc.perform(delete("/events/0").accept(MediaType.TEXT_HTML)
			.sessionAttr(TOKEN_ATTR_NAME, csrfToken).param(csrfToken.getParameterName(), csrfToken.getToken()))
			.andExpect(handler().methodName("deleteEvent"))
			//expect redirect to venues
			.andExpect(status().isFound())
			.andExpect(redirectedUrl("/events"))
			//expect redirect to contain an error message
			.andExpect(flash().attribute("deleteEventErrorMsg", "Could not find event by specified Id."));

		//verify event id was checked to be valid
		verify(eventService).findById(0L);
		//confirm service didnt attempt delete and does not further interact with event service
		verifyNoMoreInteractions(eventService);
	}
}
