package com.cop.ids.services;

import java.util.List;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.cop.ids.data.Section;
import com.mongodb.MongoClient;

public class MongoQueryService {
	
	MongoOperations mongoOps;

	public MongoQueryService() {
		mongoOps = new MongoTemplate(new SimpleMongoDbFactory(new MongoClient(), "ids"));
	}
	
	public List<Section> regexQuery(String searchText){
		Query query = new Query(Criteria.where("text").regex(searchText));
		List<Section> sections = mongoOps.find(query, Section.class);
		return sections;
	}
	
}
