package devspain.io.guedrmaster.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import devspain.io.guedrmaster.R;
import devspain.io.guedrmaster.model.Cities;
import devspain.io.guedrmaster.model.City;

/**
 * A simple {@link Fragment} subclass.
 */
public class CityListFragment extends Fragment {

    // Guardo una referencia a mi Listener
    private CityListListener mCityListListener;

    public CityListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Obtengo el root con el xml inflado que es donde esta la lista de las ciudades
        View root = inflater.inflate(R.layout.fragment_city_list, container, false);

        // Accedo a LisView, com le he dado un id de Andorid no pongo R sólo sino anteponiendo android
        ListView list = (ListView) root.findViewById(android.R.id.list);

        // Necesito un modelo con el que darle valores a la lista, que son las Cities
        final Cities cities = new Cities();

        // Creamos un adaptador para dárselo a la lista y que sepa qué datos mostrar
        ArrayAdapter<City> adapter = new ArrayAdapter<City>(
                getActivity(), // El contexto que necesita
                android.R.layout.simple_list_item_1, // El layout para saber cómo dibujar cada fila, usasmos uno por defecto
                cities.getCities());// Los datos que debe mostrar

        // Le asignamos el adaptador a la lista
        list.setAdapter(adapter);

        // Pongo a la escucha a la lista para que se entere cuando se pulse en ella
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Aviso a mi actividad, creando una interfaz, a modo de los delegados en iOS
                // para poder avisar de algo, para decir cómo son las cosas de las que avisa
                // este 'cityListFragment' a la actividad 'ForecastActivity'.
                if (mCityListListener != null) { // Compruebo tiene asociada alguna actividad
                    // Aquí puedo avisar a mi listener. Tengo que sacar la ciudad que se ha pulsado.
                    City citySelected = cities.getCities().get(position);
                    // Llamo al listener con la ciudad seleccionada y la posición por si la necesitara.
                    mCityListListener.onCitySelected(citySelected, position);
                }
            }
        });
        // Como siempre devolvemos el root
        return root;
    }

    // Estos métodos se llamarán cuando el Fragment esté enganchado
    //  a la  actividad, evitamos así algún 'NullPointerException'.

    // Aquí es donde decimos que el 'CityListListener' va a ser la actividad,
    // que en definitiva es la que se tiene que enterar cuando la lista ha
    // sido pulsada y avisada por este fragment mediante la interfaz.
    // ES DECIR, EL LISTENER DE ESTE FRAGMENT ES LA ACTIVIDAD ==> 'LifeCicle'
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Como aquí ya sé que el Frgmente está encganchado a la actvidad
        // le puedo decir que mi 'mCityListListener va a ser la actividad'.
        // Como supongo que la actividad implementa la interfaz lo casteo.
        mCityListListener = (CityListListener) getActivity();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Como aquí ya sé que el Frgmente está encganchado a la actvidad
        // le puedo decir que mi 'mCityListListnerva' a ser la actividad'.
        mCityListListener = (CityListListener) activity;
    }

    // Este es el caso contrario al 'onAttach', cuando el fragment deja de
    // estar dentro de mi actividad es decirle que 'CityListListener' es null
    // Es decir, cuando lo eliminemos y deje de estar disponible, ahorrando memoria.
    @Override
    public void onDetach() {
        super.onDetach();
        mCityListListener = null;
    }

    // Creo mi interfaz pública, ésta la tiene que implementar la actividad 'ForecastActivity'
    // que es la que se tiene que enterar cuando el usuario ha seleccionado una ciudad en list.
    public interface CityListListener {

        // Método para avisar a 'ForecastActivity' cuándo se ha seleccionado
        // una ciudad, ya que esta actividad implementa dicha interfaz
        void onCitySelected(City city, int position);
    }

}
