package ca.goldenwords.gwandroid.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.goldenwords.gwandroid.data.DataCache;

public class Node {

    public int nid;
    public String title;
    public String type;
    public int created;
    public int revision_timestamp;

    public String volume;
    public String issue;
    public int volume_id;
    public int issue_id;

    public String image_url;
    public String video_url;
    public List<String> tags;
    public String article_category;
    public String author; // field_by

    public String html_content;
    public String disqusIdentifier;

    public int cover_image;
    public int slider_item;

    public static Node fromJson(String jsonString) throws JSONException {
        return Node.fromJson(jsonString,false);
    }

    public static Node fromJson(String jsonString,boolean addToCache) throws JSONException {
        JSONObject jso = new JSONObject(jsonString);
        return Node.fromJson(jso,addToCache);
    }

    public static Node fromJson(JSONObject jo,boolean addToCache) throws JSONException {
        Node node = new Node();

        node.nid = jo.getInt("nid");
        node.type = jo.getString("type");
        node.title = jo.getString("title");
        node.created = jo.getInt("created");
        node.revision_timestamp = jo.getInt("revision_timestamp");
        node.article_category = jo.getJSONObject("article_category").getString("name");
        node.author = jo.isNull("author") ? null : jo.getString("author");

        node.image_url = jo.getString("image_url");
        node.video_url = jo.getString("video_url");
        node.html_content = jo.getString("html_content");

        node.issue = jo.getString("issue");
        node.volume = jo.getString("volume");
        Pattern p = Pattern.compile("\\w+\\s(\\d+)");
        Matcher m = p.matcher(node.issue);
        if(m.find()) node.issue_id = Integer.parseInt(m.group(1));
        else throw new JSONException("Couldn't get Issue ID");
        m = p.matcher(node.volume);
        if(m.find()) node.volume_id = Integer.parseInt(m.group(1));
        else throw new JSONException("Couldn't get Issue ID");

        node.cover_image = jo.getInt("cover_image");
        node.slider_item = jo.getInt("slider_item");

        node.disqusIdentifier = jo.getJSONObject("disqus").getString("identifier");

        JSONArray t = jo.getJSONArray("tags");
        node.tags = new ArrayList<>();
        for (int i = 0; i < t.length(); i++)
            node.tags.add(t.getString(i));

        if(addToCache) DataCache.addToCache(node);
        return node;
    }

}
