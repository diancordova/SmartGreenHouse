package com.cordova.smartgreenhouse.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;


import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import com.cordova.smartgreenhouse.Models.mPlant;
import com.cordova.smartgreenhouse.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

import static android.text.TextUtils.isEmpty;

public class PlantActivity extends AppCompatActivity {
    private Button btnAdd, btnBack;
    private ImageView img;
    String key;
    private DatabaseReference database;
    private EditText inputNamePlant, inputFirstValue, inputSecondValue;
    private StorageReference storageReference;
    private ProgressDialog pDialog;
    private int PICK_IMAGE_REQUEST = 1;
    private String url;
    private Uri  filePath;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant);
        setTitle("Add Data");
        // inisialisasi fields EditText dan Button
        inputNamePlant = (EditText) findViewById(R.id.name_plant);
        inputFirstValue = (EditText) findViewById(R.id.value_first);
        inputSecondValue = (EditText) findViewById(R.id.value_second);
        btnAdd = findViewById(R.id.btnAdd);
        btnBack = findViewById(R.id.btnBack);
        img = findViewById(R.id.img);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // mengambil referensi ke Firebase Database
        database = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        // kode yang dipanggil ketika tombol Submit diklik
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isEmpty(inputNamePlant.getText().toString()) && !isEmpty(inputFirstValue.getText().toString()) && !isEmpty(inputSecondValue.getText().toString()))

                    submitPlant();

                else
                    Snackbar.make(findViewById(R.id.btnAdd), "Data can't be Empty", Snackbar.LENGTH_LONG).show();

                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(
                        inputNamePlant.getWindowToken(), 0);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        UserActivityDrawer.class);
                startActivity(i);
                finish();
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

    }

    private void submitPlant() {
        key = database.push().getKey();
        if(bitmap==null){
            addPlant();
        }else{
            changePhoto();
        }


    }

    private void addPlant(){

        mPlant pl =  new mPlant(inputNamePlant.getText().toString(), inputFirstValue.getText().toString(), inputSecondValue.getText().toString(), key, url);
        database.child("plant").child(key).setValue(pl).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                inputNamePlant.setText("");
                inputFirstValue.setText("");
                inputSecondValue.setText("");
                Snackbar.make(findViewById(R.id.btnAdd), "Data berhasil ditambahkan", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public static Intent getActIntent(Activity activity) {
        // kode untuk pengambilan Intent
        return new Intent(activity, PlantActivity.class);
    }
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(PlantActivity.this, UserActivityDrawer.class);
        startActivity(i);
    }

    private void changePhoto(){
        final StorageReference riversRef = storageReference.child("tumbuhan/"+key+".jpg");
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




    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                img.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }



}

