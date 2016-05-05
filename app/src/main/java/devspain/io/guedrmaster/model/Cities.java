package devspain.io.guedrmaster.model;


import java.util.LinkedList;

public class Cities {

    // Array genérico que sólo acepta objetos City
    private LinkedList<City> mCities;

    public Cities() {
        mCities = new LinkedList<City>();

        mCities.add(new City("Madrid"));
        mCities.add(new City("Jaen"));
        mCities.add(new City("Quito"));
    }

    public LinkedList<City> getCities() {
        return mCities;
    }
}

