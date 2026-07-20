package com.osvaldozunniga.apiSpringBoot.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.osvaldozunniga.apiSpringBoot.model.entity.Cattle;


public interface CattleRepository extends MongoRepository<Cattle, String>{
    @Query("{ 'statusId': 1 }")
    List<Cattle> findAllActive();
}
