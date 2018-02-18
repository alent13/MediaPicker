package com.applexis.mediapicker;

import android.Manifest;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class JavaSampleActivity extends AppCompatActivity {

    private MediaPicker mediaPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        final TextView mediaType = findViewById(R.id.mediaType);
        final TextView mediaPath = findViewById(R.id.mediaPath);
        final ImageView picker = findViewById(R.id.picker);

        mediaPicker = new MediaPicker(this);

        mediaPicker.setDialog(new MPDialog.Builder(this)
                .add(MediaPicker.Companion.getCAMERA_PHOTO(), "Take a photo")
                .add(MediaPicker.Companion.getCAMERA_VIDEO(), "Make a video")
                .add(MediaPicker.Companion.getGALLERY_PHOTO(), "Photo from gallery")
                .add(MediaPicker.Companion.getGALLERY_VIDEO(), "Video from gallery")
                .build());

        picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TedPermission.with(JavaSampleActivity.this)
                        .setPermissionListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted() {
                                // After permission granted call show function with callback
                                mediaPicker.show(new Function2<Integer, String, Unit>() {
                                    @Override
                                    public Unit invoke(Integer integer, String s) {
                                        if(integer == MediaPicker.Companion.getCAMERA_PHOTO()) {
                                            mediaType.setText("Photo from camera");
                                        } else if(integer == MediaPicker.Companion.getGALLERY_PHOTO()) {
                                            mediaType.setText("Photo from gallery");
                                        } else if(integer == MediaPicker.Companion.getCAMERA_VIDEO()) {
                                            mediaType.setText("Video from camera");
                                        } else if(integer == MediaPicker.Companion.getGALLERY_VIDEO()) {
                                            mediaType.setText("Video from gallery");
                                        }
                                        mediaPath.setText(s);
                                        return null;
                                    }
                                });
                            }

                            @Override
                            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                                Toast.makeText(JavaSampleActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.RECORD_AUDIO)
                        .check();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mediaPicker.handleActivityResult(requestCode, resultCode, data);
    }
}
