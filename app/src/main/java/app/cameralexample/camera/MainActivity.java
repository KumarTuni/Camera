package app.cameralexample.camera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {


    final private int REQUEST_CODE_ASK_PERMISSIONS = 118;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 200;
    String version_name;
    Uri fileCamUri;
    private static final String IMAGE_DIRECTORY_NAME = "Camera App";
    File mediaFile;
    ImageView attachment_img;
    VideoView videoview;
    static final int REQUEST_VIDEO_CAPTURE = 1;
    private static final int PICK_IMAGE = 100;
    private static final int PICK_VIDEO = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button take_photo_btn = (Button) findViewById(R.id.takephoto_btn);
        Button take_video_btn = (Button) findViewById(R.id.takevideo_btn);
        Button pick_image_btn = (Button) findViewById(R.id.pickimage_btn);
        Button pick_video_btn = (Button) findViewById(R.id.pickvideo_btn);

        attachment_img = (ImageView) findViewById(R.id.attachment_img);
        videoview = (VideoView) findViewById(R.id.videoview_view);


        take_photo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoview.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) +
                            ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) +
                            ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        // Should we show an explanation?
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                REQUEST_CODE_ASK_PERMISSIONS);
                        // file_position=position;
                    } else {
                        camintent();
                    }

                } else {
                    camintent();
                }

            }
        });

        take_video_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachment_img.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) +
                            ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) +
                            ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        // Should we show an explanation?
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                                REQUEST_CODE_ASK_PERMISSIONS);
                        // file_position=position;
                    } else {
                        videointent();
                    }

                } else {
                    videointent();
                }

            }
        });

        pick_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    // photoPickerIntent.setType("image/*");
                    photoPickerIntent.setType("image/*");
                    String[] mimetypes = {"jpg", "jpeg", "png"};
                    photoPickerIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                    startActivityForResult(photoPickerIntent, PICK_IMAGE);
                } catch (Exception e) {
                    // toast("Something Went Erong", getActivity());
                }
            }
        });


        pick_video_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    // photoPickerIntent.setType("image/*");
                    photoPickerIntent.setType("video/*");
                  //  String[] mimetypes = {"jpg", "jpeg", "png"};
                  //  photoPickerIntent.putExtra(Intent.EXTRA_MIME_TYPES);
                    startActivityForResult(photoPickerIntent, PICK_VIDEO);
                } catch (Exception e) {
                    // toast("Something Went Erong", getActivity());
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    camintent();

                } else {
                    Toast.makeText(MainActivity.this, "Oops You Denied Permission, You can't Use App Features", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;


            case REQUEST_VIDEO_CAPTURE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    videointent();

                } else {
                    Toast.makeText(MainActivity.this, "Oops You Denied Permission, You can't Use App Features", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;


        }
    }


    void camintent() {

      /*  Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            version_name = "Nougat";
            if (intent.resolveActivity(MainActivity.this.getPackageManager()) != null) {
//            fileTemp = ImageUtils.getOutputMediaFile();
                ContentValues values = new ContentValues(1);
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
                fileCamUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//            if (fileTemp != null) {
//            fileUri = Uri.fromFile(fileTemp);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileCamUri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

            } else {
                //Toast.makeText(this, getString(R.string.error_no_camera), Toast.LENGTH_LONG).show();
            }
        } else {
            version_name = "Belowversion";
            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory
                    (Environment.DIRECTORY_PICTURES).getPath() + "/Camera");

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                            + IMAGE_DIRECTORY_NAME + " directory");

                }
            }

            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());

            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
            //        System.out.println("mediaFile:@@@@"+mediaFile);

            fileCamUri = Uri.fromFile(mediaFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileCamUri);

            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }
*/

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }
    }


    void videointent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                // Log.v("Expensesuri", fileCamUri.toString());

                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                attachment_img.setImageBitmap(imageBitmap);
                attachment_img.setVisibility(View.VISIBLE);
               /* if (version_name.equalsIgnoreCase("Nougat")) {

                    String contnt = getPath(fileCamUri);
                    try {
                        // bimatp factory
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        // downsizi ng image as it throws OutOfMemory Exception for larger
                        // images
                        options.inSampleSize = 8;

                        Bitmap bitmap = BitmapFactory.decodeFile(contnt,
                                options);
                        //  xpattachmnt_nametxt.setText(mediaFile.toString());

                        Glide.with(getApplicationContext()).load(contnt).into(attachment_img);
                        attachment_img.setVisibility(View.VISIBLE);
                        //  attachment_img.setImageBitmap(bitmap);
                        //   fileconversion(contnt.toString());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        // bimatp factory
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        // downsizi ng image as it throws OutOfMemory Exception for larger
                        // images
                        options.inSampleSize = 8;

                        //  Log.v("sdsddsf",fileCamUri.toString());
                        Bitmap bitmap = BitmapFactory.decodeFile(fileCamUri.getPath(),
                                options);
                        //  xpattachmnt_nametxt.setText(mediaFile.toString());
                        Glide.with(getApplicationContext()).load(fileCamUri.getPath()).into(attachment_img);
                        attachment_img.setVisibility(View.VISIBLE);
                        //   attachment_img.setImageBitmap(bitmap);
                        //  fileconversion(mediaFile.toString());
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }*/
            }
        } else if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK && data != null) {

            Uri videoUri = data.getData();
            videoview.setVideoURI(videoUri);
            videoview.setVisibility(View.VISIBLE);
            videoview.start();

        } else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {


            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));

                attachment_img.setImageBitmap(bitmap);
                attachment_img.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }

           /* selectedimage = data.getData();
            camFlag = 1;


            contentresolve = getActivity().getContentResolver();
            Cursor cursor = contentresolve.query(selectedimage, null, null, null, null);
            String fileSize = null;
            try {
                if (cursor != null && cursor.moveToFirst()) {

                    // Note it's called "Display Name".  This is
                    // provider-specific, and might not necessarily be the file name.
                    fileName = cursor.getString(
                            cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                    int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);

                    if (!cursor.isNull(sizeIndex)) {
                        // Technically the column stores an int, but cursor.getString()
                        // will do the conversion automatically.
                        fileSize = cursor.getString(sizeIndex);
                    } else {
                        fileSize = "Unknown";
                    }
                    if (fileSize.equalsIgnoreCase("Unknown")) {
                        toast("File Size Unknown", getActivity());
                    } else {

                        xpattachmnt_img.setVisibility(View.VISIBLE);
                        xpattachmnt_nametxt.setText("");
                        Glide.with(getActivity()).load(selectedimage).override(300, 200).into(xpattachmnt_img);


                    }
                }
            } catch (Exception e) {

            } finally {
                cursor.close();
            }*/

        } else if (requestCode == PICK_VIDEO && resultCode == RESULT_OK && data != null){

            Uri videoUri = data.getData();
            videoview.setVideoURI(videoUri);
            videoview.setVisibility(View.VISIBLE);
            videoview.start();
        }else if (resultCode == RESULT_CANCELED) {
            // user cancelled Image capture
            Toast.makeText(MainActivity.this, "User Cancelled", Toast.LENGTH_SHORT).show();
        } else {
            // failed to capture image
            Toast.makeText(MainActivity.this, "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();

        }
    }

    private String getPath(Uri selectedImaeUri) {
        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(selectedImaeUri, projection, null, null,
                null);

        if (cursor != null) {
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            return cursor.getString(columnIndex);
        }

        return selectedImaeUri.getPath();
    }

}
