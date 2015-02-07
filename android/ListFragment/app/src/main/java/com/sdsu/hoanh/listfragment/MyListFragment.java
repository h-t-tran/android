package com.sdsu.hoanh.listfragment;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hoanh on 2/7/2015.
 */
public class MyListFragment extends ListFragment {
    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        getActivity().setTitle("My List Fragment");

        List<String> locationArray = new ArrayList<String>();
        for(int i=0;i<10;i++)
        {
            locationArray.add("list frag value "+i);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(),
                //android.R.layout.simple_spinner_dropdown_item,
                //android.R.layout.simple_list_item_1,
                android.R.layout.simple_list_item_single_choice,
                locationArray);

        this.setListAdapter(dataAdapter);

    }
}
