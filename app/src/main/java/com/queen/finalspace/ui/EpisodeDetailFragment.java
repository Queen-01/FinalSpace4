package com.queen.finalspace.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.queen.finalspace.Constants;
import com.queen.finalspace.model.Episode;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Button;

import com.queen.finalspace.R;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EpisodeDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EpisodeDetailFragment extends Fragment {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 11;
    @BindView(R.id.episodeImageView) ImageView mImageView;
    @BindView(R.id.detailEpisodeName) TextView mEpisodeName;
    @BindView(R.id.detailEpisodeWriter) TextView mEpisodeWriter;
    @BindView(R.id.detailEpisodeDirector) TextView mEpisodeDirector;
    @BindView(R.id.captureImage) ImageView mImage;

    private Episode episode;
    private String mSource;
    private String currentPhotoPath;
    private static final int REQUEST_IMAGE_CAPTURE = 111;




    public EpisodeDetailFragment() {
        // Required empty public constructor
    }


    public static EpisodeDetailFragment newInstance(Episode episode) {

        EpisodeDetailFragment episodeDetailFragment = new EpisodeDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("Episode", Parcels.wrap(episode));
        episodeDetailFragment.setArguments(args);
        return episodeDetailFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        episode = Parcels.unwrap(getArguments().getParcelable("Episode"));
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_episode_detail,container,false);
        ButterKnife.bind(this, view);
        Picasso.get().load(R.drawable.images).into(mImageView);
        mEpisodeName.setText("Episode Name: "+episode.getName());
        mEpisodeWriter.setText("Episode Writer: " + episode.getWriter());
        mEpisodeDirector.setText("Episode Director :" + episode.getDirector());
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
//        if (mSource.equals(Constants.SOURCE_SAVED)) {
            inflater.inflate(R.menu.menu_photo, menu);
//        } else {
//            inflater.inflate(R.menu.menu_main, menu);
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_photo:
                onLaunchCamera();
            default:
                break;
        }
        return false;
    }

    public void onLaunchCamera(){

        Uri photoURI = FileProvider.getUriForFile(getActivity(),     getActivity().getApplicationContext().getPackageName()+".provider",
                createImageFile());
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

        // tell the camera to request write permissions
        takePictureIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = new File(storageDir, imageFileName + ".jpg");

        String currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap captureImage = (Bitmap) extras.get("data");
            mImage.setImageBitmap(captureImage);
            
        }
    }

    public void onCameraIconClicked(){
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            onLaunchCamera();
        } else {
            String[] permissionRequest = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permissionRequest, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            // we have heard back from our request for camera and write external storage.
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                onLaunchCamera();
            } else {
                Toast.makeText(getContext(), "Can't open the camera without permission", Toast.LENGTH_LONG).show();
            }
        }
    }

//    private void galleryAddPic() {
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File(currentPhotoPath);
//        Uri contentUri = Uri.fromFile(f);
//        mediaScanIntent.setData(contentUri);
//        getActivity().sendBroadcast(mediaScanIntent);
//    }

    public void encodeBitmapAndSaveToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference(Constants.FIREBASE_CHILD_EPISODES)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(episode.getImgUrl())
                .child("imageUrl");
        ref.setValue(imageEncoded);
    }

}