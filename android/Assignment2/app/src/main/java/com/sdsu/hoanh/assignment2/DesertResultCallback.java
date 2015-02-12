package com.sdsu.hoanh.assignment2;

/**
 * Callback interface invoked by the DesertFragment to notify client of the selected desert
 */
public interface DesertResultCallback{
    /**
     * notify client of the selected desert
     * @param desertName the desert name
     */
    void onDesertSelected(String desertName);
}
