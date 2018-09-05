package com.nassaty.hireme.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nassaty.hireme.R;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class StorageUtils {

    private FirebaseStorage firebaseStorage;
    public static Dialog mDialog;
    public static ProgressDialog mProgressDialog;
    private static StorageReference collectionRef, profile_picRef;
    private UploadTask mUploadTask;
    private AuthUtils authUtils;
    StorageReference storageRef;

    public StorageUtils(Context context) {
        this.firebaseStorage = FirebaseStorage.getInstance();
        this.authUtils = new AuthUtils(context);
        this.storageRef = firebaseStorage.getReference();
    }

    public static String getPath(Context context, Uri uri){
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String path = cursor.getString(columnIndex);
        cursor.close();
        return path;
    }

    public static void showDialog(Context context){
        mDialog = new Dialog(context, R.style.AppTheme);
        mDialog.addContentView(new ProgressBar(context),
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        mDialog.setCancelable(true);
        mDialog.show();

    }

    public void dismissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public void initProgressDialog(Context context) {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(context.getString(R.string.placeholder_loading));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMax(100);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }

    public static void setProgress(int i) {
        mProgressDialog.setProgress(i);
    }

    public static void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public interface onImageUploadListener{
        void isUploaded(Boolean state);
    }

    public interface onProgress{
        void progress(double val);
    }

    public void uploadImage(CircleImageView imageView, String title, final onImageUploadListener onImageUploadListener, final onProgress progress){

        collectionRef = storageRef.child("images");
        profile_picRef = collectionRef.child(authUtils.getCurrentUser().getUid()).child(title);

        mUploadTask = profile_picRef.putBytes(convertToBytes(imageView));
        mUploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                onImageUploadListener.isUploaded(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onImageUploadListener.isUploaded(false);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double count = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progress.progress(count);
            }
        });
    }

    public static byte[] convertToBytes(CircleImageView imageView){
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        return data;
    }

    public StorageReference getUserProfileImageRef(String uid, String title){
        collectionRef =  storageRef.child("images");
        profile_picRef = collectionRef.child(uid).child(title);

        return profile_picRef;
    }

    public void downloadUserImage(Context context, CircleImageView imageView, String uid, String title){
        Glide.with(context)
                .load(getUserProfileImageRef(uid, title))
                .into(imageView);
    }


}
