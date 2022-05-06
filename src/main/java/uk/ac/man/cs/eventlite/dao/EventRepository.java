package uk.ac.man.cs.eventlite.dao;

import uk.ac.man.cs.eventlite.entities.Event;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.yaml.snakeyaml.events.Event.ID;



public interface EventRepository extends CrudRepository<Event, Long> {
	public Iterable<Event> findAllByOrderByDateAscTimeAsc();
	public Iterable<Event> findAllByNameContaining(String name);
	public Iterable<Event> findAllByNameContainingIgnoreCase(String name);
	public Optional<Event> findById(ID id);
}