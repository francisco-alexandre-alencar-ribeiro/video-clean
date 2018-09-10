package com.alexandrealencar.videoclean.activities;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;
import com.alexandrealencar.videoclean.R;
import com.alexandrealencar.videoclean.adapters.LinkPageAdapter;
import com.alexandrealencar.videoclean.database.VideoCleanController;
import com.alexandrealencar.videoclean.entities.QueryHistory;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractorLinkActivity extends AppCompatActivity implements LinkPageAdapter.OnListInteraction , SearchView.OnQueryTextListener {
    private RecyclerView recyclerView;
    private LinkPageAdapter linkAdapter;
    private VideoCleanController videoCleanController = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_extractor);
        recyclerView = findViewById(R.id.recyclerViewLinkPage);
        linkAdapter = new LinkPageAdapter(this);
        recyclerView.setAdapter(linkAdapter);
        videoCleanController = new VideoCleanController(this);
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
        List<String> listlinks = new ArrayList<>();
        Matcher comparator = matcher("<a.*?a>", response);
        while (comparator.find()) {
            listlinks.add(comparator.group(0));
        }

        Object[] arraylinks = listlinks.toArray();
        listlinks.clear();

        for (Object link: arraylinks) {
            comparator = matcher("href=(\"|')http.*?(\"|')", link.toString() );
            if( comparator.find() ){
                String href = comparator.group(0).replaceAll("href=([\"'])" , "" ).replaceAll("([\"'])" , "" );
                String text = link.toString().replaceAll("<.*?>" , "" );
                listlinks.add( href + ',' + text );
            }
        }
        return listlinks;
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
                        refreshRecyclerView( getListOfLinksHref(response) );
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        message("Nenhum site encontrado!").show();
                    }
                });
        queue.add(stringRequest);
    }

    private void refreshRecyclerView(List<String> links) {
        linkAdapter.setmDataset(links);
        recyclerView.setAdapter(linkAdapter);
        if (links.isEmpty()) {
            message("Não há mídia disponível!").show();
        }
    }

    @Override
    public void onClickIem(String[] s) {
        @SuppressLint("SimpleDateFormat")
        QueryHistory queryHistory = new QueryHistory(s[1] , s[0] , new SimpleDateFormat("dd-MM-yyyy").format(new Date()) );
        videoCleanController.insert(queryHistory);
    }
}
