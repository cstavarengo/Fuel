package br.com.cristiano.etanolougasolina.adapters;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.cristiano.etanolougasolina.R;
import br.com.cristiano.etanolougasolina.constantes.ConstantesApp;
import br.com.cristiano.etanolougasolina.interfaces.AdapterInterfaceOnClick;
import br.com.cristiano.etanolougasolina.interfaces.AdapterInterfaceOnLongClick;
import br.com.cristiano.etanolougasolina.model.Abastecimento;



public class AbastecimentoAdapter extends RecyclerView.Adapter <AbastecimentoAdapter.ViewHolderAbastecimento> {


    private final static String TAG = ConstantesApp.TAG;

    private List<Abastecimento> abastecimentos = new ArrayList<>();
    private AdapterInterfaceOnClick adapterInterfaceOnClick;
    private AdapterInterfaceOnLongClick adapterInterfaceOnLongClick;

    public AbastecimentoAdapter(List<Abastecimento> abastecimentos) {
        this.abastecimentos = abastecimentos;
    }

    public void setAdapterInterfaceOnClick(AdapterInterfaceOnClick adapterInterfaceOnClick){
        this.adapterInterfaceOnClick = adapterInterfaceOnClick;
    }

    public void setAdapterInterfaceOnLongClick(AdapterInterfaceOnLongClick adapterInterfaceOnLongClick) {
        this.adapterInterfaceOnLongClick = adapterInterfaceOnLongClick;
    }

    public List<Abastecimento> getAbastecimentos() {
        return abastecimentos;
    }

    @NonNull
    @Override
    public ViewHolderAbastecimento onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.raw_abastecimento, parent, false);
        ViewHolderAbastecimento viewHolderAbastecimento = new ViewHolderAbastecimento(view);

        return viewHolderAbastecimento;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderAbastecimento holder, final int position) {

        if(abastecimentos != null && abastecimentos.size() > 0) {
            Abastecimento abastecimento = abastecimentos.get(position);

            holder.textViewId.setText(String.valueOf(abastecimento.getId()));
            holder.textViewData.setText("DATA: " + abastecimento.getData_abastecimento());
            holder.textViewCombustivel.setText("COMBUSTÍVEL: " + abastecimento.getCombustivel());
            holder.textViewValor.setText("VALOR: R$" + abastecimento.getValor());
            holder.textViewLitros.setText("LITROS: " + abastecimento.getLitros());
            holder.textViewLocal.setText("LOCAL: " + abastecimento.getLocal());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterInterfaceOnClick.onClick(position);
            }
        });


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                adapterInterfaceOnLongClick.longClick(position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.abastecimentos.size();
    }



    /**
     *    Classe view holder que será responsável por
     *      retornar um objeto completo
     */
    public class ViewHolderAbastecimento extends RecyclerView.ViewHolder {

        public TextView textViewId;
        public TextView textViewLocal;
        public TextView textViewLitros;
        public TextView textViewValor;
        public TextView textViewData;
        public TextView textViewCombustivel;

        public ViewHolderAbastecimento(@NonNull View itemView) {
            super(itemView);

            textViewId = itemView.findViewById(R.id.textViewId);
            textViewLocal = itemView.findViewById(R.id.textViewLocal);
            textViewCombustivel = itemView.findViewById(R.id.textViewCombustivel);
            textViewData = itemView.findViewById(R.id.textViewData);
            textViewLitros = itemView.findViewById(R.id.textViewLitros);
            textViewValor = itemView.findViewById(R.id.textViewValor);

        }
    }

}

