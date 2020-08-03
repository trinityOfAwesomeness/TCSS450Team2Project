package edu.tacoma.uw.tslinard.tcss450team2project.main.toDoList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.tacoma.uw.tslinard.tcss450team2project.R;

/**
 * Class to create and control the To Do List page.
 * @author Seoungdeok Jeon
 * @author Tatiana Linardopoulou
 */
public class ToDoListFragment extends Fragment {

    /**
     * Called to have the fragment instantiate its user interface view
     * with fragment_to_do_list.xml file.
     *
     * @param inflater - The LayoutInflater object that can be used to inflate views in the fragment.
     * @param container - If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState - If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return - View for the fragment's UI
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("To Do List");
        return inflater.inflate(R.layout.fragment_to_do_list, container, false);
    }
}