package com.cop.ids.data;


import java.util.ArrayList;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class FavoriteGroup {
	
	@Id
	private ObjectId id;
	
	private String username;
	
	private String name;
	
	private ArrayList<String> favorites;
	
	public FavoriteGroup(String username, String name) {
		this.username=username;
		this.name=name;
		this.favorites= new ArrayList<String>();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getFavorites() {
		return favorites;
	}

	public void setFavorites(ArrayList<String> favorites) {
		this.favorites = favorites;
	}
	public void addFavorite(String id2) {
		this.favorites.add(id2);
	}
	public void removeFavorite(String id) {
		this.favorites.remove(id);
	}
}
