package com.hanghae_hackaton.baby_care.model.dto;

import com.hanghae_hackaton.baby_care.model.Review;
import lombok.Builder;

import java.util.List;

@Builder
public record MatchBabySitterResponseDTO(
        int id,
        String name,
        int old,
        String gender,
        String profile,
        String reason,
        int pay,
        double score,
        List<String> tags,
        List<Review> reviews
) {
}
