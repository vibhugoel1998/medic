package com.example.john.medic;

/**
 * Created by john on 3/17/2018.
 */

public class pharmfire {
    public String pharm_medname;
    public String pharm_medprice;
    public String pharm_medavailability;


    public pharmfire(String pharm_medname, String pharm_medprice, String pharm_medavailability) {

        this.pharm_medname = pharm_medname;
        this.pharm_medprice = pharm_medprice;
        this.pharm_medavailability = pharm_medavailability;
    }

    public String getPharm_medname() {

        return pharm_medname;
    }

    public void setPharm_medname(String pharm_medname) {
        this.pharm_medname = pharm_medname;
    }

    public String getPharm_medprice() {
        return pharm_medprice;
    }

    public void setPharm_medprice(String pharm_medprice) {
        this.pharm_medprice = pharm_medprice;
    }

    public String getPharm_medavailability() {
        return pharm_medavailability;
    }

    public void setPharm_medavailability(String pharm_medavailability) {
        this.pharm_medavailability = pharm_medavailability;
    }
}
