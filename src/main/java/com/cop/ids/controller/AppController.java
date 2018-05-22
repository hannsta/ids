package com.cop.ids.controller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cop.ids.data.Section;
import com.cop.ids.data.Title;
import com.cop.ids.repositories.SectionRepository;
import com.cop.ids.repositories.TitleRepository;
import com.cop.ids.services.MongoQueryService;
import com.cop.ids.services.ScrapeService;


@RestController 
public class AppController {
	
	@Autowired 
	private TitleRepository titleRepository;
	
	@Autowired 
	private SectionRepository sectionRepository;
	
	
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
	

}