package com.eghl.demosdk;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eghl.demosdk.models.Card;
import com.eghl.demosdk.models.ExpressResponse;
import com.eghl.sdk.EGHL;
import com.eghl.sdk.ELogger;
import com.eghl.sdk.interfaces.MasterpassCallback;
import com.eghl.sdk.params.LightboxParams;
import com.eghl.sdk.params.MasterpassParams;
import com.eghl.sdk.params.Params;
import com.eghl.sdk.params.PaymentParams;
import com.eghl.sdk.response.QueryResponse;
import com.google.gson.Gson;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String SERVICE_ID = "OM2";
    
    private PaymentParams.Builder params;
    private EGHL eghl;
    private EditText tokenTypeEdit;
    private EditText tokenEdit;
    private EditText amountEdit;
    private EditText merchantEdit;
    private EditText emailEdit;
    private EditText nameEdit;
    private EditText serviceEdit;
    private EditText currencyEdit;
    private EditText transactionTypeEdit;
    private EditText paymentMethodEdit;
    private ProgressDialog progress;
    private Button paymentButton;
    private Button masterpassButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eghl = EGHL.getInstance();
        ELogger.setLoggable(true);
        initView();
        initListeners();
    }

    private void initListeners() {
        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cnasit = eghl.generateId("CNASIT");
                params = new PaymentParams.Builder()
                        .setMerchantReturnUrl("https://test2pay.ghl.com/IPGSimulatorJeff/RespFrmGW.aspx")
                        .setPaymentDesc("eGHL Payment testing")
                        .setCustPhone("60123456789")
                        .setLanguageCode("EN")
                        .setPageTimeout("500")
                        .setServiceId(serviceEdit.getText().toString())
                        .setAmount(amountEdit.getText().toString())
                        .setCustName(nameEdit.getText().toString())
                        .setCustEmail(emailEdit.getText().toString())
                        .setMerchantName(merchantEdit.getText().toString())
                        .setCurrencyCode(currencyEdit.getText().toString())
                        .setToken(tokenEdit.getText().toString())
                        .setTokenType(tokenTypeEdit.getText().toString())
                        .setTransactionType(transactionTypeEdit.getText().toString())
                        .setPaymentMethod(paymentMethodEdit.getText().toString())
                        .setPaymentTimeout(60*8)
                        .setPaymentId(cnasit)
                        .setOrderNumber(cnasit);

                Bundle paymentParams = params.build();
                eghl.executePayment(paymentParams, MainActivity.this);

            }
        });

        masterpassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle firstRequest = buildMasterpassReq();
                if(firstRequest == null)
                    return;
                progress.show();
                eghl.executeMasterpassRequest(MainActivity.this,firstRequest, new MasterpassCallback() {
                    @Override
                    public void onResponse(final String response) {
                            // Handle pairing or express
                        progress.dismiss();
                        if(response.contains(Params.MASTERPASS_REQ_TOKEN)&&response.contains(Params.MASTERPASS_PAIRING_TOKEN)){
                            // Needs pairing
                            proceedPairing(response);
                        }else if (response.contains(Params.MASTERPASS_PRE_CHECKOUT_ID)){
                            // Can proceed to checkout via masterpass express
                            proceedExpress(response);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "http error", e);
                        progress.dismiss();
                    }
                });

            }
        });
    }

    private void proceedExpress(String response) {
        final ExpressResponse expressResponse = new Gson().fromJson(response,ExpressResponse.class);
        List<Card> cards = expressResponse.getCards();
        final CardsAdapter adapter = new CardsAdapter(MainActivity.this, cards);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Cards");
        builder.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
            Card card = (Card) adapter.getItem(item);
                String cnasit = eghl.generateId("CNASIT");
                params = new PaymentParams.Builder()
                        .setMerchantReturnUrl("https://test2pay.ghl.com/IPGSimulatorJeff/RespFrmGW.aspx")
                        .setPaymentDesc("payment without previous pairing")
                        .setCustPhone("60123456789")
                        .setLanguageCode("EN")
                        .setPageTimeout("500")
                        .setServiceId(SERVICE_ID)
                        .setAmount(amountEdit.getText().toString())
                        .setCustName(nameEdit.getText().toString())
                        .setCustEmail(emailEdit.getText().toString())
                        .setMerchantName(merchantEdit.getText().toString())
                        .setCurrencyCode(currencyEdit.getText().toString())
                        .setToken(tokenEdit.getText().toString())
                        .setTokenType(tokenTypeEdit.getText().toString())
                        .setTransactionType(transactionTypeEdit.getText().toString())
                        .setPaymentMethod(paymentMethodEdit.getText().toString())
                        .setCardID(card.getCardId())
                        .setPreCheckoutID(expressResponse.getPreCheckoutId())
                        .setPaymentId(cnasit)
                        .setOrderNumber(cnasit);
                Bundle paymentParams = params.build();
                eghl.executePayment(paymentParams, MainActivity.this);
                dialog.dismiss();

            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void proceedPairing(String response) {
        Uri uri = Uri.parse("?"+response);
        String pairingToken = uri.getQueryParameter(Params.MASTERPASS_PAIRING_TOKEN);
        String reqToken = uri.getQueryParameter(Params.MASTERPASS_REQ_TOKEN);

        LightboxParams.Builder params = new LightboxParams.Builder()
                .setReqToken(reqToken)
                .setPairingToken(pairingToken)
                .setLightBoxCallbackURL("https://radiant-reaches-88215.herokuapp.com/commands/callback")
                .setMPEMerchantCheckoutID("a32d8440202b408dbdcc3ca8763d4625")
                .setLightboxJS("https://sandbox.static.masterpass.com/dyn/js/switch/integration/MasterPass.client.js");


        // Result of this pairing will be in the onActivityResult. With request code of EGHL.REQUEST_PAIRING.
        eghl.executePairing(params.build(),MainActivity.this);
    }

    private Bundle buildMasterpassReq(){

        final String token = tokenEdit.getText().toString();
        final String tokenType = tokenTypeEdit.getText().toString();
        final String serviceId = serviceEdit.getText().toString();
        final String amount = amountEdit.getText().toString();
        final String currencyCode = currencyEdit.getText().toString();
        final String paymentDesc = "eGHL Payment testing";

        if (TextUtils.isEmpty(token)) {
            Toast.makeText(MainActivity.this, "Token Should not be empty. Enter an email", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (TextUtils.isEmpty(tokenType)) {
            Toast.makeText(MainActivity.this, "TokenType Should not be empty. Enter 'MPE'", Toast.LENGTH_SHORT).show();
            return null;
        } else if (!tokenType.equals("MPE")) {
            Toast.makeText(MainActivity.this, "Token Type should be MPE", Toast.LENGTH_SHORT).show();
            return null;
        }

        MasterpassParams.Builder firstRequest = new MasterpassParams.Builder();
        firstRequest.setCurrencyCode(currencyCode);
        firstRequest.setAmount(amount);
        firstRequest.setToken(token);
        firstRequest.setPaymentDesc(paymentDesc);
        firstRequest.setServiceID(serviceId);
        return firstRequest.build();


    }
    private void initView() {
        amountEdit = (EditText) findViewById(R.id.amountEdit);
        merchantEdit = (EditText) findViewById(R.id.merchantEdit);
        emailEdit = (EditText) findViewById(R.id.emailEdit);
        nameEdit = (EditText) findViewById(R.id.nameEdit);
        serviceEdit = (EditText) findViewById(R.id.serviceEdit);

        currencyEdit = (EditText) findViewById(R.id.currencyEdit);
        transactionTypeEdit = (EditText) findViewById(R.id.transactionTypeEdit);
        paymentMethodEdit = (EditText) findViewById(R.id.paymentMethodEdit);
        tokenEdit = (EditText) findViewById(R.id.tokenEdit);
        tokenTypeEdit = (EditText) findViewById(R.id.tokenTypeEdit);

        paymentButton = (Button) findViewById(R.id.checkout);
        masterpassButton = (Button) findViewById(R.id.masterpass);

        progress = new ProgressDialog(MainActivity.this);
        progress.setMessage("Please wait...");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EGHL.REQUEST_PAYMENT) {
            if(data.getStringExtra(EGHL.RAW_RESPONSE)!=null&&!TextUtils.isEmpty(data.getStringExtra(EGHL.RAW_RESPONSE))){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                if(data.getStringExtra(EGHL.TXN_MESSAGE)!=null&&!TextUtils.isEmpty(data.getStringExtra(EGHL.TXN_MESSAGE))) {
                    builder.setTitle(data.getStringExtra(EGHL.TXN_MESSAGE));
                }else{
                    builder.setTitle(data.getStringExtra(QueryResponse.QUERY_DESC));

                }
                String message = "TxnStatus = " + data.getIntExtra(EGHL.TXN_STATUS,EGHL.TRANSACTION_NO_STATUS) + "\n"+"TxnMessage = "+data.getStringExtra(EGHL.TXN_MESSAGE)+"\nRaw Response:\n"+data.getStringExtra(EGHL.RAW_RESPONSE);
                builder.setMessage(message);
                builder.setPositiveButton("OK", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
            switch (resultCode) {
                case EGHL.TRANSACTION_SUCCESS:
                    Log.d(TAG, "onActivityResult: payment successful");
                    break;
                case EGHL.TRANSACTION_FAILED:
                    Log.d(TAG, "onActivityResult: payment failure");
                    break;
                case EGHL.TRANSACTION_CANCELLED:
                        Toast.makeText(this, "payment cancelled", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Log.d(TAG, "onActivityResult: " + resultCode);
                    break;
            }
        }else if(requestCode == EGHL.REQUEST_PAIRING){
            if(resultCode == EGHL.TRANSACTION_MASTERPASS_FINISHED){

                String reqVerifier = data.getStringExtra("oauth_verifier")!= null ? data.getStringExtra("oauth_verifier"):"";
                String pairingVerifier = data.getStringExtra("pairing_verifier")!= null ? data.getStringExtra("pairing_verifier"):"";
                String checkoutURL = data.getStringExtra("checkout_resource_url")!=null ? data.getStringExtra("checkout_resource_url"):"";
                String pairingToken = data.getStringExtra("pairing_token")!=null ? data.getStringExtra("pairing_token"):"";
                String reqToken = data.getStringExtra("oauth_token")!=null ? data.getStringExtra("oauth_token"):"";
                String status = data.getStringExtra("mpstatus")!=null ? data.getStringExtra("mpstatus"):"";

                switch (status){
                    case "success":
                        String cnasit = eghl.generateId("CNASIT");
                        params = new PaymentParams.Builder()
                                .setMerchantReturnUrl("https://test2pay.ghl.com/IPGSimulatorJeff/RespFrmGW.aspx")
                                .setPaymentDesc("payment without previous pairing")
                                .setCustPhone("60123456789")
                                .setLanguageCode("EN")
                                .setPageTimeout("500")
                                .setServiceId(SERVICE_ID)
                                .setAmount(amountEdit.getText().toString())
                                .setCustName(nameEdit.getText().toString())
                                .setCustEmail(emailEdit.getText().toString())
                                .setMerchantName(merchantEdit.getText().toString())
                                .setCurrencyCode(currencyEdit.getText().toString())
                                .setToken(tokenEdit.getText().toString())
                                .setTokenType(tokenTypeEdit.getText().toString())
                                .setTransactionType(transactionTypeEdit.getText().toString())
                                .setPaymentMethod(paymentMethodEdit.getText().toString())
                                .setPairingToken(pairingToken)
                                .setReqToken(reqToken)
                                .setCheckoutResourceURL(checkoutURL)
                                .setPairingVerifier(pairingVerifier)
                                .setReqVerifier(reqVerifier)
                                .setPaymentId(cnasit)
                                .setOrderNumber(cnasit);
                        Bundle paymentParams = params.build();
                        eghl.executePayment(paymentParams, MainActivity.this);

                        break;
                    case "cancel":
                    // handle cancel
                        Toast.makeText(this, "Masterpass cancelled", Toast.LENGTH_SHORT).show();
                        break;

                    case "failure":
                    //handle failure
                        Toast.makeText(this, "Masterpass failed", Toast.LENGTH_SHORT).show();

                        break;
                }
                
            }else if (resultCode == EGHL.TRANSACTION_CANCELLED){
                // user pressed back
                Toast.makeText(this, "Masterpass cancelled", Toast.LENGTH_SHORT).show();
            }

            
        }

    }
}
