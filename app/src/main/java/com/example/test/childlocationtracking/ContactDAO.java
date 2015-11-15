package com.example.test.childlocationtracking;

/**
 * Created by 21502476 on 15/11/2015.
 */
public class ContactDAO {
    public int Id;
    public String ContactType;
    public String ContactNumber;

    public ContactDAO()
    {

    }

    public ContactDAO(int id, String contactType,String contactNumber)
    {
        this.Id=id;
        this.ContactType=contactType;
        this.ContactNumber=contactNumber;
    }

    public String toString()
    {
        return this.ContactType+" : "+this.ContactNumber;
    }
}
