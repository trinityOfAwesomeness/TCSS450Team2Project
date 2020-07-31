package edu.tacoma.uw.tslinard.tcss450team2project.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.tacoma.uw.tslinard.tcss450team2project.R;

public class WeeklyScheduleFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Weekly Schedule");
        return inflater.inflate(R.layout.fragment_weekly_schedule, container, false);
    }
}