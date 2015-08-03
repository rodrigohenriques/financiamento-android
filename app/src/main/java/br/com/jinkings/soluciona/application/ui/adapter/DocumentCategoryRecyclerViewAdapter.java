package br.com.jinkings.soluciona.application.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.jinkings.financing.R;
import br.com.jinkings.soluciona.domain.model.Document;
import br.com.jinkings.soluciona.domain.model.DocumentCategory;
import br.com.jinkings.soluciona.domain.model.Simulation;

public class DocumentCategoryRecyclerViewAdapter extends RecyclerView.Adapter<DocumentCategoryRecyclerViewAdapter.ViewHolder> {

    TypedValue typedValue = new TypedValue();

    Context context;
    List<DocumentCategory> documentCategories;

    OnItemClickListener onItemClickListener;

    Simulation simulation;

    List<Bitmap> thumbs;

    public DocumentCategoryRecyclerViewAdapter(Context context, List<DocumentCategory> documentCategories, Simulation simulation) {
        this.context = context;
        this.documentCategories = documentCategories;
        this.simulation = simulation;

        loadThumbs();
    }

    public void loadThumbs() {

        thumbs = new ArrayList<>(documentCategories.size());

        for (DocumentCategory documentCategory : documentCategories) {
            File file = Document.getLocalDocumentFile(documentCategory, simulation);

            if (file != null && file.exists()) {
                thumbs.add(thumb(file));
            } else {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.placeholder);

                thumbs.add(bitmap);
            }
        }
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

        holder.imageViewPreview.setImageBitmap(thumbs.get(position));
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
        public final ImageView imageViewPreview;
        public final TextView textViewDocumentName;
        public final TextView textViewDocumentDescription;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            this.imageViewPreview = (ImageView) view.findViewById(R.id.document_item_image);
            this.textViewDocumentName = (TextView) view.findViewById(R.id.textview_document_name);
            this.textViewDocumentDescription = (TextView) view.findViewById(R.id.textview_document_description);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentCategory documentCategory);
    }

    public Bitmap thumb(File path) {
        try {
            final int THUMBNAIL_SIZE = 200;

            FileInputStream fis = new FileInputStream(path);
            Bitmap imageBitmap = BitmapFactory.decodeStream(fis);

            imageBitmap = Bitmap.createScaledBitmap(imageBitmap, THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            byte[] imageData = baos.toByteArray();

            return BitmapFactory.decodeStream(new ByteArrayInputStream(imageData));
        } catch (Exception ex) {
            Log.e(Document.class.getName(), ex.getMessage(), ex);
            return null;
        }
    }
}
