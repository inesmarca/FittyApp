package com.example.fitty.ui;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.example.fitty.R;
import com.example.fitty.api.models.Routine;
import com.google.android.material.appbar.MaterialToolbar;

public class MainFragment extends AllFragments implements View.OnClickListener {
    private View rootView;

    @Override
    public void onClick(View v) {
        getParentFragmentManager().beginTransaction().replace(R.id.main_nav_host_fragment, new UserProfile(this)).commit();
    }

    public void setTopBar() {
        MaterialToolbar toolbar = requireActivity().findViewById(R.id.topAppBar);
        toolbar.setNavigationIcon(R.drawable.ic_account_circle);
        toolbar.setTitle(getTitFragment());
        toolbar.setOnClickListener(this);
    }

    public void OnRoutineClick(Routine routine) {
        Fragment fragment = new RoutineView(this);
        Bundle bundle = new Bundle();
        bundle.putSerializable("routine", routine);
        fragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction().replace(R.id.main_nav_host_fragment, fragment).commit();
    }
}
