package com.example.patil.rocketcheck;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//import com.github.angads25.filepicker.controller.DialogSelectionListener;
//import com.github.angads25.filepicker.model.DialogConfigs;
//import com.github.angads25.filepicker.model.DialogProperties;
//import com.github.angads25.filepicker.view.FilePickerDialog;

import java.io.File;
import java.net.URISyntaxException;

public class LoadCsv extends AppCompatActivity {
    private static final int PICKFILE_RESULT_CODE = 1;
    private static final int REQUEST_ID_READ_PERMISSION = 101;
    private static final int REQUEST_ID_WRITE_PERMISSION=201;

    String FilePath="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_csv);
        android.support.v7.app.ActionBar actionbar=getSupportActionBar();
        actionbar.setTitle(Html.fromHtml("<font color='#30a5ff'>Rocketcheck</font>"));




        Button roundbutton=(Button)findViewById(R.id.roundbutton);
        roundbutton.setOnTouchListener(new View.OnTouchListener()
        {
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        Button view = (Button) v;
                        view.getBackground().setColorFilter(0x44000000, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
      /////////////////////////////////////////////////////////////////////////////////////////////


                        takePermissions();
                        filePicker();





                     //   Toast.makeText(getApplicationContext(),"this is good",Toast.LENGTH_SHORT).show();
                        // Your action here on button click









/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    case MotionEvent.ACTION_CANCEL: {
                        Button view = (Button) v;
                        view.getBackground().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }
                return true;
            }

        });

    }

    public void takePermissions()
    {
        boolean canReadAndWrite = this.askPermission(REQUEST_ID_READ_PERMISSION,
                Manifest.permission.READ_EXTERNAL_STORAGE) && this.askPermission(REQUEST_ID_WRITE_PERMISSION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private boolean askPermission(int requestId, String permissionName) {
        if (android.os.Build.VERSION.SDK_INT >= 23) {

            // Check if we have permission
            int permission = ActivityCompat.checkSelfPermission(this, permissionName);


            if (permission != PackageManager.PERMISSION_GRANTED) {
                // If don't have permission so prompt the user.
                this.requestPermissions(
                        new String[]{permissionName},
                        requestId
                );
                return false;
            }
        }
        return true;

    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //
        // Note: If request is cancelled, the result arrays are empty.
        if (grantResults.length < 0) {

            Toast.makeText(getApplicationContext(), "Permission Cancelled!", Toast.LENGTH_SHORT).show();
            takePermissions();
        }
    }



  public void filePicker()
  {
      Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
      intent.setType("*/*");
      startActivityForResult(intent,PICKFILE_RESULT_CODE);
  }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch(requestCode){
            case PICKFILE_RESULT_CODE:
                if(resultCode==RESULT_OK){
                    // FilePath = data.getData().getPath();
                    Uri uri_path=data.getData();
                    try {
                        FilePath=PathUtil.getPath(this,uri_path);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                      Toast.makeText(this,FilePath,Toast.LENGTH_LONG).show();
                    int len=FilePath.length();
                    if(FilePath.charAt(len-1)=='v' && FilePath.charAt(len-2)=='s' && FilePath.charAt(len-3)=='c')
                    {
                        Intent i=new Intent(getApplicationContext(),Scanner.class);
                        i.putExtra("file_path",FilePath);
                        startActivity(i);
                    }
                    else
                    {
                        Toast.makeText(this,"Please choose CSV file",Toast.LENGTH_LONG).show();
                    }

                }
                break;

        }
    }




}
