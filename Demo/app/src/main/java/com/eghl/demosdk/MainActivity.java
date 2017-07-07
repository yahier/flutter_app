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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.eghl.demosdk.models.Card;
import com.eghl.demosdk.models.ExpressResponse;
import com.eghl.sdk.EGHL;
import com.eghl.sdk.ELogger;
import com.eghl.sdk.interfaces.CaptureCallback;
import com.eghl.sdk.interfaces.MasterpassCallback;
import com.eghl.sdk.interfaces.QueryCallback;
import com.eghl.sdk.params.CaptureParams;
import com.eghl.sdk.params.LightboxParams;
import com.eghl.sdk.params.MasterpassParams;
import com.eghl.sdk.params.Params;
import com.eghl.sdk.params.PaymentParams;
import com.eghl.sdk.params.QueryParams;
import com.eghl.sdk.response.CaptureResponse;
import com.eghl.sdk.response.QueryResponse;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String PROD_HOST = "https://securepay.e-ghl.com/IPG/Payment.aspx";
    public static final String TEST_HOST = "https://test2pay.ghl.com/IPGSG/Payment.aspx";
    // TEST CHECKOUT ID 323e1841a06c42599f5a96e04ee21c65
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
    private EditText passwordEdit;
    private Spinner transactionTypeSpinner;
    private Spinner paymentMethodSpinner;
    private ProgressDialog progress;
    private Button paymentButton;
    private Button masterpassButton;
    private Switch prodSwitch;


    private String paymentGateway = TEST_HOST;
    private Intent lastPaymentData;
    private Button queryButton;
    private Button captureButton;


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
                //String cnasit = eghl.generateId("CNASIT");
                String cnasit = eghl.generateId("DEMO");
                params = new PaymentParams.Builder()
                        .setMerchantReturnUrl("SDK")
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
                        .setTransactionType(transactionTypeSpinner.getSelectedItem().toString())
                        .setPaymentMethod(paymentMethodSpinner.getSelectedItem().toString())
                        .setPaymentGateway(paymentGateway)
                        .setPassword(passwordEdit.getText().toString())
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
                if (firstRequest == null)
                    return;
                progress.show();
                eghl.executeMasterpassRequest(MainActivity.this, firstRequest, new MasterpassCallback() {
                    @Override
                    public void onResponse(final String response) {
                        // Handle pairing or express
                        progress.dismiss();
                        if (response.contains(Params.MASTERPASS_REQ_TOKEN) || response.contains(Params.MASTERPASS_PAIRING_TOKEN)) {
                            // Needs pairing
                            proceedPairing(response);
                        } else if (response.contains(Params.MASTERPASS_PRE_CHECKOUT_ID)) {
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


        prodSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    paymentGateway = PROD_HOST;
                } else {
                    paymentGateway = TEST_HOST;
                }
            }
        });

        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryLastPayment(lastPaymentData);


            }
        });
        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureLastPayment(lastPaymentData);
            }
        });


    }

    private void captureLastPayment(Intent lastPaymentData) {
        if (lastPaymentData == null) {
            Toast.makeText(this, "No payment record yet. Make a payment transaction and try again.", Toast.LENGTH_SHORT).show();
            return;
        }
        try{

            String rawResponse = lastPaymentData.getStringExtra(EGHL.RAW_RESPONSE);
            JSONObject jsonObject = new JSONObject(rawResponse);
            String paymentMethod = jsonObject.getString(Params.PAYMENT_METHOD);
            String serviceID = jsonObject.getString(Params.SERVICE_ID);
            String paymentId = jsonObject.getString(Params.PAYMENT_ID);
            String amount = jsonObject.getString(Params.AMOUNT);
            String currencyCode = jsonObject.getString(Params.CURRENCY_CODE);

            captureTransaction(paymentMethod, serviceID, paymentId, amount, currencyCode);
        }catch (JSONException e){

            Toast.makeText(this, "Something went wrong in parsing the last payment.", Toast.LENGTH_SHORT).show();

        }


    }

    private void queryLastPayment(Intent lastPaymentData) {
        if (lastPaymentData == null) {
            Toast.makeText(this, "No payment record yet. Make a payment transaction and try again.", Toast.LENGTH_SHORT).show();
            return;
        }
        try{

            String rawResponse = lastPaymentData.getStringExtra(EGHL.RAW_RESPONSE);
            JSONObject jsonObject = new JSONObject(rawResponse);
            String paymentMethod = jsonObject.getString(Params.PAYMENT_METHOD);
            String serviceID = jsonObject.getString(Params.SERVICE_ID);
            String paymentId = jsonObject.getString(Params.PAYMENT_ID);
            String amount = jsonObject.getString(Params.AMOUNT);
            String currencyCode = jsonObject.getString(Params.CURRENCY_CODE);


            queryTransaction(paymentMethod, serviceID, paymentId, amount, currencyCode);


        }catch (JSONException e){

            Toast.makeText(this, "Something went wrong in parsing the last payment.", Toast.LENGTH_SHORT).show();

        }




    }


    private void proceedExpress(String response) {
        final ExpressResponse expressResponse = new Gson().fromJson(response, ExpressResponse.class);
        List<Card> cards = expressResponse.getCards();
        final CardsAdapter adapter = new CardsAdapter(MainActivity.this, cards);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Cards");
        builder.setSingleChoiceItems(adapter, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
                Card card = (Card) adapter.getItem(item);
                String cnasit = eghl.generateId("DEMO");
                params = new PaymentParams.Builder()
                        .setMerchantReturnUrl("SDK")
                        .setPaymentDesc("payment without previous pairing")
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
                        .setTransactionType(transactionTypeSpinner.getSelectedItem().toString())
                        .setPaymentMethod(paymentMethodSpinner.getSelectedItem().toString())
                        .setCardID(card.getCardId())
                        .setPreCheckoutID(expressResponse.getPreCheckoutId())
                        .setPaymentId(cnasit)
                        .setPaymentGateway(paymentGateway)
                        .setPassword(passwordEdit.getText().toString())
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
        Uri uri = Uri.parse("?" + response);
        String pairingToken = uri.getQueryParameter(Params.MASTERPASS_PAIRING_TOKEN) != null ? uri.getQueryParameter(Params.MASTERPASS_PAIRING_TOKEN) : "";
        String reqToken = uri.getQueryParameter(Params.MASTERPASS_REQ_TOKEN) != null ? uri.getQueryParameter(Params.MASTERPASS_REQ_TOKEN) : "";

        LightboxParams.Builder params = new LightboxParams.Builder()
                .setReqToken(reqToken)
                .setPairingToken(pairingToken)
                .setLightBoxCallbackURL("http://dummy123asd.com")  // http://...
                .setMPEMerchantCheckoutID("323e1841a06c42599f5a96e04ee21c65") //id from masterpass
                .setLightboxJS("https://sandbox.static.masterpass.com/dyn/js/switch/integration/MasterPass.client.js"); // Production: https://static.masterpass.com/dyn/js/switch/integration/MasterPass.client.js


        // Result of this pairing will be in the onActivityResult. With request code of EGHL.REQUEST_PAIRING.
        eghl.executePairing(params.build(), MainActivity.this);
    }

    private Bundle buildMasterpassReq() {

        final String token = tokenEdit.getText().toString();
        final String tokenType = tokenTypeEdit.getText().toString();
        final String serviceId = serviceEdit.getText().toString();
        final String amount = amountEdit.getText().toString();
        final String currencyCode = currencyEdit.getText().toString();
        final String paymentDesc = "eGHL Payment testing";

        if (TextUtils.isEmpty(token)) {
            Toast.makeText(MainActivity.this, "Token Should not be empty ", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (TextUtils.isEmpty(tokenType)) {
            Toast.makeText(MainActivity.this, "TokenType Should not be empty ", Toast.LENGTH_SHORT).show();
            return null;
        }
        MasterpassParams.Builder firstRequest = new MasterpassParams.Builder();
        firstRequest.setCurrencyCode(currencyCode);
        firstRequest.setAmount(amount);
        firstRequest.setToken(token);
        firstRequest.setTokenType(tokenType);
        firstRequest.setPaymentDesc(paymentDesc);
        firstRequest.setServiceID(serviceId);
        firstRequest.setPaymentGateway(paymentGateway);
        firstRequest.setPassword(passwordEdit.getText().toString());


        return firstRequest.build();


    }

    private void initView() {

        queryButton = (Button) findViewById(R.id.queryButton);
        captureButton = (Button) findViewById(R.id.captureButton);
        transactionTypeSpinner = (Spinner) findViewById(R.id.transactionTypeSpinner);
        paymentMethodSpinner = (Spinner) findViewById(R.id.paymentMethodSpinner);
        prodSwitch = (Switch) findViewById(R.id.prodSwitch);


        amountEdit = (EditText) findViewById(R.id.amountEdit);
        passwordEdit = (EditText) findViewById(R.id.passwordEdit);
        merchantEdit = (EditText) findViewById(R.id.merchantEdit);
        emailEdit = (EditText) findViewById(R.id.emailEdit);
        nameEdit = (EditText) findViewById(R.id.nameEdit);
        serviceEdit = (EditText) findViewById(R.id.serviceEdit);

        currencyEdit = (EditText) findViewById(R.id.currencyEdit);

        tokenEdit = (EditText) findViewById(R.id.tokenEdit);
        tokenTypeEdit = (EditText) findViewById(R.id.tokenTypeEdit);

        paymentButton = (Button) findViewById(R.id.checkout);
        masterpassButton = (Button) findViewById(R.id.masterpass);

        progress = new ProgressDialog(MainActivity.this);
        progress.setMessage("Please wait...");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EGHL.REQUEST_PAYMENT && data != null) {
            lastPaymentData = data;
            if (!TextUtils.isEmpty(data.getStringExtra(EGHL.RAW_RESPONSE))) {


                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                if (data.getStringExtra(EGHL.TXN_MESSAGE) != null && !TextUtils.isEmpty(data.getStringExtra(EGHL.TXN_MESSAGE))) {
                    builder.setTitle(data.getStringExtra(EGHL.TXN_MESSAGE));
                } else {
                    builder.setTitle(data.getStringExtra(QueryResponse.QUERY_DESC));

                }
                String message = "TxnStatus = " + data.getIntExtra(EGHL.TXN_STATUS, EGHL.TRANSACTION_NO_STATUS) + "\n" + "TxnMessage = " + data.getStringExtra(EGHL.TXN_MESSAGE) + "\nRaw Response:\n" + data.getStringExtra(EGHL.RAW_RESPONSE);
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
                    Toast.makeText(this, "Payment cancelled", Toast.LENGTH_SHORT).show();
                    break;
                case EGHL.TRANSACTION_AUTHORIZED:
                    Toast.makeText(this, "Payment Authorized", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Log.d(TAG, "onActivityResult: " + resultCode);
                    break;
            }
        } else if (requestCode == EGHL.REQUEST_PAIRING && data != null) {
            if (resultCode == EGHL.TRANSACTION_MASTERPASS_FINISHED) {

                String reqVerifier = data.getStringExtra("oauth_verifier") != null ? data.getStringExtra("oauth_verifier") : "";
                String pairingVerifier = data.getStringExtra("pairing_verifier") != null ? data.getStringExtra("pairing_verifier") : "";
                String checkoutURL = data.getStringExtra("checkout_resource_url") != null ? data.getStringExtra("checkout_resource_url") : "";
                String pairingToken = data.getStringExtra("pairing_token") != null ? data.getStringExtra("pairing_token") : "";
                String reqToken = data.getStringExtra("oauth_token") != null ? data.getStringExtra("oauth_token") : "";
                String status = data.getStringExtra("mpstatus") != null ? data.getStringExtra("mpstatus") : "";

                switch (status) {
                    case "success":
                        String cnasit = eghl.generateId("DEMO");
                        params = new PaymentParams.Builder()
                                .setMerchantReturnUrl("SDK")
                                .setPaymentDesc("payment without previous pairing")
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
                                .setTransactionType(transactionTypeSpinner.getSelectedItem().toString())
                                .setPaymentMethod(paymentMethodSpinner.getSelectedItem().toString())
                                .setPairingToken(pairingToken)
                                .setReqToken(reqToken)
                                .setCheckoutResourceURL(checkoutURL)
                                .setPairingVerifier(pairingVerifier)
                                .setReqVerifier(reqVerifier)
                                .setPaymentId(cnasit)
                                .setPaymentGateway(paymentGateway)
                                .setPassword(passwordEdit.getText().toString())
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

            } else if (resultCode == EGHL.TRANSACTION_CANCELLED) {
                // user pressed back
                Toast.makeText(this, "Masterpass cancelled", Toast.LENGTH_SHORT).show();
            }


        }

    }

    private void captureTransaction(String paymentMethod, String serviceID, String paymentId, String amount, String currencyCode) {
        CaptureParams.Builder builder = new CaptureParams.Builder();
        builder.setPaymentMethod(paymentMethod);
        builder.setServiceId(serviceID);
        builder.setPaymentId(paymentId);
        builder.setAmount(amount);
        builder.setPaymentGateway(paymentGateway);
        builder.setPassword(passwordEdit.getText().toString());
        builder.setCurrencyCode(currencyCode);


        progress.show();
        eghl.executeCapture(this, builder.build(), new CaptureCallback() {
            @Override
            public void onResponse(CaptureResponse response) {
                progress.dismiss();
                String rawResponse = response.getRawResponse();
                String txnMessage = response.getTxnMessage();
                String txnStatus = response.getTxnStatus();
                showResultDialog(rawResponse, txnMessage, txnStatus);
                Log.d(TAG, "onResponse: " + rawResponse);
            }

            @Override
            public void onError(Exception e) {
                progress.dismiss();
                Log.e(TAG, "onError: capture", e);
                Toast.makeText(MainActivity.this, "Capture error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void queryTransaction(String paymentMethod, String serviceID, String paymentId, String amount, String currencyCode) {
        QueryParams.Builder builder = new QueryParams.Builder();
        builder.setPaymentMethod(paymentMethod);
        builder.setServiceId(serviceID);
        builder.setPaymentId(paymentId);
        builder.setAmount(amount);
        builder.setPaymentGateway(paymentGateway);
        builder.setPassword(passwordEdit.getText().toString());
        builder.setCurrencyCode(currencyCode);

        progress.show();
        eghl.executeQuery(this, builder.build(), new QueryCallback() {
            @Override
            public void onResponse(QueryResponse response) {
                progress.dismiss();
                String rawResponse = response.getRawResponse();
                String txnMessage = response.getTxnMessage();
                String txnStatus = response.getTxnStatus();
                showResultDialog(rawResponse, txnMessage, txnStatus);
                Log.d(TAG, "onResponse: " + rawResponse);
            }

            @Override
            public void onError(Exception e) {
                progress.dismiss();
                Log.e(TAG, "onError: query", e);
                Toast.makeText(MainActivity.this, "Query error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showResultDialog(String rawResponse, String txnMessage, String txnStatus) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(txnMessage);
        String message = "TxnStatus = " + txnStatus + "\n" + "TxnMessage = " + txnMessage + "\nRaw Response:\n" + rawResponse;
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
