package com.frog.p.bitcoinvirtualinvestment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.frog.p.bitcoinvirtualinvestment.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Exchange extends AppCompatActivity {
    Intent intent;
    TextView t1;
    TextView t2;
    TextView t3;
    TextView t4;
    Button b1;
    Button b2;
    EditText e1;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange);
        t1 = (TextView)findViewById(R.id.textView1);
        t2 = (TextView)findViewById(R.id.textView2);
        t3 = (TextView)findViewById(R.id.textView3);
        t4 = (TextView)findViewById(R.id.textView4);
        b1 = (Button)findViewById(R.id.button1);
        b2 = (Button)findViewById(R.id.button2);
        e1 = (EditText)findViewById(R.id.editText1);
        intent = new Intent(this.getIntent());
        recentview();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(e1.getText().length()==0){
                    Toast.makeText(getApplicationContext(),"공백 입니다.",Toast.LENGTH_SHORT).show();
                }
                else{
                    String name = t1.getText().toString();
                    int pcs = Integer.parseInt(e1.getText()+"");
                    int pcs2 = Integer.parseInt(load(name));
                    double temp =  pcs * Double.parseDouble(load(name+"p"));
                    double temp2 = Double.parseDouble(load("money"));
                    double x;
                    String y;
                    if(temp>temp2){
                        Toast.makeText(getApplicationContext(), "현금이 부족합니다.", Toast.LENGTH_SHORT).show();
                        e1.setText("");
                    }
                    else{
                        temp2 = temp2 - temp;
                        pcs = pcs + pcs2;
                        save("money",temp2);
                        save(name,pcs);
                        e1.setText("");
                        x = Double.parseDouble(load("money"));
                        y = String.format("%.2f",x);
                        t2.setText(y+" $");
                        t4.setText(load(name));
                        Toast.makeText(getApplicationContext(), "거래완료.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(e1.getText().length()==0){
                    Toast.makeText(getApplicationContext(),"공백 입니다.",Toast.LENGTH_SHORT).show();
                }
                else{
                    String name = t1.getText().toString();
                    int pcs = Integer.parseInt(e1.getText()+"");
                    int pcs2 = Integer.parseInt(load(name));
                    double temp =  pcs * Double.parseDouble(load(name+"p"));
                    double temp2 = Double.parseDouble(load("money"));
                    double x;
                    String y;
                    if(pcs>pcs2){
                        Toast.makeText(getApplicationContext(), "보유수량이 부족합니다.", Toast.LENGTH_SHORT).show();
                        e1.setText("");
                    }
                    else{
                        temp2 = temp2 + temp;
                        pcs2 =  pcs2 - pcs;
                        save("money",temp2);
                        save(name,pcs2);
                        e1.setText("");
                        x = Double.parseDouble(load("money"));
                        y = String.format("%.2f",x);
                        t2.setText(y+" $");
                        t4.setText(load(name));
                        Toast.makeText(getApplicationContext(), "거래완료.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
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

    void recentview(){
        int i = intent.getIntExtra("i",0);
        double temp;
        String temp2;
        temp = Double.parseDouble(load("money"));
        temp2 = String.format("%.2f",temp);
        switch(i){
            case 1:
                t1.setText("BTC");
                t2.setText(temp2+" $");
                t3.setText(load("BTCp")+" $");
                t4.setText(load("BTC"));
                break;
            case 2:
                t1.setText("BCH");
                t2.setText(temp2+" $");
                t3.setText(load("BCHp")+" $");
                t4.setText(load("BCH"));
                break;
            case 3:
                t1.setText("ETH");
                t2.setText(temp2+" $");
                t3.setText(load("ETHp")+" $");
                t4.setText(load("ETH"));
                break;
            case 4:
                t1.setText("ETC");
                t2.setText(temp2+" $");
                t3.setText(load("ETCp")+" $");
                t4.setText(load("ETC"));
                break;
            case 5:
                t1.setText("XRP");
                t2.setText(temp2+" $");
                t3.setText(load("XRPp")+" $");
                t4.setText(load("XRP"));
                break;
            case 6:
                t1.setText("LTC");
                t2.setText(temp2+" $");
                t3.setText(load("LTCp")+" $");
                t4.setText(load("LTC"));
                break;
        }
    }

}
