package br.com.cristiano.etanolougasolina.model;

/**
 *  Classe model abastecimento
 */
public class Abastecimento {

    private Long id;
    private String combustivel;
    private String data_abastecimento;
    private float litros;
    private float valor;
    private String local;

    /**
     *  Construtor da classe
     */
    public Abastecimento() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCombustivel() {
        return combustivel;
    }

    public void setCombustivel(String combustivel) {
        this.combustivel = combustivel;
    }

    public String getData_abastecimento() {
        return data_abastecimento;
    }

    public void setData_abastecimento(String data_abastecimento) {
        this.data_abastecimento = data_abastecimento;
    }

    public float getLitros() {
        return litros;
    }

    public void setLitros(float litros) {
        this.litros = litros;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }


    /**
     *  Padrão Builder para construção de objeto Abastecimento
     */
    public static class Builder{

        private Abastecimento abastecimento;

        private Long id;
        private String combustivel;
        private String data;
        private float litros;
        private float valor;
        private String local;

        public Builder(){
            this.abastecimento = new Abastecimento();
        }

        public Builder id(Long id){
            this.abastecimento.setId(id);
            return this;
        }

        public Builder combustivel(String combustivel){
            this.abastecimento.setCombustivel(combustivel);
            return this;
        }

        public Builder data(String data){
            this.abastecimento.setData_abastecimento(data);
            return this;
        }

        public Builder litros(float litros){
            this.abastecimento.setLitros(litros);
            return this;
        }

        public Builder valor(float valor){
            this.abastecimento.setValor(valor);
            return this;
        }

        public Builder local(String local){
            this.abastecimento.setLocal(local);
            return this;
        }

        public Abastecimento build(){
            return this.abastecimento;
        }
    }
}
