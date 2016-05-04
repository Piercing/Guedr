package devspain.io.guedrmaster.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import devspain.io.guedrmaster.R;
import devspain.io.guedrmaster.model.Cities;
import devspain.io.guedrmaster.model.City;

/**
 * A simple {@link Fragment} subclass.
 */
public class CityListFragment extends Fragment {


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
        Cities cities = new Cities();

        // Creamos un adaptador para dárselo a la lista y que sepa qué datos mostrar
        ArrayAdapter<City> adapter = new ArrayAdapter<City>(
                getActivity(), // El contexto que necesita
                android.R.layout.simple_list_item_1, // El layout para saber cómo dibujar cada fila, usasmos uno por defecto
                cities.getCities());// Los datos que debe mostrar

        // Le asignamos el adaptador a la lista
        list.setAdapter(adapter);

        // Como siempre devolvemos el root
        return root;
    }

}
