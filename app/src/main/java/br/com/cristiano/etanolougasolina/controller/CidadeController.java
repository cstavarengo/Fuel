package br.com.cristiano.etanolougasolina.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.cristiano.etanolougasolina.api.CidadesApi;
import br.com.cristiano.etanolougasolina.interfaces.CidadeService;
import br.com.cristiano.etanolougasolina.model.Cidade;

public class CidadeController implements CidadeService {

    private List<Cidade> cidades = new ArrayList<>();
    private CidadesApi api;


    @Override
    public List<Cidade> findAll(Long idEstado) {
        api = new CidadesApi(idEstado);
        try {
            cidades = api.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            return cidades;
        }
    }
}
