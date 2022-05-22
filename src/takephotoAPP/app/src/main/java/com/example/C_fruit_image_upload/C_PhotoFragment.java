package com.example.C_fruit_image_upload;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class C_PhotoFragment extends Fragment implements SurfaceHolder.Callback, com.example.C_fruit_image_upload.HttpPostListener {

    final static private String TAG = "HttpPost";

    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    boolean previewing = false;
    Context context;
    private AlertDialog dialog;



    @BindView(R.id.preview_layout)
    LinearLayout previewLayout;




    private OnFragmentInteractionListener mListener;

    Camera.Size previewSizeOptimal;
    private File file;


    public global_variable gv;

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Bitmap bitmap);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        ButterKnife.bind(this, view);
        context = getContext();
        gv = new global_variable();

        surfaceView = (SurfaceView) view.findViewById(R.id.camera_preview_surface);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();
        Camera.Parameters params = camera.getParameters();
        List< String > flashList = params.getSupportedFlashModes();
        for (int i=0;i< flashList.size();i++){
            Log.v("CameraFlash","Mode = " + flashList.get(i));
        }

        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(params);
        try{
            camera.setPreviewDisplay(holder);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (previewing) {
            camera.stopPreview();
            previewing = false;
        }

        if (camera != null) {
            try {
                Camera.Parameters parameters = camera.getParameters();
                //get preview sizes
                List<Camera.Size> previewSizes = parameters.getSupportedPreviewSizes();

                //find optimal - it very important
                previewSizeOptimal = getOptimalPreviewSize(previewSizes, parameters.getPictureSize().width,
                        parameters.getPictureSize().height);

                //set parameters
                if (previewSizeOptimal != null) {
                    parameters.setPreviewSize(previewSizeOptimal.width, previewSizeOptimal.height);
                }

                if (camera.getParameters().getFocusMode().contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                }
                if (camera.getParameters().getFlashMode().contains(Camera.Parameters.FLASH_MODE_AUTO)) {
                    parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
                }

                camera.setParameters(parameters);

                //rotate screen, because camera sensor usually in landscape mode
                Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                if (display.getRotation() == Surface.ROTATION_0) {
                    camera.setDisplayOrientation(90);
                } else if (display.getRotation() == Surface.ROTATION_270) {
                    camera.setDisplayOrientation(180);
                }





                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                previewing = true;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    public Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
        previewing = false;
    }


    @OnClick(R.id.make_photo_button)
    void makePhoto() {
        if (camera != null) {
            camera.takePicture(myShutterCallback,
                    myPictureCallback_RAW, myPictureCallback_JPG);

        }
    }

    Camera.ShutterCallback myShutterCallback = new Camera.ShutterCallback() {
        @Override
        public void onShutter() {

        }
    };
    Camera.PictureCallback myPictureCallback_RAW = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            LayoutInflater inflater = getActivity().getLayoutInflater();

            //待機画面
            dialog = new AlertDialog.Builder(getActivity())
                    .setView(inflater.inflate(R.layout.loading,null))
                    .setCancelable(true)
                    .create();
            dialog.show();
        }
    };


    Camera.PictureCallback myPictureCallback_JPG = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {



            Bitmap bitmapPicture
                    = BitmapFactory.decodeByteArray(data, 0, data.length);


            Bitmap croppedBitmap = null;

            Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            if (display.getRotation() == Surface.ROTATION_0) {

                //rotate bitmap, because camera sensor usually in landscape mode
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap rotatedBitmap = Bitmap.createBitmap(bitmapPicture, 0, 0, bitmapPicture.getWidth(), bitmapPicture.getHeight(), matrix, true);
                //save file
//                createImageFile(rotatedBitmap);  // 回転後の画像保存




                //save result
                if (rotatedBitmap != null) {
                    createImageFile(rotatedBitmap);   // 変更後の画像保存
                    http_upload(rotatedBitmap);
                }



            } else if (display.getRotation() == Surface.ROTATION_270) {
                // for Landscape mode
            }

//            //pass to another fragment
//            if (mListener != null) {
//                if (croppedBitmap != null)
//                    mListener.onFragmentInteraction(croppedBitmap);
//            }

            if (camera != null) {
                camera.startPreview();
            }
        }

    };




    public void createImageFile(final Bitmap bitmap) {

        File path = Environment.getExternalStorageDirectory();

        String timeStamp = new SimpleDateFormat("MMdd_HHmmssSSS").format(new Date());
        String imageFileName = "region_" + timeStamp + ".jpg";

        if(isExternalStorageWritable()) {

            ContentValues values = new ContentValues();
                    // コンテンツ クエリの列名
            // ファイル名
//            values.put(MediaStore.Images.Media.DISPLAY_NAME, "TestSampeImage11.jpg");
            values.put(MediaStore.Images.Media.DISPLAY_NAME, imageFileName);

            // マイムの設定
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            // 書込み時にメディア ファイルに排他的にアクセスする
            values.put(MediaStore.Images.Media.IS_PENDING, 1);

            ContentResolver resolver = context.getApplicationContext().getContentResolver();
            Uri collection = MediaStore.Images.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL_PRIMARY);
            Uri item = resolver.insert(collection, values);

            try (OutputStream outstream = context.getContentResolver().openOutputStream(item)) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
//                textView.setText(R.string.saved);
//                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            values.clear();
            //　排他的にアクセスの解除
            values.put(MediaStore.Images.Media.IS_PENDING, 0);
            resolver.update(item, values, null, null);
        }




    }

    //　文字と画像の送信
    public void http_upload(Bitmap croppedBitmap){

        Log.i(TAG, "post start!");

        //HttpPostTask task = new HttpPostTask(" http://127.0.0.1:8000/");

       HttpPostTask task = new HttpPostTask("http://"+gv.ip_address+"/MERON/C_photo.php");
        //HttpPostTask task = new HttpPostTask("http://192.168.10.122:8000/android");
        //HttpPostTask task = new HttpPostTask("http://192.168.10.139/211220fruit/C_photo.php");


        // 画像を追加
        AssetManager manager = context.getAssets();
        for (int i = 1; i <= 2; i++)
        {
            InputStream is = null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
//                        is = manager.open("test" + i + ".jpg");
                is = manager.open("test" + i + ".jpg");
                int len;
                byte[] buffer = new byte[10240];

                while ((len = is.read(buffer)) > 0)
                {
                    baos.write(buffer, 0, len);
                }
                task.addImage("image" + i + ".jpg", getBitmapAsByteArray(croppedBitmap));
            } catch (Exception e) {

            } finally {
                try {
                    is.close();
                } catch (IOException e) {}
                try {
                    baos.close();
                } catch (IOException e) {}
            }
        }

        // リスナーをセットする
        task.setListener(C_PhotoFragment.this);
        task.execute();
        //実行
       // task.execute();
    }

    // bitmapデータを配列化
    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        //PNG, クオリティー100としてbyte配列にデータを格納
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }


    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state));
    }



    @Override
    public void postCompletion(byte[] response) {
        Log.i(TAG, "post completion!");
        Log.i(TAG, new String(response));

        try {
            int sleepTime = 10000;  // 20000 = 20秒
            Thread.sleep(sleepTime);

            gv.setTestString(new String(response));

            //待機画面終了
            dialog.dismiss();

            //WebViewの表示のためmethod実行
            MainActivity maActivity = (MainActivity) getActivity();
            assert maActivity != null;
            maActivity.OpenWebview();

            //fragment終了
            getFragmentManager().beginTransaction().remove(this).commit();

            //ボタン再表示
            maActivity.VisibleButton();
        } catch (InterruptedException e) {
            // 例外ハンドリング
        }


    }

    @Override
    public void postFialure() {
        Log.i(TAG, "post failure!");
    }





}