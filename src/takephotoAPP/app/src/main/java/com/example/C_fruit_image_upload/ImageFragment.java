package com.example.C_fruit_image_upload;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.jetbrains.annotations.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ImageFragment extends Fragment {

    private Bitmap bitmap;

    @BindView(R.id.res_photo)
    ImageView resPhoto;




    int CameraCount = 999;


    public void imageSetupFragment(Bitmap bitmap) {
        if (bitmap != null) {
            this.bitmap = bitmap;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);

//        ph = new PhotoFragment();
//        int num = ph.CameraCount;
        Log.v("CameraCountAAAA","Mode = " + CameraCount);
        CameraCount=0;

       // Log.v("CameraCount","Mode = " + num);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_image, container, false);
        ButterKnife.bind(this, view);
        //check if bitmap exist, set to ImageView
        if (bitmap != null) {
            resPhoto.setImageBitmap(bitmap);

        }
        CameraCount+=1;
        Log.v("CameraCount","Mode = " + CameraCount);


        return view;
    }


//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        // BackStackを設定
//        fragmentTransaction.addToBackStack(null);
//
//        // counterをパラメータとして設定
//        fragmentTransaction.replace(R.id.res_photo_layout, InputTextFragment.newInstance());
//
//        fragmentTransaction.commit();
    }



