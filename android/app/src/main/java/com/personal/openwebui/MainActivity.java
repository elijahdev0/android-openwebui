package com.personal.openwebui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.getcapacitor.BridgeActivity;
import com.getcapacitor.BridgeWebViewClient;

import java.util.ArrayList;

public class MainActivity extends BridgeActivity {

    private static final int AUDIO_PERMISSION_REQUEST_CODE = 101;
    private PermissionRequest currentPermissionRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initializes the Bridge
        // If you add specific Capacitor plugins later, you might register them here
    }

    @Override
    public void onStart() {
        super.onStart();
        WebView webView = getBridge().getWebView();

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                // Check if the request is for audio capture
                boolean needsAudioPermission = false;
                for (String resource : request.getResources()) {
                    if (PermissionRequest.RESOURCE_AUDIO_CAPTURE.equals(resource)) {
                        needsAudioPermission = true;
                        break;
                    }
                }

                if (needsAudioPermission) {
                    // Check if we already have the permission
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                        // Grant the permission to the WebView
                        request.grant(request.getResources());
                    } else {
                        // Store the request and ask the user for permission
                        currentPermissionRequest = request;
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, AUDIO_PERMISSION_REQUEST_CODE);
                    }
                } else {
                    // Handle other permission requests if necessary, or deny
                    // For now, we deny non-audio requests originating from here
                     try {
                       request.deny();
                     } catch (Exception e) {
                       // Ignore exceptions if request is already finished
                     }
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == AUDIO_PERMISSION_REQUEST_CODE && currentPermissionRequest != null) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, grant it to the WebView
                currentPermissionRequest.grant(currentPermissionRequest.getResources());
            } else {
                // Permission denied, deny it for the WebView
                currentPermissionRequest.deny();
            }
            currentPermissionRequest = null; // Clear the stored request
        }
    }
}
