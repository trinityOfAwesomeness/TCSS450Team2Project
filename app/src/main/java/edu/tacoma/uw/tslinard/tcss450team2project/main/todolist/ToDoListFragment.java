package edu.tacoma.uw.tslinard.tcss450team2project.main.todolist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import edu.tacoma.uw.tslinard.tcss450team2project.R;

/**
 * Class to create and control the To Do List page.
 * @author Seoungdeok Jeon
 * @author Tatiana Linardopoulou
 */
public class ToDoListFragment extends Fragment {
    private View mView;
    private ListView listView;

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
        mView = inflater.inflate(R.layout.fragment_to_do_list, container, false);
        getActivity().setTitle("To Do List");

        listView = mView.findViewById(R.id.listview);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("1. fasdf");
        arrayList.add("2. wer");
        arrayList.add("3. qwgq");

        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getContext(), "Clicked" + position, Toast.LENGTH_SHORT).show();
            }
        });
        return mView;
    }
}