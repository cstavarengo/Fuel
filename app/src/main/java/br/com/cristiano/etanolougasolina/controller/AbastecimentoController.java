package br.com.cristiano.etanolougasolina.controller;

import android.content.Context;

import java.util.List;

import br.com.cristiano.etanolougasolina.interfaces.AbastecimentoService;
import br.com.cristiano.etanolougasolina.model.Abastecimento;
import br.com.cristiano.etanolougasolina.repositorio.AbastecimentoDAO;

/**
 *  Controller para a classe Abastecimento
 */
public class AbastecimentoController implements AbastecimentoService {

    private AbastecimentoDAO abastecimentoDAO;

    /**
     *  Construtor
     * @param context
     */
    public AbastecimentoController(Context context){
        this.abastecimentoDAO = new AbastecimentoDAO(context);
    }


    @Override
    public List<Abastecimento> listarAbastecimentos() {
        return this.abastecimentoDAO.findAll();
    }


    @Override
    public Long salvarAbastecimento(Abastecimento abastecimento) {
        return this.abastecimentoDAO.save(abastecimento);
    }


    @Override
    public Integer alterarAbastecimento(Abastecimento abastecimento) {
        return this.abastecimentoDAO.update(abastecimento);
    }


    @Override
    public Integer apagarAbastecimento(Long id) {
        return this.abastecimentoDAO.delete(id);
    }


}
