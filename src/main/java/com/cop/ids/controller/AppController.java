package com.cop.ids.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.cop.ids.data.Section;
import com.cop.ids.data.User;
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
		//return mongodb.searchQuery(searchText);
		return mongodb.regexQuery(searchText);

	}

	@RequestMapping("/scrape")
	public @ResponseBody String scrape() {
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
    public @ResponseBody void userSubmit(@RequestBody User user) {
    	System.out.println(user.getUsername());
    	String finalUsername = user.getUsername().toLowerCase();
    	user.setUsername(finalUsername);
		userRepository.insert(user);
	    return;
	}





}