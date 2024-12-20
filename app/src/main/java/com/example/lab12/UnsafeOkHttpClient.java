package com.example.lab12;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class UnsafeOkHttpClient {

    public static OkHttpClient getUnsafeOkHttpClient(){
        try{
            final TrustManager[] trustManAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManAllCerts, new SecureRandom());

            return new OkHttpClient
                    .Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustManAllCerts[0])
                    .hostnameVerifier((hostname, session) -> true)
                    .build();


        }catch (Exception e){
            throw new RuntimeException();
        }
    }
}
