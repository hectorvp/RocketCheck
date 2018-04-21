package com.example.patil.rocketcheck;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * Created by patil on 07-02-2018.
 */

public class FileOperations {

    public static final String mytag="mytag";

    public static String readFile(String path,String TransId,int CheckIn)
    {

        String result="";
        File file=new File(path);
        try {
            BufferedReader br=new BufferedReader(new FileReader(file));
            String line="";
            StringBuilder sb=new StringBuilder(line);
            while( (line=br.readLine() ) != null)
            {
                String arr[]=line.split(",");
                if(arr.length>1) {
                    if (arr[1].equalsIgnoreCase(TransId)) {                                                   //line.contains(TransId)
                        line = line.replace("\"", "");
                        if (CheckIn == 1) {
                            if (line.contains("Not Checked")) {
                                line = line.replace("Not Checked", "Checked");
                                result = "Updated Successfully";

                            } else {

                                result = "Already Checked";
                            }
                        } else {
                            if (line.contains("Not Checked")) {
                                result = "Already Unchecked ";

                            } else {

                                line = line.replace("Checked", "Not Checked");
                                result = "Updated Successfully";
                            }
                        }
                    }
                }

                line=line+"\n";

                sb.append(line);
            }
            br.close();

            FileWriter fr=new FileWriter(file);
            fr.write(sb.toString());
            fr.close();



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    return result;
    }

    public static PersonDetails getPersonDetails(String txnId,String path)
    {
        PersonDetails person=new PersonDetails();
    BufferedReader br=null;
        File file=new File(path);
        try {
             br=new BufferedReader(new FileReader(file));
            String line="";
            while((line=br.readLine())!=null) {
                line = line.replace("\"", "");
                String array[] = line.split(",");
                if (array.length > 1) {
                    if (array[1].equalsIgnoreCase(txnId))                                                   // line.contains(txnId)
                    {
                        String arr[] = line.split(",");
                        person.setTxnId(arr[1]);
                        person.setName(arr[2]);
                        person.setEmailId(arr[3]);
                        person.setPhoneNo(arr[4]);
                        person.setGender(arr[5]);
                        person.setStatus(arr[6]);
                        person.setCheckInStatus(arr[7]);
                        return person;


                    }
                }
            }

            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        return person;
    }


    public static ArrayList<PersonDetails> getAllPersonDetails(String path)
    {
        ArrayList<PersonDetails> arr=new ArrayList<PersonDetails>();
        File file=new File(path);
        try {
            BufferedReader br=new BufferedReader(new FileReader(file));
            String line="";
            for(int i=0;i<6;i++)
            {
                br.readLine();
            }
            while((line=br.readLine())!=null)
            {
                PersonDetails person=new PersonDetails();
                line = line.replace("\"", "");
                String ar[]=line.split(",");
                if(ar.length>1) {

                    person.setTxnId(ar[1]);
                    person.setName(ar[2]);
                    person.setEmailId(ar[3]);
                    person.setPhoneNo(ar[4]);
                    person.setGender(ar[5]);
                    person.setStatus(ar[6]);
                    person.setCheckInStatus(ar[7]);


                    arr.add(person);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arr;
    }



}







