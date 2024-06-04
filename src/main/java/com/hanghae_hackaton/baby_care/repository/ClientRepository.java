package com.hanghae_hackaton.baby_care.repository;

import com.hanghae_hackaton.baby_care.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Integer> {
}
