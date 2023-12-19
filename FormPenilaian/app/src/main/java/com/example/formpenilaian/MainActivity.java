package com.example.formpenilaian;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button tambahdata, hapusdata, updatedata;
    private SQLiteDatabase dbku;
    private ListView listView;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tambahdata = findViewById(R.id.tambahdata);
        hapusdata = findViewById(R.id.hapusdata);
        updatedata = findViewById(R.id.updatedata);
        listView = findViewById(R.id.listView);

        // Create or open the database
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
        dbku = dbHelper.getWritableDatabase();

        // Set up the adapter for the ListView
        String[] fromColumns = {"_id", "nama", "nilai", "grade"}; // Add "grade" column
        int[] toViews = {R.id.nrpText, R.id.namaText, R.id.nilaiText, R.id.gradeText}; // Add R.id.gradeText
        adapter = new SimpleCursorAdapter(this, R.layout.item_list, null, fromColumns, toViews, 0);
        listView.setAdapter(adapter);
        tambahdata.setOnClickListener(operasi);
        hapusdata.setOnClickListener(operasi);
        updatedata.setOnClickListener(operasi);
        updateAverageUI();
        updateListView();
    }

    @Override
    protected void onStop() {
        dbku.close();
        super.onStop();
    }

    View.OnClickListener operasi = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.tambahdata) {
                showAddDataDialog();
            }
            else if (v.getId() == R.id.hapusdata) {
                showDeleteDataDialog();
            }
            else if (v.getId() == R.id.updatedata) {
                showUpdateDataDialog();
            }
        }
    };

    private double calculateAverage() {
        String query = "SELECT AVG(nilai) AS average FROM mhs";
        Cursor cursor = dbku.rawQuery(query, null);

        double average = 0.0;
        if (cursor.moveToFirst()) {
            average = cursor.getDouble(cursor.getColumnIndex("average"));
        }
        cursor.close();

        return average;
    }

    private void updateAverageUI() {
        double average = calculateAverage();
        TextView averageTextView = findViewById(R.id.averageTextView);
        averageTextView.setText("Nilai Rata - Rata: " + String.format("%.2f", average));
    }

    private void showAddDataDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tambah Data");

        // Set up the layout for the dialog
        View view = getLayoutInflater().inflate(R.layout.tambah_dialog, null);
        builder.setView(view);

        final EditText nrpInput = view.findViewById(R.id.addnrp);
        final EditText namaInput = view.findViewById(R.id.addnama);
        final EditText nilaiInput = view.findViewById(R.id.addnilai);

        builder.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    // Get the data from the dialog
                    String nrpValue = nrpInput.getText().toString();
                    String namaValue = namaInput.getText().toString();
                    int nilaiValue = Integer.parseInt(nilaiInput.getText().toString());
                    String gradeValue = calculateGrade(nilaiValue);

                    // Insert data into the database
                    ContentValues dataku = new ContentValues();
                    dataku.put("nrp", nrpValue);
                    dataku.put("nama", namaValue);
                    dataku.put("nilai", nilaiValue);
                    dataku.put("grade", gradeValue);

                    dbku.insert("mhs", null, dataku);

                    // Update the ListView
                    updateAverageUI();
                    updateListView();


                    Toast.makeText(MainActivity.this, "Data Tersimpan. Grade: " + gradeValue, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    // Log any exceptions for debugging
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private String calculateGrade(int nilai) {
        if (nilai >= 81) {
            return "A";
        } else if (nilai >= 61 && nilai <= 80) {
            return "B";
        } else if (nilai >= 41 && nilai <= 60) {
            return "C";
        } else if (nilai >= 21 && nilai <= 40) {
            return "D";
        } else {
            return "E";
        }
    }

    private void showDeleteDataDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hapus Data");

        // Set up the layout for the dialog
        View view = getLayoutInflater().inflate(R.layout.hapus_dialog, null);
        builder.setView(view);

        final EditText nrpInput = view.findViewById(R.id.deletenrp);

        builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    // Get the data from the dialog
                    String nrpValue = nrpInput.getText().toString();

                    deleteData(nrpValue);

                    // Update the ListView
                    updateListView();
                    updateAverageUI();

                    Toast.makeText(MainActivity.this, "Data Berhasil Dihapus", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    // Log any exceptions for debugging
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void showUpdateDataDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Data");

        // Set up the layout for the dialog
        View view = getLayoutInflater().inflate(R.layout.update_dialog, null);
        builder.setView(view);

        final EditText nrpInput = view.findViewById(R.id.updatenrp);
        final EditText namaInput = view.findViewById(R.id.updatenama);
        final EditText nilaiInput = view.findViewById(R.id.updatenilai);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    // Get the data from the dialog
                    String nrpValue = nrpInput.getText().toString();
                    String namaValue = namaInput.getText().toString();
                    int nilaiValue = Integer.parseInt(nilaiInput.getText().toString());
                    String gradeValue = calculateGrade(nilaiValue); // Calculate grade

                    updateData(nrpValue, namaValue, String.valueOf(nilaiValue)); // Store nilai as text

                    // Update the ListView
                    updateListView();
                    updateAverageUI();

                    Toast.makeText(MainActivity.this, "Data Berhasil Diupdate", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    // Log any exceptions for debugging
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void updateData(String nrp, String nama, String nilai) {
        ContentValues dataku = new ContentValues();
        dataku.put("nama", nama);
        dataku.put("nilai", nilai);
        dbku.update("mhs", dataku, "nrp = ?", new String[]{nrp});
    }

    private void deleteData(String nrp) {
        // Perform the delete operation
        dbku.delete("mhs", "nrp = ?", new String[]{nrp});
    }

    private void updateListView() {
        Cursor cur = dbku.rawQuery("SELECT nrp as _id, nama, nilai, grade FROM mhs", null); // Add "grade" column
        adapter.changeCursor(cur);
    }

    public class MyDatabaseHelper extends SQLiteOpenHelper {
        public MyDatabaseHelper(Context context) {
            super(context, "db.sql", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table if not exists mhs(_id INTEGER PRIMARY KEY, nrp TEXT, nama TEXT, nilai INTEGER, grade TEXT);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Handle database schema upgrades here, if needed.
        }
    }
}