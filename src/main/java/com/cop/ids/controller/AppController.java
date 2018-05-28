package com.cop.ids.controller;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cop.ids.data.FavoriteGroup;
import com.cop.ids.data.Greeting;
import com.cop.ids.data.Section;
import com.cop.ids.data.Title;
import com.cop.ids.data.User;
import com.cop.ids.repositories.FavoriteGroupRepository;
import com.cop.ids.repositories.SectionRepository;
import com.cop.ids.repositories.TitleRepository;
import com.cop.ids.repositories.UserRepository;
import com.cop.ids.services.MongoQueryService;
import com.cop.ids.services.ScrapeService;



@RestController 
public class AppController {
	
	@Autowired 
	private TitleRepository titleRepository;
	
	@Autowired 
	private SectionRepository sectionRepository;
	
	@Autowired 
	private UserRepository userRepository;
		
	@RequestMapping("/high")
	public String high() {
		System.out.println("high");
		return "high";
	}	

	@RequestMapping("/getByTitleId")
	public Iterable<Section> getSectionByChapter(@RequestParam(value="title", defaultValue="1") int title) {
		Iterable<Section> results = sectionRepository.findByTitleId(title);
		return results;
	}
	
	@RequestMapping("/search")
	public Iterable<Section> search(@RequestParam(value="search", defaultValue="1") String searchText) {
		MongoQueryService mongodb = new MongoQueryService();
		return mongodb.regexQuery(searchText);
	}

	@RequestMapping("/scrape")
	public String scrape() {
		ScrapeService scrapeService = new ScrapeService();
		scrapeService.scrape();
		return "success!!";
	}
	
	@RequestMapping("/getUserName")
	public String getUserName(){
		org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return user.getUsername();
	}

	
    @RequestMapping(value="/addUser", method=RequestMethod.GET) 
    public User userForm() {
        return new User();
    }

    @RequestMapping(value="/addUser", method=RequestMethod.POST) 
    public String userSubmit(@RequestBody User user) {
    	System.out.println(user.getUsername());
		userRepository.insert(user);
	    return "Success";
	}





}