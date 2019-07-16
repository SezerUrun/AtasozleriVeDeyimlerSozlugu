package com.atasozlerivedeyimlersozlugu;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ArrayList<Atasozu> atasozleri=new ArrayList<>();
    ArrayList<Deyim> deyimler=new ArrayList<>();
    EditText aramaCubugu;
    Button atasozuAra,deyimAra;
    TextView sonuclar,sonuclarT;
    int id=0;
    SharedPreferences settings;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this,"ca-app-pub-2616577523967769~5458688585");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        aramaCubugu=findViewById(R.id.aramaCubugu);
        atasozuAra=findViewById(R.id.atasozuAra);
        deyimAra=findViewById(R.id.deyimAra);
        sonuclar=findViewById(R.id.sonuclar);
        sonuclarT=findViewById(R.id.sonuclarT);
        atasozuAra.setOnClickListener(this);
        deyimAra.setOnClickListener(this);
        aramaCubugu.clearFocus();

        //Başlangıçta EditText'e odaklanmayı kapatma
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        aramaCubugu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                aramaCubugu.setText("");
                aramaCubugu.setFocusable(true);
                return false;
            }
        });

        DataBaseHelper dbHelper = new DataBaseHelper(getApplicationContext());
        SharedPreferences settings = getSharedPreferences("SQL", 0);
        boolean firstTime = settings.getBoolean("firstTime", true);
        if (firstTime) {
            parseXML();
            id=0;
            while(id<atasozleri.size()){
                dbHelper.AtasozuEkle(new Atasozu(id,atasozleri.get(id).getIcerik()));
                id++;
            }
            id=0;
            while(id<deyimler.size()){
                dbHelper.DeyimEkle(new Deyim(id,deyimler.get(id).getIcerik()));
                id++;
            }

            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("firstTime", false);
            editor.commit();

        }
    }





    private void parseXML() {
        XmlPullParserFactory parserFactory;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = getAssets().open("atasozleri_ve_deyimler.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);
            processParsing(parser);
        }
        catch (XmlPullParserException e) {
        }
        catch (IOException e) {
        }
    }
    private void processParsing(XmlPullParser parser) throws IOException, XmlPullParserException{
        int eventType = parser.getEventType();
        Atasozu currentAtasozu = null;
        Deyim currentDeyim = null;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String eltName = null;
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    eltName = parser.getName();
                    if ("atasozu".equals(eltName)) {
                        currentAtasozu = new Atasozu();
                        currentAtasozu.setIcerik(parser.nextText());
                        atasozleri.add(currentAtasozu);
                    }
                    if ("deyim".equals(eltName)) {
                        currentDeyim = new Deyim();
                        currentDeyim.setIcerik(parser.nextText());
                        deyimler.add(currentDeyim);
                    }
                    break;
            }
            eventType = parser.next();
        }
    }



    @Override
    public void onClick(View view) {
        InputMethodManager inputManager = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);

        DataBaseHelper dbHelper = new DataBaseHelper(getApplicationContext());
        String aranan="";
        sonuclar.setText("");
        sonuclarT.setVisibility(View.VISIBLE);
        int viewId=view.getId();
        try{
            aranan=aramaCubugu.getText().toString();
        }
        catch (Exception e){
            sonuclar.setText("Yanlış giriş.");
        }

        //Atasozleri tablosunda arama yapma
        if (viewId==atasozuAra.getId()){
            List<Atasozu> bulunanAtasozleri = dbHelper.AtasozuArama(aranan);
            if (aranan.length()<2){ //2 karakterden az giriş yapılmış ise
                sonuclar.setText("Hatalı giriş.");
                return;
            }
            if (bulunanAtasozleri.size()>0){
                for (int i=0;i<bulunanAtasozleri .size();i++){
                    sonuclar.setText(sonuclar.getText() + bulunanAtasozleri.get(i).getIcerik().toString() + "\n\n");
                }
            }
            else {
                sonuclar.setText("Aranan kelime bulunamadı.");
            }



        }

        if (viewId==deyimAra.getId()){

            //Deyimler tablosunda arama yapma
            List<Deyim> bulunanDeyimler = dbHelper.DeyimArama(aranan);
            if (aranan.length()<2){ //2 karakterden az giriş yapılmış ise
                sonuclar.setText("Hatalı giriş.");
                return;
            }
            if (bulunanDeyimler.size()>0){
                for (int i=0;i<bulunanDeyimler.size();i++){
                    sonuclar.setText(sonuclar.getText()+bulunanDeyimler.get(i).getIcerik().toString()+"\n\n");
                }
            }
            else {
                if (sonuclar.getText().toString().length()==0) {
                    sonuclar.setText("Aranan kelime bulunamadı.");
                }
            }
        }
    }


}