package uk.ac.man.cs.eventlite.entities;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import javax.persistence.OneToMany;

@Entity
@Table(name = "venue")
public class Venue {
	
	@Id
	@GeneratedValue
	private long id;
	
	@Size(max = 256)
	private String name;
	
	@Size(max = 300)
	private String address;

	@OneToMany(targetEntity=Event.class,mappedBy="venue")
	private Set<Event> events;
	
	@Min(value=0, message = "Must be a postive value ")
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
	
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddress() {
		return address;
	}
	
	private Set<Event> getEvent(){
		return events;
	}
	
	public void setEvents(Set<Event> events) {
		this.events = events;
	}
}
