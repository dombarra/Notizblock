package eu.eurohardware24.notizblock;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.vending.billing.IInAppBillingService;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;

import inappbilling.util.IabHelper;
import inappbilling.util.IabResult;
import inappbilling.util.Inventory;
import inappbilling.util.Purchase;

public class OptionsActivity extends AppCompatActivity implements View.OnClickListener {

    Button textsizeplusButton, textsizeminusButton, lnplusButton,lnminusButton,noAdsButton,functionsButton;
    TextView textsizeTextView, lnTextView, noAdsText, functionsText;
    ImageButton back;
    Intent i = new Intent();
    float textsize = 25;
    int notes = 1;
    int lnote = 1;
    int rnote = 1;
    public static final String PREFS_NAME = "MyPrefsFile";
    int tg;
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    int lz;
    long letzteWerbung, jetzt;
    private AdView adView;
    LinearLayout layout, noAdsLayout,functionsLayout;
    private static final String MY_BANNER_UNIT_ID = "ca-app-pub-8124355001128596/3339556799";
    private static final String MY_INTERSTITIAL_UNIT_ID = "ca-app-pub-8124355001128596/1403745594";
    private InterstitialAd interstitial;
    AdRequest adRequest;

    Boolean noAds= false;
    Boolean functions = false;
    Boolean unlimitedNotes = false;

    private static final String TAG = "eu.eurohardware24.notizblock";
    static final String IAP_SKU = "eu.eurohardware24.notizblock.noads";
    static final String IAP_SKU2 = "eu.eurohardware24.notizblock.functions";
    static final String IAP_SKU3 = "eu.eurohardware24.notizblock.unlimitednotes";
    static final String IAP_SKU4 = "eu.eurohardware24.notizblock.10morenotes";
    String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApKWlanclnIbiuPm2IQErIOzrtdd2lQXDw4PmflaZJtdXPPzFaN8yc0faE9ApFrDWjXA2eEfA0KAMAicE7CfRhpTHDckayEod7xqiHe4LjabRKiuN5OfUT6ypskoMeTz4xlv0ogcZvjLY0FkEwig+6iOg3weQgq9bReQTB2nxR+xsNT6JbOjF3GDvsa5LPcw9y60UKABqNaxak9UEHdT9y58iOUsvAcDtdl6VJEQKYG9MqgPmniaBQbBEQ2IPo+6ZJOrLMm/CEXN+gBCPtpFHHeyUazWFS1vXPx83CHDBphCAGUEwA1mhsKpErFnWyGemaxiuDd5O/Qcn53GIOU8ncQIDAQAB";
    IabHelper mHelper;

    IInAppBillingService mService;

    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
        }
    };



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Pass on the activity result to the helper for handling
        Log.d(TAG,"onActivityResult");
        if (!this.mHelper.handleActivityResult(requestCode, resultCode, data)) {
            Log.d(TAG,"cleared the launch flow");
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {


            if (result.isFailure()) {
                // handle error here
            }
            else {
                // does the user have the premium upgrade?
                inventory.getPurchase(IAP_SKU);
                Boolean IAPgekauft = inventory.hasPurchase(IAP_SKU);

                if (IAPgekauft){
                    noAdsLayout.setVisibility(View.GONE);
                    editor = settings.edit();
                    noAds=true;
                    editor.putBoolean("noAds", noAds);
                    editor.commit();
                    adView.setVisibility(View.GONE);


                }
                if (!IAPgekauft){
                    editor = settings.edit();
                    noAds=false;
                    editor.putBoolean("noAds", noAds);
                    editor.commit();

                }
                inventory.getPurchase(IAP_SKU2);
                Boolean IAPgekauft2 = inventory.hasPurchase(IAP_SKU2);

                if (IAPgekauft2){
                    editor = settings.edit();
                    functions=true;
                    notes=3;
                    lnTextView.setText(""+lnote+ " "+ getString(R.string.of) +" "+ notes);
                    lnminusButton.setEnabled(true);
                    lnplusButton.setEnabled(true);
                    textsizeplusButton.setEnabled(true);
                    textsizeminusButton.setEnabled(true);
                    editor.putBoolean("functions", functions);
                    editor.putInt("notes", notes);
                    editor.commit();

                }
                if (!IAPgekauft2){
                    editor = settings.edit();
                    functions= false;
                    notes=1;
                    editor.putInt("notes", notes);
                    editor.putBoolean("functions", functions);
                    editor.commit();

                }

                inventory.getPurchase(IAP_SKU3);
                Boolean IAPgekauft3 = inventory.hasPurchase(IAP_SKU3);

                if (IAPgekauft3){

                    editor = settings.edit();
                    unlimitedNotes = true;
                    notes = 999999;
                    lnTextView.setText(""+lnote+ " "+ getString(R.string.of) +" "+ " ∞");
                    editor.putInt("notes",notes);
                    editor.putBoolean("unlimitedNotes", unlimitedNotes);
                    editor.commit();

                }
                if (!IAPgekauft3){
                    editor = settings.edit();
                    unlimitedNotes = false;
                    editor.putBoolean("unlimitedNotes", unlimitedNotes);
                    editor.commit();

                }

                if (noAds && unlimitedNotes){
                    noAdsLayout.setVisibility(View.GONE);
                    functionsLayout.setVisibility(View.GONE);
                }
                // update UI accordingly
            }
        }
    };

    IabHelper.QueryInventoryFinishedListener
            mQueryFinishedListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory)
        {
            if (result.isFailure()) {
                // handle error
                return;
            }
            if (!noAds){
                String IAPPrice =
                        inventory.getSkuDetails(IAP_SKU).getPrice();
                String IAPTitle =
                        inventory.getSkuDetails(IAP_SKU).getTitle();
                String IAPDescription = inventory.getSkuDetails(IAP_SKU).getDescription();

                noAdsText.setText(IAPDescription);
                noAdsText.setVisibility(View.VISIBLE);
                noAdsButton.setText(IAPTitle + " - "+ IAPPrice);
                noAdsButton.setVisibility(View.VISIBLE);
            }


            if (functions){
                String IAPPrice3 =
                        inventory.getSkuDetails(IAP_SKU3).getPrice();
                String IAPTitle3 =
                        inventory.getSkuDetails(IAP_SKU3).getTitle();
                String IAPDescription3 = inventory.getSkuDetails(IAP_SKU3).getDescription();

                functionsText.setText(IAPDescription3);
                functionsText.setVisibility(View.VISIBLE);
                functionsButton.setText(IAPTitle3 + " - "+ IAPPrice3);
                functionsButton.setVisibility(View.VISIBLE);
            }else {
                String IAPPrice2 =
                        inventory.getSkuDetails(IAP_SKU2).getPrice();
                String IAPTitle2 =
                        inventory.getSkuDetails(IAP_SKU2).getTitle();
                String IAPDescription2 = inventory.getSkuDetails(IAP_SKU2).getDescription();

                functionsText.setText(IAPDescription2);
                functionsText.setVisibility(View.VISIBLE);
                functionsButton.setText(IAPTitle2 + " - "+ IAPPrice2);
                functionsButton.setVisibility(View.VISIBLE);
            }
            /*
            if (functions){
                String IAPPrice4 =
                        inventory.getSkuDetails(IAP_SKU4).getPrice();
                String IAPTitle4 =
                        inventory.getSkuDetails(IAP_SKU4).getTitle();
                String IAPDescription4 = inventory.getSkuDetails(IAP_SKU4).getDescription();

                noAdsText.setText(IAPDescription2);
                noAdsText.setVisibility(View.VISIBLE);
                noAdsButton.setText(IAPTitle2 + " - "+ IAPPrice2);
                noAdsButton.setVisibility(View.VISIBLE);
            }
            */
            try {

                queryPurchasedItems();

            }
            catch(IllegalStateException ex){ //ADDED THIS CATCH
                result = new IabResult(6, "Helper is not setup.");
            }
            // update the UI
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);


        settings = getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();
        noAds = settings.getBoolean("noAds", noAds);
        functions = settings.getBoolean("functions", functions);
        layout = (LinearLayout)findViewById(R.id.Werbung2);

        noAdsLayout = (LinearLayout) findViewById(R.id.noAdsLayout);
        functionsLayout = (LinearLayout) findViewById(R.id.functionsLayout);

        // Create the adView.
        adView = new AdView(this);
        adView.setAdUnitId(MY_BANNER_UNIT_ID);
        adView.setAdSize(AdSize.BANNER);

        noAdsButton = (Button)findViewById(R.id.noAdsButton);
        noAdsButton.setOnClickListener(this);
        noAdsButton.setVisibility(View.GONE);

        noAdsText = (TextView)findViewById(R.id.noAdsText);
        noAdsText.setVisibility(View.GONE);

        functionsButton = (Button)findViewById(R.id.functionsButton);
        functionsButton.setOnClickListener(this);
        functionsButton.setVisibility(View.GONE);
        functionsText = (TextView)findViewById(R.id.functionsText);
        functionsText.setVisibility(View.GONE);

        unlimitedNotes = settings.getBoolean("unlimitedNotes", unlimitedNotes);


        if (noAds){}
        else{
            layout.addView(adView);

            // Initiate a generic request.
            AdRequest adRequest = new AdRequest.Builder().build();
            // Load the adView with the ad request.
            adView.loadAd(adRequest);
        }
        //if (noAds && functions && unlimitedNotes){}
       // else {



            // compute your public key and store it in base64EncodedPublicKey

            mHelper = new IabHelper(this, base64EncodedPublicKey);

            mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                public void onIabSetupFinished(IabResult result) {
                    if (!result.isSuccess()) {
                        // Oh noes, there was a problem.
                        Log.d(TAG, "Problem setting up In-app Billing: " + result);
                    }
                    ArrayList<String> additionalSkuList = new ArrayList<String>();
                    additionalSkuList.add(IAP_SKU);
                    additionalSkuList.add(IAP_SKU2);
                    additionalSkuList.add(IAP_SKU3);
                    //additionalSkuList.add(IAP_SKU4);
                    if( !mHelper.isAsyncInProgress()) {
                        mHelper.queryInventoryAsync(true, additionalSkuList,
                                mQueryFinishedListener);
                    }


                    // Hooray, IAB is fully set up!
                }
            });

      //  }

        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(MY_INTERSTITIAL_UNIT_ID);
        AdRequest Request = new AdRequest.Builder().build();
        interstitial.loadAd(Request);

        back = (ImageButton)findViewById(R.id.backButton);
        back.setOnClickListener(this);
        textsizeplusButton = (Button)findViewById(R.id.texstsizeplusButton);
        textsizeminusButton = (Button)findViewById(R.id.textsizeminusButton);
        textsizeplusButton.setOnClickListener(this);
        textsizeminusButton.setOnClickListener(this);
        textsizeTextView = (TextView)findViewById(R.id.textsizeTextView);

        lnplusButton = (Button)findViewById(R.id.lnplusButton);
        lnplusButton.setOnClickListener(this);
        lnminusButton = (Button)findViewById(R.id.lnminusButton);
        lnminusButton.setOnClickListener(this);

        lnTextView = (TextView)findViewById(R.id.lnTextView);

        lnminusButton.setEnabled(false);
        lnplusButton.setEnabled(false);

        if (functions){
            lnminusButton.setEnabled(true);
            lnplusButton.setEnabled(true);
            textsizeminusButton.setEnabled(true);
            textsizeplusButton.setEnabled(true);
        }else {
            lnminusButton.setEnabled(false);
            lnplusButton.setEnabled(false);
            textsizeminusButton.setEnabled(false);
            textsizeplusButton.setEnabled(false);
        }

        textsize = settings.getFloat("textsize", textsize);
        notes = settings.getInt("notes", notes);
        lnote = settings.getInt("lnote", lnote);
        rnote = settings.getInt("rnote", rnote);


        if (unlimitedNotes){
            notes = 999999;
            lnTextView.setText(""+lnote+ " "+ getString(R.string.of) +" "+ " ∞");
        }else {
            lnTextView.setText(""+lnote+ " "+ getString(R.string.of) +" "+ notes);
        }

        tg = textg(textsize);
        textsizeTextView.setText(""+tg);

        lz=0;
        lz= settings.getInt("lz", lz);
        letzteWerbung= System.currentTimeMillis();
        letzteWerbung = settings.getLong("letzteWerbung", letzteWerbung);


    }


    public int textg(float a){
        int b = (int)a;
        return b;
    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase)
        {
            if (result.isFailure()) {
                Log.d(TAG, "Error purchasing: " + result);
                try {

                    queryPurchasedItems();

                }
                catch(IllegalStateException ex){ //ADDED THIS CATCH
                    result = new IabResult(6, "Helper is not setup.");
                }
                return;
            }
            else if (purchase.getSku().equals(IAP_SKU)) {
                noAdsLayout.setVisibility(View.GONE);
                adView.setVisibility(View.GONE);
                editor = settings.edit();
                noAds=true;
                editor.putBoolean("noAds", noAds);
                editor.commit();
                // consume the gas and update the UI
            }
            else if (purchase.getSku().equals(IAP_SKU2)) {
                functionsLayout.setVisibility(View.GONE);
                editor = settings.edit();
                functions=true;
                notes = 3;
                lnTextView.setText(""+lnote+ " "+ getString(R.string.of) +" "+ notes);
                editor.putInt("notes", notes);
                lnminusButton.setEnabled(true);
                lnplusButton.setEnabled(true);
                textsizeplusButton.setEnabled(true);
                textsizeminusButton.setEnabled(true);
                editor.putBoolean("functions", functions);
                editor.commit();
                // consume the gas and update the UI
            }

            else if (purchase.getSku().equals(IAP_SKU3)) {
                noAdsLayout.setVisibility(View.GONE);
                functionsLayout.setVisibility(View.GONE);
                editor = settings.edit();
                unlimitedNotes = true;
                notes = 999999;
                editor.putInt("notes", notes);
                lnTextView.setText(""+lnote+ " "+ getString(R.string.of) +" "+ " ∞");
                editor.putBoolean("unlimitedNotes", unlimitedNotes);
                editor.commit();
                // consume the gas and update the UI
            }
        }
    };

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        if (v == noAdsButton){
            if (mHelper.isSetupDone() && !mHelper.isAsyncInProgress()){
                mHelper.flagEndAsync();
                mHelper.launchPurchaseFlow(this, IAP_SKU, 10001,
                        mPurchaseFinishedListener, "IAP");
            }
        }

        if (v == functionsButton){
            if (!functions){
                if (mHelper.isSetupDone() && !mHelper.isAsyncInProgress()) {
                    mHelper.flagEndAsync();
                    mHelper.launchPurchaseFlow(this, IAP_SKU2, 10001,
                            mPurchaseFinishedListener, "IAP2");
                }
            }else {
                if (mHelper.isSetupDone() && !mHelper.isAsyncInProgress()){
                    mHelper.flagEndAsync();
                    mHelper.launchPurchaseFlow(this, IAP_SKU3, 10001,
                            mPurchaseFinishedListener, "IAP3");
                }


            }

        }

        jetzt = System.currentTimeMillis();
        if (jetzt >= (letzteWerbung+1200000)){
            if (lz >= 50){
                if (noAds==false){
                    displayInterstitial();
                }
                lz=0;
            } else {
                lz=lz+1;
            }} else {
            lz=lz+1;
        }
        editor.putInt("lz", lz);
        editor.putLong("letzteWerbung", letzteWerbung);

        if (v == back){
            i.setClass(this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
        }

        if (v == textsizeplusButton){
            if (textsize<50){
                textsize = textsize+1;
                tg = textg(textsize);
                textsizeTextView.setText(""+tg);
                editor.putFloat("textsize", textsize);
                editor.commit();
            }

        }

        if (v == textsizeminusButton){
            if (textsize>1){
                textsize = textsize-1;
                tg = textg(textsize);
                textsizeTextView.setText(""+tg);
                editor.putFloat("textsize", textsize);
                editor.commit();
            }
        }

        if (v == lnplusButton){
            if (lnote<notes){
                lnote=lnote+1;
                if (unlimitedNotes){
                    notes = 999999;
                    lnTextView.setText(""+lnote+ " "+ getString(R.string.of) +" "+ " ∞");
                }else {
                    lnTextView.setText(""+lnote+ " "+ getString(R.string.of) +" "+ notes);
                }
                editor.putInt("lnote", lnote);
                editor.commit();
            }

        }

        if (v == lnminusButton){
            if (lnote>1){
                lnote=lnote-1;
                if (unlimitedNotes){
                    notes = 999999;
                    lnTextView.setText(""+lnote+ " "+ getString(R.string.of) +" "+ " ∞");
                }else {
                    lnTextView.setText(""+lnote+ " "+ getString(R.string.of) +" "+ notes);
                }
                editor.putInt("lnote", lnote);
                editor.commit();
            }

        }





    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if (mHelper != null){
            mHelper.dispose();
        }


    }

    public void displayInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    private void queryPurchasedItems() {
        //check if user has bought "remove adds"
        if(mHelper.isSetupDone() && !mHelper.isAsyncInProgress()) {
            mHelper.queryInventoryAsync(mGotInventoryListener);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}


