package uk.ac.man.cs.eventlite.dao;

import java.util.List;
import java.util.Optional;

import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

public interface VenueService {

	public long count();

	public Iterable<Venue> findAll();
	
	public Venue save(Venue venue);
	
	public Optional<Venue> findById(long id);

	public Iterable<Venue> findall(String place);

	public void deleteById(long id);
	
	public Iterable<Venue> findAllInAlphabeticalOrder();

	public List<Venue> findByMostEvents();
}
