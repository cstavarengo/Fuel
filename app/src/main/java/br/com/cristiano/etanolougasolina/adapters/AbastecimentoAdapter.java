package br.com.cristiano.etanolougasolina.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.cristiano.etanolougasolina.R;
import br.com.cristiano.etanolougasolina.interfaces.AdapterInterfaceOnClick;
import br.com.cristiano.etanolougasolina.interfaces.AdapterInterfaceOnLongClick;
import br.com.cristiano.etanolougasolina.model.Abastecimento;



public class AbastecimentoAdapter extends RecyclerView.Adapter <AbastecimentoAdapter.ViewHolderAbastecimento> {

    List<Abastecimento> abastecimentos = new ArrayList<>();
    AdapterInterfaceOnClick adapterInterfaceOnClick;
    AdapterInterfaceOnLongClick adapterInterfaceOnLongClick;

    public AbastecimentoAdapter(List<Abastecimento> abastecimentos) {
        this.abastecimentos = abastecimentos;
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
            holder.textViewData.setText(abastecimento.getData_abastecimento());
            holder.textViewCombustivel.setText(abastecimento.getCombustivel());
            holder.textViewValor.setText(String.valueOf(abastecimento.getValor()));
            holder.textViewLitros.setText(String.valueOf(abastecimento.getLitros()));
            holder.textViewLocal.setText(abastecimento.getLocal());


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

        }
    }

}

