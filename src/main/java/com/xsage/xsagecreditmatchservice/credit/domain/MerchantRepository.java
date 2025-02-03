package com.xsage.xsagecreditmatchservice.credit.domain;

import com.xsage.xsagecreditmatchservice.shared.util.ValidCurrencyPair;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NavigableSet;

@Repository
public interface MerchantRepository extends CrudRepository<Merchant, Long> {
    List<Merchant> findMerchantByLocation(String location);

    //Query implements the Haversine formula
    @Query(value = "SELECT * FROM Merchant " +
            "WHERE (6371 * 2 * ASIN(SQRT(POWER(SIN((?1 - ABS(latitude)) * PI() / 180 / 2), 2) + " +
            "COS(?1 * PI() / 180) * COS(ABS(latitude) * PI() / 180) * POWER(SIN((?2 - longitude) * PI() / 180 / 2), 2)))) <= ?3",
            nativeQuery = true)
    List<Merchant> findNearbyLocationsByLatAndLog(double targetLatitude, double targetLongitude, double distanceThreshold);

    NavigableSet<Merchant> findBy(ValidCurrencyPair currencyPair);
}
