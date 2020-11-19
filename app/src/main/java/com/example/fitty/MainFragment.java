package com.example.fitty;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.example.fitty.models.Routine;
import com.google.android.material.appbar.MaterialToolbar;

public class MainFragment extends AllFragments {

    public void setTopBar() {
        MaterialToolbar toolbar = requireActivity().findViewById(R.id.topAppBar);
        toolbar.setNavigationIcon(R.drawable.ic_account_circle);
        toolbar.setTitle(getTitFragment());
    }

    public void OnRoutineClick(Routine routine) {
        Fragment fragment = new RoutineView();
        Bundle bundle = new Bundle();
        bundle.putSerializable("routine", routine);
        fragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction().replace(R.id.main_nav_host_fragment, fragment).commit();
    }
}
