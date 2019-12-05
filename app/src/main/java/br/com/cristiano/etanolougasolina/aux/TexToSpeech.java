package br.com.cristiano.etanolougasolina.aux;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import java.util.Locale;

public class TexToSpeech extends Application implements TextToSpeech.OnInitListener {

    // Objeto da classe que converte texto em voz
    private TextToSpeech textToSpeech;
    private Context context;

    public TexToSpeech(Context context) {
        this.context = context;
        textToSpeech = new TextToSpeech(context, this);
    }


    public void toPronounce(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }else{
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }


    public void stopTalking() {
        textToSpeech.stop();
    }


    @Override
    public void onInit(int status) {

        if(status == TextToSpeech.SUCCESS){
            textToSpeech.setLanguage(new Locale(Locale.getDefault().getLanguage()));
        }else{
            Toast.makeText(context.getApplicationContext(), "Não foi possível ajustar o língua portuguesa", Toast.LENGTH_SHORT).show();
        }
    }


    public void destroy(){
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }
}
