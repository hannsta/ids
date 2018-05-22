package com.cop.ids.data;


import javax.annotation.Generated;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.annotation.Id;

@Cacheable
public class Chapter {
    @Id
    private int id;
    private int titleId;
	private String name;

	public Chapter() {
		
	}
	public Chapter(int id, String name, int titleId){
		this.titleId=titleId;
		this.id=id;
		this.name=name;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getTitleId() {
		return titleId;
	}
	public void setTitleId(int titleId) {
		this.titleId = titleId;
	}

	
}
