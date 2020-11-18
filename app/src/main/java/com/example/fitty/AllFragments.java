package com.example.fitty;

import android.content.res.Configuration;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

public class AllFragments extends Fragment {

    String titFragment;

    public void setTitle(String title) {
        titFragment = title;
    }

    public String getTitFragment() {
        return titFragment;
    }

    public void orientationChange(GridLayoutManager gridLayoutManager, Configuration configuration) {
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            gridLayoutManager.setSpanCount(4);
        } else {
            gridLayoutManager.setSpanCount(2);
        }
    }
}
