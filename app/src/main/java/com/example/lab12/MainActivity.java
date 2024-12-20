package com.example.lab12;

import android.app.AlertDialog;
import android.app.VoiceInteractor;
import android.os.Bundle;
import android.view.PixelCopy;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Bundle;
import android.util.Log;
import com.google.gson.Gson;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.btn_search).setOnClickListener(view -> {

            String URL = "https://tools-api.italkutalk.com/java/lab12";

            Request request = new Request.Builder().url(URL).build();

            //OkHttpClient okHttpClient = new OkHttpClient();
            OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    if(e.getMessage() != null){
                        Log.e("查詢失敗",e.getMessage());
                    }
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if(response.code() == 200){
                        if(response.body() == null) return;

                        Data data = new Gson().fromJson(response.body().string(), Data.class);

                        final String[] items = new String[data.result.results.length];

                        for (int i = 0; i < items.length; i++) {
                            items[i] = "\n列車即將進入:"+data.result.results[i].Station+
                                    "\n列車行駛目的地:"+data.result.results[i].Destination;
                        }

                        runOnUiThread(() ->{
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("台北捷運列車到站站名")
                                    .setItems(items, null)
                                    .show();
                        });
                    } else if (!response.isSuccessful()) {
                        Log.e("伺服器錯誤", response.code()+" "+response.message());
                    } else {
                        Log.e("其他錯誤", response.code()+" "+response.message());
                    }
                }
            });
        });














    }
}