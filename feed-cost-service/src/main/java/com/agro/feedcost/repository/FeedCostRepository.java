package com.agro.feedcost.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agro.feedcost.entity.FeedCost;
import com.agro.feedcost.entity.FeedType;

public interface FeedCostRepository extends JpaRepository<FeedCost, Long> {

    Optional<FeedCost> findByFeedType(FeedType feedType);
}
