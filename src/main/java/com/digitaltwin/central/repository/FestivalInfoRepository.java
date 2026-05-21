package com.digitaltwin.central.repository;

import com.digitaltwin.central.model.FestivalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FestivalInfoRepository extends JpaRepository<FestivalInfo, Long> {
}
