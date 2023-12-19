package com.example.jsonparser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainActivity extends AppCompatActivity {
    private ListView lv;
    ArrayList<HashMap<String, String>> contactList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        contactList = new ArrayList<>();
        lv = findViewById(R.id.listView);

        ExecutorService service = Executors.newSingleThreadExecutor();

        Future<Integer> status = service.submit(() -> {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and get a response
            String url = "https://gist.githubusercontent.com/Baalmart/8414268/raw/83e5ea9d1d88c765712f9392aa31c45e889f4f09/contacts";
            String jsonstr = sh.makeServiceCall(url);
            Log.e("Main", "Response from url: " + jsonstr);
            if (jsonstr != null){
                try {
                    JSONObject jsonobj = new JSONObject(jsonstr);
                    // Get JSONArray node
                    JSONArray contacts = jsonobj.getJSONArray("contacts");
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String id = c.getString("id");
                        String name = c.getString("name");
                        String email = c.getString("email");
                        String gender = (c.getString("gender").equals("male")) ? "Laki-laki" : "Perempuan";
                        JSONObject phone = c.getJSONObject("phone");
                        String p_mobile = phone.getString("mobile");
                        // tmp hashmap for single contact
                        HashMap<String, String> contact = new HashMap<>();
                        // adding each child node to hashmap
                        contact.put("id", id);
                        contact.put("name", name);
                        contact.put("email", email);
                        contact.put("gender", gender);
                        contact.put("mobile", p_mobile);
                        contactList.add(contact);
                    }
                }catch (final JSONException e){
                    Log.e("Main", "JSON Parsing Error: " + e.getMessage());
                    runOnUiThread(() ->
                            Toast.makeText(MainActivity.this, "JSON Parsing Errror", Toast.LENGTH_LONG).show()
                    );
                }
            }else {
                Log.e("Main", "Couldn't get JSON from Server");
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "Couldn't get JSON from Server. Check Logcat for possible errors!", Toast.LENGTH_SHORT).show();
                });
                return 0;
            }
            return 1;
        });
        try {
            if (status.get() == 1) {
                ListAdapter adapter = new SimpleAdapter(MainActivity.this, contactList, R.layout.item_list,
                        new String[]{"id", "name", "email", "gender", "mobile"}, new int[]{R.id.id, R.id.nama, R.id.email, R.id.gender, R.id.mobile});
                lv.setAdapter(adapter);
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}