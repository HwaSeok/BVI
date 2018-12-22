package com.frog.p.bitcoinvirtualinvestment;


import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.frog.p.bitcoinvirtualinvestment.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private TextView t2;
    private TextView t4;
    private TextView t6;
    private TextView t8;
    private TextView t10;
    private TextView t12;
    private TextView t14;
    private TextView t16;
    private TextView t20;
    private TextView t21;
    private EditText e;

    private AdView mAdView;

    public static final String[] ENDPOINT = new String[6];
    private OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t2 = (TextView)findViewById(R.id.textView2);
        t4 = (TextView)findViewById(R.id.textView4);
        t6 = (TextView)findViewById(R.id.textView6);
        t8 = (TextView)findViewById(R.id.textView8);
        t10 = (TextView)findViewById(R.id.textView10);
        t12 = (TextView)findViewById(R.id.textView12);
        t14 = (TextView)findViewById(R.id.textView14);
        t16 = (TextView)findViewById(R.id.textView16);
        t20 = (TextView)findViewById(R.id.textView20);
        t21 = (TextView)findViewById(R.id.textView21);
        e = (EditText)findViewById(R.id.editText);

        ENDPOINT[0] = "https://api.coinhills.com/v1/cspa/btc/";
        ENDPOINT[1] = "https://api.coinhills.com/v1/cspa/bch/";
        ENDPOINT[2] = "https://api.coinhills.com/v1/cspa/eth/";
        ENDPOINT[3] = "https://api.coinhills.com/v1/cspa/etc/";
        ENDPOINT[4] = "https://api.coinhills.com/v1/cspa/xrp/";
        ENDPOINT[5] = "https://api.coinhills.com/v1/cspa/ltc/";

        MobileAds.initialize(this, "ca-app-pub-");

        mAdView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        String dirPath = getFilesDir().getAbsolutePath();
        File file = new File(dirPath+"/mfile.txt");
        if(!file.exists()){
            try{
                InputStream in = getResources().openRawResource(R.raw.filesmfile);
                InputStreamReader isr = new InputStreamReader(in,"utf-8");
                BufferedReader buffer = new BufferedReader(isr);
                String line = buffer.readLine();

                File savefile = new File(dirPath+"/mfile.txt");
                FileOutputStream fos = new FileOutputStream(savefile);
                fos.write(line.getBytes());
                fos.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        for(int i=0; i<6; i++){
            load2(i);
        }

        recentview();



        Button button2=(Button)findViewById(R.id.button2);

        Button button4=(Button)findViewById(R.id.button4);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,TableActivity.class);
                startActivity(intent);
                finish();
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double money = Double.parseDouble(e.getText().toString());
                e.setText("");
                save("money",money);
                save("BTC",0);
                save("BCH",0);
                save("ETH",0);
                save("ETC",0);
                save("XRP",0);
                save("LTC",0);
                save("moneyall",money);
                save("firstmoney",money);
                recentview();
                Toast.makeText(getApplicationContext(), "설정완료.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onResume() {
        super.onResume();
        recentview();
    }



    protected void save(String word, double number){
        try{
            FileReader fr = new FileReader(getFilesDir()+"/mfile.txt");
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            JSONObject obj = new JSONObject(line);
            obj.put(word , number);
            String jsonstr = obj.toString();
            FileWriter fw = new FileWriter(getFilesDir()+"/mfile.txt");
            fw.write(jsonstr);
            fw.close();
        }catch (IOException e){
            e.printStackTrace();
        }catch(JSONException e){
            e.printStackTrace();
        }
    }
    protected  String load(String word){
        try{
            FileReader fr = new FileReader(getFilesDir()+"/mfile.txt");
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            JSONObject obj = new JSONObject(line);
            String x = obj.get(word).toString();
            return x;
        }catch (IOException e){
            e.printStackTrace();
            return "IO Exception";
        }catch (JSONException e){
            e.printStackTrace();
            return "JSON Exception";
        }
    }

    protected void recentview(){
        moneymath();

        double nowmoney = 0;
        double percent = 0;
        double moneyall = Double.parseDouble(load("moneyall"));
        double firstmoney = Double.parseDouble(load("firstmoney"));
        nowmoney = moneyall - firstmoney;
        percent = (moneyall/firstmoney-1)*100;
        if(moneyall==firstmoney) percent = 0;
        double money;
        String money2,money3,money4,percent2;
        money = Double.parseDouble(load("money"));
        money2 = String.format("%.2f",money);
        money = Double.parseDouble(load("moneyall"));
        money3 = String.format("%.2f",money);
        money4 = String.format("%.2f",nowmoney);
        percent2 = String.format("%.2f",percent);

        if(nowmoney<0) t20.setTextColor(Color.parseColor("#ed2432"));
        else t20.setTextColor(Color.parseColor("#3776ea"));

        if(percent<0) t21.setTextColor(Color.parseColor("#ed2432"));
        else t21.setTextColor(Color.parseColor("#3776ea"));

        t2.setText(money2+" $");
        t4.setText(load("BTC"));
        t6.setText(load("BCH"));
        t8.setText(load("ETH"));
        t10.setText(load("ETC"));
        t12.setText(load("XRP"));
        t14.setText(load("LTC"));
        t16.setText(money3+" $");
        t20.setText(money4+" $");
        t21.setText(percent2+"%");
    }

    void moneymath(){
        double btc,bch,eth,etc,xrp,ltc,money;
        double result = 0;
        money = Double.parseDouble(load("money"));
        btc = Double.parseDouble(load("BTC")) * Double.parseDouble(load("BTCp"));
        bch = Double.parseDouble(load("BCH")) * Double.parseDouble(load("BCHp"));
        eth = Double.parseDouble(load("ETH")) * Double.parseDouble(load("ETHp"));
        etc = Double.parseDouble(load("ETC")) * Double.parseDouble(load("ETCp"));
        xrp = Double.parseDouble(load("XRP")) * Double.parseDouble(load("XRPp"));
        ltc = Double.parseDouble(load("LTC")) * Double.parseDouble(load("LTCp"));
        result = btc + bch + eth + etc + xrp + ltc + money;
        save("moneyall",result);
    }

    void load2(final int i) {
        Request request = new Request.Builder()
                .url(ENDPOINT[i])
                .build();


        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this, "Error during loading : "
                        + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, final Response response)
                    throws IOException {
                final String body = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        parseResponse(body,i);

                    }
                });
            }
        });

    }

    void parseResponse(String body,int i) {
        try {
            JSONObject jsonObject = new JSONObject(body);
            JSONObject coinObject = jsonObject.getJSONObject("data");
            switch (i){
                case 0:
                    JSONObject bObject = coinObject.getJSONObject("CSPA:BTC");
                    save("BTCp",bObject.getDouble("cspa"));
                    save("BTC24",bObject.getDouble("cspa_change_24h_pct"));
                    break;

                case 1:
                    JSONObject b2Object = coinObject.getJSONObject("CSPA:BCH");
                    save("BCHp",b2Object.getDouble("cspa"));
                    save("BCH24",b2Object.getDouble("cspa_change_24h_pct"));
                    break;

                case 2:
                    JSONObject eObject = coinObject.getJSONObject("CSPA:ETH");
                    save("ETHp",eObject.getDouble("cspa"));
                    save("ETH24",eObject.getDouble("cspa_change_24h_pct"));
                    break;

                case 3:
                    JSONObject e2Object = coinObject.getJSONObject("CSPA:ETC");
                    save("ETCp",e2Object.getDouble("cspa"));
                    save("ETC24",e2Object.getDouble("cspa_change_24h_pct"));
                    break;

                case 4:
                    JSONObject xObject = coinObject.getJSONObject("CSPA:XRP");
                    save("XRPp",xObject.getDouble("cspa"));
                    save("XRP24",xObject.getDouble("cspa_change_24h_pct"));
                    break;

                case 5:
                    JSONObject lObject = coinObject.getJSONObject("CSPA:LTC");
                    save("LTCp",lObject.getDouble("cspa"));
                    save("LTC24",lObject.getDouble("cspa_change_24h_pct"));
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
