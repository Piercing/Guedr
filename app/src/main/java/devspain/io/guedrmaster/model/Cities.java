package devspain.io.guedrmaster.model;


import java.util.LinkedList;

import devspain.io.guedrmaster.R;

public class Cities {

    // Array genérico que sólo acepta objetos City
    private LinkedList<City> mCities;

    public Cities() {
        mCities = new LinkedList<City>();

        mCities.add(new City("Madrid", new Forecast(25, 10, 30, "Soleado con nubes", R.drawable.sun_cloud)));
        mCities.add(new City("Jaen", new Forecast(36, 23, 319, "Sol a tope", R.drawable.ico_01)));
        mCities.add(new City("Quito", new Forecast(30, 15, 40, "Arcoiris", R.drawable.ico_10)));
    }

    public LinkedList<City> getCities() {
        return mCities;
    }
}

