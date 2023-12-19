package com.example.penjualan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText etNamaPelanggan;
    EditText etNamaBarang;
    EditText etJmlBarang;
    EditText etHarga;
    EditText etUangBayar;

    TextView tvNamaPembeli;
    TextView tvNamaBarang;
    TextView tvJmlBarang;
    TextView tvHarga;
    TextView tvUangBayar;
    TextView tvTotal;
    TextView tvKembalian;
    TextView tvBonus;
    TextView tvKeterangan;

    Button btnProses;
    Button btnHapus;
    Button btnKeluar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnProses = findViewById(R.id.btnProses);
        btnHapus = findViewById(R.id.btnHapus);
        btnKeluar = findViewById(R.id.btnKeluar);


        etNamaPelanggan = findViewById(R.id.etNamaPelanggan);
        etNamaBarang = findViewById(R.id.etNamaBarang);
        etJmlBarang = findViewById(R.id.etJmlBarang);
        etHarga = findViewById(R.id.etHarga);
        etUangBayar = findViewById(R.id.etUangBayar);

        tvNamaPembeli = findViewById(R.id.tvNamaPembeli);
        tvNamaBarang = findViewById(R.id.tvNamaBarang);
        tvJmlBarang = findViewById(R.id.tvJmlBarang);
        tvHarga = findViewById(R.id.tvHarga);
        tvUangBayar = findViewById(R.id.tvUangBayar);
        tvTotal = findViewById(R.id.tvTotal);
        tvKembalian = findViewById(R.id.tvKembalian);
        tvBonus = findViewById(R.id.tvBonus);
        tvKeterangan = findViewById(R.id.tvKeterangan);

        btnProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String NamaPembeli = etNamaPelanggan.getText().toString();
                String NamaBarang = etNamaBarang.getText().toString();
                int JmlBarang = Integer.parseInt(etJmlBarang.getText().toString());
                int Harga = Integer.parseInt(etHarga.getText().toString());
                int UangBayar = Integer.parseInt(etUangBayar.getText().toString());
                double Total = JmlBarang * Harga;
                double Kembalian = UangBayar - (JmlBarang * Harga);

                tvNamaPembeli.setText("Nama Pembeli: " + NamaPembeli);
                tvNamaBarang.setText("Nama Barang: " + NamaBarang);
                tvJmlBarang.setText("Jumlah Barang : " + JmlBarang);
                tvHarga.setText("Harga Barang: Rp." + Harga);
                tvUangBayar.setText("Uang Bayar: Rp." + UangBayar);
                tvTotal.setText("Total Belanja: Rp." + Total);
                tvKembalian.setText("Uang Kembalian: Rp." + Kembalian);
                tvBonus.setText("Bonus : SSD NVME GEN 4.0 1TB");
                tvKeterangan.setText("Keterangan : Tunggu Kembalian");
            }
        });

        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tvNamaPembeli.setText("");
                tvNamaBarang.setText("");
                tvJmlBarang.setText("");
                tvHarga.setText("");
                tvUangBayar.setText("");
                tvTotal.setText("");
                tvKembalian.setText("");
                tvBonus.setText("");
                tvKeterangan.setText("");
            }
        });
        btnKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}