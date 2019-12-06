package br.com.cristiano.etanolougasolina.api;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.cristiano.etanolougasolina.constantes.ApiConstantes;
import br.com.cristiano.etanolougasolina.constantes.ConstantesApp;
import br.com.cristiano.etanolougasolina.model.Estado;

public class EstadosApi extends AsyncTask<Void, Void, List<Estado>> {

    private static final String TAG = ConstantesApp.TAG;

    private List<Estado> estados = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected List<Estado> doInBackground(Void... voids) {

        try {
            URL url = new URL(ApiConstantes.URL_ESTADOS);
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
                Type type = new TypeToken<ArrayList<Estado>>(){}.getType();
                estados = g.fromJson(response.toString(), type);
            }
        } catch (MalformedURLException e){
            Log.e(TAG, "Erro de formatação de url", e);
        } catch (IOException e) {
            Log.e(TAG, "Erro de conexão ", e);
        }

        return this.estados;
    }
}
