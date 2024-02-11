package algonquin.cst2335.guo00079;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class
SecondActivity extends AppCompatActivity {
    public static final String IMAGE_FILE_NAME = "Picture.png";
    public static final String KEY_PHONE_NUMBER = "PhoneNumber";
    private Button callNumberButton;
    private Button changePictureButton;
    private EditText phoneNumberEditText;
    private ImageView profileImage;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        editor = prefs.edit();

        Intent fromPrevious = getIntent();
        String emailAddress=fromPrevious.getStringExtra("EmailAddress");
        callNumberButton=findViewById(R.id.callNumberButton);
        changePictureButton=findViewById(R.id.changePictureButton);
        phoneNumberEditText=findViewById(R.id.phoneNumberEditText);
        profileImage=findViewById(R.id.profileImage);

        callNumberButton.setOnClickListener(clk->{
            Intent call = new Intent(Intent.ACTION_DIAL);
            String phoneNumber= phoneNumberEditText.getText().toString();
            call.setData(Uri.parse("tel:" + phoneNumber));
        });
        ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override

                    public void onActivityResult(ActivityResult result) {

                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Bitmap thumbnail = data.getParcelableExtra("data");
                            profileImage.setImageBitmap(thumbnail);

                            FileOutputStream fOut = null;

                            try { fOut = openFileOutput(IMAGE_FILE_NAME, Context.MODE_PRIVATE);
                                thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);

                                fOut.flush();

                                fOut.close();
                            }
                            catch (FileNotFoundException e)
                            {
                                e.printStackTrace();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });
        changePictureButton.setOnClickListener(clk->{
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            cameraResult.launch(cameraIntent);
        });

        File file = new File( getFilesDir(), IMAGE_FILE_NAME);
        if(file.exists())
        {
            Bitmap theImage = BitmapFactory.decodeFile(file.getAbsolutePath());
            profileImage.setImageBitmap(theImage);
        }

        String phoneNumber = prefs.getString(KEY_PHONE_NUMBER, "");
        phoneNumberEditText.setText(phoneNumber);
    }

    @Override
    protected void onPause() {
        super.onPause();
        editor.putString(KEY_PHONE_NUMBER, phoneNumberEditText.getText().toString());
        editor.apply();
    }
}