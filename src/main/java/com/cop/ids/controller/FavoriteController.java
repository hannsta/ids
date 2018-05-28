package com.cop.ids.controller;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cop.ids.data.FavoriteGroup;
import com.cop.ids.data.Section;
import com.cop.ids.repositories.FavoriteGroupRepository;
import com.cop.ids.services.MongoQueryService;

@RestController 
public class FavoriteController {
	@Autowired FavoriteGroupRepository favoriteRepository;
	
	
	@RequestMapping("/getFavoriteGroups")
	public Iterable<FavoriteGroup> getFavoriteGroups() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return favoriteRepository.findByUsername(user.getUsername());	
	}
	
	@RequestMapping("/getFavoriteGroup")
	public FavoriteGroup getFavoriteGroup(@RequestParam(value="favorite", defaultValue="name") String favoriteName) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return favoriteRepository.findByUsernameAndName(user.getUsername(), favoriteName);	
	}

	
	@RequestMapping("/addFavoriteGroup")
	public Iterable<FavoriteGroup> addFavoriteGroup(@RequestParam(value="favorite", defaultValue="name") String favoriteName) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		FavoriteGroup myFav = new FavoriteGroup(user.getUsername(), favoriteName);
		favoriteRepository.insert(myFav);
		return favoriteRepository.findByUsername(user.getUsername());	
	}
	
	
	
	@RequestMapping("/addToFavoriteGroup")
	public FavoriteGroup addToFavoriteGroup(@RequestParam(value="favorite", defaultValue="name") String favoriteName, @RequestParam(value="objectId", defaultValue="") String id) {
		System.out.println("id should be" + id);
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		FavoriteGroup myFav = favoriteRepository.findByUsernameAndName(user.getUsername(), favoriteName);
		myFav.addFavorite(id);
		favoriteRepository.save(myFav);
		return myFav;
	}
	@RequestMapping("/removeFromFavoriteGroup")
	public String removeFromFavoriteGroup(@RequestParam(value="favorite", defaultValue="name") String favoriteName, @RequestParam(value="objectId", defaultValue="name") String id) {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		FavoriteGroup myFav = new FavoriteGroup(user.getUsername(), favoriteName);
		myFav.removeFavorite(id);
		favoriteRepository.save(myFav);
		return "success";
	}
	@RequestMapping("/getFavorites")
	public Iterable<Section> getFavorites(@RequestParam(value="favorite", defaultValue="name") String favoriteName){
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		MongoQueryService mqs = new MongoQueryService();

		FavoriteGroup myFav = favoriteRepository.findByUsernameAndName(user.getUsername(), favoriteName);
		List<Section> results = mqs.favoriteQuery(myFav.getFavorites());
		return results;
		
	}
}
