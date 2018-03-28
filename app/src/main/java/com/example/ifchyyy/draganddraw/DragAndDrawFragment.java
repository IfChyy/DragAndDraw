package com.example.ifchyyy.draganddraw;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Ivo Georgiev (IfChyy)
 *
 */

public class DragAndDrawFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //init the view with the layout
        View view = inflater.inflate(R.layout.fragment_drag_and_draw, container, false);

        return view;
    }

    //called to create the fragment in a activity
    public DragAndDrawFragment newIns() {
        return new DragAndDrawFragment();
    }
}
