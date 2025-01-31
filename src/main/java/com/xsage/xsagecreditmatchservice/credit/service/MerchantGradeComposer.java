package com.xsage.xsagecreditmatchservice.credit.service;

import com.xsage.xsagecreditmatchservice.credit.domain.*;
import com.xsage.xsagecreditmatchservice.shared.util.ValidCurrencyPair;
import com.xsage.xsagecreditmatchservice.shared.util.ValidTransactionStatus;
import lombok.AllArgsConstructor;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@AllArgsConstructor
public class MerchantGradeComposer {
    MerchantRepository merchantRepository;
    MerchantBankInfoRepository merchantBankInfoRepository;
    TransactionFulfillmentRepository transactionFulfillmentRepository;
    InternalCurrencyPairPriceRepository internalCurrencyPairPriceRepository;
    ProductRepository productRepository;

    private final int transactionId;
    private List<MerchantGradeSheetDTO> merchantGradeSheetDTOS;
    private static Calendar calendar = Calendar.getInstance();


    //TODO: write implementation alg for setLocation and setCompletion scores
    public MerchantGradeComposer(int transactionId) {
        this.transactionId = transactionId;
//        get a list of merchant within reasonable location and verified.
//        2. do a parallel stream loop
//        3.use an orchastretor to fill up the values 4.trim
        String cityName = "";
        double targetLatitude = 0.0;
        double targetLongitude = 0.0;
        TransactionData transactionData = null;
        verifiedMerchantWithinLocationAndSelect(cityName, targetLatitude, targetLongitude, transactionData);
    }

    private void verifiedMerchantWithinLocationAndSelect(String location, double targetLatitude,
                                                         double targetLongitude, TransactionData transactionData) {
        List<Merchant> merchantList;
        double emptyValue = 0.0;
        double distanceThreshold = 1.0;
        if (targetLatitude != emptyValue || targetLongitude != emptyValue) {
            merchantList = merchantRepository.findNearbyLocationsByLatAndLog(targetLatitude,
                    targetLongitude, distanceThreshold);
        } else {
            merchantList = merchantRepository.findMerchantByLocation(location);
        }

        selectMerchant(scoreSelectedMerchant(merchantList, transactionData));
    }

    private void selectMerchant(List<MerchantGradeSheetDTO> merchantList) {
        int maxNumOfTopMerchantToSelect = 10;
        this.merchantGradeSheetDTOS = merchantList.stream()
                .sorted(Comparator.comparing(m -> m.getTotalScore())).limit(maxNumOfTopMerchantToSelect)
                .collect(Collectors.toList());
    }

    private List<MerchantGradeSheetDTO> scoreSelectedMerchant(List<Merchant> scoreMerchant, TransactionData transactionData) {
        MerchantGradeSheetDTO merchantList = new MerchantGradeSheetDTO();
        merchantGradeSheetDTOS = scoreMerchant.parallelStream()
                .map(m -> scoreSelectedMerchantHelper(m, transactionData, merchantList)).collect(Collectors.toList());

        return merchantGradeSheetDTOS;
    }

    private MerchantGradeSheetDTO scoreSelectedMerchantHelper(Merchant merchant,
                                                              TransactionData transactionData,
                                                              MerchantGradeSheetDTO merchantGradeSheetDTO) {
        merchantGradeSheetDTO.setMerchantId(merchant.getUserId());
        merchantGradeSheetDTO.setLocation(getLocationScore(transactionData, merchant));
        merchantGradeSheetDTO.setCompletion(getCompletionScore(merchant));
        merchantGradeSheetDTO.setDurationOnPlatform(setDurationOnPlatform(merchant));
        merchantGradeSheetDTO.setMerchantCommissionRate(setMerchantCommissionRate(transactionData, merchant));
        merchantGradeSheetDTO.setCustomerRepeatRate(getCustomerRepeatRate(merchant));
        merchantGradeSheetDTO.setTimeToComplete(setTimeToComplete(merchant));
        merchantGradeSheetDTO.setPenalty(getPenaltyScore(merchant));
        merchantGradeSheetDTO.setQuota(setQuota(merchant));
        merchantGradeSheetDTO.setTransferEnabled(setTransferEnabled(transactionData, merchant));
        merchantGradeSheetDTO.setFulfillmentInstitutionSymmetry(setFulfillmentInstitutionSymmetry(transactionData, merchant));
        return merchantGradeSheetDTO;

    }

    private double getWeight(String scoreType) {
        throw new UnsupportedOperationException();
    }

    private double getLocationScore(TransactionData transactionData, Merchant merchantData) {
        double emptyValue = 0.0;
        int maxDistanceInKM = 10;
        if (transactionData.getTargetLatitude() != emptyValue || transactionData.getTargetLongitude() != emptyValue) {
            return calculateDistance(merchantData.getLatitude(), merchantData.getLongitude(),
                    transactionData.getTargetLatitude(),
                    transactionData.getTargetLongitude()) + getWeight("location");
        } else if (merchantData.getLocation().equals(transactionData.getLocation())) {
            int midArbitraryScore = 5;
            return maxDistanceInKM - midArbitraryScore + getWeight("location");
        }
        return maxDistanceInKM + getWeight("location");
    }

    private long getCompletionScore(Merchant merchantData) {
        long timestamp = merchantData.getVerifiedTimeStamp().getTime(); // Timestamp in milliseconds

        // Get the current timestamp
        long currentTimestamp = System.currentTimeMillis();

        // Calculate the difference in milliseconds
        long differenceInMillis = Math.abs(currentTimestamp - timestamp);

        // Calculate the number of days divided by 100
        return TimeUnit.MILLISECONDS.toDays(differenceInMillis) / 100;
    }

    private double setTimeToComplete(Merchant merchantData) {
        //        get the difference between requestTimeStamp and fulfillmentTime(b-a), add this dif for every record then divide by 10

        double totalTimeInMins = 0.0;
        double averageTimeInMins = 0.0;
        List<TransactionFulfillment> transactionFulfillments =
                transactionFulfillmentRepository.
                        findTop10TransactionFulfillmentByStatusAndMerchantIdEquals(ValidTransactionStatus.SUCCESS,
                                merchantData.getUserId());

        for (TransactionFulfillment record : transactionFulfillments) {
            long differenceInMillis = record.getFulfillmentTime().getTime() - record.getRequestTimeStamp().getTime();
            long differenceInMins = differenceInMillis / (1000 * 60);
            totalTimeInMins = totalTimeInMins + differenceInMins;
        }
        averageTimeInMins = totalTimeInMins / 10;
        return averageTimeInMins;
    }

    private double setDurationOnPlatform(Merchant merchantData) {
        int K = 10; //constant
        Date timestamp = merchantData.getVerifiedTimeStamp();

        // Get the current month
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // Add 1 since January is represented as 0

        // Calculate the difference between current month and timestamp
        Calendar timestampCalendar = calendar.getInstance();
        timestampCalendar.setTime(timestamp);
        int timestampMonth = timestampCalendar.get(Calendar.MONTH) + 1; // Add 1 since January is represented as 0
        int monthDifference = currentMonth - timestampMonth;
        return monthDifference / K;
    }

    private double setMerchantCommissionRate(TransactionData transactionData, Merchant merchantData) {
//        if we sell at 4$ and buy for 2$ from the merchant, the rate difference here is 2$,
//        get merchant product based on transaction pair
//        2. get currency pair from internal table
//        3. compare the dif
        ValidCurrencyPair currency = transactionData.getCurrency();
        InternalCurrencyPairPrice internalCurrencyPairPrice = internalCurrencyPairPriceRepository.getInternalCurrencyPairPriceByValidCurrencyPair(currency);
        Product merchantProduct = productRepository.getProductByTradingPairAndMerchantId(currency, merchantData.getId());
        double rate = internalCurrencyPairPrice.getSellRate() - merchantProduct.getSellRate();
        return rate;
    }

    private double getCustomerRepeatRate(Merchant merchantData) {
//        aim here is to calculate what % of people returned after using a particular merchant
        return merchantData.getCustomerRepeatRate();
    }

    private double getPenaltyScore(Merchant merchantData) {
        return merchantData.getPenalty().getEnumValue();
    }

    private int setQuota(Merchant merchantData) {
//        Todo: revisit and consider time in calculation also standardize magic num based on - or + effect
        int merchantMaxQuota = 10;
        List<TransactionFulfillment> transactionFulfillments =
                transactionFulfillmentRepository.
                        findTop10TransactionFulfillmentByStatusAndMerchantIdEquals(ValidTransactionStatus.PROCESSING,
                                merchantData.getUserId());
        if (transactionFulfillments.size() >= merchantMaxQuota) return 4;
        return 0;
    }


    private double setTransferEnabled(TransactionData transactionData, Merchant merchantData) {
        int transferEnabledMaxScore = 1;
        MerchantBankInfo merchantBankInfo = merchantBankInfoRepository.findById(merchantData.getId()).get();

        if (transactionData.getBankName() != "" && merchantBankInfo.getBankName() != "") return transferEnabledMaxScore;
        return 0;
    }

    private double setFulfillmentInstitutionSymmetry(TransactionData transactionData, Merchant merchantData) {
        int fulfillmentInstitutionSymmetryMaxScore = 4;
        MerchantBankInfo merchantBankInfo = merchantBankInfoRepository.findById(merchantData.getId()).get();
        if (merchantBankInfo.getBankName().equalsIgnoreCase(transactionData.getBankName()))
            return fulfillmentInstitutionSymmetryMaxScore;
        return 0.0;
    }

    public static double calculateDistance(double merchantLat, double merchantLon, double clientLat, double clientLon) {
        // Convert degrees to radians
        double lat1Rad = Math.toRadians(merchantLat);
        double lon1Rad = Math.toRadians(merchantLon);
        double lat2Rad = Math.toRadians(clientLat);
        double lon2Rad = Math.toRadians(clientLon);

        // Earth's radius in kilometers
        double radius = 6371.0;

        // Difference in coordinates
        double dlon = lon2Rad - lon1Rad;
        double dlat = lat2Rad - lat1Rad;

        // Haversine formula
        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Calculate the distance
        double distance = radius * c;

        return distance;
    }
}
