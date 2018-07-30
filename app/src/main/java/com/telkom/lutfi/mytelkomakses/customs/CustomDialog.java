package com.telkom.lutfi.mytelkomakses.customs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.telkom.lutfi.mytelkomakses.R;

import java.util.HashMap;
import java.util.Map;

public class CustomDialog {

    private Context context;
    private Dialog dialog;
    private CustomToast toast;
    private ProgressDialog progressDialog;

    public CustomDialog() {
    }

    public CustomDialog(Context context) {
        this.context = context;
        this.dialog = new Dialog(context);
        toast = new CustomToast(context);
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Loading");
    }

    public void showEditTeknisi(final String UID, String nip, String nama, final String email, final String pass) {
        dialog.setContentView(R.layout.dialog_edit_teknisi);
        EditText nipEdit = dialog.findViewById(R.id.nip);
        final EditText namaEdit = dialog.findViewById(R.id.nama);
        final EditText emailEdit = dialog.findViewById(R.id.email);
        final EditText passEdit = dialog.findViewById(R.id.pass);
        Button simpanBtn = dialog.findViewById(R.id.simpan);
        Button batalBtn = dialog.findViewById(R.id.batal);

//        menampilkan ke layout
        nipEdit.setText(nip);
        nipEdit.setEnabled(false);
        namaEdit.setText(nama);
        emailEdit.setText(email);
        passEdit.setText(pass);

        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        simpanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
//                mengambil data dari auth
                String oldEmail = email;
                String oldPass = pass;
                final String nama = namaEdit.getText().toString();
                final String email = emailEdit.getText().toString();
                final String pass = passEdit.getText().toString();

                if (!nama.isEmpty() && !email.isEmpty() && !pass.isEmpty()) {
                    firebaseAuth.signInWithEmailAndPassword(oldEmail, oldPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                        prosses update email
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final Task<AuthResult> userTask = task;
                                userTask.getResult().getUser().updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    prosses update password
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            userTask.getResult().getUser().updatePassword(pass).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                input email & pass baru
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Map<String, Object> map = new HashMap<>();
                                                        map.put("nama", nama);
                                                        map.put("email", email);
                                                        map.put("pass", pass);
                                                        firestore.collection("user").document(UID).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    dialog.dismiss();
                                                                    progressDialog.dismiss();
                                                                    toast.showToast("Berhasil update");
                                                                } else {
                                                                    progressDialog.dismiss();
                                                                    toast.showToast("Gagal update");
                                                                }
                                                            }
                                                        });
                                                    } else {
                                                        progressDialog.dismiss();
                                                        toast.showToast("Password tidak valid");
                                                    }
                                                }
                                            });
                                        } else {
                                            progressDialog.dismiss();
                                            Log.d("ERROR UPDATE", "KENAPA YA" + String.valueOf(task.getException()));
                                            toast.showToast("Email tidak valid");
                                        }
                                    }
                                });
                            } else {
                                progressDialog.dismiss();
                                toast.showToast("Ada yang tidak beres");
                            }
                        }
                    });
                } else {
                    progressDialog.dismiss();
                    toast.showToast("Tidak boleh ada yang kosong");
                }
            }
        });

        batalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
