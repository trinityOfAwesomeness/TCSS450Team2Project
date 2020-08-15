package edu.tacoma.uw.tslinard.tcss450team2project.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import edu.tacoma.uw.tslinard.tcss450team2project.R;

/**
 * Class to create and control the Reset Password page.
 *
 * @author Seoungdeok Jeon
 * @author Tatiana Linardopoulou
 */
public class ResetPasswordFragment extends Fragment {

    private ResetPasswordListener mResetPasswordListener;

    /**
     * Called to do initial creation of ResetPasswordFragment.
     *
     * @param savedInstanceState - If the fragment is being re-created from a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mResetPasswordListener = (ResetPasswordListener) getActivity();
    }

    /**
     * Called to have the fragment instantiate its user interface view
     * with fragment_reset_password.xml file.
     *
     * @param inflater           - The LayoutInflater object that can be used to inflate views in the fragment.
     * @param container          - If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState - If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return - View for the fragment's UI
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        final EditText newPasswordEditText = view.findViewById(R.id.et_new_password);
        Button resetPasswordButton = view.findViewById(R.id.btn_reset_password);

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = newPasswordEditText.getText().toString();
                if (mResetPasswordListener != null) {
                    mResetPasswordListener.resetPassword(newPassword);
                    getFragmentManager().popBackStack();
                }
            }
        });
        return view;
    }

    /**
     * Interface for resetting password.
     */
    public interface ResetPasswordListener {
        /**
         * Resetting password through
         * posting the new password to the web service.
         *
         * @param newPassword - new password
         */
        void resetPassword(String newPassword);
    }
}