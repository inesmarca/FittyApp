package com.example.fitty;

import android.view.View;

import androidx.navigation.Navigation;

import com.google.android.material.appbar.MaterialToolbar;

public class SecondaryFragment extends AllFragments implements View.OnClickListener {

    MaterialToolbar toolbar;
    AllFragments lastFragment;

    public void setTopBar() {
        toolbar = requireActivity().findViewById(R.id.topAppBar);
        toolbar.setTitle(titFragment);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        toolbar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Navigation.findNavController(requireView()).navigateUp();
    }
}
