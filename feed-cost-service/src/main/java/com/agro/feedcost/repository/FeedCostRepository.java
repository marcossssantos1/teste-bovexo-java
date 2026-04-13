package com.agro.feedcost.repository;

import com.agro.feedcost.entity.FeedCost;
import com.agro.feedcost.entity.FeedType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FeedCostRepository extends JpaRepository<FeedCost, Long> {

    Optional<FeedCost> findByFeedType(FeedType feedType);
}
