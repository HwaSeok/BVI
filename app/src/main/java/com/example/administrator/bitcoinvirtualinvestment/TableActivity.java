package com.example.administrator.bitcoinvirtualinvestment;



import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TableActivity extends AppCompatActivity {
    public static final String ENDPOINT = "https://api.coinhills.com/v1/cspa/btc/";
    private OkHttpClient okHttpClient = new OkHttpClient();
    private TextView btc;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.table);
        btc = (TextView) findViewById(R.id.textView3);
        Button button1=(Button)findViewById(R.id.button);
        Button button2=(Button)findViewById(R.id.button2);
        Button button3=(Button)findViewById(R.id.button3);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TableActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TableActivity.this,TableActivity.class);
                startActivity(intent);
            }
        });

        mHandler.sendEmptyMessage(0);
    }





    private void load() {
        Request request = new Request.Builder()
                .url(ENDPOINT)
                .build();


        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(TableActivity.this, "Error during loading : "
                        + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException {
                final String body = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        parseBpiResponse(body);

                    }
                });
            }
        });

    }

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            load();
            mHandler.sendEmptyMessageDelayed(0, 3000);
        }
    };

    private void parseBpiResponse(String body) {
        try {
            StringBuilder builder = new StringBuilder();

            JSONObject jsonObject = new JSONObject(body);
            JSONObject coinObject = jsonObject.getJSONObject("data");
            JSONObject btcObject = coinObject.getJSONObject("CSPA:BTC");
            builder.append(btcObject.getInt("cspa")+"$");




            btc.setText(builder.toString());

        } catch (Exception e) {

        }
    }

}
