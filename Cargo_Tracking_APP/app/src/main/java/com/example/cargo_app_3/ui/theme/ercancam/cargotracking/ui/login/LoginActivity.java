package com.example.cargo_app_3.ui.theme.ercancam.cargotracking.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cargo_app_3.R;
import com.example.cargo_app_3.ui.theme.ercancam.cargotracking.ApiService;
import com.example.cargo_app_3.ui.theme.ercancam.cargotracking.RetrofitClient;
import com.example.cargo_app_3.ui.theme.ercancam.cargotracking.model.CourierDto;
import com.example.cargo_app_3.ui.theme.ercancam.cargotracking.shipments.OrdersActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText etCourierId, etPassword;
    private Button btnLogin;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etCourierId = findViewById(R.id.etCourierId);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        btnLogin.setOnClickListener(v -> {
            String courierIdStr = etCourierId.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (courierIdStr.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Kurye ID ve parola gerekli", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals("1234")) {
                Toast.makeText(this, "Parola yanlış", Toast.LENGTH_SHORT).show();
                return;
            }

            Long courierId;
            try {
                courierId = Long.parseLong(courierIdStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Geçerli bir kurye ID giriniz", Toast.LENGTH_SHORT).show();
                return;
            }

            apiService.getCourierById(courierId).enqueue(new Callback<CourierDto>() {
                @Override
                public void onResponse(Call<CourierDto> call, Response<CourierDto> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        // Kurye bulundu, giriş başarılı
                        Intent intent = new Intent(LoginActivity.this, OrdersActivity.class);
                        intent.putExtra("courierId", courierId);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Kurye bulunamadı", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<CourierDto> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Sunucu hatası: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        });
    }
}
