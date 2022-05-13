package uk.ac.man.cs.eventlite.dao;


import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import uk.ac.man.cs.eventlite.entities.Event;

@Service
public class EventServiceImpl implements EventService {

	private final static Logger log = LoggerFactory.getLogger(EventServiceImpl.class);

	private final static String DATA = "data/events.json";
	
	@Autowired
	private EventRepository eventRepository;

	@Override
	public long count() {
		return eventRepository.count();
	}

	@Override
	public Iterable<Event> findAll() {
		return eventRepository.findAllByOrderByDateAscTimeAsc();
	}
	

	@Override
	public Event save(Event event) {
		return eventRepository.save(event);
	}
	
	@Override
	public Iterable<Event> findAll(String name){
		return eventRepository.findAllByNameContainingIgnoreCase(name);
	}  
	

	public Optional<Event> findById(long id){
		return eventRepository.findById(id);
	}
	
	
	@Override
	public void delete(Event event) {
		eventRepository.delete(event);
	}
	
	@Override
	public void deleteById(long id) {
		eventRepository.deleteById(id);
	}

	@Override
	public void deleteAll() {
		eventRepository.deleteAll();
	}

	@Override
	public void deleteAll(Iterable<Event> events) {
		eventRepository.deleteAll(events);
	}

	@Override
	public void deleteAllById(Iterable<Long> ids) {
		eventRepository.deleteAllById(ids);
	}

	@Override
	public boolean existsById(long id) {
		return eventRepository.existsById(id);
	}

	@Override
	public List<Event> getNextThree() {
		List<Event> nextThreeEvents = new ArrayList<>();
		for(Event e : findAll()) {
			if (e.getDate().isBefore(LocalDate.now())) continue;
			nextThreeEvents.add(e);
			if (nextThreeEvents.size() == 3) break;
			
		}
		return nextThreeEvents;
	}
	
	@Override
	public List<Event> getRecentEvents(){
		List<Event> recentEvents = new ArrayList<>();
		for(Event e : findAll()) {
			if (e.getDate().isBefore(LocalDate.now())) continue;
			recentEvents.add(e);
			if (recentEvents.size() == 5) break;
			
		}
		return recentEvents;
		}
	}

