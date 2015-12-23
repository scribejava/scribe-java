package com.github.scribejava.apis.examples;

import com.github.scribejava.apis.ProductHuntApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.*;
import com.github.scribejava.core.oauth.OAuth20ServiceImpl;

import java.util.Scanner;

/**
 * Created by ahua on 12/22/15.
 */
public class ProductHunt20Example {
    private static final String NETWORK_NAME = "ProductHunt";
    private static final String PROTECTED_RESOURCE_URL = "https://api.producthunt.com/v1/posts";
    private static final Token EMPTY_TOKEN = null;

    public static void main(String[] args){
        // Replace these with your client id and secret
        final String clientId = "your client id";
        final String clientSecret = "your client secret";

        final OAuth20ServiceImpl service = (OAuth20ServiceImpl) new ServiceBuilder().provider(ProductHuntApi.class)
                .apiKey(clientId).apiSecret(clientSecret)
                .scope("public+private")
                .callback("http://example.com/callback")
                .build();
        Scanner in = new Scanner(System.in);

        System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
        System.out.println();

        // Obtain the Authorization URL
        System.out.println("Fetching the Authorization URL...");
        final String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
        System.out.println("Got the Authorization URL!");
        System.out.println("Now go and authorize ScribeJava here:");
        System.out.println(authorizationUrl);
        System.out.println("And paste the authorization code here");
        System.out.print(">>");
        Verifier verifier = new Verifier(in.nextLine());
        System.out.println();

        // Trade the Request Token and Verfier for the Access Token
        System.out.println("Trading the Request Token for an Access Token...");
        Token accessToken = service.getAccessToken(EMPTY_TOKEN, verifier);
        System.out.println("Got the Access Token!");
        System.out.println("(if your curious it looks like this: " + accessToken + " )");
        System.out.println();

        // Now let's go and ask for a protected resource!
        System.out.println("Now we're going to access a protected resource...");
        while(true){
            final OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL, service);
            request.addHeader("x-li-format", "json");
            request.addHeader("Accept-Language", "ru-RU");
            service.signRequest(accessToken, request);
            final Response response = request.send();
            System.out.println();
            System.out.println(response.getCode());
            System.out.println(response.getBody());

            System.out.println();
        }


    }
}
