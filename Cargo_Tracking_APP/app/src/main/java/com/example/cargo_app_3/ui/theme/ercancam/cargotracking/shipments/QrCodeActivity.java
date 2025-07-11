package com.example.cargo_app_3.ui.theme.ercancam.cargotracking.shipments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cargo_app_3.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QrCodeActivity extends AppCompatActivity {
    private ImageView ivQrCode;

    // Backend base URL - bunu kendi IP ve portuna göre değiştir
    private static final String BACKEND_BASE_URL = "http://192.168.62.37:8080/api/shipments/deliver/confirm/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        // ActionBar geri butonunu aktif et
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ivQrCode = findViewById(R.id.ivQrCode);

        // Intent'ten sadece UUID (rastgele oluşturulmuş qrCode) alıyoruz
        String qrCodeUUID = getIntent().getStringExtra("qrCode");
        if (qrCodeUUID != null && !qrCodeUUID.isEmpty()) {
            // Backend API URL formatında QR kod içeriği oluştur
            String qrCodeText = BACKEND_BASE_URL + qrCodeUUID;

            try {
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.encodeBitmap(qrCodeText, BarcodeFormat.QR_CODE, 400, 400);
                ivQrCode.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
                Toast.makeText(this, "QR kod oluşturulamadı", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "QR kod bilgisi bulunamadı", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
