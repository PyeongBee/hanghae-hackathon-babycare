package com.hanghae_hackaton.baby_care.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "TB_REVIEW_M")
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    Integer clientId;
    Integer babySitterId;
    Integer score;
    String text;

    @Builder
    public Review (Integer clientId, Integer babySitterId, Integer score, String text){
        this.clientId = clientId;
        this.babySitterId = babySitterId;
        this.score = score;
        this.text = text;
    }
}
