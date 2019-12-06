package br.com.cristiano.etanolougasolina.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.cristiano.etanolougasolina.api.EstadosApi;
import br.com.cristiano.etanolougasolina.interfaces.EstadoService;
import br.com.cristiano.etanolougasolina.model.Estado;

public class EstadoController implements EstadoService {

    EstadosApi api;

    @Override
    public List<Estado> findAll() {

        List<Estado> estados = new ArrayList<>();
        api = new EstadosApi();

        try {
            estados = api.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            return estados;
        }
    }
}
