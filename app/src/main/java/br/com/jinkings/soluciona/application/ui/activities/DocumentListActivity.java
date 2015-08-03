package br.com.jinkings.soluciona.application.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.util.List;

import br.com.jinkings.financing.R;
import br.com.jinkings.soluciona.application.ui.adapter.DocumentCategoryRecyclerViewAdapter;
import br.com.jinkings.soluciona.application.ui.extras.DocumentListExtras;
import br.com.jinkings.soluciona.application.ui.recyclerview.DividerItemDecoration;
import br.com.jinkings.soluciona.domain.model.Document;
import br.com.jinkings.soluciona.domain.model.DocumentCategory;
import br.com.jinkings.soluciona.domain.model.DocumentCategoryFields;
import br.com.jinkings.soluciona.domain.model.DocumentFields;
import br.com.jinkings.soluciona.domain.model.Simulation;
import br.com.jinkings.soluciona.domain.model.User;
import butterknife.InjectView;
import butterknife.OnClick;

public class DocumentListActivity extends MainActivity implements DocumentCategoryRecyclerViewAdapter.OnItemClickListener {

    @InjectView(R.id.document_list_textview_empty_list)
    View emptyList;

    @InjectView(R.id.document_list_recycler_view_container)
    ViewGroup recyclerViewContainer;

    @InjectView(R.id.document_list_recycler_view)
    RecyclerView recyclerView;

    @InjectView(R.id.document_list_document_preview_container)
    View viewDocumentPreview;

    @InjectView(R.id.document_list_document_preview)
    ImageView imageViewPreview;

    User user;
    Simulation simulation;

    List<DocumentCategory> documentCategories;

    DocumentCategory selectedDocumentCategory;

    final int REQUEST_TAKE_PHOTO = 1;

    Uri fileUri;

    DocumentCategoryRecyclerViewAdapter adapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_document_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupRecyclerView();

        user = (User) ParseUser.getCurrentUser();

        Intent intent = getIntent();

        String simulationId = intent.getStringExtra(DocumentListExtras.EXTRA_SIMULATION_SELECTED);

        simulation = loadPinnedSimulation(simulationId);

        loadDocumentData();
    }

    private void setupRecyclerView() {
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
    }

    private Simulation loadPinnedSimulation(String simulationId) {
        ParseQuery<Simulation> query = Simulation.getQuery();
        query.fromLocalDatastore();

        try {
            return query.get(simulationId);
        } catch (ParseException e) {
            Log.e(logTag, e.getMessage(), e);
            return null;
        }
    }

    private void loadDocumentData() {
        startProgress(recyclerViewContainer);

        ParseQuery<DocumentCategory> query = DocumentCategory.getQuery();

        query.whereEqualTo(DocumentCategoryFields.CATEGORIA, user.getJobCategory());
        query.findInBackground(new FindCallback<DocumentCategory>() {
            @Override
            public void done(List<DocumentCategory> list, ParseException e) {

                if (e != null) {
                    int errorMsgId = R.string.default_loading_documents_error_message;

                    justSnackIt(errorMsgId);

                    Log.e(logTag, e.getMessage(), e);

                    finishProgress();
                } else {
                    documentCategories = list;

                    if (documentCategories.isEmpty()) {
                        emptyList.setVisibility(View.VISIBLE);
                        finishProgress();
                    } else {

                        emptyList.setVisibility(View.GONE);

                        final Handler handler = new Handler();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                adapter = new DocumentCategoryRecyclerViewAdapter(DocumentListActivity.this, documentCategories, simulation);
                                adapter.setOnItemClickListener(DocumentListActivity.this);

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        recyclerView.setAdapter(adapter);
                                        finishProgress();
                                    }
                                });
                            }
                        }).start();
                    }
                }
            }
        });
    }

    @Override
    public void onItemClick(final DocumentCategory documentCategory) {
        selectedDocumentCategory = documentCategory;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(documentCategory.getName());
        builder.setItems(R.array.document_options, new DialogInterface.OnClickListener() {

            final int VIEW_DOCUMENT = 0;
            final int REMOVE_DOCUMENT = 1;
            final int INPUT_DOCUMENT = 2;

            @Override
            public void onClick(DialogInterface dialogInterface, int selectedIndex) {
                switch (selectedIndex) {
                    case VIEW_DOCUMENT:
                        documentPreview();
                        break;
                    case REMOVE_DOCUMENT:
                        deleteDocument();
                        break;
                    case INPUT_DOCUMENT:
                        dispatchTakePictureIntent();
                        break;
                }
            }
        });

        builder.setPositiveButton(R.string.dialog_close_button, null);
        builder.create().show();
    }

    private void deleteDocument() {
        new UpdateOrCreateDocument().execute(Action.REMOVE_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                adapter.loadThumbs();
                adapter.notifyDataSetChanged();

                new UpdateOrCreateDocument().execute(Action.SAVE_FILE);
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                justSnackIt(R.string.user_canceled_image_capture_message);
            } else {
                // failed to capture image
                justSnackIt(R.string.image_capture_error);
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            fileUri = Document.getLocalDocumentFileURI(selectedDocumentCategory, simulation);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            // start the image capture Intent
            startActivityForResult(intent, REQUEST_TAKE_PHOTO);
        }
    }

    private void documentPreview() {
        // bimatp factory
        BitmapFactory.Options options = new BitmapFactory.Options();

        // downsizing image as it throws OutOfMemory Exception for larger images
        options.inSampleSize = 8;

        File file = Document.getLocalDocumentFile(selectedDocumentCategory, simulation);

        if (file != null && file.exists()) {
            final Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), options);

            imageViewPreview.setImageBitmap(bitmap);

            viewDocumentPreview.setVisibility(View.VISIBLE);
        } else {
            justSnackIt(R.string.no_document_available_message);
        }
    }

    @OnClick(R.id.document_item_button_close_preview)
    public void closePreview() {
        viewDocumentPreview.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        if (viewDocumentPreview.getVisibility() == View.VISIBLE) {
            closePreview();
        } else {
            super.onBackPressed();
        }
    }

    private class UpdateOrCreateDocument extends AsyncTask<Action, ProgressEvent, Void> {

        Document document;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startProgress(recyclerViewContainer);
        }

        @Override
        protected Void doInBackground(Action... params) {

            final Action action = params[0];

            ParseQuery<Document> query = Document.getQuery();

            query.whereEqualTo(DocumentFields.CATEGORIA_DOCUMENTO, selectedDocumentCategory);
            query.whereEqualTo(DocumentFields.SIMULACAO, simulation);

            query.findInBackground(new FindCallback<Document>() {

                @Override
                public void done(List<Document> list, ParseException e) {
                    if (e != null) {
                        Log.e(logTag, e.getMessage(), e);
                    } else {

                        if (list.size() > 0) {
                            document = list.get(0);
                        } else {
                            document = new Document();

                            document.setCategory(selectedDocumentCategory);
                            document.setSimulation(simulation);
                        }

                        switch (action) {
                            case SAVE_FILE:
                                Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath());
                                document.setFile(bitmap);
                                break;
                            case REMOVE_FILE:
                                document.removeFile();
                                File file = document.getLocalFile();
                                while (file.exists()) {
                                    file.delete();
                                }
                                onProgressUpdate(ProgressEvent.UPDATE_RECYCLER_VIEW);
                                break;
                        }

                        document.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e != null) {
                                    Log.e(logTag, e.getMessage(), e);
                                } else {
                                    Log.i(logTag, "Documento salvo com sucesso: " + document);
                                    justSnackIt(R.string.document_updated_success_message);
                                }

                                onProgressUpdate(ProgressEvent.FINISH);
                            }
                        });
                    }
                }
            });

            return null;
        }

        @Override
        protected void onProgressUpdate(ProgressEvent... values) {
            super.onProgressUpdate(values);

            ProgressEvent event = values[0];
            switch (event) {
                case UPDATE_RECYCLER_VIEW:
                    adapter.loadThumbs();
                    adapter.notifyDataSetChanged();
                    break;
                case FINISH:
                    finishProgress();
                    break;
            }

        }
    }

    enum Action {
        SAVE_FILE, REMOVE_FILE
    }

    enum ProgressEvent {
        UPDATE_RECYCLER_VIEW, FINISH
    }
}
