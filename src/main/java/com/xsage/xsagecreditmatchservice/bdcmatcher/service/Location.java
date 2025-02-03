package com.xsage.xsagecreditmatchservice.bdcmatcher.service;

public class Location {
    private final double latitude;
    private final double longitude;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public boolean isWithinRange(Location userLocation, double km) {
        final int earthRadius = 6371; // Earth radius in km
        double latDistance = Math.toRadians(userLocation.latitude - this.latitude);
        double lonDistance = Math.toRadians(userLocation.longitude - this.longitude);
        double haversineComponent = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                   Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(userLocation.latitude)) *
                   Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double centralAngle = 2 * Math.atan2(Math.sqrt(haversineComponent), Math.sqrt(1 - haversineComponent));
        double distance = earthRadius * centralAngle; // convert to km
        return distance <= km;
    }
}
