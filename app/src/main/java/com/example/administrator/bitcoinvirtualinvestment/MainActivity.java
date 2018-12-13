package com.example.administrator.bitcoinvirtualinvestment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    private TextView t2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t2 = (TextView)findViewById(R.id.textView2);
        save("No",9999);
        t2.setText(load("No"));


        Button button2=(Button)findViewById(R.id.button2);
        Button button3=(Button)findViewById(R.id.button3);

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



}