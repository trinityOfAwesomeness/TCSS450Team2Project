package authenticate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import edu.tacoma.uw.tslinard.tcss450team2project.R;
import main.MainMenuActivity;

public class SignInActivity extends AppCompatActivity implements LoginFragment.LoginFragmentListener{

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
        if(!mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.sign_in_fragment_id, new LoginFragment())
                    .commit();
        } else {
            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
            finish();
        }

//        Button createAccountButton = findViewById(R.id.createAccount_button);
//        createAccountButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.sign_in_fragment_id, new CreateAccountFragment());
//                transaction.commit();
//            }
//        });

    }


    @Override
    public void login(String email, String pwd) {
        Toast.makeText(getApplicationContext(),email + ": " + pwd, Toast.LENGTH_SHORT).show();
        mSharedPreferences.edit().putBoolean(getString(R.string.LOGGEDIN), true).commit();
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
        finish();
    }


}