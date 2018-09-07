package com.alexandrealencar.videoclean.activities;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;
import com.alexandrealencar.videoclean.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    EditText links = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        links = findViewById(R.id.links);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    private Matcher matcher(String regex, String response) {
        response = response.replace("\n", "").replace("\r", "");
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        return pattern.matcher(response);
    }

    private List<String> getListOfLinksHref(String response ){
        List<String> links = new ArrayList<>();
        Matcher comparator = matcher("<a.*?>", response);
        while (comparator.find()) {
            links.add(comparator.group(0));
        }
        comparator = matcher("href=(\"|')http.*?(\"|')", links.toString() );
        links.clear();
        while (comparator.find()) {
            links.add(  comparator.group(0).replaceAll("href=([\"'])" , "" ).replaceAll("([\"'])" , "" )  );
        }
        return links;
    }

    @Override
    public boolean onQueryTextSubmit(String url) {
        boolean isUrl = ( Patterns.WEB_URL.matcher(url).find() && matcher("https?" , url ).find() );
        if (isUrl) {
            getContentSite(url);
        }
        return false;
    }

    @SuppressLint("WrongConstant")
    @Override
    public boolean onQueryTextChange(String url) {
        boolean isUrl = ( Patterns.WEB_URL.matcher(url).find() && matcher("https?://" , url ).find() );


        if( !url.isEmpty() ){

            final Toast toast = message("Url válida para essa consulta!");

            if (!isUrl) {
                toast.setText("Url inválida para essa consulta!");
            }

            toast.show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    toast.cancel();
                }
            }, 1500);
        }

        return false;
    }

    private Toast message(String message ) {
        return Toast.makeText(this , message ,Toast.LENGTH_SHORT);
    }

    private void getContentSite(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        links.setText( getListOfLinksHref(response).toString() );
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //message("Nenhum site encontrado!").show();
                    }
                });
        queue.add(stringRequest);
    }
}
