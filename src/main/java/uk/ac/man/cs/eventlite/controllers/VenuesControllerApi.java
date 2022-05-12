package uk.ac.man.cs.eventlite.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uk.ac.man.cs.eventlite.assemblers.EventModelAssembler;
import uk.ac.man.cs.eventlite.assemblers.VenueModelAssembler;
import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;
import uk.ac.man.cs.eventlite.exceptions.EventNotFoundException;

@RestController
@RequestMapping(value = "/api/venues", produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE })
public class VenuesControllerApi {
	
	private static final String NOT_FOUND_MSG = "{ \"error\": \"%s\", \"id\": %d }";
	
	private Venue venue;
	
	@Autowired
	private VenueService venueService;

	@Autowired
	private EventModelAssembler eventAssembler;
	
	@Autowired
	private VenueModelAssembler venueAssembler;
	
	private EventService eventService;
	
	@GetMapping
	public CollectionModel<EntityModel<Venue>> getAllVenues() {
		return venueAssembler.toCollectionModel(venueService.findAll())
				.add(linkTo(methodOn(VenuesControllerApi.class).getAllVenues()).withSelfRel());
	}
	
	@GetMapping("/{id}")
	public EntityModel<Venue> getVenue(@PathVariable("id") long id) {
		
		Venue venue = venueService.findById(id).orElseThrow(() -> new EventNotFoundException(id));

		return venueAssembler.toModel(venue);
	}
	
	
	@GetMapping("/{id}/next3events")
	public CollectionModel<EntityModel<Event>> Next3(@PathVariable("id") long id) {
		
		Venue venue = venueService.findById(id).orElseThrow(() -> new EventNotFoundException(id));
		
		Set<Event> events = venue.getEvents();
		
		List<Event> event = new ArrayList<>();
		
		event.addAll(events);
		
		Collections.sort(event, (a,b)->b.getDate().compareTo(a.getDate()));
		
		ListIterator<Event> event1 = event.listIterator();
		
		List<Event> event2 = new ArrayList<>();
		
		if(event1.hasNext()) {
			event2.add(event1.next());
		}
		else {
			return eventAssembler.toCollectionModel(event2);
		}
		
		if(event1.hasNext()) {
			event2.add(event1.next());
		}
		else {
			return eventAssembler.toCollectionModel(event2);
		}
		if(event1.hasNext()) {
			event2.add(event1.next());
		}
		else {
			return eventAssembler.toCollectionModel(event2);
		}
		
		

		return eventAssembler.toCollectionModel(event2);
		
		
		
		
		
		
		
	}
	

}