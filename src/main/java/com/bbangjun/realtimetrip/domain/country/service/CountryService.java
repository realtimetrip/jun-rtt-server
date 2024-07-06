package com.bbangjun.realtimetrip.domain.country.service;

import com.bbangjun.realtimetrip.domain.country.entity.Country;
import com.bbangjun.realtimetrip.domain.country.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CountryService {
    private final CountryRepository countryRepository;

    public Boolean createChatRoom() {
        List<Country> countries = Arrays.asList(
                new Country("KR", "대한민국", "Asia"),
                new Country("JP", "일본", "Asia"),
                new Country("GB", "영국", "Europe"),
                new Country("ES", "스페인", "Europe"),
                new Country("US", "미국", "N.America"),
                new Country("CA", "캐나다", "N.America"),
                new Country("BR", "브라질", "S.America"),
                new Country("AR", "아르헨티나", "S.America"),
                new Country("AU", "호주", "Oceania"),
                new Country("NZ", "뉴질랜드", "Oceania"),
                new Country("ZA", "남아프리카공화국", "Africa"),
                new Country("EG", "이집트", "Africa")
        );

        for (Country country : countries) {
            if (!countryRepository.existsById(country.getCountryCode()))
                countryRepository.save(country);
            else
                return false;
        }

        return true;
    }
}
