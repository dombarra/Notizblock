package eu.eurohardware24.notizblock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;

import static com.google.firebase.analytics.FirebaseAnalytics.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAnalytics mFirebaseAnalytics;
    private AdView mAdView;

    public static final String PREFS_NAME = "MyPrefsFile";
    EditText Text;

    Button clear;
    String text;
    int Hintergrund;
    LinearLayout haupt;
    int lz,tg;
    long letzteWerbung, jetzt;
    ImageButton optionsButton, textminusbutton,textplusbutton;
    float textsize = 25;
    TextView textsizeview;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    int lnote = 1;
    Intent i = new Intent();
    Boolean noAds= false;
    Boolean functions = false;
    private AdView adView;
    LinearLayout layout,adLayout;
    private static final String MY_BANNER_UNIT_ID = "ca-app-pub-8124355001128596/3339556799";
    private static final String MY_INTERSTITIAL_UNIT_ID = "ca-app-pub-8124355001128596/1403745594";
    private InterstitialAd interstitial;
    AdRequest adRequest;

    InputMethodManager imm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAnalytics = getInstance(this);
        adView = new AdView(this);
        adView.setAdUnitId(MY_BANNER_UNIT_ID);
        adView.setAdSize(AdSize.BANNER);
        adLayout = (LinearLayout) findViewById(R.id.adLayout);
        settings = getSharedPreferences(PREFS_NAME, 0);
        noAds = settings.getBoolean("noAds", noAds);
        if (noAds){
        }else{
            adLayout.addView(adView);

            // Initiate a generic request.
            AdRequest adRequest = new AdRequest.Builder().build();
            // Load the adView with the ad request.
            adView.loadAd(adRequest);
        }

        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(MY_INTERSTITIAL_UNIT_ID);
        AdRequest Request = new AdRequest.Builder().build();
        interstitial.loadAd(Request);
        optionsButton = (ImageButton)findViewById(R.id.optionsButton);
        optionsButton.setOnClickListener(this);
        clear = (Button)findViewById(R.id.clearButton);
        clear.setOnClickListener(this);
        Text = (EditText)findViewById(R.id.editText);
        settings = getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();
        functions = settings.getBoolean("functions", functions);
        textsize = settings.getFloat("textsize", textsize);
        Text.setTextSize(textsize);
        textsizeview = (TextView)findViewById(R.id.textsSizeView);
        textminusbutton = (ImageButton)findViewById(R.id.textkleinerButton);
        textplusbutton = (ImageButton)findViewById(R.id.textgroesserButton);
        textminusbutton.setOnClickListener(this);
        textplusbutton.setOnClickListener(this);
        textsizeview.setText(""+(int)textsize);
        text = settings.getString("Text", text);
        if (text != null){
            Text.setText(text);
        }
        if (!functions){
            textminusbutton.setVisibility(View.GONE);
            textplusbutton.setVisibility(View.GONE);
            textsizeview.setVisibility(View.GONE);
        }
        imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        lnote = settings.getInt("lnote", lnote);
        if (settings.getString("Text"+lnote, text)!= null){
            text = settings.getString("Text"+lnote, text);
            Text.setText(text);
        }

        lz=0;
        lz= settings.getInt("lz", lz);
        letzteWerbung= System.currentTimeMillis();
        letzteWerbung = settings.getLong("letzteWerbung", letzteWerbung);
    }


    public int textg(float a){
        int b = (int)a;
        return b;
    }

    @Override
    protected void onStop(){
        super.onStop();
        text = Text.getText().toString();
        settings = getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();
        editor.putInt("Hintergrund", Hintergrund);
        editor.putString("Text"+lnote, text);
        editor.putInt("lz", lz);
        editor.putLong("letzteWerbung", letzteWerbung);
        editor.commit();
    }

    @Override
    protected void onPause(){
        super.onPause();
        text = Text.getText().toString();
        settings = getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();
        editor.putInt("Hintergrund", Hintergrund);
        editor.putString("Text"+lnote, text);
        editor.putInt("lz", lz);
        editor.putLong("letzteWerbung", letzteWerbung);
        editor.commit();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        text = Text.getText().toString();
        settings = getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();
        editor.putInt("Hintergrund", Hintergrund);
        editor.putString("Text"+lnote, text);
        editor.putInt("lz", lz);
        editor.putLong("letzteWerbung", letzteWerbung);
        editor.commit();
    }

    @Override
    public void onStart() {
        super.onStart();

    }



    @Override
    public void onClick(View v) {
        if (v == textminusbutton){
            if (textsize>=2){
                textsize=textsize-1;
                Text.setTextSize(textsize);
                textsizeview.setText(""+(int)textsize);
                settings = getSharedPreferences(PREFS_NAME, 0);
                editor = settings.edit();
                editor.putFloat("textsize", textsize);
                editor.commit();
            }
        }
        if (v == textplusbutton){
            if (textsize<50){
                textsize=textsize+1;
                Text.setTextSize(textsize);
                textsizeview.setText(""+(int)textsize);
                settings = getSharedPreferences(PREFS_NAME, 0);
                editor = settings.edit();
                editor.putFloat("textsize", textsize);
                editor.commit();
            }
        }

        if (v == optionsButton){
            imm.hideSoftInputFromWindow(Text.getWindowToken(), 0);
            i.setClass(this, OptionsActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);

        }

        if (v == haupt){
            imm.hideSoftInputFromWindow(Text.getWindowToken(), 0);
        }
        if (v == clear){
            Text.setText("");
            imm.hideSoftInputFromWindow(Text.getWindowToken(), 0);
        }

        jetzt = System.currentTimeMillis();
        if (jetzt >= (letzteWerbung+1800000)){
            if (lz >= 10){
                if (!noAds){
                    displayInterstitial();}
                lz=0;
                letzteWerbung = System.currentTimeMillis();
            }else {
                lz=lz+1;
            }
        } else {
            lz=lz+1;
        }

    }



    public void displayInterstitial() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }


}
