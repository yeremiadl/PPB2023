package com.example.aplikasipenjualan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private Button proses, clear, finish;

    private EditText editNama, editBarang, editHarga, editJumlah, editUang;

    private TextView textNama, textBarang, textHarga, textJumlah, textUang, textTotal, textKembalian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editNama = findViewById(R.id.editName);
        editBarang = findViewById(R.id.editBarang);
        editHarga = findViewById(R.id.editHarga);
        editJumlah = findViewById(R.id.editJumlah);
        editUang = findViewById(R.id.editBayar);

        textNama = findViewById(R.id.viewNama);
        textBarang = findViewById(R.id.viewBarang);
        textHarga = findViewById(R.id.viewJumlah);
        textJumlah = findViewById(R.id.viewJumlah);
        textUang = findViewById(R.id.viewUang);
        textTotal = findViewById(R.id.viewTotal);
        textKembalian = findViewById(R.id.viewKembalian);

        proses = findViewById(R.id.btnProses);
        clear = findViewById(R.id.btnClear);
        finish = findViewById(R.id.btnFinish);

        proses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goproses();
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goclear();
            }
        });
    }

    private void goclear(){
        editNama.getText().clear();
        editBarang.getText().clear();
        editHarga.getText().clear();
        editJumlah.getText().clear();
        editUang.getText().clear();

        textNama.setText("");
        textBarang.setText("");
        textHarga.setText("");
        textUang.setText("");
        textTotal.setText("");
        textKembalian.setText("");
    }

    private void goproses(){
        String name = editNama.getText().toString();
        String item = editBarang.getText().toString();
        String price = editHarga.getText().toString();
        String qty = editJumlah.getText().toString();
        String pay = editUang.getText().toString();

        textNama.setText("Nama Pembeli : " + name);
        textBarang.setText("Nama Barang : " + item);
        textHarga.setText("Harga per unit : " + price);
        textUang.setText("Uang diterima : " + pay);


        int payint = Integer.parseInt(pay);
        int qtyint = Integer.parseInt(qty);
        int priceint = Integer.parseInt(price);
        int tempTotal = qtyint * priceint;
        textTotal.setText("Total Harga : " + tempTotal);

        int change = payint - tempTotal;
        if(payint > tempTotal){
            textKembalian.setText("Total Kembalian : " + change);
        }else{
            textKembalian.setText("Total Kembalian : Tidak ada kembalian");
        }

    }
}