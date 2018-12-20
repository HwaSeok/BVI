package com.frog.p.bitcoinvirtualinvestment;



import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.frog.p.bitcoinvirtualinvestment.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TableActivity extends AppCompatActivity {
    public static final String[] ENDPOINT = new String[6];
    private OkHttpClient okHttpClient = new OkHttpClient();

    TextView[] t = new TextView[13];

    private AdView mAdView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table);

        MobileAds.initialize(this, "ca-app-pub-");

        mAdView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        for (int i = 1; i < 13; i++) {
            int id = getResources().getIdentifier("textView" + i,
                    "id", "com.frog.p.bitcoinvirtualinvestment");
            t[i] = (TextView)findViewById(id);
        }

        ENDPOINT[0] = "https://api.coinhills.com/v1/cspa/btc/";
        ENDPOINT[1] = "https://api.coinhills.com/v1/cspa/bch/";
        ENDPOINT[2] = "https://api.coinhills.com/v1/cspa/eth/";
        ENDPOINT[3] = "https://api.coinhills.com/v1/cspa/etc/";
        ENDPOINT[4] = "https://api.coinhills.com/v1/cspa/xrp/";
        ENDPOINT[5] = "https://api.coinhills.com/v1/cspa/ltc/";

        Button button1=(Button)findViewById(R.id.button);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TableActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        recentview();
        mHandler.sendEmptyMessage(0);
    }





    private void load2(final int i) {
        Request request = new Request.Builder()
                .url(ENDPOINT[i])
                .build();


        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(TableActivity.this, "Error during loading : "
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

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            for(int i=0; i<6; i++){
                load2(i);
            }
            recentview();
            mHandler.sendEmptyMessageDelayed(0, 3000);
        }
    };

    private void parseResponse(String body,int i) {
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
        double temp;
        String temp2;
        temp = Double.parseDouble(load("BTCp"));
        temp2 = String.format("%.2f",temp);
        t[1].setText(temp2+"$");
        t[2].setText(load("BTC24")+"%");

        temp = Double.parseDouble(load("BCHp"));
        temp2 = String.format("%.2f",temp);
        t[3].setText(temp2+"$");
        t[4].setText(load("BCH24")+"%");

        temp = Double.parseDouble(load("ETHp"));
        temp2 = String.format("%.2f",temp);
        t[5].setText(temp2+"$");
        t[6].setText(load("ETH24")+"%");

        temp = Double.parseDouble(load("ETCp"));
        temp2 = String.format("%.2f",temp);
        t[7].setText(temp2+"$");
        t[8].setText(load("ETC24")+"%");

        temp = Double.parseDouble(load("XRPp"));
        temp2 = String.format("%.2f",temp);
        t[9].setText(temp2+"$");
        t[10].setText(load("XRP24")+"%");

        temp = Double.parseDouble(load("LTCp"));
        temp2 = String.format("%.2f",temp);
        t[11].setText(temp2+"$");
        t[12].setText(load("LTC24")+"%");
    }

    void onClick(View v){
        Intent intent = new Intent(TableActivity.this, Exchange.class);
        switch (v.getId()){
            case R.id.table1:
                intent.putExtra("i",1);
                break;
            case R.id.table2:
                intent.putExtra("i",2);
                break;
            case R.id.table3:
                intent.putExtra("i",3);
                break;
            case R.id.table4:
                intent.putExtra("i",4);
                break;
            case R.id.table5:
                intent.putExtra("i",5);
                break;
            case R.id.table6:
                intent.putExtra("i",6);
                break;
        }

        startActivity(intent);
    }

}
