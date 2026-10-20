package com.osvaldozunniga.apiSpringBoot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.osvaldozunniga.apiSpringBoot.model.entity.FarmEvent;

public interface FarmEventRepository extends MongoRepository<FarmEvent, String> {
}
