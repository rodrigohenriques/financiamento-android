package br.com.jinkings.soluciona.domain.model;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.Serializable;

@ParseClassName("CategoriaDocumento")
public class DocumentCategory extends ParseObject implements Serializable {

    public String getName() {

        try {
            fetchIfNeeded();
        } catch (ParseException e) {
            Log.e(PropertyStatus.class.getName(), e.getMessage(), e);
        }

        return getString(DocumentCategoryFields.NOME);
    }

    public String getDescription() {

        try {
            fetchIfNeeded();
        } catch (ParseException e) {
            Log.e(PropertyStatus.class.getName(), e.getMessage(), e);
        }

        return getString(DocumentCategoryFields.DESCRICAO);
    }

    public static ParseQuery<DocumentCategory> getQuery() {
        return ParseQuery.getQuery(DocumentCategory.class);
    }

    public String getFileName(String simulationId) {
        return String.format("%s_%s.jpg", simulationId, getObjectId());
    }
}
