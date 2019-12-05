package br.com.cristiano.etanolougasolina.aux;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

/**
 *  Classe vibrar o telefone
 */
public class Vibrar {

    private Context context;

    // Construtor recebe contexto
    public Vibrar(Context context) {
        this.context = context;
    }


    public void vibrar(int milisegundos){
        Vibrator v = (Vibrator) context.getSystemService(Context. VIBRATOR_SERVICE ) ;
        if(v != null){
            if (Build.VERSION. SDK_INT >= Build.VERSION_CODES. O ) {
                v.vibrate(VibrationEffect.createOneShot (milisegundos, VibrationEffect. DEFAULT_AMPLITUDE)) ;
            } else {
                v.vibrate( milisegundos ) ;
            }
        }
    }

}
