package com.hanghae_hackaton.baby_care.controller;

import com.hanghae_hackaton.baby_care.model.BabySitter;
import com.hanghae_hackaton.baby_care.model.dto.MatchBabySitterResponseDTO;
import com.hanghae_hackaton.baby_care.model.dto.MatchBabySittersRequestDTO;
import com.hanghae_hackaton.baby_care.service.BabyCareService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BabyCareController {
    private final BabyCareService babyCareService;

    @GetMapping("/babysitters/{id}")
    public ResponseEntity<MatchBabySitterResponseDTO> getAllBabySitters(
            @PathVariable Integer id) {
        return ResponseEntity.ok(babyCareService.getBabySitter(id));
    }

    @GetMapping("/babysitters")
    public ResponseEntity<List<MatchBabySitterResponseDTO>> getAllBabySitters() {
        return ResponseEntity.ok(babyCareService.getBabySitters());
    }

    @PostMapping("/babysitters/match")
    public ResponseEntity<List<MatchBabySitterResponseDTO>> matchBabySitters(@RequestBody MatchBabySittersRequestDTO requestDTO) {
        System.out.println("requestDTO = " + requestDTO);
        return ResponseEntity.ok(babyCareService.matchBabySitters(requestDTO));
    }
}
