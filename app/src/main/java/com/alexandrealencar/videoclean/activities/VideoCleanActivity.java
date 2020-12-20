package com.alexandrealencar.videoclean.activities;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Patterns;
import android.widget.Toast;
import com.alexandrealencar.videoclean.entities.InputStreamVolleyRequest;
import com.alexandrealencar.videoclean.adapters.LinkPageAdapter;
import com.alexandrealencar.videoclean.database.QueryContract;
import com.alexandrealencar.videoclean.database.VideoCleanController;
import com.alexandrealencar.videoclean.entities.QueryHistory;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VideoCleanActivity extends AppCompatActivity implements LinkPageAdapter.OnListInteraction {
    protected RecyclerView recyclerView;
    protected LinkPageAdapter linkAdapter;
    protected VideoCleanController videoCleanController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        videoCleanController = new VideoCleanController(this);
    }

    protected void refreshRecyclerView(List<String[]> links) {
        linkAdapter.setmDataset(links);
        recyclerView.setAdapter(linkAdapter);
        if (links.isEmpty()) {
            message("Não há mídia disponível!").show();
        }
    }

    protected Toast message(String message) {
        return Toast.makeText(this, message, Toast.LENGTH_SHORT);
    }

    protected Matcher matcher(String regex, String response) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        return pattern.matcher(response);
    }

    protected List<String[]> getListOfLinksHtml(String response) {
        List<String[]> links = new ArrayList<>();
        Matcher comparator = matcher("\"http.*(\\.mp4|\\.m3u8|\\.avi|\\.flv|\\.mov|\\.mpeg|\\.mkv|\\.wmv).*?\"", response);

        while (comparator.find()) {
            String value = comparator.group(0).replaceAll("\"", "");
            links.add(new String[]{value, value});
        }
        return links;
    }

    private String makeUrl(String url , Matcher comparator ){
        boolean isUrl = (Patterns.WEB_URL.matcher(comparator.group(0)).find());
        String link = "";
        String comparation = comparator.group(0).replace("src=", "").replaceAll("\'", "");
        if (isUrl && matcher("https?", comparator.group(0)).find()) {
            link = comparation;
        } else if (!isUrl && !matcher("https?", comparator.group(0)).find()) {
            link = url + comparation;
        } else if (isUrl && !matcher("https?", comparator.group(0)).find()) {
            String[] arrayString = comparation.replace("//", "/").split("/");
            List<String> listString = new ArrayList<>();
            for (String str : arrayString) {
                if (!str.equals("")) {
                    listString.add(str);
                }
            }
            link = listString.toString().replace(",", "/").replaceAll("\\[|\\]", "").replaceAll(" ", "");
            if (url.equals("https://")) {
                link = "https://" + link;
            } else {
                link = "http://" + link;
            }
        }
        return link;
    }

    protected void getListOfLinksHtml(final MainActivity mainActivity , final String url, String response) {
        final List<String[]> links = this.getListOfLinksHtml(response);
        List<String> linksIframe = new ArrayList<>();
        Matcher comparator = matcher("<iframe.*?>", response);
        while (comparator.find()){
            Matcher secundario = matcher("src='.*?'", comparator.group(0).replaceAll("\"", "\'"));
            if( secundario.find() ){
                linksIframe.add( makeUrl( url , secundario ) );
            }
        }
        if (!linksIframe.isEmpty()){
            for (final String link : linksIframe ){
                RequestQueue queue = Volley.newRequestQueue(this);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, link,
                    new Response.Listener<String>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResponse(String response) {
                            links.addAll(getListOfLinksHtml(response));
                            mainActivity.refreshRecyclerView(links);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            mainActivity.refreshRecyclerView(links);
                        }
                    });
                queue.add(stringRequest);
            }
        } else {
            mainActivity.refreshRecyclerView(links);
        }
    }

    protected String getAbsoluteUrl(String url) {
        String urlClone = url;
        for (int i = url.length() - 1; url.charAt(i) != '/'; i--) {
            url = url.substring(0, i);
        }
        return (url.length() >= 7 && url.length() <= 8) ? urlClone + "/" : url;
    }

    @SuppressWarnings("deprecation")
    private void copiarParaAreaDeTransferencia(String text) {
        ClipboardManager Copiar = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        assert Copiar != null;
        Copiar.setText(text);
        Toast.makeText(this, "Texto copiado com sucesso", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickIem(String[] url) {
        copiarParaAreaDeTransferencia(url[0]);
        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra(QueryContract.QueryEntry.COLUMN_NAME_LINK, url);
        startActivity(intent);
    }

    @Override
    public void onClickLongIem(String[] s) {
        downloadFile(s);
    }

    private void downloadFile(final String[] s ){
        final Context context = this;

        Cursor cursor = videoCleanController.select( " ( " + QueryContract.QueryEntry.COLUMN_NAME_LINK + " = ? OR " + QueryContract.QueryEntry.COLUMN_NAME_PATH + " = ? ) AND " + QueryContract.QueryEntry.COLUMN_NAME_IS_DOWNLOAD + " = ? ", new String[]{ s[0] , s[0] , "1" });
        if ( !cursor.moveToNext() ) {
            InputStreamVolleyRequest request = new InputStreamVolleyRequest(Request.Method.GET, s[0],
                    new Response.Listener<byte[]>() {
                        @Override
                        public void onResponse(byte[] response) {
                            try {
                                if (response != null) {
                                    Cursor cursor = videoCleanController.select(QueryContract.QueryEntry.COLUMN_NAME_LINK + " = ? ", new String[]{ s[0] });
                                    FileOutputStream outputStream;
                                    String name = s[1].replaceAll("[/:]", "");
                                    outputStream = openFileOutput(name, Context.MODE_PRIVATE);
                                    outputStream.write(response);
                                    outputStream.close();
                                    QueryHistory queryHistory = new QueryHistory();
                                    queryHistory.setLink(s[0]);
                                    queryHistory.setPath(context.getFilesDir().getCanonicalPath() + "/" + name);
                                    queryHistory.setIsDownload(1);
                                    queryHistory.setDateUpdate(new Date().getTime());
                                    queryHistory.setDescription(s[1]);
                                    queryHistory.setDateCreate(new Date().getTime());
                                    if ( !cursor.moveToNext() ){
                                        videoCleanController.insert(queryHistory);
                                    } else {
                                        queryHistory.setVisualized(cursor.getInt(cursor.getColumnIndex(QueryContract.QueryEntry.COLUMN_NAME_VISUALIZED)));
                                        queryHistory.setDescription(cursor.getString(cursor.getColumnIndex(QueryContract.QueryEntry.COLUMN_NAME_DESCRIPTION)));
                                        queryHistory.setId(cursor.getLong(cursor.getColumnIndex(QueryContract.QueryEntry._ID)));
                                        queryHistory.setIsFavorite(cursor.getInt(cursor.getColumnIndex(QueryContract.QueryEntry.COLUMN_NAME_IS_FAVORITE)));
                                        queryHistory.setCurrentPosition(cursor.getInt(cursor.getColumnIndex(QueryContract.QueryEntry.COLUMN_NAME_CURRENT_POSITION)));
                                        videoCleanController.update(queryHistory);
                                    }
                                    Toast.makeText(context, "Download completo.", Toast.LENGTH_LONG).show();
                                    afterDownload();
                                }
                            } catch (Exception e) {
                                Toast.makeText(context, "UNABLE TO DOWNLOAD FILE", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }, null);
            RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext(), new HurlStack());
            mRequestQueue.add(request);
        } else {
            Toast.makeText(context, "O download já existe", Toast.LENGTH_LONG).show();
        }
    }

    public void afterDownload(){}
}
