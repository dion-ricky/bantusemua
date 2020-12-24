package com.example.bantusemua;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bantusemua.DbRepo.Donasi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.net.URL;

public class PengajuanDonasi extends AppCompatActivity {

    TextView tvTenggatWaktu;
    ImageView ivUploadFoto;

    EditText etHeadline, etTargetDana, etDeskripsi;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;

    private String kategoriDonasi; // Diubah nilainya melalui chip

    private final int RC_TAKE_PICTURE = 10;
    private final int RC_FROM_GALLERY = 11;

    private String currentPhotoPath;
    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengajuan_donasi);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        tvTenggatWaktu = findViewById(R.id.tvTenggatWaktu);

        etHeadline = findViewById(R.id.etHeadline);
        etTargetDana = findViewById(R.id.etTargetDana);
        etDeskripsi = findViewById(R.id.etDeskripsi);

        Button btnUbahTenggatWaktu = findViewById(R.id.btnUbahTenggatWaktu);
        btnUbahTenggatWaktu.setOnClickListener(new UbahTenggatWaktuClickListener());

        ImageView ivGalangDanaBack = findViewById(R.id.ivGalangDanaBack);
        ivGalangDanaBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PengajuanDonasi.this.finish();
            }
        });

        ChipGroup cgKategoriDonasi = findViewById(R.id.cgKategoriDonasi);
        cgKategoriDonasi.setOnCheckedChangeListener(new KategoriDonasiChipChecked());

        ivUploadFoto = findViewById(R.id.ivUploadFoto);
        ivUploadFoto.setOnClickListener(new UploadFotoClickListener());

        ImageButton btnGalangDana = findViewById(R.id.btnGalangDana);
        btnGalangDana.setOnClickListener(new GalangDanaClickListener());
    }

    public void insertToDb(
            String judul, String tenggatWaktu, String pelaksana, String kategori,
            Double target, Double terkumpul, Uri photoUri) {

        Donasi donasi = new Donasi(
                judul, tenggatWaktu, pelaksana, kategori, target, terkumpul, ""
        );

        DatabaseReference newEntry = mDatabase.child("donasi").push();
        newEntry.setValue(donasi);

        FirebaseUser currentUser = mAuth.getCurrentUser();

        String newEntryKey = newEntry.getKey();
        String fileName = "image/" + newEntryKey + "/" + currentUser.getUid() + ".jpg";

        storageUpload(photoUri, fileName, newEntryKey);
    }

    public void updatePhotoUrlDonasi(String key, String photoUrl) {
        mDatabase.child("donasi").child(key).child("photoUrl").setValue(photoUrl);
    }

    private void storageUpload(Uri photoUri, String filename, final String key) {
        StorageReference fotoRef = mStorageRef.child(filename);

        UploadTask uploadTask = fotoRef.putFile(photoUri);
        Toast.makeText(this, "Uploading ...", Toast.LENGTH_SHORT).show();

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PengajuanDonasi.this, "Upload file gagal", Toast.LENGTH_SHORT).show();
            }
        });

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                StorageMetadata metadata = taskSnapshot.getMetadata();
                Task<Uri> t1 = metadata.getReference().getDownloadUrl();
                t1.addOnSuccessListener(
                        new OnSuccessStorageUpload(key)
                );
            }
        });
    }

    private void selectImage(final Context context) {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your picture");

        builder.setItems(options, new UploadDialogClickListener(options, context));

        builder.show();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName, ".jpg", storageDir);

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void handlePhotofromCamera() {
        Glide.with(this).load(new File(currentPhotoPath)).centerCrop().into(ivUploadFoto);

        // Scaning masuk ke gallery android (opsional)
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);

        photoUri = contentUri;
    }

    private void handlePhotofromGallery(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};

        Toast.makeText(this, "Handle from gallery", Toast.LENGTH_SHORT).show();

        if (selectedImage != null) {
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn,
                    null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                Glide.with(this).load(new File(picturePath)).centerCrop().into(ivUploadFoto);
                cursor.close();
            }

            photoUri = selectedImage;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) return;

        switch (requestCode) {
            case RC_FROM_GALLERY:
                if (resultCode != RESULT_OK || data == null) break;
                handlePhotofromGallery(data);
                break;
            case RC_TAKE_PICTURE:
                if (resultCode != RESULT_OK || currentPhotoPath == null) break;
                handlePhotofromCamera();
                break;
        }
    }

    class OnSuccessStorageUpload implements OnSuccessListener<Uri> {

        private final String key;

        public OnSuccessStorageUpload(String key) {
            super();
            this.key = key;
        }

        @Override
        public void onSuccess(Uri uri) {
            updatePhotoUrlDonasi(key, uri.toString());
        }
    }

    class GalangDanaClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String judul, kategori, tenggatWaktu, pelaksana;
            Double target, terkumpul;

            judul = etHeadline.getText().toString();
            kategori = kategoriDonasi;
            tenggatWaktu = tvTenggatWaktu.getText().toString();
            pelaksana = mAuth.getCurrentUser().getDisplayName();
            target = Double.valueOf(etTargetDana.getText().toString());
            terkumpul = 0.0;

            // Data validation, make sure none is null
            if (target == 0.0) return;
            if (judul.length() == 0) return;
            if (kategori.length() == 0) return;
            if (pelaksana.length() == 0) return;
            if (tenggatWaktu.length() == 0 || tenggatWaktu.equals("dd/mm/yyyy")) return;

            insertToDb(judul, tenggatWaktu, pelaksana, kategori, target, terkumpul, photoUri);
            PengajuanDonasi.this.finish();
        }
    }

    class UbahTenggatWaktuClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
            MaterialDatePicker<Long> picker = builder.build();
            picker.show(PengajuanDonasi.this.getSupportFragmentManager(), picker.toString());

            picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                @Override
                public void onPositiveButtonClick(Long selection) {
                    Date date = new Date(selection);
                    DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    String tenggatWaktu = format.format(date);

                    tvTenggatWaktu.setText(tenggatWaktu);
                }
            });
        }
    }

    class KategoriDonasiChipChecked implements ChipGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(ChipGroup group, int checkedId) {
            Chip checkedChip = findViewById(checkedId);

            if (checkedChip != null) {
                kategoriDonasi = checkedChip.getText().toString();
            }
        }
    }

    class UploadFotoClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED||checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    String [] Permisions = { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                    requestPermissions(Permisions,100);
                }
                else{
                    selectImage(PengajuanDonasi.this);
                }
            } else {
                selectImage(PengajuanDonasi.this);
            }
        }
    }

    class UploadDialogClickListener implements DialogInterface.OnClickListener {
        private final CharSequence[] options;
        private final Context context;

        public UploadDialogClickListener(final CharSequence[] options, final Context context) {
            super();

            this.options = options;
            this.context = context;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (options[which].equals("Take Photo")) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                Log.d("TAKE_PHOTO", "Taking a photo");

                if (takePicture.resolveActivity(getPackageManager()) != null) {
                    File photoFile = null;

                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Handle error
                        ex.printStackTrace();
                    }

                    if (photoFile != null) {
                        Log.d("TAKE_PHOTO", "Photo file is initiated");

                        Uri photoURI = FileProvider.getUriForFile(context,
                                "com.example.bantusemua", photoFile);
                        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePicture, RC_TAKE_PICTURE);
                    }
                }
            } else if (options[which].equals("Choose from Gallery")) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, RC_FROM_GALLERY);
            } else if (options[which].equals("Cancel")) {
                dialog.dismiss();
            }
        }
    }
}