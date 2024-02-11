package algonquin.cst2335.guo00079;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_EMAIL_ADDRESS = "LoginName";
    private static String TAG = "MainActivity";

    private Button loginButton;
    private EditText emailEditText;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d( TAG, "onCreate");
        prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        editor = prefs.edit();

        loginButton = findViewById(R.id.loginButton);
        emailEditText=findViewById(R.id.emailEditText);
        loginButton.setOnClickListener( clk-> {
            Intent nextPage = new Intent( MainActivity.this, SecondActivity.class);
            nextPage.putExtra(KEY_EMAIL_ADDRESS, emailEditText.getText().toString() );
            startActivity( nextPage);
            editor.putString(KEY_EMAIL_ADDRESS, emailEditText.getText().toString());
            editor.apply();
        } );
        String emailAddress = prefs.getString(KEY_EMAIL_ADDRESS, "");
        emailEditText.setText(emailAddress);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d( TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d( TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d( TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d( TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d( TAG, "onDestroy");
    }
}