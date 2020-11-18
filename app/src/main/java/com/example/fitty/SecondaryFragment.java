package com.example.fitty;

import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;

public class SecondaryFragment extends AllFragments implements View.OnClickListener {

    MaterialToolbar toolbar;
    AllFragments lastFragment;

    public SecondaryFragment(AllFragments fragment) {
        lastFragment = fragment;
    }

    @Override
    public void onClick(View v) {
        toolbar.setNavigationIcon(R.drawable.ic_account_circle);
        toolbar.setOnClickListener(null);
        toolbar.setTitle(lastFragment.getTitFragment());
        getParentFragmentManager().beginTransaction().replace(R.id.main_nav_host_fragment, lastFragment).commit();
    }

    public void setTopBar() {
        toolbar = requireActivity().findViewById(R.id.topAppBar);
        toolbar.setTitle(titFragment);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setOnClickListener(this);
    }
}
