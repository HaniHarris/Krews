package com.krews.krews;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback, Camera.PictureCallback {

    ConstraintLayout constraintCam;
    SurfaceView surfaceView;
    LinearLayout layImage;
    ImageView ivImage,ivCapture;
    Button btnSave;
    SurfaceHolder surfaceHolder;
    Camera camera;
    private static final int PERMISSION_REQUEST_CODE = 101;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        constraintCam=findViewById(R.id.constraintCam);
        surfaceView=findViewById(R.id.surfaceView);
        layImage=findViewById(R.id.layImage);
        ivImage=findViewById(R.id.ivImage);
        ivCapture=findViewById(R.id.ivCapture);
        btnSave=findViewById(R.id.btnSave);

        constraintCam.setOnTouchListener(new OnSwipeTouchListener(CameraActivity.this) {
            public void onSwipeLeft() {
                onBackPressed();
            }

        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final Dialog alertDialog = new Dialog(CameraActivity.this);
                    alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    if(alertDialog.getWindow()!=null){
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));}
                    alertDialog.setContentView(R.layout.alert_dialog);
                    alertDialog.setCancelable(false);
                    alertDialog.setCanceledOnTouchOutside(false);

                    TextView tvTitle = alertDialog.findViewById(R.id.tvTitle);
                    tvTitle.setText(getString(R.string.save1));

                    TextView tvMessage = alertDialog.findViewById(R.id.tvMessage);
                    tvMessage.setText(getString(R.string.save2));

                    TextView btnYes = alertDialog.findViewById(R.id.btnYes);
                    btnYes.setText(R.string.ok);

                    final TextView btnNo = alertDialog.findViewById(R.id.btnNo);
                    btnNo.setText(R.string.cancel);

                    btnYes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try
                            {
                                alertDialog.dismiss();
                                if (checkPermissions())
                                {
                                    setupSurfaceHolder();
                                }
                                else
                                {
                                    requestPermission();
                                }

                            } catch (Exception ignored) {
                            }
                        }
                    });

                    btnNo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                alertDialog.dismiss();

                            } catch (Exception ignored) {
                            }
                        }
                    });

                    alertDialog.show();
                } catch (Exception ignored) {
                }
            }
        });

        if(surfaceView!=null)
        {
            if (checkPermissions())
            {
                setupSurfaceHolder();
            }
            else
            {
                requestPermission();
            }
        }

        ivCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try
                {
                    if (checkPermissions())
                    {
                        captureImage();
                    }
                    else
                    {
                        requestPermission();
                    }
                }
                catch (Exception ignored)
                {

                }
            }
        });
    }

    private boolean checkPermissions() {
        int result0 = ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA);
        return result0 == PackageManager.PERMISSION_GRANTED;

    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(CameraActivity.this,new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0) {

                boolean camAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                if (camAccepted)
                {
                    setupSurfaceHolder();
                }
                else
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA))
                        {
                            permissionAlert("1");
                        }
                        else
                        {
                            permissionAlert("2");
                        }
                    }

                }
            }
        }
    }

    private void permissionAlert(final String type) {
        try {
            final Dialog alertDialog = new Dialog(this);
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if(alertDialog.getWindow()!=null){
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));}
            alertDialog.setContentView(R.layout.alert_dialog);
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);

            TextView tvTitle = alertDialog.findViewById(R.id.tvTitle);
            tvTitle.setText(getString(R.string.permisiondenied1));

            TextView tvMessage = alertDialog.findViewById(R.id.tvMessage);
            tvMessage.setText(getString(R.string.permisiondenied2));

            TextView btnYes = alertDialog.findViewById(R.id.btnYes);
            btnYes.setText(R.string.ok);

            final TextView btnNo = alertDialog.findViewById(R.id.btnNo);
            btnNo.setText(R.string.cancel);

            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        alertDialog.dismiss();
                        if(type.equalsIgnoreCase("1"))
                        {
                            ActivityCompat.requestPermissions(CameraActivity.this,new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
                        }
                        else
                        {
                            Intent i=new Intent();
                            i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri= Uri.fromParts("package",getPackageName(),null);
                            i.setData(uri);
                            startActivity(i);

                        }
                    } catch (Exception ignored) {
                    }
                }
            });

            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        alertDialog.dismiss();
                    } catch (Exception ignored) {
                    }
                }
            });

            alertDialog.show();
        } catch (Exception ignored) {
        }
    }

    private void setupSurfaceHolder()
    {
        surfaceView.setVisibility(View.VISIBLE);
        ivCapture.setVisibility(View.VISIBLE);
        layImage.setVisibility(View.GONE);
        btnSave.setVisibility(View.GONE);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
    }

    public void captureImage() {
        if (camera != null) {
            camera.takePicture(null, null, this);
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        startCamera();
    }

    private void startCamera() {
        camera = Camera.open();
        camera.setDisplayOrientation(90);
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        }
        catch (Exception ignored)
        {

        }
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        resetCamera();
    }

    public void resetCamera() {
        if (surfaceHolder.getSurface() == null) {
            return;
        }

        if (camera != null) {
            camera.stopPreview();
            try {
                camera.setPreviewDisplay(surfaceHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            camera.startPreview();
        }
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        releaseCamera();
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
        try
        {
            Bitmap imageBitmap= rotateImage(BitmapFactory.decodeByteArray(bytes, 0, bytes.length),90);
            showImage(imageBitmap);

            resetCamera();
        }
        catch (Exception ignored)
        {
            Snackbar snackbar=Snackbar.make(constraintCam,"Something Went Wrong. Try Again Later.",Snackbar.LENGTH_LONG);
            View sbView=snackbar.getView();
            sbView.setBackgroundResource(R.color.colorPrimaryDark);
            snackbar.show();
        }
    }

    public static Bitmap rotateImage(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix mtx = new Matrix();
        mtx.setRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    private void showImage(Bitmap imageBitmap)
    {
        surfaceView.setVisibility(View.GONE);
        ivCapture.setVisibility(View.GONE);
        layImage.setVisibility(View.VISIBLE);
        btnSave.setVisibility(View.VISIBLE);

        ivImage.setImageBitmap(imageBitmap);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in1,R.anim.slide_out1);
    }
}