package com.example.runningtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class UpdateActivity extends AppCompatActivity {

    private String id;
    private String time;
    private String distance;
    private String timeStart;
    private Uri imageUri;
    private TextView imgView;
    private RunningViewModel viewModel;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        TextView timeStartView;
        TextView distanceView;
        TextView timeView;

        Intent intent= getIntent();
        Bundle bundle = intent.getExtras();

        id = bundle.getString("id");
        distance = bundle.getString("distance");
        time = bundle.getString("time");
        timeStart = bundle.getString("timeStart");

        timeStartView = findViewById(R.id.updateDateTimeView);
        distanceView = findViewById(R.id.updateDistanceView);
        timeView = findViewById(R.id.updateDurationView);
        imgView = findViewById(R.id.updateImageView);


        timeStartView.setText(timeStart);
        distanceView.setText(distance+"km");
        timeView.setText(time);

        viewModel = new ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(RunningViewModel.class);

    }

    public void onClickedUpdate(View v){
        EditText edit = findViewById(R.id.enterInfoView);
        String info = edit.getText().toString();
        if(imageUri !=null){
            photoTofolder();
        }

        Running a = new Running(Integer.parseInt(id),Float.parseFloat(distance),time,info,getImageUri(),timeStart);

        viewModel.insert(a);
        finish();
    }

    public void onClickedDelete(View v){

        viewModel.delete(Integer.parseInt(id));
        finish();
    }

    public static final int PICK_IMAGE = 1;

    public void onClickedPhoto(View v){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }
    public String getImagePath() {
        return ((new ContextWrapper(getApplicationContext())).getDir("images", MODE_PRIVATE)).getAbsolutePath() + '/' + id + ".png";
    }

//    storng the selected image to the folder
    public void photoTofolder(){

            File filepath = new File(getImagePath());
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(filepath);
                MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri).compress(Bitmap.CompressFormat.PNG, 100, fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    public String getImageUri() {
        File file = new File(getImagePath());
        if (file.exists()){
            Uri getUri = Uri.fromFile(file);
            return getUri.toString();
        }
        return null;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            //TODO: action
            imageUri = data.getData();
            imgView.setText("*Image Added*");
        }
    }
}