package com.sdsu.hoanh.listfragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoanh on 2/6/2015.
 */
public class MyFragment extends Fragment {
    public MyFragment() {

        java.util.List<String> l=null;
        //List<String> lables = null;
        //ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(null, 5, null);
    }

    @Override
    public void onAttach(Activity act)
    {
        super.onAttach(act);
        List<String> locationArray = new ArrayList<String>();
        for(int i=0;i<10;i++)
        {
            locationArray.add("value "+i);
        }
        try
        {
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(act,android.R.layout.simple_spinner_item, locationArray);
        }
        catch(Exception e)
        {
            String s = e.getMessage();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);

        Activity ownerAct = this.getActivity();

        Spinner spinner = (Spinner)rootView.findViewById(R.id.spinner2);
        List<String> locationArray = new ArrayList<String>();
        for(int i=0;i<10;i++)
        {
            locationArray.add("value "+i);
        }

//        java.util.List<String> l=null;
//        List<String> lables = null;
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ownerAct,
                android.R.layout.simple_spinner_item, locationArray);

        // Drop down layout style - list view with radio button
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);


        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        return rootView;
    }
}
