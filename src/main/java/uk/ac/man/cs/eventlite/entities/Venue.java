package uk.ac.man.cs.eventlite.entities;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "venue")
public class Venue {
	
	@Id
	@GeneratedValue
	private long id;

	private String name;

	private int capacity;
	
	private Collection<Event> events = new HashSet<Event>();

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
	 
	@OneToMany(mappedBy = "venues")
	public Collection<Event> getEvents(){
		return events;
	}
	public void setEvents(Collection<Event> events) {
		this.events = events;
	}
	
}
