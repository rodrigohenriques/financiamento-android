package br.com.jinkings.soluciona.domain.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

@ParseClassName("DocumentoSimulacao")
public class Document extends ParseObject {

    private static final String DIRECTORY_NAME = "jinkings";

    public Simulation getSimulation() {
        try {
            fetchIfNeeded();

            Simulation simulation = (Simulation) getParseObject(DocumentFields.SIMULACAO);

            simulation.fetchIfNeeded();

            return simulation;
        } catch (ParseException e) {
            Log.e(PropertyStatus.class.getName(), e.getMessage(), e);
            return null;
        }
    }

    public DocumentCategory getCategory() {
        try {
            fetchIfNeeded();

            DocumentCategory documentCategory = (DocumentCategory) getParseObject(DocumentFields.CATEGORIA_DOCUMENTO);

            documentCategory.fetchIfNeeded();

            return documentCategory;
        } catch (ParseException e) {
            Log.e(PropertyStatus.class.getName(), e.getMessage(), e);

            return null;
        }
    }

    public ParseFile getFile() {
        return getParseFile(DocumentFields.DOCUMENTO);
    }

    public Bitmap getFileBitmap() throws ParseException {
        ParseFile file = getFile();

        byte[] bitmapdata = file.getData();

        return BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
    }

    public void setFile(Bitmap bitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, stream);
        // get byte array here
        byte[] byteArray = stream.toByteArray();

        final ParseFile file = new ParseFile(byteArray);
        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if (e == null) {
                    put(DocumentFields.DOCUMENTO, file);
                    saveInBackground();
                } else {
                    Log.e(Document.class.getName(), e.getMessage(), e);
                }
            }
        });
    }

    public void removeFile() {
        remove(DocumentFields.DOCUMENTO);
    }

    public static File getLocalDocumentFile(DocumentCategory documentCategory, Simulation simulation) {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(DIRECTORY_NAME, "Oops! Failed create " + DIRECTORY_NAME + " directory");
                return null;
            }
        }

        return new File(mediaStorageDir.getPath() + File.separator + documentCategory.getFileName(simulation.getObjectId()));
    }

    public static Uri getLocalDocumentFileURI(DocumentCategory documentCategory, Simulation simulation) {
        return Uri.fromFile(getLocalDocumentFile(documentCategory, simulation));
    }

    public static Bitmap thumb(DocumentCategory documentCategory, Simulation simulation) {
        try {
            final int THUMBNAIL_SIZE = 200;

            Bitmap imageBitmap = getLocalDocumentBitmap(documentCategory, simulation);

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

    public static Bitmap getLocalDocumentBitmap(DocumentCategory documentCategory, Simulation simulation) {
        try {
            File file = getLocalDocumentFile(documentCategory, simulation);

            FileInputStream fis = new FileInputStream(file);
            Bitmap imageBitmap = BitmapFactory.decodeStream(fis);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            byte[] imageData = baos.toByteArray();

            return BitmapFactory.decodeStream(new ByteArrayInputStream(imageData));
        } catch (Exception ex) {
            Log.e(Document.class.getName(), ex.getMessage(), ex);
            return null;
        }
    }

    public void setCategory(DocumentCategory documentCategory) {
        put(DocumentFields.CATEGORIA_DOCUMENTO, documentCategory);
    }

    public void setSimulation(Simulation simulation) {
        put(DocumentFields.SIMULACAO, simulation);
    }

    public static ParseQuery<Document> getQuery() {
        return ParseQuery.getQuery(Document.class);
    }

    public File getLocalFile() {
        return getLocalDocumentFile(getCategory(), getSimulation());
    }
}
