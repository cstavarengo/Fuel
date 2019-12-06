package br.com.cristiano.etanolougasolina.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.cristiano.etanolougasolina.R;
import br.com.cristiano.etanolougasolina.adapters.AbastecimentoAdapter;
import br.com.cristiano.etanolougasolina.api.CidadesApi;
import br.com.cristiano.etanolougasolina.api.EstadosApi;
import br.com.cristiano.etanolougasolina.aux.Internet;
import br.com.cristiano.etanolougasolina.aux.TexToSpeech;
import br.com.cristiano.etanolougasolina.aux.Vibrar;
import br.com.cristiano.etanolougasolina.constantes.ConstantesApp;
import br.com.cristiano.etanolougasolina.controller.AbastecimentoController;
import br.com.cristiano.etanolougasolina.interfaces.AdapterInterfaceOnClick;
import br.com.cristiano.etanolougasolina.interfaces.AdapterInterfaceOnLongClick;
import br.com.cristiano.etanolougasolina.model.Abastecimento;
import br.com.cristiano.etanolougasolina.model.Cidade;
import br.com.cristiano.etanolougasolina.model.Estado;

public class AbastecimentoActivity extends AppCompatActivity {

    private final static String TAG = ConstantesApp.TAG;
    private int posicao;

    private SharedPreferences pref;
    private Vibrar vibrar;
    private TexToSpeech texToSpeech;

    private Button adicionar;
    private Button cancelar;
    private Button alterar;
    private Button cancelarAlt;

    private Spinner spinnerEstados;
    private Spinner spinnerCidades;

    private TextView textViewIdAlterar;

    private EditText editTextLitros;
    private EditText editTextValor;
    private EditText editTextAlterarLitros;
    private EditText editTextAlterarValor;

    private RadioButton radioGas;
    private RadioButton radioEta;
    private RadioButton radioGasAlt;
    private RadioButton radioEtaAlt;

    private RecyclerView recyclerView;
    private AlertDialog alertDialog;
    private AlertDialog alertDialogAlt;
    private AlertDialog.Builder builder;
    private AlertDialog.Builder builderAlt;

    private View layout_abastecimento;
    private View layout_alterar;
    private LayoutInflater inflater;

    private LinearLayoutManager linearLayoutManager;

    private AbastecimentoController abastecimentoController;
    private AbastecimentoAdapter abastecimentoAdapter;
    private List<Abastecimento> abastecimentos;

    private ArrayAdapter<Estado> adapterEstado;
    private ArrayAdapter<Cidade> adapterCidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abastecimento);

        pref = getApplicationContext().getSharedPreferences(ConstantesApp.PREFS, Context.MODE_PRIVATE);
        vibrar = new Vibrar(this);
        texToSpeech = new TexToSpeech(this);

        recyclerView = findViewById(R.id.recyclerView);

        inflater = LayoutInflater.from(this);
        layout_abastecimento = inflater.inflate(R.layout.layout_novo_abastecimento, null, false);
        layout_alterar       = inflater.inflate(R.layout.layout_alteracao, null, false);

        linearLayoutManager = new LinearLayoutManager(this);

        abastecimentoController = new AbastecimentoController(this);
        abastecimentos = abastecimentoController.listarAbastecimentos();

        abastecimentoAdapter = new AbastecimentoAdapter(abastecimentos);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(abastecimentoAdapter);

        configuracaoObjetos(); // configura os objetos da view de inserção de novo abastecimento

        abastecimentoAdapter.setAdapterInterfaceOnClick(new AdapterInterfaceOnClick() {
            @Override
            public void onClick(int position) {
                posicao = position;
                Abastecimento abastecimento = abastecimentoAdapter.getAbastecimentos().get(position);
                textViewIdAlterar.setText(String.valueOf(abastecimento.getId()));
                editTextAlterarLitros.setText(String.valueOf(abastecimento.getLitros()));
                editTextAlterarValor.setText(String.valueOf(abastecimento.getValor()));
                if(abastecimento.getCombustivel().equals(ConstantesApp.GASOLINA))
                    radioGasAlt.setChecked(true);
                else
                    radioEtaAlt.setChecked(true);
                abrirTelaAlteracao();
            }
        });

        abastecimentoAdapter.setAdapterInterfaceOnLongClick(new AdapterInterfaceOnLongClick() {
            @Override
            public void longClick(final int position) {
                posicao = position;
                Abastecimento abastecimento = abastecimentoAdapter.getAbastecimentos().get(position);
                final Long id = abastecimento.getId();

                AlertDialog.Builder builder = new AlertDialog.Builder(AbastecimentoActivity.this);
                builder.setTitle("Apagar Abastecimento")
                        .setMessage("Deseja apagar o registro de abastecimento?")
                        .setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("APAGAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                abastecimentoController.apagarAbastecimento(id);
                                abastecimentoAdapter.getAbastecimentos().remove(posicao);
                                abastecimentoAdapter.notifyItemRemoved(position);
                            }
                        });

                AlertDialog a = builder.create();
                a.show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Se vier a flag setada, abre direto para adicionar novo abastecimento
        Intent intent = getIntent();
        boolean flag = intent.getBooleanExtra("flag", false);
        if(flag)
            abrirTelaNovoAbastecimento();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_abastecimento, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItemADD:
                abrirTelaNovoAbastecimento();
                break;
            case android.R.id.home:
                Log.d(TAG, "onBackPressed: ");
                Intent internet = new Intent(this, MainActivity.class);
                startActivity(internet);
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent internet = new Intent(this, MainActivity.class);
        startActivity(internet);
        finish();
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

    private void abrirTelaNovoAbastecimento() {
        if(Internet.internetConectada(this)) {
            alertDialog.show();
            carregarEstados();
        } else {
            Toast.makeText(this, "Necessário conexão com a Internet!", Toast.LENGTH_SHORT).show();
        }
    }

    private void abrirTelaAlteracao(){
        alertDialogAlt.show();
    }

    private void adicionarNovoAbastecimento() {
        String valor = editTextValor.getText().toString();
        String litros = editTextLitros.getText().toString();
        String combustivel = radioGas.isChecked()? radioGas.getText().toString(): radioEta.getText().toString();

        Estado estado = (Estado) spinnerEstados.getSelectedItem();
        Cidade cidade = (Cidade) spinnerCidades.getSelectedItem();
        String local = cidade.getNome() + "-" + estado.getSigla();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String data = simpleDateFormat.format(date);

        Abastecimento abastecimento = new Abastecimento.Builder()
                .combustivel(combustivel)
                .data(data)
                .litros(Float.parseFloat(litros))
                .valor(Float.parseFloat(valor))
                .local(local)
                .build();

        Long id = abastecimentoController.salvarAbastecimento(abastecimento);
        abastecimento.setId(id);

        this.abastecimentoAdapter.getAbastecimentos().add(0, abastecimento);  // insere o contato na primeira posição da lista
        this.recyclerView.scrollToPosition(0);      // scroll para o topo da recycler view
        this.abastecimentoAdapter.notifyItemInserted(0); // atualizar a lista quando inserido na posição 0
        limparCampos();
        alertDialog.dismiss();
    }

    private void configuracaoObjetos(){
        configurarAlertNovoAbastecimennto();
        configurarAlertAlteracaoAbastecimento();
        configurarAlertAlteracaoAbastecimento();
    }

    private void configurarAlertAlteracaoAbastecimento() {
        builderAlt = new AlertDialog.Builder(this);
        builderAlt.setView(layout_alterar).setCancelable(false);
        alertDialogAlt = builderAlt.create();

        alterar = layout_alterar.findViewById(R.id.buttonAlterarAbastecimento);
        cancelarAlt = layout_alterar.findViewById(R.id.buttonCancelarAlteracao);

        editTextAlterarLitros = layout_alterar.findViewById(R.id.editTextLitrosAlterar);
        editTextAlterarValor = layout_alterar.findViewById(R.id.editTextValorAlterar);

        textViewIdAlterar = layout_alterar.findViewById(R.id.textViewIdAlterar);

        radioGasAlt = layout_alterar.findViewById(R.id.radioButtonGasolinaAlterar);
        radioEtaAlt = layout_alterar.findViewById(R.id.radioButtonEtanolAlterar);

        alterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Abastecimento abastecimento = abastecimentoController.buscarAbastecimento(Long.valueOf(textViewIdAlterar.getText().toString()));

                if (abastecimento != null) {
                    String litros = editTextAlterarLitros.getText().toString();
                    String valor = editTextAlterarValor.getText().toString();
                    if (valor.isEmpty() || litros.isEmpty()) {
                        String alerta = "Não deixe campos vazios!";
                        if(pref.getBoolean(ConstantesApp.VIBRATION_STATE, false))
                            vibrar.vibrar(600);
                        if(pref.getBoolean(ConstantesApp.AUDIO_STATE, false))
                            texToSpeech.toPronounce(alerta);
                        Toast.makeText(AbastecimentoActivity.this, alerta, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (radioGasAlt.isChecked())
                            abastecimento.setCombustivel(ConstantesApp.GASOLINA);
                        else
                            abastecimento.setCombustivel(ConstantesApp.ETANOL);
                        abastecimento.setLitros(Float.parseFloat(litros));
                        abastecimento.setValor(Float.parseFloat(valor));
                        abastecimentoController.alterarAbastecimento(abastecimento);
                        alertDialogAlt.dismiss();

                        abastecimentoAdapter.getAbastecimentos().set(posicao, abastecimento);
                        abastecimentoAdapter.notifyItemChanged(posicao);
                    }
                }
            }
        });

        cancelarAlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogAlt.dismiss();
            }
        });
    }

    private void configurarAlertNovoAbastecimennto() {
        builder = new AlertDialog.Builder(this);
        builder.setView(layout_abastecimento).setCancelable(true);
        alertDialog = builder.create();

        adicionar = layout_abastecimento.findViewById(R.id.buttonAdicionarNovoAbastecimento);
        cancelar = layout_abastecimento.findViewById(R.id.buttonCancelar);

        spinnerCidades = layout_abastecimento.findViewById(R.id.spinnerCidades);
        spinnerEstados = layout_abastecimento.findViewById(R.id.spinnerEstados);

        editTextLitros = layout_abastecimento.findViewById(R.id.editTextLitros);
        editTextValor = layout_abastecimento.findViewById(R.id.editTextValor);

        radioGas = layout_abastecimento.findViewById(R.id.radioButtonGasolina);
        radioEta = layout_abastecimento.findViewById(R.id.radioButtonEtanol);

        adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(camposPreenchidos()){
                    adicionarNovoAbastecimento();
                    limparCampos();
                } else {
                    String alerta = "Por favor, preencha todos os campos!";

                    if(pref.getBoolean(ConstantesApp.VIBRATION_STATE, false))
                        vibrar.vibrar(600);
                    if(pref.getBoolean(ConstantesApp.AUDIO_STATE, false))
                        texToSpeech.toPronounce(alerta);

                    Toast.makeText(AbastecimentoActivity.this, alerta, Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limparCampos();
                alertDialog.dismiss();
            }
        });

        spinnerEstados.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Estado estado = (Estado) parent.getItemAtPosition(position);
                CidadesApi cidadesApi = new CidadesApi(estado.getId());
                try {
                    List<Cidade> cidades = cidadesApi.execute().get();
                    adapterCidade = new ArrayAdapter<Cidade>(AbastecimentoActivity.this, android.R.layout.simple_spinner_item, cidades);
                    spinnerCidades.setAdapter(adapterCidade);
                    spinnerCidades.setSelection(0);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void limparCampos(){
        editTextValor.setText("");
        editTextLitros.setText("");
        spinnerEstados.setAdapter(null);
        spinnerCidades.setAdapter(null);
        adapterEstado.clear();
        adapterCidade.clear();
    }

    private Boolean camposPreenchidos(){
        String valor = editTextValor.getText().toString();
        String litros = editTextLitros.getText().toString();

        if(valor.isEmpty() || valor == null || litros.isEmpty() || litros == null)
            return false;

        return true;
    }

    private void carregarEstados(){
        try {
            EstadosApi estadosApi = new EstadosApi();
            List<Estado> estados = estadosApi.execute().get();
            adapterEstado = new ArrayAdapter<Estado>(AbastecimentoActivity.this, android.R.layout.simple_spinner_item, estados);
            spinnerEstados.setAdapter(adapterEstado);
            estadosApi.cancel(true);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
