package uk.ac.man.cs.eventlite.controllers;

import java.util.List;
import java.util.TreeMap;

import javax.validation.Valid;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.TwitterService;
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
	
	
	private TwitterService twitterService;
	
	@ExceptionHandler(EventNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String eventNotFoundHandler(EventNotFoundException ex, Model model) {
		model.addAttribute("not_found_id", ex.getId());

		return "events/not_found";
	}

	@GetMapping("/{id}")
	public String getEvent(@PathVariable("id") long id, Model model) {
		Event event = eventService.findById(id).orElseThrow(() -> new EventNotFoundException(id));
		model.addAttribute("event", event);
		model.addAttribute("venue", event.getVenue());
		return "events/show";
	}
	

	@GetMapping
	public String getAllEvents(Model model) {

		List<Event> previousEvents = eventService.findAllPrevious();
		List<Event> upcomingEvents = eventService.findAllUpcoming();	
		
		model.addAttribute("events",upcomingEvents);
		model.addAttribute("previousEvents",previousEvents);

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
	
	@GetMapping("update/{id}")
	 public String showUpdateEvent(@PathVariable("id") long id,Model model) {
	  Event event = eventService.findById(id).orElseThrow(() -> new EventNotFoundException(id));
	  model.addAttribute("event",event);
	  model.addAttribute("venues",venueService.findAll());
	  
	  return "events/update";
	 }
	 
	 @PostMapping(value="/updateEvent", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	 public String updateEvent(@RequestBody @Valid @ModelAttribute Event event) {
	  eventService.save(event);
	  return "redirect:/events";
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
	
	@GetMapping(value = "/postTweet")
	  public String postTweet(@RequestHeader String referer, @RequestParam(name="tweet") String tweetContent, Model model) throws TwitterException {

	   TwitterService.createTweet(tweetContent);

	   return "redirect:" + referer;
	   
	  }
	
	public String getEvents(Model model) {
		model.addAttribute("recent_events",eventService.findAllUpcoming());
		model.addAttribute("previous_events",eventService.findAllPrevious());
		
		try {
			
			Twitter twitter = TwitterService.getTwitterinstance();
			List<Status> twitterFeedList = twitter.getUserTimeline();
			TreeMap<String, List<String>> tweetsMap = new TreeMap<String, List<String>>();
			
			int i = 0;
			for(Status t: twitterFeedList) {
				if(i<5) {
					List<String> contentList = new ArrayList<String>();
					contentList.add("https://twitter.com/" + t.getUser().getScreenName()+"/status/"+t.getId());
					contentList.add(t.getText());
					tweetsMap.put(t.getCreatedAt().toString(), contentList);
				}
				else break;
			}
			i += 1;
			model.addAttribute("tweets", tweetsMap);
			
		}catch (TwitterException e) {
			e.printStackTrace();
		}
		
		return "events/index";
	}
	
	

}
