package algonquin.cst2335.guo00079.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import algonquin.cst2335.guo00079.R;
import algonquin.cst2335.guo00079.data.MainViewModel;
import algonquin.cst2335.guo00079.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private MainViewModel model;
    private ActivityMainBinding variableBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = this;
        model = new ViewModelProvider(this).get(MainViewModel.class);

        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());
        variableBinding.mybutton.setOnClickListener(v ->
        {
            model.editString.postValue(variableBinding.myEditText.getText().toString());
        });
        model.editString.observe(this, s->{
            variableBinding.myText.setText("Your edit text has: "+ s);
        });
        variableBinding.myimagebutton.setOnClickListener(click->{
            Toast.makeText(context,"The width = " + click.getWidth() + " and height = " + click.getHeight(),Toast.LENGTH_LONG).show();
        });

        variableBinding.myRadioButton.setOnClickListener( click->{
            Toast.makeText(context,"myRadioButton checked: " + ((CompoundButton)click).isChecked() ,Toast.LENGTH_LONG).show();
        });

        variableBinding.myCheckBox.setOnClickListener( click->{
            Toast.makeText(context,"myCheckBox checked: " + ((CompoundButton)click).isChecked() ,Toast.LENGTH_LONG).show();
        });

        variableBinding.mySwitch.setOnClickListener( click->{
            Toast.makeText(context,"mySwitch on: " + ((CompoundButton)click).isChecked() ,Toast.LENGTH_LONG).show();
        });
    }
}