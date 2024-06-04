package com.hanghae_hackaton.baby_care.repository;

import com.hanghae_hackaton.baby_care.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findAllByBabySitterId(Integer babySitterId);
}
