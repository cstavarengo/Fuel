package br.com.cristiano.etanolougasolina.api;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.cristiano.etanolougasolina.constantes.ApiConstantes;
import br.com.cristiano.etanolougasolina.constantes.ConstantesApp;
import br.com.cristiano.etanolougasolina.model.Cidade;

public class CidadesApi extends AsyncTask<Void, Void, List<Cidade>> {

    private final static String TAG = ConstantesApp.TAG;
    private List<Cidade> cidades = new ArrayList<>();
    private Long id;

    public CidadesApi(Long idEstado) {
        this.id = idEstado;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected List<Cidade> doInBackground(Void... voids) {

        try {
            URL url = new URL(ApiConstantes.URL_CIDADES + "/" + id);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setConnectTimeout(5000);
            urlConnection.connect();

            try (BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))){
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null){
                    response.append(inputLine);
                }

                Gson g = new GsonBuilder().create();
                Type type = new TypeToken<ArrayList<Cidade>>(){}.getType();
                cidades = g.fromJson(response.toString(), type);
            }

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cidades;
    }
}

