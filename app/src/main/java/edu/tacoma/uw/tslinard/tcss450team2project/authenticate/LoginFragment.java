package edu.tacoma.uw.tslinard.tcss450team2project.authenticate;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import edu.tacoma.uw.tslinard.tcss450team2project.R;

public class LoginFragment extends Fragment {

    private LoginFragmentListener mLoginFragmentListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        getActivity().setTitle("Sign In");
        mLoginFragmentListener = (LoginFragmentListener) getActivity();
        final EditText emailText = view.findViewById(R.id.email_address_id);
        final EditText pwdText = view.findViewById(R.id.password_id);
        Button loginButton = view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String email = emailText.getText().toString();
                String pwd = pwdText.getText().toString();
                if(TextUtils.isEmpty(email) || !email.contains("@")) {
                    Toast.makeText(v.getContext(), "Enter valid email address"
                            , Toast.LENGTH_SHORT).show();
                    emailText.requestFocus();
                } else {
                    mLoginFragmentListener.login(email, pwd);
                }
            }
        });

        // add listener to create account button
        Button createAccountButton = view.findViewById(R.id.createAccount_button);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginFragmentListener.launchCreateAccountFragment();
            }
        });
        return view;
    }

    public interface LoginFragmentListener {
        void login(String email, String pwd);
        void launchCreateAccountFragment();
    }
}