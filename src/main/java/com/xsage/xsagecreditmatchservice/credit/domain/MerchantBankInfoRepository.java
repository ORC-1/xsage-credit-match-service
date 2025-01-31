package com.xsage.xsagecreditmatchservice.credit.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantBankInfoRepository extends CrudRepository<MerchantBankInfo, Long> {
    MerchantBankInfo findMerchantBankInfoByBankAccountCurrencyDenomination(String bankAccountCurrency);
}
