package com.cop.ids.repositories;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.cop.ids.data.User;

@Document(collection = "user")

public interface UserRepository extends MongoRepository<User, Long>{
	public User findByUsername(String username);

}
