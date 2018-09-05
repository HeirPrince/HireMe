package com.nassaty.hireme.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nassaty.hireme.R;
import com.nassaty.hireme.model.User;
import com.nassaty.hireme.utils.AuthUtils;
import com.nassaty.hireme.utils.Constants;
import com.nassaty.hireme.utils.StorageUtils;

import java.io.IOException;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.media.MediaRecorder.VideoSource.CAMERA;

public class SetInfo extends AppCompatActivity {

    private static final int GALLERY = 100;
    CircleImageView profile;
    EditText username;
    ProgressBar progressBar;
    TextView status;
    User user;

    FirebaseFirestore firebaseFirestore;
    private StorageUtils storageUtils;
    AuthUtils authUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_info);
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageUtils = new StorageUtils(this);

        profile = findViewById(R.id.profile_space);
        username = findViewById(R.id.username);
        progressBar = findViewById(R.id.horizontal_progress_bar);
        status = findViewById(R.id.progress_status);
        user = new User();

        authUtils = new AuthUtils(this);

    }

    public void registerUSer(View view) {

        String imageTitle = String.valueOf(new Random().nextInt());
        
        user.setEmail(authUtils.getCurrentUser().getEmail());
        user.setPhone_number(authUtils.getCurrentUser().getPhoneNumber());
        user.setUID(authUtils.getCurrentUser().getUid());
        user.setUser_name(username.getText().toString());
        user.setImageTitle(imageTitle);


            storageUtils.uploadImage(profile, imageTitle, new StorageUtils.onImageUploadListener() {
                @Override
                public void isUploaded(Boolean state) {
                    if (state) {
                        insertNewUser(user);
                    } else {
                        initProgress(false, 0);
                    }
                }
            }, new StorageUtils.onProgress() {
                @Override
                public void progress(double val) {
                    initProgress(true, val);
                }
            });


    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Pick from Gallery",
                "Capture Photo"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallery();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    profile.setImageBitmap(bitmap);
                    profile.setScaleType(ImageView.ScaleType.CENTER_CROP);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(SetInfo.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            profile.setImageBitmap(thumbnail);
        }
    }

    public void upload(View view) {
        showPictureDialog();
    }

    //TODO progress not loading soln = use firebase storage directly
    public void initProgress(boolean state, double val) {
        if (state && progressBar != null) {
            status.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            status.setText(String.valueOf(val));
            progressBar.setProgress((int) val);
        } else {
            status.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
    }

    public void insertNewUser(User user) {
        CollectionReference userRef = firebaseFirestore.collection(Constants.userRef);
        userRef.add(user)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        finish();
                        startActivity(new Intent(SetInfo.this, MainActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SetInfo.this, "error creating profile", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
