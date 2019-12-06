package br.com.cristiano.etanolougasolina.views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.com.cristiano.etanolougasolina.R;
import br.com.cristiano.etanolougasolina.adapters.AbastecimentoAdapter;
import br.com.cristiano.etanolougasolina.api.CidadesApi;
import br.com.cristiano.etanolougasolina.api.EstadosApi;
import br.com.cristiano.etanolougasolina.constantes.ConstantesApp;
import br.com.cristiano.etanolougasolina.controller.AbastecimentoController;
import br.com.cristiano.etanolougasolina.interfaces.AdapterInterfaceOnClick;
import br.com.cristiano.etanolougasolina.model.Abastecimento;
import br.com.cristiano.etanolougasolina.model.Cidade;
import br.com.cristiano.etanolougasolina.model.Estado;

public class AbastecimentoActivity extends AppCompatActivity {

    private final static String TAG = ConstantesApp.TAG;

    private Button adicionar;
    private Button cancelar;

    private Spinner spinnerEstados;
    private Spinner spinnerCidades;

    private EditText editTextLitros;
    private EditText editTextValor;

    private RadioButton radioGas;
    private RadioButton radioEta;

    private RecyclerView recyclerView;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;

    private View layout_abastecimento;
    private LayoutInflater inflater;

    private LinearLayoutManager linearLayoutManager;

    private AbastecimentoController abastecimentoController;
    private AbastecimentoAdapter abastecimentoAdapter;
    private List<Abastecimento> abastecimentos;

    private CidadesApi cidadesApi;

    private ArrayAdapter<Estado> adapterEstado;
    private ArrayAdapter<Cidade> adapterCidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abastecimento);

        recyclerView = findViewById(R.id.recyclerView);

        inflater = LayoutInflater.from(this);
        layout_abastecimento = inflater.inflate(R.layout.layout_novo_abastecimento, null, false);

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
                // TODO implementar onClick
                Toast.makeText(AbastecimentoActivity.this, "Position: " + position, Toast.LENGTH_SHORT).show();
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
        }
        return true;
    }

    private void abrirTelaNovoAbastecimento() {
        alertDialog.show();
        carregarEstados();
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
                    Toast.makeText(AbastecimentoActivity.this, "Por favor, preencha todos os campos!", Toast.LENGTH_SHORT).show();
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
