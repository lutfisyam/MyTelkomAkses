package com.telkom.lutfi.mytelkomakses.post;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.telkom.lutfi.mytelkomakses.R;
import com.telkom.lutfi.mytelkomakses.TeamLeaderOrderTeknisi.MenuOrderTeknisi;
import com.telkom.lutfi.mytelkomakses.model.Order;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.annotation.Nullable;
import javax.net.ssl.HttpsURLConnection;


public class PostData extends AppCompatActivity {
    private ProgressDialog progress;

    private FirebaseFirestore mFireStore;

    Button button;
    TextView selisihTgl;
    public String sc, name, alamat, kontak, ncli, ndem, alproname;
    public Date tgl, waktuse, waktumu;

    private Spinner bulanSpinner;
    private EditText tahunInput;

    private ArrayList<String> daftarBulan = new ArrayList<>();

    private String bulanDipilih;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_pirnt_repot);
        mFireStore = FirebaseFirestore.getInstance();
        final Query query = mFireStore.collection("order").whereEqualTo("jenis", "Pasang Baru");

        selisihTgl = findViewById(R.id.selisih_tgl);
        bulanSpinner = findViewById(R.id.bulan);
        tahunInput = findViewById(R.id.tahun);
        button = findViewById(R.id.print);

        daftarBulan.add("Semua");
        daftarBulan.add("Januari");
        daftarBulan.add("Februari");
        daftarBulan.add("Maret");
        daftarBulan.add("April");
        daftarBulan.add("Mei");
        daftarBulan.add("Juni");
        daftarBulan.add("Juli");
        daftarBulan.add("Agustus");
        daftarBulan.add("September");
        daftarBulan.add("Oktober");
        daftarBulan.add("November");
        daftarBulan.add("Desember");
        ArrayAdapter<String> bulanAdapter = new ArrayAdapter<>(PostData.this, R.layout.support_simple_spinner_dropdown_item, daftarBulan);
        bulanSpinner.setAdapter(bulanAdapter);
        bulanSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String bulan_ini = (String) DateFormat.format("MM", new Date());
                String tahun_ini = (String) DateFormat.format("yyyy", new Date());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tahunInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    Toast.makeText(PostData.this, "Tahun : " + tahunInput.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedBulanSpinner = String.valueOf(bulanSpinner.getSelectedItemPosition());
                if (selectedBulanSpinner.length() == 1) {
                    selectedBulanSpinner = "0" + selectedBulanSpinner;
                }
                // SEMUA
                if (tahunInput.getText().toString().isEmpty() && selectedBulanSpinner.equals("00")) {
                    Toast.makeText(PostData.this, "Print Semua record", Toast.LENGTH_SHORT);
                    query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            assert queryDocumentSnapshots != null;
                            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                Order model = doc.toObject(Order.class);
                                sc = model.getSc();
                                name = model.getNama();
                                alamat = model.getAlamat();
                                kontak = model.getKontak();
                                ncli = model.getNcli();
                                ndem = model.getNdem();
                                alproname = model.getAlproname();
                                tgl = model.getTgl();
                                waktuse = model.getWaktuselesai();
                                waktumu = model.getWaktumulai();
                                new SendRequest().execute();
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }

                            }
                        }

                    });
                    Intent intent = new Intent(PostData.this, MenuOrderTeknisi.class);
                    startActivity(intent);
                    finish();
                } else {
                    // TERFILTER
                    query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            assert queryDocumentSnapshots != null;
                            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                Order model = doc.toObject(Order.class);
                                tgl = model.getTgl();
                                String bulan_laporan = (String) DateFormat.format("MM", tgl);
                                String tahun_laporan = (String) DateFormat.format("yyyy", tgl);
                                String selectedBulanSpinner = String.valueOf(bulanSpinner.getSelectedItemPosition());
                                if (selectedBulanSpinner.length() == 1) {
                                    selectedBulanSpinner = "0" + selectedBulanSpinner;
                                }
                                if (bulan_laporan.equals(selectedBulanSpinner) && tahun_laporan.equals(tahunInput.getText().toString())) {
                                    sc = model.getSc();
                                    name = model.getNama();
                                    alamat = model.getAlamat();
                                    kontak = model.getKontak();
                                    ncli = model.getNcli();
                                    ndem = model.getNdem();
                                    alproname = model.getAlproname();
                                    tgl = model.getTgl();
                                    waktuse = model.getWaktuselesai();
                                    waktumu = model.getWaktumulai();
                                    new SendRequest().execute();
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            }
                        }

                    });
                    Intent intent = new Intent(PostData.this, MenuOrderTeknisi.class);
                    startActivity(intent);
                    finish();
                }

            }

        });

    }


    public class SendRequest extends AsyncTask<String, Void, String> {


        protected void onPreExecute() {
        }

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("https://script.google.com/macros/s/AKfycbxglKQPeV9VSwMpKTqxrXVQDYd-MnSqSsD5uLMEMBrMVZ4HRYM/exec");
                // https://script.google.com/macros/s/AKfycbyuAu6jWNYMiWt9X5yp63-hypxQPlg5JS8NimN6GEGmdKZcIFh0/exec
                JSONObject postDataParams = new JSONObject();

                //int i;
                //for(i=1;i<=70;i++)


                //    String usn = Integer.toString(i);

                String id = "187X8i6BNqUgetyH87v2ddHrZFjvPAGHz8RE_lbyerjA";

                postDataParams.put("sc", sc);
                postDataParams.put("nama", name);
                postDataParams.put("alamat", alamat);
                postDataParams.put("kontak", kontak);
                postDataParams.put("ncli", ncli);
                postDataParams.put("ndem", ndem);
                postDataParams.put("alproname", alproname);
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
