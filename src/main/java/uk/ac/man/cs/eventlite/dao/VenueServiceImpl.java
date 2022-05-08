package uk.ac.man.cs.eventlite.dao;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;

import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

@Service
public class VenueServiceImpl implements VenueService {

	private final static Logger log = LoggerFactory.getLogger(VenueServiceImpl.class);

	private final static String DATA = "data/venues.json";

	@Autowired
	private VenueRepository venueRepository;
	
	
	@Override
	public long count() {
		return venueRepository.count();
	}

	@Override
	public Iterable<Venue> findAll() {
		return venueRepository.findAll();
	}
	
	@Override 
	public Venue save(Venue venue) {
		return venueRepository.save(venue);
	}
	
	@Override
	public Iterable<Venue> findall(String place){
		return venueRepository.findAllByNameContainingIgnoreCase(place);
	}

	@Override
	public Optional<Venue> findById(long id) {
		return venueRepository.findById(id);
	}
	
	@Override
	public void deleteById(long id) {
		 venueRepository.deleteById(id);
	 }
	
	@Override
	public Iterable<Venue> findAllInAlphabeticalOrder() {
		ArrayList<Venue> orderedVenues = (ArrayList<Venue>) StreamSupport
				.stream(venueRepository.findAll().spliterator(), false)
				.collect(Collectors.toList());
		Collections.sort(orderedVenues, new Comparator<Venue>() {
			  public int compare(Venue o1, Venue o2) {
			      return o1.getName().compareToIgnoreCase(o2.getName());
			  }
			});
		return orderedVenues;
	}

}
