package com.cordova.smartgreenhouse.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cordova.smartgreenhouse.Models.mPlant;
import com.cordova.smartgreenhouse.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;


public class EditPlant extends AppCompatActivity {
    private DatabaseReference databaseReference;
    String url;
    String uId;
    private EditText inputNamePlant,inputFirstValue,inputSecondValue;
    private ImageView image;
    private Button btnEditPlant;
    private DatabaseReference database;
    private int PICK_IMAGE_REQUEST = 1;
    private ProgressDialog pDialog;
    private StorageReference storageReference;
    private Uri  filePath;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i = getIntent();
        String nama =  i.getStringExtra("nama");
        String value1 =  i.getStringExtra("value1");
        String value2 =  i.getStringExtra("value2");
        url = i.getStringExtra("url");
        uId = i.getStringExtra("uId");

        database = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();


        Toast.makeText(this, nama, Toast.LENGTH_SHORT).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_plant);

        inputNamePlant = findViewById(R.id.name);
        inputFirstValue = findViewById(R.id.nilaiPH);
        inputSecondValue = findViewById(R.id.status);
        btnEditPlant = findViewById(R.id.btnEditPlant);
        image = findViewById(R.id.img);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        inputNamePlant.setText(nama);
        inputFirstValue.setText(value1);
        inputSecondValue.setText(value2);
        Glide.with(getApplicationContext()).load(url).placeholder(R.drawable.loading).into(image);

        setTitle("Edit Plant");
        btnEditPlant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bitmap==null){
                    addPlant();
                }else{
                    changePhoto();
                }
                Snackbar.make(findViewById(R.id.btnEditPlant), "Data succesfully edit", Snackbar.LENGTH_LONG).show();
//                onBackPressed();
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showFileChooser();
            }
        });



    }
    private void addPlant(){
        showDialog();
        mPlant pl =  new mPlant(inputNamePlant.getText().toString(), inputFirstValue.getText().toString(), inputSecondValue.getText().toString(), uId, url);
        database.child("plant").child(uId).setValue(pl).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Snackbar.make(findViewById(R.id.btnEditPlant), "Edit Sukses", Snackbar.LENGTH_LONG).show();
                hideDialog();
            }
        });
    }
    private void changePhoto(){
        final StorageReference riversRef = storageReference.child("tumbuhan/"+uId+".jpg");
        showDialog();

        riversRef.putFile(filePath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()){
                    throw task.getException();
                }
                return riversRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    Uri downUri = task.getResult();
                    url =  downUri.toString();
                    addPlant();
                    hideDialog();

                }
            }
        });

    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
