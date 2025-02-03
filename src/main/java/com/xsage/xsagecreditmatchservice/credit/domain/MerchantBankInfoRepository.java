package com.xsage.xsagecreditmatchservice.credit.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantBankInfoRepository extends CrudRepository<MerchantBankInfo, Long> {
    MerchantBankInfo findMerchantBankInfoByBankAccountCurrencyDenomination(String bankAccountCurrency);
    Optional<MerchantBankInfo> findByMerchantId(Merchant merchant);
}
