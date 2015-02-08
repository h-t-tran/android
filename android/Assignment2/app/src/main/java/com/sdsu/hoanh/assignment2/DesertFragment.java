package com.sdsu.hoanh.assignment2;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class DesertFragment extends ListFragment {

    private static final String _cupCake = "Cupcake";
    private static final String _donut = "Donut";
    private static final String _gingerBread = "Gingerbread";
    private static final String _iceCream = "Ice Cream";
    private static final String _jellyBean = "Jelly Bean";

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        getActivity().setTitle("My List Fragment");

        List<String> dessertArray = new ArrayList<String>();
        dessertArray.add(_cupCake);
        dessertArray.add(_donut);
        dessertArray.add(_gingerBread);
        dessertArray.add(_iceCream);
        dessertArray.add(_jellyBean);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(),
                //android.R.layout.simple_list_item_1,
                android.R.layout.simple_list_item_single_choice,
                dessertArray);

        this.setListAdapter(dataAdapter);

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

}
