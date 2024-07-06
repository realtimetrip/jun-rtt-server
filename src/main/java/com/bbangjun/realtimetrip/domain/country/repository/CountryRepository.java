package com.bbangjun.realtimetrip.domain.country.repository;

import com.bbangjun.realtimetrip.domain.country.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository  extends JpaRepository<Country, String> {
}
