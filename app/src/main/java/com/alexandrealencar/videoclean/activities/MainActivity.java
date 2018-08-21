package com.alexandrealencar.videoclean.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.alexandrealencar.videoclean.adapters.LinkAdapter;
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

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, LinkAdapter.OnListInteraction {

    private RecyclerView recyclerView;
    private LinkAdapter linkAdapter;
    private TextView messageErro;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        linkAdapter = new LinkAdapter(this);
        recyclerView.setAdapter(linkAdapter);
        messageErro = findViewById(R.id.messageErro);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);

        Intent i = getIntent();
        if (Intent.ACTION_SEND.equals(i.getAction())) {
            Bundle extras = i.getExtras();
            if (extras != null && extras.containsKey(Intent.EXTRA_TEXT)) {
                searchView.onActionViewExpanded();
                searchView.setQuery(extras.getString("android.intent.extra.TEXT"), true);
            }
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String url) {
        boolean isUrl = ( Patterns.WEB_URL.matcher(url).find() && matcher("https?" , url ).find() );
        if (isUrl) {
            getContentSite(url);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String url) {
        boolean isUrl = ( Patterns.WEB_URL.matcher(url).find() && matcher("https?" , url ).find() );
        if (!isUrl) {
            showMessage("Url inválida para essa consulta!");
        }else if (isUrl) {
            showMessage("Url válida para essa consulta!");
        } else if ( url.isEmpty() ){
            showMessage("");
        }
        return false;
    }

    private void getContentSite(String url) {
        final String absolute = getAbsoluteUrl(url);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        refreshRecyclerView(getListOfLinks(absolute, response));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showMessage("Nenhum site encontrado!");
                    }
                });
        queue.add(stringRequest);
    }

    private void refreshRecyclerView(List<String> links) {
        if (!links.isEmpty()) {
            showRecycler(links);
        } else {
            showMessage("Não há mídia disponível!");
        }
    }

    private void showMessage(String message) {
        recyclerView.setVisibility(View.INVISIBLE);
        messageErro.setVisibility(View.VISIBLE);
        messageErro.setText(message);
    }

    private void showRecycler(List<String> links) {
        linkAdapter.setmDataset(links);
        recyclerView.setAdapter(linkAdapter);
        recyclerView.setVisibility(View.VISIBLE);
        messageErro.setVisibility(View.INVISIBLE);
    }

    private Matcher matcher(String regex, String response) {
        response = response.replace("\n", "").replace("\r", "");
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        return pattern.matcher(response);
    }

    private List<String> getListOfLinks(String url, String response) {
        List<String> links = new ArrayList<>();
        Matcher comparator = matcher("<video.*?</video>", response);

        while (comparator.find()) {
            String value = comparator.group(0);
            if (value.contains("source")) {
                Matcher secondary = matcher("\\<source(.*?)\\>", value);
                while (secondary.find()) {
                    links.add(secondary.group(0));
                }
            } else {
                links.add(value);
            }
        }

        comparator = matcher("src='.*?'", links.toString().replaceAll("\"", "\'"));
        links.clear();

        while (comparator.find()) {
            links.add((!comparator.group(0).contains("http") ? url : "") + comparator.group(0).replace("src=", "").replaceAll("\'", ""));
        }

        if (links.isEmpty()) {
            comparator = matcher("http.*?\"", response.replaceAll("\'", "\""));
            while (comparator.find()) {
                String value = comparator.group(0).replaceAll("\"", "");
                if (value.contains(".mp4")) {
                    links.add((!value.contains("http") ? url : "") + value);
                }
            }
        }
        for (int i = 0; i < links.size(); i++) {
            links.set(i, (links.get(i).contains("://")) ? links.get(i) : links.get(i).replaceAll("/", ""));
        }

        return links;
    }

    private String getAbsoluteUrl(String url) {
        for (int i = url.length() - 1; url.charAt(i) != '/'; i--) {
            url = url.substring(0, i);
        }
        return (url.length() >= 7 && url.length() <= 8) ? url + "/" : url;
    }

    @Override
    public void onClickIem(String url) {
        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra("url", url);
        startActivity(intent);
    }

}
