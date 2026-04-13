package com.agro.feedmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agro.feedmanagement.entity.FeedRecord;

public interface FeedRecordRepository extends JpaRepository<FeedRecord, Long> {

    List<FeedRecord> findByAnimalId(String animalId);
}