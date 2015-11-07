package edu.cmu.juicymeeting.juicymeeting;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class JoinGroupActivity extends AppCompatActivity {
    public static final String PASS = "PASS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_group);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_join_group, menu);
        return true;
    }

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

    public void joinGroupOk(View view) {
        EditText editText = (EditText) findViewById(R.id.joinGroupPass);
        Editable editable = editText.getText();
        String number = editable.toString();
        TextView textView = (TextView) findViewById(R.id.joinGroupHint);
        if(number.length() != 4) {
            textView.setText("Must be exactly 4 digits!");
        }
        else {
            textView.setText("");
            Intent resultIntent = new Intent();
            resultIntent.putExtra(PASS, number);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }
}