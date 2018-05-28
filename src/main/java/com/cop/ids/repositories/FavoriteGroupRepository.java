package com.cop.ids.repositories;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.cop.ids.data.FavoriteGroup;


@Document(collection = "favorites")

public interface FavoriteGroupRepository extends MongoRepository<FavoriteGroup, Long>{
	public List<FavoriteGroup> findByUsername(String username);
	public FavoriteGroup findByUsernameAndName(String username, String name);


}
