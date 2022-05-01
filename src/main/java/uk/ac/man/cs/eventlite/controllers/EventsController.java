package uk.ac.man.cs.eventlite.controllers;

import java.util.List;

import javax.validation.Valid;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.annotation.JsonTypeInfo.None;

import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.exceptions.EventNotFoundException;
import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

@Controller
@RequestMapping(value = "/events", produces = { MediaType.TEXT_HTML_VALUE })
public class EventsController {

	@Autowired
	private EventService eventService;

	@Autowired
	private VenueService venueService;

	@ExceptionHandler(EventNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String eventNotFoundHandler(EventNotFoundException ex, Model model) {
		model.addAttribute("not_found_id", ex.getId());

		return "events/not_found";
	}

	@GetMapping("/{id}")
	public String getEvent(@PathVariable("id") long id, Model model) {
		throw new EventNotFoundException(id);
	}

	@GetMapping
	public String getAllEvents(Model model) {

		Iterable<Event> events = eventService.findAll();
		List<Venue> venues = new ArrayList<Venue>();
		events.forEach((element) -> {
			if(!venues.contains(element.getVenue())) {
				venues.add(element.getVenue());
			}
		});
		model.addAttribute("events",events);
		model.addAttribute("venues",venues);


		return "events/index";
	}
	
	@GetMapping("/add-event")
	public String addEvent(Model model) {
		model.addAttribute("venues", venueService.findAll());
		model.addAttribute("newEvent", new Event());
		return "events/add-event";
	}

	
	@GetMapping("/search")
	public String Search(Model model, @RequestParam(value="name") String name) {
		Iterable<Event> search = eventService.findAll(name);
		model.addAttribute("events",search);
		return "events/index";
		
	}

	@RequestMapping(method = RequestMethod.POST)
	public String createNewEvent(@Valid Event e, BindingResult bindingResult, Model model) throws InterruptedException{
		StringBuilder errString = new StringBuilder();



		if(bindingResult.hasErrors()){
		for(FieldError error : bindingResult.getFieldErrors())
		errString.append("Invalid ").append(error.getField())
		.append(" input: ").append(error.getRejectedValue()).append("\n");



		model.addAttribute("errorMsg", errString);
		model.addAttribute("event", e);
		return "events/addEvent";
		}
		
		eventService.save(e);
		return "redirect:/events";
		}
	
	@PutMapping("/{id}")
	public Event replaceEvent(@RequestBody Event newEvent, @PathVariable Long id) {
		return eventService.findById(id)
				.map(event -> {
					event.setName(newEvent.getName());
					return eventService.save(event);
				})
				.orElseGet(() -> {
					newEvent.setId(id);
					return eventService.save(newEvent);
				});
	}
	
	
	@DeleteMapping("/{id}")
	public String deleteEvent(@PathVariable("id") long id, RedirectAttributes redirectAttrs) {
		if (!eventService.existsById(id)) {
			throw new EventNotFoundException(id);
		}

		eventService.deleteById(id);
		redirectAttrs.addFlashAttribute("ok_message", "Event deleted.");

		return "redirect:/events";
	}

	@DeleteMapping
	public String deleteAllEvents(RedirectAttributes redirectAttrs) {
		eventService.deleteAll();
		redirectAttrs.addFlashAttribute("ok_message", "All events deleted.");

		return "redirect:/events";
	}

}
