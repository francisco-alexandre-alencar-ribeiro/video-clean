package com.alexandrealencar.videoclean.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Patterns;
import android.widget.Toast;

import com.alexandrealencar.videoclean.adapters.LinkPageAdapter;
import com.alexandrealencar.videoclean.database.QueryContract;
import com.alexandrealencar.videoclean.database.VideoCleanController;

import java.util.ArrayList;
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
        response = response.replace("\n", "").replace("\r", "");
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        return pattern.matcher(response);
    }

    protected List<String[]> getListOfLinksHtml(String url, String response) {
        List<String[]> links = new ArrayList<>();
        Matcher comparator = matcher("<video.*?</video>", response);

        while (comparator.find()) {
            String value = comparator.group(0);
            if (value.contains("source")) {
                Matcher secondary = matcher("\\<source(.*?)\\>", value);
                while (secondary.find() && secondary.group(0).contains(".mp4")) {
                    links.add(new String[]{secondary.group(0), secondary.group(0)});
                }
            } else if (value.contains(".mp4")) {
                links.add(new String[]{value, value});
            }
        }
        String listLinks = "";
        for (String[] link : links) {
            listLinks += link[0];
        }
        comparator = matcher("src='.*?'", listLinks.replaceAll("\"", "\'"));
        links.clear();
        while (comparator.find()) {
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
            if (!links.contains(new String[]{link, link})) {
                links.add(new String[]{link, link});
            }
        }

        if (links.isEmpty()) {
            comparator = matcher("http.*?\"", response.replaceAll("\'", "\""));
            while (comparator.find()) {
                String value = comparator.group(0).replaceAll("\"", "");
                String link = (!value.contains("http") ? url : "") + value;
                if (value.contains(".mp4") && !links.contains(new String[]{link, link})) {
                    links.add(new String[]{link, link});
                }
            }
        }
        for (int i = 0; i < links.size(); i++) {
            String link = (links.get(i)[0].contains("://")) ? links.get(i)[0] : links.get(i)[0].replaceAll("\\/", "");
            link = link.replaceAll("\\\\", "/");
            links.set(i, new String[]{link, link});
        }

        return links;
    }

    protected String getAbsoluteUrl(String url) {
        String urlClone = url;
        for (int i = url.length() - 1; url.charAt(i) != '/'; i--) {
            url = url.substring(0, i);
        }
        return (url.length() >= 7 && url.length() <= 8) ? urlClone + "/" : url;
    }

    @Override
    public void onClickIem(String[] url) {
        Intent intent = new Intent(this, VideoActivity.class);
        intent.putExtra(QueryContract.QueryEntry.COLUMN_NAME_LINK, url);
        startActivity(intent);
    }
}
