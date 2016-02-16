package com.example.mabel.retrofit2httpbin;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


                testInstance();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static void testInstance() {
        // This can be used in any Activity, etc.
        HttpApi api = HttpApi.getInstance();

        // Add headers to be added to every api request
        api.addHeader("Authorization", "MyToken123");

        // Prepare the HTTP request & asynchronously execute HTTP request
        //Call<HttpApi.HttpBinResponse> call = api.getService().postWithJson(new HttpApi.LoginData("username", "secret"));
        Call<HttpApi.HttpBinResponse> call = api.getService().get();
        call.enqueue(new Callback<HttpApi.HttpBinResponse>() {
            /**
             * onResponse is called when any kind of response has been received.
             */
            public void onResponse(Response<HttpApi.HttpBinResponse> response, Retrofit retrofit) {
                // http response status code + headers
                System.out.println("Response status code: " + response.code());

                // isSuccess is true if response code => 200 and <= 300
                if (!response.isSuccess()) {
                    // print response body if unsuccessful
                    try {
                        System.out.println(response.errorBody().string());
                    } catch (IOException e) {
                        // do nothing
                    }
                    return;
                }

                // if parsing the JSON body failed, `response.body()` returns null
                HttpApi.HttpBinResponse decodedResponse = response.body();
                if (decodedResponse == null) return;

                // at this point the JSON body has been successfully parsed
                System.out.println("Response (contains request infos):");
                System.out.println("- url:         " + decodedResponse.url);
                System.out.println("- ip:          " + decodedResponse.origin);
                System.out.println("- headers:     " + decodedResponse.headers);
                System.out.println("- args:        " + decodedResponse.args);
//                System.out.println("- form params: " + decodedResponse.form);
                System.out.println("- json params: " + decodedResponse.json);
            }

            /**
             * onFailure gets called when the HTTP request didn't get through.
             * For instance if the URL is invalid / host not reachable
             */
            public void onFailure(Throwable t) {
                System.out.println("onFailure");
                System.out.println(t.getMessage());
            }
        });
    }




}
