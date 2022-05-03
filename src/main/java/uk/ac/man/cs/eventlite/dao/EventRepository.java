package uk.ac.man.cs.eventlite.dao;

import uk.ac.man.cs.eventlite.entities.Event;
import org.springframework.data.repository.CrudRepository;



public interface EventRepository extends CrudRepository<Event, Long> {
	public Iterable<Event> findAllByOrderByDateAscTimeAsc();
	public Iterable<Event> findAllByNameContaining(String name);
	public Iterable<Event> findAllByNameContainingIgnoreCase(String name);
}