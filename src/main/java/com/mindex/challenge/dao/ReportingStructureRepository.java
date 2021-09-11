package com.mindex.challenge.dao;

import com.mindex.challenge.data.ReportingStructure;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

@Repository
public interface ReportingStructureRepository extends MongoRepository<ReportingStructure, String> {

}
