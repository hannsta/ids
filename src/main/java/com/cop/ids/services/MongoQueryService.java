package com.cop.ids.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Service;

import com.cop.ids.data.Section;
import com.mongodb.MongoClient;
@Service
public class MongoQueryService {
	
	MongoOperations mongoOps;

	public MongoQueryService() {
		mongoOps = new MongoTemplate(new SimpleMongoDbFactory(new MongoClient(), "ids"));
	}
	
	public List<Section> regexQuery(String searchText){
		Query query = new Query(Criteria.where("name").regex(searchText.toUpperCase()));
		List<Section> sections = mongoOps.find(query, Section.class);
		return sections;
	}
	public List<Section> searchQuery(String searchText){
		
		TextCriteria criteria = TextCriteria.forDefaultLanguage()
				  .matchingAny(searchText.split(" "));
		Query query = TextQuery.queryText(criteria)
				  .sortByScore()
				  .with(new PageRequest(0, 20));
		List<Section> sections = mongoOps.find(query, Section.class);
		return sections;
	}
	public List<Section> favoriteQuery(ArrayList<String> ids){
		System.out.println(ids);
		Query query = new Query();
		query.addCriteria(Criteria.where("id").in(ids));
		List<Section> sections = mongoOps.find(query, Section.class);
		return sections;
	}
}
