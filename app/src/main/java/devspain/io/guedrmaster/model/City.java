package devspain.io.guedrmaster.model;


import java.io.Serializable;
import java.util.LinkedList;

public class City implements Serializable {

    private String mName;
    // Guardo varias predicciones de distintos días para cada ciudad
    private LinkedList<Forecast> mForecast;

    // Añado un nuevo constructor, ya que al descargar datos
    // de Internet 'City' no siempre va ha tener un forecast
    // como tenemos ahora almacenado, sino que lo tenemos que
    // bajar de Internet con lo cual sólo me hará falta el nombre
    public City(String name) {
        mName = name;
    }

    public City(String name, LinkedList<Forecast> forecast) {
        mName = name;
        mForecast = forecast;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public LinkedList<Forecast> getForecast() {
        return mForecast;
    }

    public void setForecast(LinkedList<Forecast> forecast) {
        mForecast = forecast;
    }

    @Override
    public String toString() {
        return getName();
    }
}
