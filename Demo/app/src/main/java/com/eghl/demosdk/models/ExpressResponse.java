
package com.eghl.demosdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExpressResponse {

    @SerializedName("PreCheckoutId")
    @Expose
    private String preCheckoutId;
    @SerializedName("Cards")
    @Expose
    private List<Card> cards = null;

    public String getPreCheckoutId() {
        return preCheckoutId;
    }

    public void setPreCheckoutId(String preCheckoutId) {
        this.preCheckoutId = preCheckoutId;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

}
