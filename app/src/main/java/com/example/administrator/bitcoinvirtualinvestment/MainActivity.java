package com.example.administrator.bitcoinvirtualinvestment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


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

        recentview();


        Button button2=(Button)findViewById(R.id.button2);
        Button button3=(Button)findViewById(R.id.button3);
        Button button4=(Button)findViewById(R.id.button4);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,TableActivity.class);
                startActivity(intent);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,TableActivity.class);
                startActivity(intent);
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
            }
        });
    }

    protected void save(String word, double number){
        try{
            FileReader fr = new FileReader(getFilesDir()+"mfile.txt");
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            JSONObject obj = new JSONObject(line);
            obj.put(word , number);
            String jsonstr = obj.toString();
            FileWriter fw = new FileWriter(getFilesDir()+"mfile.txt");
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
            FileReader fr = new FileReader(getFilesDir()+"mfile.txt");
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
        double nowmoney = 0;
        double percent = 0;
        double moneyall = Double.parseDouble(load("moneyall"));
        double firstmoney = Double.parseDouble(load("firstmoney"));
        nowmoney = moneyall - firstmoney;
        percent = (moneyall/firstmoney-1)*100;

        t2.setText(load("money")+" $");
        t4.setText(load("BTC"));
        t6.setText(load("BCH"));
        t8.setText(load("ETH"));
        t10.setText(load("ETC"));
        t12.setText(load("XRP"));
        t14.setText(load("LTC"));
        t16.setText(load("moneyall")+" $");
        t20.setText(nowmoney+" $");
        t21.setText(percent+"%");
    }



}