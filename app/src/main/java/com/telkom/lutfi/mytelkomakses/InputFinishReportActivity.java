package com.telkom.lutfi.mytelkomakses;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class InputFinishReportActivity extends AppCompatActivity {

    ImageView img;
    private Button uploadBtn;
    private ProgressDialog progressDialog;
    private StorageReference buktiStorageRef;
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore mFireStore;
    private String SCid, bukti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_finish_report);

        SCid = getIntent().getStringExtra("OTNIEL_KONTOL");

        firebaseStorage = FirebaseStorage.getInstance();
        buktiStorageRef = firebaseStorage.getReference().child("bukti");
        Button btn = (Button) findViewById(R.id.opencamera);
        uploadBtn = (Button) findViewById(R.id.upload_butki_btn);
        img = (ImageView) findViewById(R.id.photoImage);
        progressDialog = new ProgressDialog(InputFinishReportActivity.this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent();
                inten.setType("image/*");
                inten.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(inten, "Pilih Bukti"), 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        final Uri gambarURI = data.getData();
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), gambarURI);
            img.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (gambarURI != null) {
            uploadBtn.setEnabled(true);
            uploadBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    buktiStorageRef.child(String.valueOf(SCid + ".jpg"));
                    buktiStorageRef.putFile(gambarURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                buktiStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Map<String, Object> validMap = new HashMap<>();
                                        validMap.put("bukti", uri.toString());
                                        validMap.put("status", "selesai");
                                        validMap.put("waktuselesai", new Date());
                                        FirebaseFirestore.getInstance().collection("order").document(SCid)
                                                .update(validMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(InputFinishReportActivity.this, "Upload Sukses", Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }
                                        });
                                    }
                                });

                            }
                        }
                    });

                }
            });
        } else {
            Toast.makeText(InputFinishReportActivity.this, "Tidak ada bukti", Toast.LENGTH_SHORT).show();
        }
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Bukti", null);
        return Uri.parse(path);
    }
}
