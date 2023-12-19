package com.example.kalkulatorsederhana;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;

import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private EditText editBil1, editBil2;
    private TextView textHasil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editBil1=(EditText)findViewById(R.id.editTextBil1);
        editBil2=(EditText)findViewById(R.id.editTextBil2);
        textHasil=(TextView)findViewById(R.id.textViewHasil);
    }
    public void tambah(View v){
        float bil1,bil2, hasil;

        bil1=Float.parseFloat(editBil1.getText().toString());
        bil2=Float.parseFloat(editBil2.getText().toString());
        hasil=bil1+bil2;

        textHasil.setText(bil1+"+"+bil2+" = "+hasil);
    }

    public void kurang(View v){
        float bil1,bil2, hasil;

        bil1=Float.parseFloat(editBil1.getText().toString());
        bil2=Float.parseFloat(editBil2.getText().toString());
        hasil=bil1-bil2;

        textHasil.setText(bil1+"-"+bil2+" = "+hasil);
    }
    public void bagi(View v){
        float bil1, bil2, hasil;

        bil1 = Float.parseFloat(editBil1.getText().toString());
        bil2 = Float.parseFloat(editBil2.getText().toString());

        if (bil2 == 0) {
            textHasil.setText("Tidak terdefinisi");
        } else {
            hasil = bil1 / bil2;
            textHasil.setText(bil1 + "/" + bil2 + " = " + hasil);
        }
    }

    public void kali(View v){
        float bil1,bil2, hasil;
        bil1=Float.parseFloat(editBil1.getText().toString());
        bil2=Float.parseFloat(editBil2.getText().toString());
        hasil=bil1*bil2;
        textHasil.setText(bil1+"*"+bil2+" = "+hasil);
    }
}

