package br.com.cristiano.etanolougasolina.constantes;

/**
 *  Constantes para auxiliar na criação do banco de tabelas
 */
public class BancoConstantes {

    public static final String DATABASE_NAME = "banco.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "abastecimento";


    public static final String COLUNA_ID = "id";
    public static final String COLUNA_COMBUSTIVEL = "combustivel";
    public static final String COLUNA_DATA = "data_abastecimento";
    public static final String COLUNA_LITROS = "litros";
    public static final String COLUNA_VALOR = "valor";
    public static final String COLUNA_LOCAL = "local";


    public static final String CREATE_TABLE = "CREATE TABLE "+ TABLE_NAME +"(" + COLUNA_ID + " INT NOT NULL AUTOINCREMENT PRIMARY KEY, " +
                                                COLUNA_COMBUSTIVEL +" VARCHAR(15), " + COLUNA_DATA + " VARCHAR(30)" +
                                                COLUNA_LITROS + " REAL, " + COLUNA_VALOR +" REAL, " + COLUNA_LOCAL + " VARCHAR(50))";

    public static final String DROP_TABLE = "DROP IF EXISTS TABLE " + TABLE_NAME;
}
