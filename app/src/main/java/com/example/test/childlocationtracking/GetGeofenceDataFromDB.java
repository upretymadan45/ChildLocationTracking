package com.example.test.childlocationtracking;

/**
 * Created by 21502476 on 6/11/2015.
 */
public class GetGeofenceDataFromDB {
    public int Id;
    public String Address;
    public String Latitude;
    public String Longitude;
    public String Radius;

    public GetGeofenceDataFromDB(int id,String address, String latitude,String longitude, String radius)
    {
        this.Id=id;
        this.Address=address;
        this.Latitude=latitude;
        this.Longitude=longitude;
        this.Radius=radius;
    }

    public GetGeofenceDataFromDB()
    {

    }
    public String toString()
    {
        return Address;
    }
}
