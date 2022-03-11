package uk.ac.man.cs.eventlite.entities;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.OneToMany;

@Entity
@Table(name = "venue")
public class Venue {
	
	@Id
	@GeneratedValue
	private long id;

	private String name;

	@OneToMany(targetEntity=Event.class,mappedBy="venue")
	private Set<Event> events;
	private int capacity;

	public Venue() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	private Set<Event> getEvent(){
		return events;
	}
	
	public void setEvents(Set<Event> events) {
		this.events = events;
	}
}
