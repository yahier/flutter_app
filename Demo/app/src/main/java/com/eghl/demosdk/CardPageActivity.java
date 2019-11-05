package com.eghl.demosdk;

import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.eghl.sdk.EGHL;
import com.eghl.sdk.params.PaymentParams;
import com.eghl.sdk.payment.CardPageCallBack;
import com.eghl.sdk.payment.CardPageFragmentActivity;

public class CardPageActivity extends AppCompatActivity implements View.OnClickListener, CardPageCallBack {

    private Button buttonMakePayment;
    private CheckBox checkBoxCustomerConsent;

    private EGHL eghl;
    private PaymentParams.Builder params;
    CardPageFragmentActivity cardPageFragmentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_page);

        initView();
        setupListener();
        setupFragment();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initCardView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //handle android back button pressed
    }

    private void initView() {
        eghl = EGHL.getInstance();

        buttonMakePayment = findViewById(R.id.buttonMakePayment);
        checkBoxCustomerConsent = findViewById(R.id.checkBoxCustomerConsent);

        cardPageFragmentActivity = new CardPageFragmentActivity();
    }

    private void setupListener() {
        buttonMakePayment.setOnClickListener(this);
    }

    private void setupFragment (){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmenteGHLCardPage, cardPageFragmentActivity);
        fragmentTransaction.commit();
    }

    private void initCardView() {
        String paymentId = eghl.generateId("SIT");

        params = new PaymentParams.Builder()
                .setServiceId("SIT")
                .setPassword("sit12345")
                .setCustPhone("0123456789")
                .setCustEmail("johndoe@test.com")
                .setCustIp("127.0.0.1")
                .setToken("")
                .setTokenType("")
                .setDebugPaymentURL(true)
                .setPaymentId(paymentId);
        Bundle paymentParams = params.build();

        cardPageFragmentActivity.loadCardFragment(paymentParams);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EGHL.REQUEST_PAYMENT && data != null) {
            setResult(resultCode, data);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonMakePayment:
                // fetchSOPToken will return result in onFetchSOPTokenSuccess by implements CardUICallBack
                cardPageFragmentActivity.fetchSOPToken();
                break;
            default:
                break;
        }
    }

    @Override
    public void onFetchCardInfoSuccess() {
        // Do anything
    }

    @Override
    public void onFetchCardInfoError(String s) {
        // Do anything
    }

    @Override
    public void onFetchSOPTokenSuccess(String token, String tokenType, String paymentId) {
        params = new PaymentParams.Builder()
                .setMerchantReturnUrl("SDK")
                .setMerchantCallbackUrl("SDK")
                .setPaymentDesc("eGHL Payment testing")
                .setCustPhone("0123456789")
                .setLanguageCode("EN")
                .setPageTimeout("600")
                .setDebugPaymentURL(true)
                .setServiceId("SIT")
                .setPassword("sit12345")
                .setAmount("1.00")
                .setCustName("Someone")
                .setCustEmail("johndoe@test.com")
                .setMerchantName("eGHL")
                .setCustConsent(checkBoxCustomerConsent.isChecked())
                .setCurrencyCode("MYR")
                .setTransactionType("SALE")
                .setPaymentMethod("CC")
                .setPaymentId(paymentId)
                .setOrderNumber(paymentId)
                .setToken(token)
                .setTokenType(tokenType)
                .setCustIp("127.0.0.1");

        Bundle paymentParams = params.build();
        eghl.executePayment(paymentParams, this);
    }

    @Override
    public void onFetchSOPTokenError(String s) {
        // Do anything
    }
}