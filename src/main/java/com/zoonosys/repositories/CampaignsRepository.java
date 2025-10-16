package com.zoonosys.repositories;

import com.zoonosys.models.Campaigns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignsRepository extends JpaRepository<Campaigns,Long> {
    List<Campaigns> findByUserId(long userId);

    List<Campaigns> findByNameContainingIgnoreCase(String name);
}
