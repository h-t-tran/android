package com.sdsu.hoanh.assignment2;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class DesertFragment extends ListFragment {

    private static final String DESERT_NAME_KEY = "com.sdsu.hoanh.assignment2.DesertFragment.DESERT_NAME_KEY";
    public static final String _cupCake = "Cupcake";
    public static final String _donut = "Donut";
    public static final String _gingerBread = "Gingerbread";
    public static final String _iceCream = "Ice Cream";
    public static final String _jellyBean = "Jelly Bean";

    private static final int _invalid = -1;
    private int _selectedDesertIdx = _invalid;
    private List<String> _dessertArray = new ArrayList<String>();
    private DesertResultCallback _desertCallbackClient;
    /**
     * Factory method to create a fragment with a bundle containing the passed in desert.
     * @param desert name of the desert we want to pass to the fragment
     * @return the desert fragment instance
     */
    public static DesertFragment newInstance(String desert)
    {
        Bundle arg = new Bundle();
        arg.putCharSequence(DESERT_NAME_KEY, desert);

        DesertFragment frag = new DesertFragment();
        frag.setArguments(arg);
        return frag;
    }

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        //getActivity().setTitle("My List Fragment");

        //
        // set up the dessert selection
        //
        _dessertArray.add(_cupCake);
        _dessertArray.add(_donut);
        _dessertArray.add(_gingerBread);
        _dessertArray.add(_iceCream);
        _dessertArray.add(_jellyBean);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(),
                //android.R.layout.simple_list_item_1,
                //android.R.layout.simple_list_item_single_choice,
                android.R.layout.simple_list_item_checked,
                _dessertArray);

        this.setListAdapter(dataAdapter);

        //
        // get the passed in desert name
        //
        _retrieveInputDesert();
    }

    /**
     * Get the desert passed in from the host activity and select the item in the list.
     */
    private void _retrieveInputDesert()
    {

        Bundle arg = getArguments();
        if(arg != null) {
            String desertName = (String)arg.getCharSequence(DESERT_NAME_KEY);
            // make sure we have a desert.
            if(desertName != null) {
                // find the index of the desert in the list and save it for later.
                _selectedDesertIdx = _dessertArray.indexOf(desertName);
            }
        }

    }



    /**
     * Inform the fragment of the desert to select
     * @param desert
     */
    public void setSelectedDesert(String desert)
    {
        int idx = _dessertArray.indexOf(desert);
        if(idx >= 0)
        {
            _selectedDesertIdx = idx;
            this.getListView().setItemChecked(_selectedDesertIdx, true);
        }
    }

    /**
     *
     * @return the most recent desert selection.
     */
    public String getSelectedDesert()
    {
        return _dessertArray.get(_selectedDesertIdx);
    }
    
    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
        if(a instanceof DesertResultCallback) {
            _desertCallbackClient = (DesertResultCallback) a;
        }
    }

    @Override
    public void onActivityCreated(Bundle bundle)
    {
        super.onActivityCreated(bundle);
        ListView lv = this.getListView();

        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // select the passed in desert if it is valid.
        if(_selectedDesertIdx != _invalid) {
            lv.setItemChecked(_selectedDesertIdx, true);
        }

        // Setting the item click listener for listView
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(_desertCallbackClient != null)
                {
                    String selDesert = _dessertArray.get(position);
                    _desertCallbackClient.onDesertSelected(selDesert);
                }
            }
        });

    }

}
