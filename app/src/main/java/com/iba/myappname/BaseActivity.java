package com.iba.myappname;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;

import com.iba.myappname.biometric.BiometricPromptUtils;

public class BaseActivity extends AppCompatActivity {

    void showBiometricPrompt(BiometricAuthListener authListener) {
        BiometricManager biometricManager = BiometricManager.from(getApplicationContext());
        int canAuthenticate = biometricManager.canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG
                        | BiometricManager.Authenticators.DEVICE_CREDENTIAL
                        | BiometricManager.Authenticators.BIOMETRIC_WEAK
        );
        if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {

            BiometricPrompt.AuthenticationCallback bpCallback = new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    authListener.onBiometricSuccess();
                }

                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    Log.e(">>>>>>>>>>>", "onAuthenticationError: " + errorCode + " " + errString);
                    if (errorCode == BiometricPrompt.ERROR_UNABLE_TO_PROCESS) {
                        Toast.makeText(getApplicationContext(), getString(R.string.error_while_unlocking), Toast.LENGTH_SHORT).show();
                        authListener.onBiometricError(errorCode, errString);
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.error_while_unlocking), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    finish();
                }
            };

            BiometricPrompt biometricPrompt = BiometricPromptUtils.createBiometricPrompt(this, bpCallback);

            BiometricPrompt.PromptInfo promptInfo = BiometricPromptUtils.createPromptInfo(this);
            biometricPrompt.authenticate(promptInfo);
        } else {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.unable_to_use_biometric_unlock),
                    Toast.LENGTH_LONG).show();
            // Graceful exit — finishAffinity tears down the whole task without
            // killing the process abruptly the way System.exit(0) does.
            new Handler(Looper.getMainLooper()).postDelayed(() -> ActivityCompat.finishAffinity(this), 3000);
            authListener.onBiometricSuccess();
        }
    }

    interface BiometricAuthListener {
        void onBiometricSuccess();

        default void onBiometricError(int errorCode, @NonNull CharSequence errString){}
    }
}
