//package com.queen.finalspace.ui;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import androidx.core.content.FileProvider;
//
//import android.Manifest;
//import android.content.ActivityNotFoundException;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.icu.text.SimpleDateFormat;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.queen.finalspace.R;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Date;
//
//public class CameraActivity extends AppCompatActivity {
//    private ImageView imageView;
//    private Button btOpen;
//    private TextView textView;
//    static final int REQUEST_IMAGE_CAPTURE = 1;
//    String currentPhotoPath;
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_camera);
//
//        imageView = findViewById(R.id.captureImage);
//        btOpen = findViewById(R.id.btopen);
//
////        if (ContextCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.CAMERA) !=
////        PackageManager.PERMISSION_GRANTED){
////            ActivityCompat.requestPermissions(CameraActivity.this, new String[]{
////                    Manifest.permission.CAMERA
////            },
////                    100);
////        }
//
//        btOpen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if (intent.resolveActivity(getPackageManager()) != null){
//                    File photoFile = null;
//                }
//                try {
//                    photoFile = createImageFile();
//
//                }catch (IOException e) {
//
//                }
//                if (photoFile != null){
//                    Uri photoURI = File.provider.getUriForFile(this, "com.queen.finalspace.provider", photoFile);
//
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
//                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
//                }
//            }
//        });
//    }
//
//    private File createImageFile() {
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "Episode_JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(storageDir, imageFileName + ".jpg", storageDir);
//        String currentPhotoPath = image.getAbsolutePath();
//        return image;
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if (requestCode == 100){
//            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
//            imageView.setImageBitmap(captureImage);
//        }
//    }
//
//    private void galleryAddPic() {
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File(currentPhotoPath);
//        Uri contentUri = Uri.fromFile(f);
//        mediaScanIntent.setData(contentUri);
//        this.sendBroadcast(mediaScanIntent);
//    }
//
//    private void setPic() {
//        int targetW = imageView.getWidth();
//        int targetH = imageView.getHeight();
//
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        bmOptions.inJustDecodeBounds = true;
//    }
//
//}