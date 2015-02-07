package com.sdsu.hoanh.listfragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoanh on 2/6/2015.
 */
public class MyFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    public MyFragment() {

        java.util.List<String> l=null;
        //List<String> lables = null;
        //ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(null, 5, null);
    }

    @Override
    public void onAttach(Activity act)
    {
        super.onAttach(act);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_blank, container, false);

        Activity ownerAct = this.getActivity();

        final Spinner spinner = (Spinner)rootView.findViewById(R.id.spinner2);
        List<String> locationArray = new ArrayList<String>();
        for(int i=0;i<10;i++)
        {
            locationArray.add("value "+i);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ownerAct,
                android.R.layout.simple_spinner_item,
                locationArray);

        // Drop down layout style - list view with radio button
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);


        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(this);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position,
                                       long id) {
                // On selecting a spinner item
                String label = parent.getItemAtPosition(position).toString();

                // Showing selected spinner item
                //Toast.makeText(parent.getContext(), "You selected: " + label,  Toast.LENGTH_LONG).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        //
        // button
        //
        Button btn = (Button)rootView.findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        _populateListview(rootView);

        return rootView;
    }

    private void _populateListview(View rootView)
    {
        ListView myListView = (ListView) rootView.findViewById(R.id.listView);
        ArrayList<String> myStringArray1 = new ArrayList<String>();
        for(int i=0;i<10;i++)
        {
            myStringArray1.add("value "+i);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, myStringArray1);

        //CustomAdapter adapter = new CustomAdapter(getActivity(), R.layout.row, myStringArray1);
        myListView.setAdapter(dataAdapter);


        myListView.setItemChecked(1, true);
        myListView.setBackgroundColor(Color.BLUE);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
//                view.animate().setDuration(2000).alpha(0)
//                        .withEndAction(new Runnable() {
//                            @Override
//                            public void run() {
//                                list.remove(item);
//                                adapter.notifyDataSetChanged();
//                                view.setAlpha(1);
//                            }
//                        });
            }

        });
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // On selecting a spinner item
        String label = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "You selected: " + label,  Toast.LENGTH_LONG).show();

    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
}
