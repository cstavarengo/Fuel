package br.com.cristiano.etanolougasolina.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import br.com.cristiano.etanolougasolina.constantes.ConstantesApp;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = ConstantesApp.TAG;

    private EditText editTextGasolina;
    private EditText editTextEtanol;
    private Button buttonVerificar;
    private TextView textViewResultado;

    private TexToSpeech texToSpeech;

    private MenuItem som;
    private MenuItem vibracao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEtanol = findViewById(R.id.etValorEtanol);
        editTextGasolina = findViewById(R.id.etValorGasolina);
        buttonVerificar = findViewById(R.id.buttonVerificar);
        textViewResultado = findViewById(R.id.textViewResultado);

        texToSpeech = new TexToSpeech(this);

        buttonVerificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String resposta = "";

                if(camposPreenchidos()){
                    double gas = Double.valueOf(editTextGasolina.getText().toString());
                    double et = Double.valueOf(editTextEtanol.getText().toString());
                    VerificadorCombustivel verificadorCombustivel = new VerificadorCombustivel();

                    if(verificadorCombustivel.ehGasolina(gas, et))
                        resposta = "Abasteça com Gasolina!";
                    else
                        resposta = "Abasteça com Etanol!";

                    texToSpeech.toPronounce(resposta);
                    textViewResultado.setText(resposta);

                } else{
                    resposta = "Preencha todos os campos!";
                    texToSpeech.toPronounce(resposta);
                    textViewResultado.setText(resposta);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        som = menu.findItem(R.id.menuItemSom);
        vibracao = menu.findItem(R.id.menuItemVibracao);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;

        switch (item.getItemId()){
            case R.id.menuItemNovoAbastecimento:
                intent = new Intent(this, AbastecimentoActivity.class);
                intent.putExtra("flag", true);
                startActivity(intent);
                break;
            case R.id.menuItemAbastecimentoAct:
                intent = new Intent(this, AbastecimentoActivity.class);
                startActivity(intent);
                break;
            case R.id.menuItemAbastecimentoSobre:
                exibirAlertaSobre();
                break;
        }
        return true;
    }

    private void exibirAlertaSobre() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setCancelable(true)
                .setView(R.layout.layout_sobre);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean camposPreenchidos() {
        String valorG = editTextGasolina.getText().toString();
        String valorA = editTextGasolina.getText().toString();

        if(valorA.isEmpty() || valorG.isEmpty() || valorA == null || valorG == null)
            return false;

        return true;
    }

}
