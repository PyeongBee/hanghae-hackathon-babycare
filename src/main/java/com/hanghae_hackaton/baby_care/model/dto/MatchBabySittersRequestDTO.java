package com.hanghae_hackaton.baby_care.model.dto;

import java.util.List;

public record MatchBabySittersRequestDTO(
        List<String> tags,
        String content,
        String date
) {}
