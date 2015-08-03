package br.com.jinkings.soluciona.application.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

import br.com.jinkings.financing.R;
import br.com.jinkings.soluciona.application.ui.adapter.DocumentCategoryRecyclerViewAdapter;
import br.com.jinkings.soluciona.application.ui.extras.CapturedImageExtras;
import br.com.jinkings.soluciona.application.ui.extras.DocumentListExtras;
import br.com.jinkings.soluciona.application.ui.recyclerview.DividerItemDecoration;
import br.com.jinkings.soluciona.domain.model.DocumentCategory;
import br.com.jinkings.soluciona.domain.model.DocumentCategoryFields;
import br.com.jinkings.soluciona.domain.model.Simulation;
import br.com.jinkings.soluciona.domain.model.User;
import butterknife.InjectView;

public class DocumentListActivity extends MainActivity implements DocumentCategoryRecyclerViewAdapter.OnItemClickListener {

    @InjectView(R.id.document_list_textview_empty_list)
    View emptyList;

    @InjectView(R.id.document_list_recycler_view)
    RecyclerView recyclerView;

    User user;
    Simulation simulation;

    List<DocumentCategory> documentCategories;

    DocumentCategory selectedDocumentCategory;

    @Override
    protected int getContentView() {
        return R.layout.activity_document_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        user = (User) ParseUser.getCurrentUser();

        Intent intent = getIntent();

        simulation = (Simulation) intent.getSerializableExtra(DocumentListExtras.EXTRA_SIMULATION_SELECTED);

        loadDocumentData();
    }


    private void loadDocumentData() {
        startProgress();

        ParseQuery<DocumentCategory> query = DocumentCategory.getQuery();

        query.whereEqualTo(DocumentCategoryFields.CATEGORIA, user.getJobCategory());
        query.findInBackground(new FindCallback<DocumentCategory>() {
            @Override
            public void done(List<DocumentCategory> list, ParseException e) {

                if (e != null) {
                    int errorMsgId = R.string.default_loading_documents_error_message;

                    justSnackIt(errorMsgId);

                    Log.e(logTag, e.getMessage(), e);
                } else {
                    documentCategories = list;

                    if (list.isEmpty()) {
                        emptyList.setVisibility(View.VISIBLE);
                    } else {
                        emptyList.setVisibility(View.GONE);
                        DocumentCategoryRecyclerViewAdapter adapter = new DocumentCategoryRecyclerViewAdapter(DocumentListActivity.this, documentCategories);
                        adapter.setOnItemClickListener(DocumentListActivity.this);
                        recyclerView.setAdapter(adapter);
                    }
                }

                finishProgress();
            }
        });
    }

    @Override
    public void onItemClick(final DocumentCategory documentCategory) {
        selectedDocumentCategory = documentCategory;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(R.array.document_options, new DialogInterface.OnClickListener() {

            final int VIEW_DOCUMENT = 0;
            final int REMOVE_DOCUMENT = 1;
            final int INPUT_DOCUMENT = 2;

            @Override
            public void onClick(DialogInterface dialogInterface, int selectedIndex) {
                switch (selectedIndex) {
                    case VIEW_DOCUMENT:
                        justSnackIt(selectedIndex);
                        break;
                    case REMOVE_DOCUMENT:
                        justSnackIt(selectedIndex);
                        break;
                    case INPUT_DOCUMENT:
                        captureImage(documentCategory);
                        break;
                }
            }
        });

        builder.setPositiveButton(R.string.dialog_close_button, null);
        builder.create().show();
    }

    private void captureImage(DocumentCategory documentCategory) {
        Intent intent = new Intent(this, CapturedImageActivity.class);

        intent.putExtra(CapturedImageExtras.EXTRA_SIMULATION, simulation);
        intent.putExtra(CapturedImageExtras.EXTRA_DOCUMENT_CATEGORY, documentCategory);

        startActivity(intent);
    }
}
