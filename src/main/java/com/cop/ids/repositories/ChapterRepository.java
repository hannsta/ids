package com.cop.ids.repositories;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.cop.ids.data.Chapter;
import org.springframework.stereotype.Repository;

@Document(collection = "chapter")
public interface ChapterRepository extends MongoRepository<Chapter, Long>{
	public List<Chapter> findByTitleId(int titleId);
}

