package br.com.cristiano.etanolougasolina.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private Boolean autorizadoVibrar;
    private Boolean autorizadoSoar;

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
                    String gas = String.valueOf(editTextGasolina.getText());
                    String et = String.valueOf(editTextEtanol.getText());
                    VerificadorCombustivel verificadorCombustivel = new VerificadorCombustivel();

                    if(verificadorCombustivel.ehGasolina(Float.valueOf(gas), Float.parseFloat(et)))
                        resposta = "Abasteça com Gasolina!";
                    else
                        resposta = "Abasteça com Etanol!";

                    if(autorizadoSoar)
                        texToSpeech.toPronounce(resposta);

                    textViewResultado.setText(resposta);
                    textViewResultado.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                } else{
                    resposta = "Preencha todos os campos!";

                    if(autorizadoSoar)
                        texToSpeech.toPronounce(resposta);

                    textViewResultado.setText(resposta);
                    textViewResultado.setTextColor(getResources().getColor(R.color.colorAccent));

                    if(autorizadoVibrar) {
                        Vibrar vibrar = new Vibrar(MainActivity.this);
                        vibrar.vibrar(700);
                    }
                }
            }
        });

        pref = MainActivity.this.getSharedPreferences(ConstantesApp.PREFS, Context.MODE_PRIVATE);
        editor = pref.edit();
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

        configuracaoMenuItem();

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
                finish();
                break;

            case R.id.menuItemAbastecimentoAct:
                intent = new Intent(this, AbastecimentoActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.menuItemAbastecimentoSobre:
                exibirAlertaSobre();
                break;

            case R.id.menuItemSom:
                if(autorizadoSoar){
                    editor.putBoolean(ConstantesApp.AUDIO_STATE, false);
                    editor.commit();
                }else{
                    editor.putBoolean(ConstantesApp.AUDIO_STATE, true);
                    editor.commit();
                }
                configuracaoMenuItem();
                break;

            case R.id.menuItemVibracao:
                if(autorizadoVibrar){
                    editor.putBoolean(ConstantesApp.VIBRATION_STATE, false);
                    editor.commit();
                }else{
                    editor.putBoolean(ConstantesApp.VIBRATION_STATE, true);
                    editor.commit();
                }
                configuracaoMenuItem();
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
        String valorG = String.valueOf(editTextGasolina.getText());
        String valorA = String.valueOf(editTextEtanol.getText());

        if(valorA.isEmpty() || valorG.isEmpty())
            return false;

        return true;
    }

    private void configuracaoMenuItem(){
        autorizadoVibrar = pref.getBoolean(ConstantesApp.VIBRATION_STATE, false);
        autorizadoSoar = pref.getBoolean(ConstantesApp.AUDIO_STATE, false);

        if(!autorizadoVibrar)
            vibracao.setIcon(getResources().getDrawable(R.drawable.ic_do_not_disturb_black_24dp));
        else
            vibracao.setIcon(getResources().getDrawable(R.drawable.ic_vibration_black_24dp));


        if(!autorizadoSoar)
            som.setIcon(getResources().getDrawable(R.drawable.ic_volume_off_black_24dp));
        else
            som.setIcon(getResources().getDrawable(R.drawable.ic_volume_up_black_24dp));
    }
}
