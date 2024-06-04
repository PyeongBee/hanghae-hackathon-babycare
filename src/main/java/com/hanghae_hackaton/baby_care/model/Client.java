package com.hanghae_hackaton.baby_care.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(name = "TB_CLIENT_M")
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    String name;
    Integer old;
    String gender;
    String introduction;
//    List<String> needs;

    @Builder
    public Client (String name, Integer old, String gender, String introduction){
        this.name = name;
        this.old = old;
        this.gender = gender;
        this.introduction = introduction;
    }
}
