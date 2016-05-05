package devspain.io.guedrmaster.model;


import java.io.Serializable;

public class City implements Serializable{

    private String mName;
    private Forecast mForecast;

    // Añado un nuevo constructor, ya que al descargar datos
    // de Internet 'City' no siempre va ha tener un forecast
    // como tenemos ahora almacenado, sino que lo tenemos que
    // bajar de Internet con lo cual sólo me hará falta el nombre
    public City(String name) {
        mName = name;
    }

    public City(String name, Forecast forecast) {
        mName = name;
        mForecast = forecast;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Forecast getForecast() {
        return mForecast;
    }

    public void setForecast(Forecast forecast) {
        mForecast = forecast;
    }

    @Override
    public String toString() {
        return getName();
    }
}
