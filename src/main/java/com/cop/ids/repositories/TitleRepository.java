package com.cop.ids.repositories;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import com.cop.ids.data.Title;
import org.springframework.stereotype.Repository;

@Document(collection = "title")
public interface TitleRepository extends MongoRepository<Title, Long>{

}
