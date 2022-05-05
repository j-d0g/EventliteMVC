package uk.ac.man.cs.eventlite.dao;

import uk.ac.man.cs.eventlite.entities.Venue;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.yaml.snakeyaml.events.Event.ID;



public interface VenueRepository extends CrudRepository<Venue, Long> {

	Iterable<Venue> findAllByNameContainingIgnoreCase(String place); 
	
	public Optional<Venue> findById(ID id);

}