package br.com.cristiano.etanolougasolina.banco;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import br.com.cristiano.etanolougasolina.constantes.BancoConstantes;

/**
 *  Classe padrão Singleton para criação e conexão com o banco de dados
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper databaseHelper = null;

    public static DatabaseHelper getInstance(Context ctx) {
        if (databaseHelper == null) {
            databaseHelper = new DatabaseHelper(ctx.getApplicationContext());
        }
        return databaseHelper;
    }

    private DatabaseHelper(Context context) {
        super(context, BancoConstantes.DATABASE_NAME, null, BancoConstantes.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BancoConstantes.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(BancoConstantes.DROP_TABLE);
        db.execSQL(BancoConstantes.CREATE_TABLE);
    }
}
