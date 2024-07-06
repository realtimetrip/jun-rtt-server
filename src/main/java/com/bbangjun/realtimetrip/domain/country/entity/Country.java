package com.bbangjun.realtimetrip.domain.country.entity;

import com.bbangjun.realtimetrip.domain.chat.entity.ChatRoom;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Entity
@Table(name = "country")
public class Country {

    @Id
    private String countryCode;

    // roomname 역할
    private String countryName;

    private String continent;

    @OneToOne(mappedBy = "country", cascade = CascadeType.ALL, orphanRemoval = true)
    private ChatRoom chatRoom;

    public Country(String countryCode, String countryName, String continent) {
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.continent = continent;
    }
}
