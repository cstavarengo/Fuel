package br.com.cristiano.etanolougasolina.repositorio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.cristiano.etanolougasolina.banco.DatabaseHelper;
import br.com.cristiano.etanolougasolina.constantes.BancoConstantes;
import br.com.cristiano.etanolougasolina.model.Abastecimento;

public class AbastecimentoDAO {

    private SQLiteDatabase banco;
    private DatabaseHelper databaseHelper;

    /**
     *
     * @param context
     */
    public AbastecimentoDAO(Context context) {
        databaseHelper = DatabaseHelper.getInstance(context);
        banco = databaseHelper.getWritableDatabase();
    }


    /**
     *  Salva no banco um abastecimento
     * @param abastecimento
     * @return Long
     */
    public Long save(Abastecimento abastecimento){
        ContentValues values = new ContentValues();
        values.put(BancoConstantes.COLUNA_COMBUSTIVEL, abastecimento.getCombustivel());
        values.put(BancoConstantes.COLUNA_DATA, abastecimento.getData_abastecimento());
        values.put(BancoConstantes.COLUNA_LITROS, abastecimento.getLitros());
        values.put(BancoConstantes.COLUNA_VALOR, abastecimento.getValor());
        values.put(BancoConstantes.COLUNA_LOCAL, abastecimento.getLocal());

        return banco.insert(BancoConstantes.TABLE_NAME, null, values);
    }


    /**
     *  Lista todos os abastecimentos do banco
     * @return List
     */
    public List<Abastecimento> findAll(){
        List<Abastecimento> abastecimentos = new ArrayList<>();

        Cursor cursor = banco.query(BancoConstantes.TABLE_NAME, new String[]{BancoConstantes.COLUNA_ID,
                BancoConstantes.COLUNA_COMBUSTIVEL,
                BancoConstantes.COLUNA_DATA,
                BancoConstantes.COLUNA_VALOR,
                BancoConstantes.COLUNA_LITROS,
                BancoConstantes.COLUNA_LOCAL}, null, null, null, null,   null);

        while (cursor.moveToNext()){

            Abastecimento abastecimento = new Abastecimento.Builder()
                    .id(cursor.getLong(cursor.getColumnIndex(BancoConstantes.COLUNA_ID)))
                    .combustivel(cursor.getString(cursor.getColumnIndex(BancoConstantes.COLUNA_COMBUSTIVEL)))
                    .data(cursor.getString(cursor.getColumnIndex(BancoConstantes.COLUNA_DATA)))
                    .litros(cursor.getFloat(cursor.getColumnIndex(BancoConstantes.COLUNA_LITROS)))
                    .valor(cursor.getFloat(cursor.getColumnIndex(BancoConstantes.COLUNA_VALOR)))
                    .local(cursor.getString(cursor.getColumnIndex(BancoConstantes.COLUNA_LOCAL)))
                    .build();

            abastecimentos.add(abastecimento);
        }
        return abastecimentos;
    }


    /**
     *  Retorna um abastecimento de acordo com o ID fornecido
     * @param id
     * @return Abastecimento
     */
    public Abastecimento findOne(Long id){

        Abastecimento abastecimento = null;

        String query = "SELECT * FROM " + BancoConstantes.TABLE_NAME + " WHERE " + BancoConstantes.COLUNA_ID + " = " + id;

        Cursor cursor = banco.rawQuery(query, null);

        if(cursor.moveToFirst()){

            abastecimento = new Abastecimento.Builder()
                    .id(cursor.getLong(cursor.getColumnIndex(BancoConstantes.COLUNA_ID)))
                    .combustivel(cursor.getString(cursor.getColumnIndex(BancoConstantes.COLUNA_COMBUSTIVEL)))
                    .data(cursor.getString(cursor.getColumnIndex(BancoConstantes.COLUNA_DATA)))
                    .litros(cursor.getFloat(cursor.getColumnIndex(BancoConstantes.COLUNA_LITROS)))
                    .valor(cursor.getFloat(cursor.getColumnIndex(BancoConstantes.COLUNA_VALOR)))
                    .local(cursor.getString(cursor.getColumnIndex(BancoConstantes.COLUNA_LOCAL)))
                    .build();
        }
        return abastecimento;
    }


    /**
     *  Alterar Abastecimento
     * @param abastecimento
     * @return integer
     */
    public Integer update(Abastecimento abastecimento){
        ContentValues values = new ContentValues();
        values.put(BancoConstantes.COLUNA_COMBUSTIVEL, abastecimento.getCombustivel());
        values.put(BancoConstantes.COLUNA_DATA, abastecimento.getData_abastecimento());
        values.put(BancoConstantes.COLUNA_LOCAL, abastecimento.getLocal());
        values.put(BancoConstantes.COLUNA_VALOR, abastecimento.getValor());
        values.put(BancoConstantes.COLUNA_LITROS, abastecimento.getLitros());

        return banco.update(BancoConstantes.TABLE_NAME, values, "id = ?", new String[]{abastecimento.getId().toString()});
    }


    /**
     *  Apagar um abastecimento
     * @param id
     * @return integer
     */
    public Integer delete(Long id){
        String[] args = { id.toString() };
        return banco.delete(BancoConstantes.TABLE_NAME, "id = ?", args);
    }
}
