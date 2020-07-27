package edu.tacoma.uw.tslinard.tcss450team2project.authenticate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import edu.tacoma.uw.tslinard.tcss450team2project.R;


public class CreateAccountFragment extends Fragment {

    private CreateAccountListener mCreateAccountListener;

    public interface CreateAccountListener {
        public void createAccount(Account account);
    }

    public CreateAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCreateAccountListener = (CreateAccountListener) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_account, container, false);
        getActivity().setTitle("Create Account");
        final EditText firstNameEditText = v.findViewById(R.id.et_first_name);
        final EditText lastNameEditText = v.findViewById(R.id.et_last_name);
        final EditText userNameEditText = v.findViewById(R.id.et_user_name);
        final EditText emailEditText = v.findViewById(R.id.et_email);
        final EditText passwordEditTExt = v.findViewById(R.id.et_password);
        Button signUpButton = v.findViewById(R.id.btn_sign_up);
        signUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                String userName = userNameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditTExt.getText().toString();
                Account account = new Account(firstName, lastName, userName, email, password);
                if(mCreateAccountListener != null){
                    mCreateAccountListener.createAccount(account);
                }
            }
        });
        return v;
    }

}