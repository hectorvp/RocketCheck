package com.example.patil.rocketcheck;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.samples.vision.barcodereader.BarcodeCapture;
import com.google.android.gms.samples.vision.barcodereader.BarcodeGraphic;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.File;
import java.io.IOException;
import java.util.List;

import xyz.belvi.mobilevisionbarcodescanner.BarcodeRetriever;

public class Scanner extends AppCompatActivity implements BarcodeRetriever,NavigationView.OnNavigationItemSelectedListener {
    String fpath;
    private DrawerLayout drawerlayout;
    private ActionBarDrawerToggle mtoggle;
    EditText et;
    String txnId;

FloatingActionButton button;
    private static final int REQUEST_ID_READ_PERMISSION = 100;
    private static final int REQUEST_ID_WRITE_PERMISSION=200;
    Switch simpleSwitch;
    int checkIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        android.support.v7.app.ActionBar actionbar=getSupportActionBar();
        actionbar.setTitle(Html.fromHtml("<font color='#30a5ff'>rocketcheck</font>"));

        drawerlayout=(DrawerLayout)findViewById(R.id.DrawerLayout);
        mtoggle=new ActionBarDrawerToggle(this,drawerlayout,R.string.open,R.string.close);
        drawerlayout.addDrawerListener(mtoggle);
        mtoggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        setNavigationViewListener();

    button=(FloatingActionButton)findViewById(R.id.button);
        et=(EditText)findViewById(R.id.editText);
        simpleSwitch = (Switch) findViewById(R.id.switch1);
        addListenerOnButton();


        Bundle b = getIntent().getExtras();
        fpath = b.getString("file_path");



        BarcodeCapture barcodeCapture = (BarcodeCapture) getSupportFragmentManager().findFragmentById(R.id.barcode);
        barcodeCapture.setShowDrawRect(true);
        barcodeCapture.setRetrieval(this);

        //askPermissionAndRead();   This nethod is to trigger file operations


    }

    public void addListenerOnButton()
    {
        button.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view) {


             txnId=et.getText().toString();

             if( ! TextUtils.isEmpty(txnId)) {
                 if (simpleSwitch.isChecked())
                     checkIn = 0;
                 else
                     checkIn = 1;
                 Toast.makeText(Scanner.this, txnId + " " + checkIn + " " + fpath, Toast.LENGTH_LONG).show();
                 askPermissionAndRead();
             }else
             {
                 et.setError("Please fill Transaction Id field");

             }
            }});
    }







    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        CharSequence s=item.getTitle();
      //  Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
       if(mtoggle.onOptionsItemSelected(item))
       {
           return true;
       }
        return super.onOptionsItemSelected(item);
    }




//////////////////////////////////////////FILE_OPERATIONS/////////////////////////////////////////////////////////////////////


    public void askPermissionAndRead() {
        boolean canReadAndWrite = this.askPermission(REQUEST_ID_READ_PERMISSION,
                Manifest.permission.READ_EXTERNAL_STORAGE) && this.askPermission(REQUEST_ID_WRITE_PERMISSION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //
        if (canReadAndWrite) {
            performFileOperations();
        }

    }


    private void performFileOperations()   /////////This is main controller function //////////////
    {
        String msg = FileOperations.readFile(fpath,txnId,checkIn);
      //  Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        if(msg=="Updated Successfully")
        {
            PersonDetails person=FileOperations.getPersonDetails(txnId,fpath);
            AlertDialog.Builder builder=new AlertDialog.Builder(Scanner.this)
                    .setTitle("User Details")
                    .setMessage("TransactionId :"+txnId+"\nName :"+person.getName()+"\nPhoneNo :"+person.getPhoneNo()+"\nEmail_Id :"
                    +person.getEmailId()+"\nStatus :"+person.getStatus()+"\nCheckInStatus :"+person.getCheckInStatus());
            builder.show();

        }
        else
            if(msg=="")
            {
                Toast.makeText(this,"Entry Not Found",Toast.LENGTH_SHORT).show();

            }
        else
            {
                Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
            }

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
        //4
        // Note: If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0) {
            switch (requestCode) {
                case REQUEST_ID_READ_PERMISSION: {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                       performFileOperations();
                    }
                }
                case REQUEST_ID_WRITE_PERMISSION: {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                        performFileOperations();
                    }
                }
            }
        } else {
            Toast.makeText(getApplicationContext(), "Permission Cancelled!", Toast.LENGTH_SHORT).show();
        }
    }





//////////////////////////////////BARCODE METHODS////////////////////////////////////////////////////////////////////





    // for one time scan
    @Override
    public void onRetrieved(final Barcode barcode) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 400);
                toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,150);

                if(simpleSwitch.isChecked())
                    checkIn=0;
                else
                    checkIn=1;

                txnId=barcode.displayValue;
                et.setText(txnId);


                Toast.makeText(Scanner.this, barcode.displayValue+" "+checkIn+" "+fpath, Toast.LENGTH_LONG).show();

                askPermissionAndRead();

//                AlertDialog.Builder builder = new AlertDialog.Builder(Scanner.this)
//                        .setTitle("code retrieved")
//                        .setMessage(barcode.displayValue);
//                builder.show();
            }
        });


    }

    // for multiple callback
    @Override
    public void onRetrievedMultiple(final Barcode closetToClick, final List<BarcodeGraphic> barcodeGraphics) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String message = "Code selected : " + closetToClick.displayValue + "\n\nother " +
                        "codes in frame include : \n";
                for (int index = 0; index < barcodeGraphics.size(); index++) {
                    Barcode barcode = barcodeGraphics.get(index).getBarcode();
                    message += (index + 1) + ". " + barcode.displayValue + "\n";
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(Scanner.this)
                        .setTitle("code retrieved")
                        .setMessage(message);
                builder.show();
            }
        });

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {
        // when image is scanned and processed
    }

    @Override
    public void onRetrievedFailed(String reason) {
        // in case of failure
    }

    @Override
    public void onPermissionRequestDenied() {
        Toast.makeText(this,"Permission Denied!!!",Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        CharSequence cs=item.getTitle();
        String title=cs.toString();
        if(title.contains("About Us"))
        {
            Intent i=new Intent(this,AboutUs.class);
            startActivity(i);
        }
        else {
            //Toast.makeText(this, cs + " " + item.getItemId(), Toast.LENGTH_SHORT).show();
            //;


            drawerlayout.closeDrawer(GravityCompat.START);
            Intent i = new Intent(this, ShowLists.class);
            i.putExtra("path", fpath);
            startActivity(i);





        }
            return true;
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


}




////////////////////////ReadMe for this page///////////////////////////////


