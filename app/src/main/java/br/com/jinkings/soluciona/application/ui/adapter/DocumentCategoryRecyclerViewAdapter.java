package br.com.jinkings.soluciona.application.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.jinkings.financing.R;
import br.com.jinkings.soluciona.domain.model.DocumentCategory;

public class DocumentCategoryRecyclerViewAdapter extends RecyclerView.Adapter<DocumentCategoryRecyclerViewAdapter.ViewHolder> {

    TypedValue typedValue = new TypedValue();

    Context context;
    List<DocumentCategory> documentCategories;

    OnItemClickListener onItemClickListener;

    public DocumentCategoryRecyclerViewAdapter(Context context, List<DocumentCategory> documentCategories) {
        this.context = context;
        this.documentCategories = documentCategories;
    }

    @Override
    public DocumentCategoryRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_document, parent, false);
        view.setBackgroundResource(typedValue.resourceId);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final DocumentCategory documentCategory = documentCategories.get(position);

        holder.textViewDocumentName.setText(documentCategory.getName());
        holder.textViewDocumentDescription.setText(documentCategory.getDescription());
        holder.view.setBackgroundResource(R.drawable.btn_flat_selector);
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(documentCategory);
            }
        });
    }

    @Override
    public int getItemCount() {
        return documentCategories.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView textViewDocumentName;
        public final TextView textViewDocumentDescription;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.textViewDocumentName = (TextView) view.findViewById(R.id.textview_document_name);
            this.textViewDocumentDescription = (TextView) view.findViewById(R.id.textview_document_description);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentCategory documentCategory);
    }
}
