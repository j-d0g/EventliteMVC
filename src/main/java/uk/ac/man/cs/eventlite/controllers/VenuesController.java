package uk.ac.man.cs.eventlite.controllers;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



import javax.servlet.http.HttpServletRequest;



import uk.ac.man.cs.eventlite.entities.Venue;
import uk.ac.man.cs.eventlite.entities.Event;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;



import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
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
	
	
	
	@GetMapping("/add-venue")
	public String addVenue(Model model) {
	return "venues/add-venue";
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
	

}