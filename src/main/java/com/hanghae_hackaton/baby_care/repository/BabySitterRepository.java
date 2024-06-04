package com.hanghae_hackaton.baby_care.repository;

import com.hanghae_hackaton.baby_care.model.BabySitter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BabySitterRepository extends JpaRepository<BabySitter, Integer> {
}
