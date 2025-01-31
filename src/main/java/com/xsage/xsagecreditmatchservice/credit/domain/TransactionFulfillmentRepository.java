package com.xsage.xsagecreditmatchservice.credit.domain;

import com.xsage.xsagecreditmatchservice.shared.util.ValidTransactionStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionFulfillmentRepository extends CrudRepository<TransactionFulfillment, Long> {
    List<TransactionFulfillment> findTop10TransactionFulfillmentByStatusAndMerchantIdEquals(ValidTransactionStatus validTransactionStatus, String merchantId);
}
