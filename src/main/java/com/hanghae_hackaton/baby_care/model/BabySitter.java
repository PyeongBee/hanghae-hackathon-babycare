package com.hanghae_hackaton.baby_care.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "TB_BABY_SITTER_M")
@NoArgsConstructor
public class BabySitter{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String name;
    Integer old;
    String gender;
    String title;
    String content;
    Integer pay;

    @Builder
    public BabySitter (String name, Integer old, String gender, String content){
        this.name = name;
        this.old = old;
        this.gender = gender;
        this.title = title;
        this.content = content;
        this.pay = pay;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", old=" + old +
                ", gender='" + gender + '\'' +
                ", profile='" + content + '\'' +
                '}';
    }
}
