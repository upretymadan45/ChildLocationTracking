package com.example.test.childlocationtracking;

import com.google.android.gms.location.Geofence;

/**
 * Created by 21502476 on 24/10/2015.
 */
public final class Constants {
    private Constants() {
    }

    public static final String TAG = "Child Location Tracking";

    // Request code to attempt to resolve Google Play services connection failures.
    public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    // Timeout for making a connection to GoogleApiClient (in milliseconds).
    public static final long CONNECTION_TIME_OUT_MS = 100;

    public static final long GEOFENCE_EXPIRATION_TIME = Geofence.NEVER_EXPIRE;

    // Keys for flattened geofences stored in SharedPreferences.
    public static final String KEY_LATITUDE = "com.example.test.childlocationtracking.KEY_LATITUDE";
    public static final String KEY_LONGITUDE = "com.example.test.childlocationtracking.KEY_LONGITUDE";
    public static final String KEY_RADIUS = "com.example.test.childlocationtracking.KEY_RADIUS";
    public static final String KEY_EXPIRATION_DURATION =
            "com.example.test.childlocationtracking.KEY_EXPIRATION_DURATION";
    public static final String KEY_TRANSITION_TYPE =
            "com.example.test.childlocationtracking.KEY_TRANSITION_TYPE";
    // The prefix for flattened geofence keys.
    public static final String KEY_PREFIX = "com.example.test.childlocationtracking.KEY";

    // Invalid values, used to test geofence storage when retrieving geofences.
    public static final long INVALID_LONG_VALUE = -999l;
    public static final float INVALID_FLOAT_VALUE = -999.0f;
    public static final int INVALID_INT_VALUE = -999;
}
