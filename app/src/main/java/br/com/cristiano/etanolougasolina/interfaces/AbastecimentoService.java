package br.com.cristiano.etanolougasolina.interfaces;

import java.util.List;

import br.com.cristiano.etanolougasolina.model.Abastecimento;

/**
 *  Interface com os métodos de manipulação dos dados exigidos
 */
public interface AbastecimentoService {

    List<Abastecimento> listarAbastecimentos();

    Abastecimento buscarAbastecimento(Long id);

    Long salvarAbastecimento(Abastecimento abastecimento);

    Integer alterarAbastecimento(Abastecimento abastecimento);

    Integer apagarAbastecimento(Long id);

}
