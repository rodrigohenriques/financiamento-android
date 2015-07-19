package br.com.jinkings.soluciona.application.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.jinkings.financing.R;
import br.com.jinkings.soluciona.domain.model.Simulation;

/**
 * Created by rodrigohenriques on 7/4/15.
 */
public class SimulationRecyclerViewAdapter extends RecyclerView.Adapter<SimulationRecyclerViewAdapter.ViewHolder> {

    TypedValue typedValue = new TypedValue();

    Context context;
    List<Simulation> simulations;

    OnItemClickListener onItemClickListener;

    public SimulationRecyclerViewAdapter(Context context, List<Simulation> simulations) {
        this.context = context;
        this.simulations = simulations;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simulation_list_item, parent, false);
        view.setBackgroundResource(typedValue.resourceId);
        return new ViewHolder(view);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final Simulation simulation = simulations.get(position);

        int color;

        if (simulation.getStatus() != null) {
            color = simulation.getStatus().getColor();
        } else {
            color = Color.parseColor("#607D8B");
        }

        holder.textViewPriceAndDate.setText(simulation.getPriceAndDate());
        holder.textViewTypeAndLocation.setText(simulation.getTypeAndLocation());
        holder.textViewStatus.setText(simulation.getStatusString());
        holder.textViewStatus.setTextColor(color);
        holder.view.setBackgroundResource(R.drawable.btn_flat_selector);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(simulation);
            }
        });
    }

    @Override
    public int getItemCount() {
        return simulations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView textViewPriceAndDate;
        public final TextView textViewTypeAndLocation;
        public final TextView textViewStatus;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.textViewPriceAndDate = (TextView) view.findViewById(R.id.value_and_date);
            this.textViewTypeAndLocation = (TextView) view.findViewById(R.id.simulation_type);
            this.textViewStatus = (TextView) view.findViewById(R.id.simulation_status);
        }

        @Override
        public String toString() {
            return "ViewHolder{" +
                    "view=" + view +
                    ", textViewPriceAndDate=" + textViewPriceAndDate +
                    ", textViewTypeAndLocation=" + textViewTypeAndLocation +
                    ", textViewStatus=" + textViewStatus +
                    '}';
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Simulation simulation);
    }
}
