package com.xsage.xsagecreditmatchservice.credit.domain;

import com.xsage.xsagecreditmatchservice.shared.util.ValidTransactionStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
public class TransactionFulfillment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
//    TODO: map to merchant
    private Long merchantId;

    @CreationTimestamp
    private Date requestTimeStamp;

    private Date fulfillmentTime;

    @Enumerated(EnumType.STRING)
    private ValidTransactionStatus status;
}
