package com.kalamazoo.ccpd.ccpdapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class FAQSubmission extends AppCompatActivity {

    private String kID = "k14rw01";
    private Switch ccMeSwitch;
    private EditText enterMessage;
    private String message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faqsubmission);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ccMeSwitch = (Switch) findViewById(R.id.ccme);
                enterMessage = (EditText) findViewById(R.id.emailcontent);
                message = enterMessage.getText().toString();
                if (!message.equals("")) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto", "career@kzoo.edu", null));
                    if (ccMeSwitch.isChecked()) {
                        emailIntent.putExtra(Intent.EXTRA_CC, new String[]{kID + "@kzoo.edu"});
                    }
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "New Question Submitted by " + kID);
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Question:\n" + message + "\nSubmitted by:\n" + kID);
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                } else {
                    Toast.makeText(getApplicationContext(), "Question empty! Please enter your question in the field provided", Toast.LENGTH_LONG).show();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * @Override public boolean onCreateOptionsMenu(Menu menu) {
     * // Inflate the menu; this adds items to the action bar if it is present.
     * getMenuInflater().inflate(R.menu.menu_faqsubmission, menu);
     * return true;
     * }
     **/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
