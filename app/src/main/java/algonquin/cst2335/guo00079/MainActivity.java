package algonquin.cst2335.guo00079;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is main activity to valid the complexity of an password entry
 * @author Liying Guo
 * @version 1.0
 * @since 2024-Feb-23
 */
public class MainActivity extends AppCompatActivity {
    /**
     * This holds the text at the centre of the screen
     */
    TextView tv;
    /**
     * This holds password entry
     */
    EditText et;
    /**
     * This button to valid password
     */
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.textView);
        et = findViewById(R.id.editText);
        btn = findViewById(R.id.button);
        btn.setOnClickListener(click->{
            String password =et.getText().toString();
            if(checkPasswordComplexity(password))
            {
                tv.setText("Your password is complex enough");
            }
            else {
                tv.setText("You shall not pass!");
            }
//            Toast.makeText(this,"requirement was not met",Toast.LENGTH_LONG).show();
        });
    }
    /***
     * password should have a digit, an upper case, a lower case, and a special character.
     *
     * Regx:
     * (?=.*[0-9]): At least one digit.
     * (?=.*[a-z]): At least one lowercase letter.
     * (?=.*[A-Z]): At least one uppercase letter.
     * (?=.*[@#$%^&+=!]): At least one special character.
     * .{1,}: Minimum length of 1 characters.
     */
    private boolean checkPasswordComplexity(String password) {
        // Define the regex pattern
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*]).{1,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}