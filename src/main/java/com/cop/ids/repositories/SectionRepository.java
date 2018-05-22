package com.cop.ids.repositories;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.cop.ids.data.Section;

@Document(collection = "section")
public interface SectionRepository extends MongoRepository<Section, String> {

	public List<Section> findByTitleId(int titleId);
	public List<Section> findByChapterId(int chapterId);
	
	
}