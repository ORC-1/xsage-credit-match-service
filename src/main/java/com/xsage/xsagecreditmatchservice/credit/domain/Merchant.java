package com.xsage.xsagecreditmatchservice.credit.domain;

import com.xsage.xsagecreditmatchservice.shared.util.PenaltyWeight;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Setter
@Getter
@ToString
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    private String userId;

    @NotNull
    private String businessName;

    @NotNull
    private String location;

    private Boolean verified;

    private Date verifiedTimeStamp;

    @Nullable
    private Double latitude;

    @Nullable
    private Double longitude;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PenaltyWeight penalty;

    @Nullable
//    TODO: write EOD method that calculates this based on:
//     1. get every customer this merchant interacted with
//     2. get the how many times the customer used the system after using the merchant from the date they interacted with the merchant
//     3. divide by 100
//     4. Decide minimum score of which a merchant hits, he's penalized
    private Double customerRepeatRate;
}
