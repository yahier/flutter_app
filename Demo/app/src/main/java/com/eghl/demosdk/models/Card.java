
package com.eghl.demosdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Card {

    @SerializedName("LastFour")
    @Expose
    private String lastFour;
    @SerializedName("CardId")
    @Expose
    private String cardId;
    @SerializedName("BrandId")
    @Expose
    private String brandId;
    @SerializedName("CardAlias")
    @Expose
    private Object cardAlias;
    @SerializedName("ExpiryMonth")
    @Expose
    private Integer expiryMonth;
    @SerializedName("SelectedAsDefault")
    @Expose
    private Boolean selectedAsDefault;
    @SerializedName("BNBUnverified")
    @Expose
    private Object bNBUnverified;
    @SerializedName("CardHolderName")
    @Expose
    private String cardHolderName;
    @SerializedName("ExtensionPoint")
    @Expose
    private Object extensionPoint;
    @SerializedName("BillingAddress")
    @Expose
    private BillingAddress billingAddress;
    @SerializedName("BrandName")
    @Expose
    private String brandName;
    @SerializedName("ExpiryYear")
    @Expose
    private Integer expiryYear;

    public String getLastFour() {
        return lastFour;
    }

    public void setLastFour(String lastFour) {
        this.lastFour = lastFour;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public Object getCardAlias() {
        return cardAlias;
    }

    public void setCardAlias(Object cardAlias) {
        this.cardAlias = cardAlias;
    }

    public Integer getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(Integer expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public Boolean getSelectedAsDefault() {
        return selectedAsDefault;
    }

    public void setSelectedAsDefault(Boolean selectedAsDefault) {
        this.selectedAsDefault = selectedAsDefault;
    }

    public Object getBNBUnverified() {
        return bNBUnverified;
    }

    public void setBNBUnverified(Object bNBUnverified) {
        this.bNBUnverified = bNBUnverified;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public Object getExtensionPoint() {
        return extensionPoint;
    }

    public void setExtensionPoint(Object extensionPoint) {
        this.extensionPoint = extensionPoint;
    }

    public BillingAddress getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(BillingAddress billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Integer getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(Integer expiryYear) {
        this.expiryYear = expiryYear;
    }

}
