package com.thinkerwolf.hantis.example;

import com.thinkerwolf.hantis.orm.annotation.*;

/**
 * Auto genarated by Hantis 
 */
@Entity(name = "double_key")
public class DoubleKey {
	/**  */
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Double id;
	
	/**  */
	@Column(name = "name")
	private String name;
	
	
	public DoubleKey() {}
	
	public void setId(Double id) {
		this.id = id;
	}
		
	public Double getId() {
		return this.id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
		
	public String getName() {
		return this.name;
	}
	
	
	// Self code start
	
	// Self code end
}