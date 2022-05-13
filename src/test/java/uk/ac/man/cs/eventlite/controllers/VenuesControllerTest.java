package uk.ac.man.cs.eventlite.controllers;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import java.util.ArrayList;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import uk.ac.man.cs.eventlite.config.Security;
import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;
@WebMvcTest(VenuesController.class)
public class VenuesControllerTest {
@Mock
private Venue venue;
@Mock
private Event event;
@Autowired
private MockMvc mvc;
@MockBean
private VenueService venueService;
@MockBean
private EventService eventService;



@Test
public void AftterVenueWithNoAuthourization() throws Exception {
	mvc.perform(post("/venues").contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.param("name", "Venue Name")
			.param("address", "Road Road,M13 9PR")
			.param("capacity", "100")
			.accept(MediaType.TEXT_HTML).with(csrf()))
	.andExpect(status().isFound())
	.andExpect(header().string("Location", endsWith("/sign-in")));
	verify(venueService, never()).save(venue);
	}

@Test
public void AfterVenueIfIncorrectRole() throws Exception {
	mvc.perform(post("/venues").with(user("Rob").roles("USER"))
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.param("name", "Venue Name")
			.param("address", "Road Road,M13 9PR")
			.param("capacity", "100")
			.accept(MediaType.TEXT_HTML).with(csrf()))
	.andExpect(status().isForbidden());
	verify(venueService, never()).save(venue);
	}

@Test
public void AfterVenuetWithNoAuthourization() throws Exception {
	mvc.perform(post("/venues").with(user("Rob").roles(Security.ADMIN_ROLE))
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.param("name", "Venue Name")
			.param("address", "Road Road,M13 9PR")
			.param("capacity", "100")
			.accept(MediaType.TEXT_HTML)).andExpect(status().isForbidden());
	verify(venueService, never()).save(venue);
	}

public void AfterVenue() throws Exception {
	ArgumentCaptor<Venue> arg = ArgumentCaptor.forClass(Venue.class);
	mvc.perform(MockMvcRequestBuilders.post("/venues/newVenue").with(user("Organiser").roles(Security.ADMIN_ROLE))
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.param("name", "Venue Name")
			.param("address", "Road Road,M13 9PR")
			.param("capacity", "100")
			.accept(MediaType.TEXT_HTML).with(csrf()))
			.andExpect(status().isFound()).andExpect(content().string(""))
			.andExpect(view().name("redirect:/venues")).andExpect(model().hasNoErrors())
			.andExpect(handler().methodName("newVenue")).andExpect(flash().attributeExists("ok_message"));
	verify(venueService).save(arg.capture());
	assertThat(arg.getValue().getName(), equalTo("Venue Name"));
	assertThat(arg.getValue().getName(), equalTo("Road Name"));
	assertThat(arg.getValue().getAddress(), equalTo("M13 9PR"));
	assertThat(Integer.toString(arg.getValue().getCapacity()), equalTo("100"));
}
@Test
public void AfterVenueNoData() throws Exception {
	ArgumentCaptor<Venue> arg = ArgumentCaptor.forClass(Venue.class);
	mvc.perform(MockMvcRequestBuilders.post("/venues/newVenue").with(user("Organiser").roles(Security.ADMIN_ROLE))
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.param("name", "")
			.param("address", "Road Road,M13 9PR")
			.param("capacity", "")
			.accept(MediaType.TEXT_HTML)).andExpect(status().isForbidden());



	verify(venueService, never()).save(venue);
	}
@Test
public void updateVenueNoAuthentication() throws Exception {
	mvc.perform(MockMvcRequestBuilders.post("/venues").contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.param("id", "1")
			.param("name", "testing")
			.param("address", "address")
			.param("capacity", "100")
			.accept(MediaType.TEXT_HTML).with(csrf())).andExpect(status().isFound())
	.andExpect(header().string("Location", endsWith("/sign-in")));
	verify(venueService, never()).save(venue);



}



@Test
@WithMockUser(roles= "ADMINISTRATOR")
public void updateVenueIncorrectly() throws Exception{



when(venueService.findById(0)).thenReturn(null);



mvc.perform(MockMvcRequestBuilders.patch("/venues/0").accept(MediaType.TEXT_HTML).with(csrf())
.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
.param("capacity","1000")
.param("coordinates","6X 8TY")
.param("name","venue")
.sessionAttr("venue",venue)
.param("description","testing"))
.andExpect(status().isMethodNotAllowed());



}



@Test
@WithMockUser(roles= "ADMINISTRATOR")
public void updateVenueWithNoName() throws Exception{



when(venueService.findById(0)).thenReturn(null);



mvc.perform(MockMvcRequestBuilders.patch("/venues/0").accept(MediaType.TEXT_HTML).with(csrf())
.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
.param("id","01")
.param("address","address")
.param("capacity","1000")
.sessionAttr("venue",venue)
.param("description","testing"))
.andExpect(status().isMethodNotAllowed());



}



}