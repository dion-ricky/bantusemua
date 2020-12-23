package com.example.bantusemua;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PengajuanDonasi extends AppCompatActivity {

    TextView tvTenggatWaktu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengajuan_donasi);

        tvTenggatWaktu = findViewById(R.id.tvTenggatWaktu);

        Button btnUbahTenggatWaktu = findViewById(R.id.btnUbahTenggatWaktu);
        btnUbahTenggatWaktu.setOnClickListener(new UbahTenggatWaktuClickListener());

        ImageView ivGalangDanaBack = findViewById(R.id.ivGalangDanaBack);
        ivGalangDanaBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PengajuanDonasi.this.finish();
            }
        });
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
}