package com.example.fitty;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitty.adapters.CategoriesAdapter;
import com.example.fitty.models.Category;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Routines#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Routines extends Fragment implements CategoriesAdapter.OnCategoryListener {

    CategoriesAdapter adapter;
    private ArrayList<Category> categories;

    public Routines() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Rutines.
     */
    // TODO: Rename and change types and number of parameters
    public static Routines newInstance(String param1, String param2) {
        Routines fragment = new Routines();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_routines, container, false);

        categories = new ArrayList<>();
        categories.add(new Category(0, "cardio", "cardio"));
        categories.add(new Category(1, "tren superior", "tren superior"));
        categories.add(new Category(2, "piernas", "piernas"));
        categories.add(new Category(3, "fuerza", "fuerza"));
        categories.add(new Category(4, "yoga", "yoga"));
        categories.add(new Category(5, "resistencia", "resistencia"));
        categories.add(new Category(6, "relajacion", "relajacion"));
        categories.add(new Category(7, "elongacion", "elongacion"));

        RecyclerView listView = rootView.findViewById(R.id.listCategories);
        adapter = new CategoriesAdapter(categories, this);
        listView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        listView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void OnCategoryClick(int position) {
        Fragment fragment = new CategoryRoutines();
        Bundle bundle = new Bundle();
        bundle.putInt("idCategory", position);
        bundle.putString("titCategory", categories.get(position).getName());
        fragment.setArguments(bundle);
        getParentFragmentManager().beginTransaction().replace(R.id.main_nav_host_fragment, fragment).commit();
    }
}