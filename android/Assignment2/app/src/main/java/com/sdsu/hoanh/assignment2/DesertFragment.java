package com.sdsu.hoanh.assignment2;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * The reusable fragment containing the desert list selection.
 */
public class DesertFragment extends ListFragment {

    private static final String DESERT_NAME_KEY = "com.sdsu.hoanh.assignment2.DesertFragment.DESERT_NAME_KEY";
    public static String _cupCake;
    public static String _donut;
    public static String _gingerBread;
    public static String _iceCream;
    public static String _jellyBean;

    private static final int _invalid = -1;
    private int _selectedDesertIdx = _invalid;
    private List<String> _dessertArray = new ArrayList<String>();


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

    /**
     * The creation method
     */
    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        // initialize the menu items
        _cupCake = getResources().getString(R.string.desert_cupcake);
        _donut = getResources().getString(R.string.desert_donut);
        _gingerBread = getResources().getString(R.string.desert_gingerbread);
        _iceCream = getResources().getString(R.string.desert_icecream);
        _jellyBean = getResources().getString(R.string.desert_jellybean);

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
        return _selectedDesertIdx == _invalid ? null :_dessertArray.get(_selectedDesertIdx);
    }

    /**
     * setup the listview and its listener.
     * @param bundle
     */
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
                String selDesert = _dessertArray.get(position);
                DesertFragment.this.setSelectedDesert(selDesert);
            }
        });

        this.registerForContextMenu(lv);

    }

    /**
     * Override to create the desert context menu
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        this.getActivity().getMenuInflater().inflate(R.menu.desert_list, menu);
    }

    /**
     * handler when the context menu is selected.  We save the selected menu
     * @param menu the menu object that is selected
     * @return true always
     */
    @Override
    public boolean onContextItemSelected(MenuItem menu)
    {

        switch (menu.getItemId())
        {
            case R.id.desert_donut:
                setSelectedDesert(_donut);
                break;
            case R.id.dessert_cupcake:
                setSelectedDesert(_cupCake);
                break;
            case R.id.desert_gingerbread:
                setSelectedDesert(_gingerBread);
                break;
            case R.id.desert_jellybean:
                setSelectedDesert(_jellyBean);
                break;
            case R.id.desert_icecream:
                setSelectedDesert(_iceCream);
                break;
        }

        return true;
    }

}
