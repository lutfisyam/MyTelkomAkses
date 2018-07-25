package com.telkom.lutfi.mytelkomakses;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class InputFinishReportActivity extends AppCompatActivity {

    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_finish_report);

        Button btn =(Button)findViewById(R.id.opencamera);
        img = (ImageView)findViewById(R.id.photoImage);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten =new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(inten,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = (Bitmap)data.getExtras().get("data");
        img.setImageBitmap(bitmap);
    }
}
