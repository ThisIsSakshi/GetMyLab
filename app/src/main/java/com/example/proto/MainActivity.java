package com.example.proto;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private EditText uid, password;
    ImageView login;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get all data from activity_main
        login = (ImageView) findViewById(R.id.login);
        uid = (EditText) findViewById(R.id.uid);
        password = (EditText) findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //check connectivity
                if (!isOnline(MainActivity.this)) {
                    Toast.makeText(MainActivity.this, "No network connection", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(MainActivity.this, "into loginAccess", Toast.LENGTH_LONG).show();
                //from login.java
                new loginAccess().execute();
            }

            //code to check online details
            private boolean isOnline(Context mContext) {
                ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                return netInfo != null && netInfo.isConnectedOrConnecting();
            }
            //Close code that check online details
        });
        //Close log in
    }

    private class loginAccess extends AsyncTask<String, String, String> {

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... arg0) {
            String Uid = uid.getText().toString();
            String pwd = password.getText().toString();
            String response;

            try {
                String link = "https://neha1997.000webhostapp.com/loginpost.php";
                String data = URLEncoder.encode("username", "UTF-8") + "=" +
                        URLEncoder.encode(Uid, "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" +
                        URLEncoder.encode(pwd, "UTF-8");
                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(data);
                wr.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    //break;
                }

                response = sb.toString();
                /*if (!Objects.equals(response, "")) {
                    Intent i = new Intent(getApplicationContext(), Welcome.class);
                    i.putExtra("uid", Uid);
                    i.putExtra("pass", pwd);
                    startActivity(i);
                    finish();
                    Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                } */

                return response;
            } catch (Exception e) {
                return "Exception: " + e.getMessage();
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected void onPostExecute(String res) {
            String Uid = uid.getText().toString();
            String pwd = password.getText().toString();
            pDialog.dismiss();
            if (!Objects.equals(res, "")) {
                Intent i = new Intent(getApplicationContext(), Inside.class);
                i.putExtra("uid", Uid);
                i.putExtra("pass", pwd);
                startActivity(i);
                finish();
                Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_LONG).show();
            }
            if (Objects.equals(res, ""))
                Toast.makeText(MainActivity.this, "Please Enter Correct informations", Toast.LENGTH_LONG).show();

        }

    }
}
