package com.telkom.lutfi.mytelkomakses;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    // Deklarasi variabel
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        // Logika untuk mengecek apakah sudah ter-login atau belum
        if (mAuth.getCurrentUser() != null) {
            // jika sudah maka ...
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }


        final EditText inputEmail = (EditText) findViewById(R.id.input_login_email);
        final EditText inputPass = (EditText) findViewById(R.id.input_login_password);
        Button btnLogin = (Button) findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString();
                String pass = inputPass.getText().toString();

                // validasi form login jika kosong
                if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)) {
                    progressDialog.setMessage("Prosessing...");
                    progressDialog.show();
                    // fungsi buatan
                    loginUser(email, pass);
                } else {
                    // jika kosong ....
                    Toast.makeText(LoginActivity.this, "Tolong diisi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    void loginUser(String email, String pass) {

        // fungsi bawaan untuk login
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            final String uid = task.getResult().getUser().getUid();

                            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                @Override
                                public void onSuccess(InstanceIdResult instanceIdResult) {
                                    FirebaseFirestore.getInstance().collection("user").document(uid)
                                            .update("token_id", instanceIdResult.getToken());
                                }
                            });

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                            finish();

                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Gagal login", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
