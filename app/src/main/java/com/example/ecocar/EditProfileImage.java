package com.example.ecocar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileImage extends AppCompatActivity implements View.OnClickListener {

    private Button save, close, change;
    private CircleImageView image;
    private Uri resultUri;

    private void uploadProfileImage () {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Configura tu perfil");
        progressDialog.setMessage("Por favor mantega en espera, estamos configurando tus dados");
        progressDialog.show();

        if (resultUri != null) {
            CurrentUser.getInstance().setPhotoURL(resultUri.toString());
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            DatabaseReference UsersRef = mDatabase.child("users").child(CurrentUser.getInstance().getUid());
            UsersRef.child("photoURL").setValue(resultUri.toString()).addOnCompleteListener(task -> {
                progressDialog.dismiss();
                Intent intent = new Intent();
                setResult(Activity.RESULT_OK, intent);
                finish();
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_image);
        setTitle("Edita tu foto");

        save = findViewById(R.id.EditProfile_save);
        close = findViewById(R.id.EditProfile_close);
        change = findViewById(R.id.EditProfile_change);
        image = findViewById(R.id.EditProfile_image);

        if (!CurrentUser.getInstance().getPhotoURL().equals("")) image.setImageURI(Uri.parse(CurrentUser.getInstance().getPhotoURL()));

        save.setOnClickListener(this);
        close.setOnClickListener(this);
        change.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.EditProfile_save:
                uploadProfileImage();
                break;
            case R.id.EditProfile_close:
                finish();
                break;
            case R.id.EditProfile_change:
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                Log.i("awe", "asd: " + result.getOriginalUri().toString());
                image.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, result.getError().toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
}