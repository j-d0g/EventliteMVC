package uk.ac.man.cs.eventlite.controllers;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;


import javax.servlet.http.HttpServletRequest;



import uk.ac.man.cs.eventlite.entities.Venue;
import uk.ac.man.cs.eventlite.exceptions.EventNotFoundException;
import uk.ac.man.cs.eventlite.exceptions.VenueNotFoundException;
import uk.ac.man.cs.eventlite.entities.Event;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;


import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import javax.validation.Valid;



@Controller
@RequestMapping(value = "/venues", produces = { MediaType.TEXT_HTML_VALUE })
public class VenuesController {
	
	@Autowired
	private EventService eventService;

	@Autowired
	private VenueService venueService;
	
	@GetMapping("/{id}")
	public String getVenue(@PathVariable("id") long id, Model model) {
		Venue venue = venueService.findById(id).orElseThrow(() -> new VenueNotFoundException(id));
		model.addAttribute("venue", venue);
		
//		Sort events in order
//		Iterable<Event> events = eventService.findAll();
//		Set<Event> sortedEvents = new HashSet<>();
//		for (Event event : events) {
//			if (event.getVenue().equals(venue)){
//				sortedEvents.add(event);
//			}
//		}
		
		model.addAttribute("event", venue.getEvents());
		return "venues/show";
	}

	@GetMapping
	public String getAllVenues(Model model) {
		model.addAttribute("venues", venueService.findAll());
		return "venues/index";
	}
	
	@GetMapping("/add-venue")
	public String addVenue(Model model) {
	return "venues/add-venue";
	}
	
	@GetMapping("/search-venue")
	public String searchVenue(Model model,@RequestParam(value="place") String place) {
		Iterable<Venue> search = venueService.findall(place);
		model.addAttribute("venues",search);
		return "venues/search-venue";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String createNewVenue(@Valid Venue v, BindingResult bindingResult, Model model) throws InterruptedException{
	StringBuilder errString = new StringBuilder();



	if(bindingResult.hasErrors()){
	for(FieldError error : bindingResult.getFieldErrors())
	errString.append("Invalid ").append(error.getField())
	.append(" input: ").append(error.getRejectedValue()).append("\n");



	model.addAttribute("errorMsg", errString);
	model.addAttribute("venue", v);
	return "venues/addVenue";
	}
	venueService.save(v);
	return "redirect:/events";
	}
	
	@GetMapping(value = "/update/{id")
	public String editVenue(@PathVariable("id") long id, Model model) {
		Venue v = venueService.findById(id).orElseThrow(() -> new VenueNotFoundException(id));
		model.addAttribute("venue",v);
		
		return "venues/update";
	}
	
	@PostMapping(value = "/update/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String SaveEditedVenue(@RequestBody @Valid @ModelAttribute Venue venue,
			BindingResult errors, Model model, RedirectAttributes redirectAttrs) {
		if(errors.hasErrors()) {
			model.addAttribute("venue",venue);
			return "venues/update";
		}
		
		venueService.save(venue);
		redirectAttrs.addFlashAttribute("all_done","Venue updated.");
		
		return "redirect:/venues";
	}
	

}