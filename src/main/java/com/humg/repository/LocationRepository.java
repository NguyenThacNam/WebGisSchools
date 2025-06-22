package com.humg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.humg.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Integer> {

	List<Location> findByNameContaining(String keyword);

}

