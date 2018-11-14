package com.thinkerwolf.hantis.example;

import com.thinkerwolf.hantis.orm.annotation.*;

/**
 * Auto genarated by Hantis 
 */
@Entity(name = "user")
public class User {
	/**  */
	@Id
	@Column(name = "id")
	private Integer id;
	
	/**  */
	@Column(name = "name")
	private String name;
	
	/**  */
	@Column(name = "seq")
	private Integer seq;
	
	
	public User() {}
	
	public void setId(Integer id) {
		this.id = id;
	}
		
	public Integer getId() {
		return this.id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
		
	public String getName() {
		return this.name;
	}
	
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
		
	public Integer getSeq() {
		return this.seq;
	}
	
	
	// Self code start
	
	// Self code end
}