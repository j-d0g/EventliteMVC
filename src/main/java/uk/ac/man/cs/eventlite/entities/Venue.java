package uk.ac.man.cs.eventlite.entities;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.persistence.OneToMany;

@Entity
@Table(name = "venue")
public class Venue {
	
	@Id
	@GeneratedValue
	private long id;
	
	@NotBlank(message = "the venue name can't be blank")
	@Size(max=255,message = "Venue name shouldn't be longer than 256 characters")

	private String name;

	
	//@NotBlank(message = "The road name can't be blank")
	//@Size(max = 299, message="Road name shouldn't be longer than 300 characters")
	
	//@NotBlank(message = "The postcode can't be blank")
	//@Size(min=6, max=8, message = "Postcode should be valid")
	
	//private String postcode;
	//private String road;
	
	//@NotNull
	//@Min(0)

	@OneToMany(targetEntity=Event.class,mappedBy="venue")
	private Set<Event> events;
	private int capacity;
	
	private transient String address;
	
	//public void postLoad() {
	//	this.address = road + ", " + postcode;
//	}

	public Venue() {
	}
	
	public String getAddress() {
		return address;
	}
	
	//public void setRoad(String road) {
	//	this.road = road;
	//}
	
	//public String getRoad() {
	//	return road;
	//}
	
	//public void setPostcode(String postcode) {
	//	this.postcode = postcode;
	//}
	
	//public String getPostcode() {
	//	return postcode;
	//}

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
	
	public Set<Event> getEvents(){
		return events;
	}
	
	public void setEvents(Set<Event> events) {
		this.events = events;
	}
}
