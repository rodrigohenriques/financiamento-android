package br.com.jinkings.soluciona.application.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

import br.com.jinkings.financing.R;
import br.com.jinkings.soluciona.application.ui.extras.CapturedImageExtras;
import br.com.jinkings.soluciona.domain.model.DocumentCategory;
import br.com.jinkings.soluciona.domain.model.Simulation;
import butterknife.InjectView;

public class CapturedImageActivity extends MainActivity {

    @InjectView(R.id.captured_image_view)
    ImageView imageView;

    final int REQUEST_TAKE_PHOTO = 1;

    String mCurrentPhotoPath;

    Simulation simulation;
    DocumentCategory documentCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        simulation = (Simulation) intent.getSerializableExtra(CapturedImageExtras.EXTRA_SIMULATION);
        documentCategory = (DocumentCategory) intent.getSerializableExtra(CapturedImageExtras.EXTRA_DOCUMENT_CATEGORY);

        dispatchTakePictureIntent();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_captured_image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e(logTag, ex.getMessage(), ex);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = documentCategory.getFileName(simulation);
        File storageDir = getFilesDir();
        File image = new File(storageDir, imageFileName + ".jpg");

        if (!image.exists()) {
            if (!image.mkdirs()) {
                Log.e(logTag, "Falha ao criar o diretório: " + image);
            }

            if (image.createNewFile()) {
                return image;
            } else {
                throw new IOException("Falha ao criar o arquivo: " + image);
            }
        }

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                setPic();
            } else {
                finish();
            }
        }
    }

    private void setPic() {
        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
        imageView.setImageBitmap(bitmap);
    }
}
