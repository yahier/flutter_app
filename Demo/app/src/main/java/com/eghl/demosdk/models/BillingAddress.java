
package com.eghl.demosdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BillingAddress {

    @SerializedName("City")
    @Expose
    private String city;
    @SerializedName("Country")
    @Expose
    private String country;
    @SerializedName("CountrySubdivision")
    @Expose
    private String countrySubdivision;
    @SerializedName("Line1")
    @Expose
    private String line1;
    @SerializedName("Line2")
    @Expose
    private Object line2;
    @SerializedName("Line3")
    @Expose
    private Object line3;
    @SerializedName("PostalCode")
    @Expose
    private String postalCode;
    @SerializedName("ExtensionPoint")
    @Expose
    private Object extensionPoint;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountrySubdivision() {
        return countrySubdivision;
    }

    public void setCountrySubdivision(String countrySubdivision) {
        this.countrySubdivision = countrySubdivision;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public Object getLine2() {
        return line2;
    }

    public void setLine2(Object line2) {
        this.line2 = line2;
    }

    public Object getLine3() {
        return line3;
    }

    public void setLine3(Object line3) {
        this.line3 = line3;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Object getExtensionPoint() {
        return extensionPoint;
    }

    public void setExtensionPoint(Object extensionPoint) {
        this.extensionPoint = extensionPoint;
    }

}
