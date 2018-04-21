package com.example.patil.rocketcheck;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowLists extends AppCompatActivity {
    String filepath;
    String TxnId[], Name[], Status[];
    int size;
    ListView listview;
    private static final int REQUEST_ID_READ_PERMISSION = 102;
    private static final int REQUEST_ID_WRITE_PERMISSION=202;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_lists);

        android.support.v7.app.ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(Html.fromHtml("<font color='#30a5ff'>rocketcheck</font>"));

        Bundle b = getIntent().getExtras();
        filepath = b.getString("path");



            ArrayList<PersonDetails> arr = FileOperations.getAllPersonDetails(filepath);
            TxnId = new String[arr.size()];
            Name = new String[arr.size()];
            Status = new String[arr.size()];
            size = arr.size();
            for (int i = 0; i < arr.size(); i++) {
                PersonDetails person;
                person = arr.get(i);
                TxnId[i] = person.getTxnId();
                Name[i] = person.getName();
                Status[i] = person.getCheckInStatus();
                CustomAdapter customAdapter = new CustomAdapter();
                listview = (ListView) findViewById(R.id.listview);
                listview.setAdapter(customAdapter);
                listview.setClickable(true);

            }


//        ListView ls=(ListView)findViewById(R.id.listview);
//        ListAdapter adap=new ArrayAdapter<String>(this,R.layout.simple_list,Name);
//        ls.setAdapter(adap);



        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Animation animation1 = new AlphaAnimation(0.3f, 1.0f);
                animation1.setDuration(2000);
                arg1.startAnimation(animation1);

                TextView selected=(TextView)arg1.findViewById(R.id.txnId);
                String tid=selected.getText().toString();
                PersonDetails person=FileOperations.getPersonDetails(tid,filepath);
                AlertDialog.Builder builder=new AlertDialog.Builder(ShowLists.this)
                        .setTitle("User Details")
                        .setMessage("TransactionId :"+tid+"\nName :"+person.getName()+"\nPhoneNo :"+person.getPhoneNo()+"\nEmail_Id :"
                                +person.getEmailId()+"\nStatus :"+person.getStatus()+"\nCheckInStatus :"+person.getCheckInStatus());
                builder.show();

            }
        });

    }




    class CustomAdapter extends BaseAdapter {

        @Override
        public boolean isEnabled(int pos)
        {
            return true;
        }

        @Override
        public int getCount() {
            return size;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.list_design, null);
            TextView name = (TextView) convertView.findViewById(R.id.name);

            TextView txnid = (TextView) convertView.findViewById(R.id.txnId);
            TextView status = (TextView) convertView.findViewById(R.id.status);
            name.setText(Name[position]);
            txnid.setText(TxnId[position]);
            status.setText(Status[position]);


            return convertView;
        }
    }



    public boolean askPermissionAndRead() {
        boolean canReadAndWrite = this.askPermission(REQUEST_ID_READ_PERMISSION,
                Manifest.permission.READ_EXTERNAL_STORAGE) && this.askPermission(REQUEST_ID_WRITE_PERMISSION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return canReadAndWrite;


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
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //
        // Note: If request is cancelled, the result arrays are empty.
        if (grantResults.length < 0) {

            Toast.makeText(getApplicationContext(), "Permission Cancelled!", Toast.LENGTH_SHORT).show();
        }
    }


}
