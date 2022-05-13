package uk.ac.man.cs.eventlite.dao;

import uk.ac.man.cs.eventlite.entities.Event;

import java.util.List;
import java.util.Optional;


public interface EventService {

	public long count();
	
	public Iterable<Event> findAll();
	
	public Event save(Event event);
	
	public Iterable<Event> findAll(String name);

	public Optional<Event> findById(long id);
	
	public void delete(Event event);
	
	public void deleteById(long id);

	public void deleteAll();

	public void deleteAll(Iterable<Event> events);

	public void deleteAllById(Iterable<Long> ids);

	public boolean existsById(long id);

	public List<Event> getNextThree();

	List<Event> findAllPrevious();

	public List<Event> findAllUpcoming();

}
