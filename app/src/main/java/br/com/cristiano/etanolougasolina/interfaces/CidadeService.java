package br.com.cristiano.etanolougasolina.interfaces;

import java.util.List;

import br.com.cristiano.etanolougasolina.model.Cidade;

public interface CidadeService {

    List<Cidade> findAll(Long idEstado);
}
