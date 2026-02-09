package com.champ.Service;


import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import java.io.IOException;
import java.util.Collections;

public class OAuthService {

    public static Credential authorize() throws IOException {
        // ✅ Load from environment variables
        String clientId = System.getenv("CLIENT_ID");
        String clientSecret = System.getenv("CLIENT_SECRET");
        String refreshToken = System.getenv("REFRESH_TOKEN");


        // ✅ Build a credential directly from the refresh token
        GoogleCredential credential = new GoogleCredential.Builder()
                .setClientSecrets(clientId, clientSecret)
                .setTransport(new NetHttpTransport())
                .setJsonFactory(GsonFactory.getDefaultInstance())
                .build()
                .setRefreshToken(refreshToken);

        // Refresh access token before use
        credential.refreshToken();
        String accessToken = credential.getAccessToken();



        return credential;
    }
}
