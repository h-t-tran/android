package com.sdsu.hoanh.listfragment;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
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
                android.R.layout.simple_list_item_1,
                //android.R.layout.simple_list_item_single_choice,
                locationArray);

        this.setListAdapter(dataAdapter);

    }

    @Override
    public void onAttach(Activity act)
    {
        super.onAttach(act);

    }

    @Override
    public void onActivityCreated(Bundle bundle)
    {
        super.onActivityCreated(bundle);

        ListView lv = this.getListView();
        lv.setSelection(2);

        Object o = lv.getSelectedItem();
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        int lightGreen = 0xA9BCF500;
        ColorDrawable cd = new ColorDrawable(lightGreen);
        lv.setSelector(cd);
    }

//    private static void setDefaultListSelector(ListView listView, Context context) {
//        TypedArray typedArray = context.obtainStyledAttributes(null, new int[]{android.R.attr.listSelector}, android.R.attr.listViewStyle, 0);
//        Drawable drawable = typedArray.getDrawable(0);
//        listView.setSelector(drawable);
//    }
}
