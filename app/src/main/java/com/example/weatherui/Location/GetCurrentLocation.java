package com.example.weatherui.Location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class GetCurrentLocation {

    private FusedLocationProviderClient fusedLocationClient;

    public GetCurrentLocation(Context context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    @SuppressLint("MissingPermission")
    public void fetchLocation(OnSuccessListener<Location> listener) {
        fusedLocationClient.getLastLocation().addOnSuccessListener(listener);
    }
}
