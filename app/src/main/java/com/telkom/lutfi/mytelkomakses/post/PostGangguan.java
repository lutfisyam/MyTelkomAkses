package com.telkom.lutfi.mytelkomakses.post;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.telkom.lutfi.mytelkomakses.R;
import com.telkom.lutfi.mytelkomakses.model.Order;
import com.telkom.lutfi.mytelkomakses.model.User;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Iterator;

import javax.annotation.Nullable;
import javax.net.ssl.HttpsURLConnection;


public class PostGangguan extends AppCompatActivity {
    private ProgressDialog progress;

    private FirebaseFirestore mFireStore;

    Button button;
    public String sc, name, alamat, kontak, ncli, ndem, alproname, nonin, notic, jenisgg;
    public Date waktuse, waktumu, tgl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alret_pirnt_repot);
        mFireStore = FirebaseFirestore.getInstance();
        final Query query = mFireStore.collection("order");

        button = (Button) findViewById(R.id.print);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            Order model = doc.toObject(Order.class);
                            notic = model.getNo_tiket();
                            nonin = model.getNo_internet();
                            name = model.getNama();
                            alamat = model.getAlamat();
                            kontak = model.getKontak();
                            jenisgg = model.getJenis_gangguan();
                            tgl = model.getTgl();
                            waktuse = model.getWaktuselesai();
                            waktumu = model.getWaktumulai();
                            Toast.makeText(PostGangguan.this, name, Toast.LENGTH_LONG).show();
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }

                            new SendRequest().execute();
                        }
                    }
                });
            }

        });

    }


    public class SendRequest extends AsyncTask<String, Void, String> {


        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("https://script.google.com/macros/s/AKfycbxOnyXkl9ZaZdQBBnm3ruMEVG4DIItJSAP2_tGelsfb3at8xyI/exec");
                // https://script.google.com/macros/s/AKfycbyuAu6jWNYMiWt9X5yp63-hypxQPlg5JS8NimN6GEGmdKZcIFh0/exec
                JSONObject postDataParams = new JSONObject();

                //int i;
                //for(i=1;i<=70;i++)


                //    String usn = Integer.toString(i);

                String id = "187X8i6BNqUgetyH87v2ddHrZFjvPAGHz8RE_lbyerjA";

                postDataParams.put("no_tiket", notic);
                postDataParams.put("no_internet", nonin);
                postDataParams.put("nama", name);
                postDataParams.put("alamat", alamat);
                postDataParams.put("kontak", kontak);
                postDataParams.put("jenis_gangguan", ndem);
                postDataParams.put("tgl", tgl);
                postDataParams.put("waktumulai", waktumu);
                postDataParams.put("waktuselesai", waktuse);
                postDataParams.put("id", id);


                Log.e("params", postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();

        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}
