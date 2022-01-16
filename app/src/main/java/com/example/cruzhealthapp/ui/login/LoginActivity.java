package com.example.cruzhealthapp.ui.login;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.auth0.android.Auth0;
import com.auth0.android.authentication.AuthenticationException;
import com.auth0.android.callback.Callback;
import com.auth0.android.provider.WebAuthProvider;
import com.auth0.android.result.Credentials;
import com.example.cruzhealthapp.R;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button loginWithTokenButton = findViewById(R.id.loginButton);
        loginWithTokenButton.setOnClickListener(v -> login());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (WebAuthProvider.resume(intent)) {
            return;
        }
        super.onNewIntent(intent);
    }

    private void login() {
        Auth0 auth0 = new Auth0(getString(R.string.auth0_client_id), getString(R.string.auth0_domain));

        WebAuthProvider.login(auth0)
                .withScheme("http")
                .withAudience("http://34.135.251.234/api")
                .withScope("create:user")
                .start(LoginActivity.this, new Callback<Credentials, AuthenticationException>() {
                    @Override
                    public void onFailure(@NonNull final AuthenticationException exception) {
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Error: "
                                + exception.getMessage(), Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onSuccess(@NonNull final Credentials credentials) {
                        runOnUiThread(() -> Toast.makeText(LoginActivity.this,
                                "Log In - Success",
                                Toast.LENGTH_SHORT).show());
                        startActivity(new Intent(LoginActivity.this, UploadHealthInfo.class));
                    }
                });
    }

}