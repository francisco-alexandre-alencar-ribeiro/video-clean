package com.alexandrealencar.videoclean.activities;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Handler;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;
import com.alexandrealencar.videoclean.R;
import com.alexandrealencar.videoclean.adapters.LinkPageAdapter;
import com.alexandrealencar.videoclean.database.QueryContract;
import com.alexandrealencar.videoclean.entities.QueryHistory;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

public class ExtractorLinkActivity extends VideoCleanActivity implements SearchView.OnQueryTextListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_extractor);
        recyclerView = findViewById(R.id.recyclerViewLinkPage);
        linkAdapter = new LinkPageAdapter(this);
        recyclerView.setAdapter(linkAdapter);
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

    private List<String[]> getListOfLinksHref(String url , String response ){
        url = getAbsoluteUrl(url);

        List<String[]> listlinks = new LinkedList<>();
        Matcher comparator = matcher("<a.*?a>", response);
        while (comparator.find()) {
            listlinks.add(new String[]{comparator.group(0) , comparator.group(0)});
        }

        for (int i = 0 ; i < listlinks.size() ; i++) {
            String[] link = listlinks.get(i);

            comparator = matcher("href=(\"|').*?(\"|')", link[0]);
            if( comparator.find() ){
                String href = "";
                if( !comparator.group(0).contains("http") ){
                    href += url;
                }
                href += comparator.group(0).replaceAll("href=([\"'])" , "" ).replaceAll("([\"'])" , "" );
                String text = link[0].replaceAll("<.*?>" , "" );
                listlinks.set(i , new String[]{href,text} );
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

    private void getContentSite(final String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                        refreshRecyclerView( getListOfLinksHref(url,response) );
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

    @Override
    public void onClickIem(final String[] s) {
        @SuppressLint("SimpleDateFormat")
        final RequestQueue queue = Volley.newRequestQueue(this);
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, s[0],
                new Response.Listener<String>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(String response) {
                    try {
                        String link = getListOfLinksHtml( s[0], response).get(0)[0];
                        @SuppressLint("SimpleDateFormat")
                        Cursor cursor = videoCleanController.select(QueryContract.QueryEntry.COLUMN_NAME_LINK + " = ? " , new String[]{link});
                        if ( !cursor.moveToNext() ){
                            QueryHistory queryHistory = new QueryHistory(s[1] , link , new Date().getTime() , new Date().getTime() );
                            queryHistory.setIsFavorite(1);
                            videoCleanController.insert(queryHistory);
                            Toast toast = message("Adicionado aos favoritos!");
                            toast.show();
                            final Toast finalToast = toast;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finalToast.cancel();
                                }
                            }, 2000);
                        }
                    }catch (IndexOutOfBoundsException e){
                        message("Não há mídia disponível!").show();
                    }
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
}