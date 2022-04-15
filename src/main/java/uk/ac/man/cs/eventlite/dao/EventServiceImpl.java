package uk.ac.man.cs.eventlite.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.ac.man.cs.eventlite.entities.Event;

@Service
public class EventServiceImpl implements EventService {

	@Autowired
	private EventRepository eventRepository;

	@Override
	public long count() {
		//long count = 0;
		//Iterator<Event> i = findAll().iterator();

//		for (; i.hasNext(); count++) {
	//		i.next();
		//}

		//return count;
		return eventRepository.count();
	}
		

	@Override
	public Iterable<Event> findAll() {
		//ArrayList<Event> events = new ArrayList<Event>();

	//	try {
			//ObjectMapper mapper = new ObjectMapper();
			//mapper.registerModule(new JavaTimeModule());

		//	InputStream in = new ClassPathResource(DATA).getInputStream();

	//		events = mapper.readValue(in, mapper.getTypeFactory().constructCollectionType(List.class, Event.class));
//		} catch (Exception e) {
			//log.error("Exception while reading file '" + DATA + "': " + e);
			// If we can't read the file, then the event list is empty...
		//}

		//return events;
		return eventRepository.findAllByOrderByDateAscTimeAsc();
	}

	@Override
	public Event save(Event event) {
		return eventRepository.save(event);
	}
	
	@Override
	public Iterable<Event> findAll(String name){
		return eventRepository.findAllByNameContaining(name);
	}  
	
}
