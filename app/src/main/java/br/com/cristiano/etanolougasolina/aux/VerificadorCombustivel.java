package br.com.cristiano.etanolougasolina.aux;

import br.com.cristiano.etanolougasolina.interfaces.VerificarCombustivel;

/**
 *  Classe responsável por verificar qual combustivel é mais vantajoso
 */
public class VerificadorCombustivel implements VerificarCombustivel {

    private final double TAXA = 0.7;

    public VerificadorCombustivel() {
    }

    @Override
    public Boolean ehGasolina(double gasolina, double etanol) {
        return (etanol / gasolina) > TAXA;
    }
}
