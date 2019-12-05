package br.com.cristiano.etanolougasolina.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import br.com.cristiano.etanolougasolina.R;
import br.com.cristiano.etanolougasolina.aux.TexToSpeech;
import br.com.cristiano.etanolougasolina.aux.VerificadorCombustivel;
import br.com.cristiano.etanolougasolina.aux.Vibrar;

public class MainActivity extends AppCompatActivity {

    private EditText editTextGasolina;
    private EditText editTextEtanol;
    private Button buttonVerificar;
    private TexToSpeech texToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEtanol = findViewById(R.id.etValorEtanol);
        editTextGasolina = findViewById(R.id.etValorGasolina);
        buttonVerificar = findViewById(R.id.buttonVerificar);

        texToSpeech = new TexToSpeech(this);

        buttonVerificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(camposPreenchidos()){
                    double gas = Double.valueOf(editTextGasolina.getText().toString());
                    double et = Double.valueOf(editTextEtanol.getText().toString());
                    String resposta = "";
                    VerificadorCombustivel verificadorCombustivel = new VerificadorCombustivel();

                    if(verificadorCombustivel.ehGasolina(gas, et))
                        resposta = "Abasteça com Gasolina!";
                    else
                        resposta = "Abasteça com Etanol!";

                    Toast.makeText(MainActivity.this, resposta, Toast.LENGTH_SHORT).show();

                    texToSpeech.toPronounce(resposta);

                } else{
                    texToSpeech.toPronounce("Preencha todos os campos!");
                    Vibrar vibrar = new Vibrar(MainActivity.this);
                    vibrar.vibrar(700);
                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        this.texToSpeech.stopTalking();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.texToSpeech.destroy();
    }


    private boolean camposPreenchidos() {
        String valorG = editTextGasolina.getText().toString();
        String valorA = editTextGasolina.getText().toString();

        if(valorA.isEmpty() || valorG.isEmpty())
            return false;

        return true;
    }

}
