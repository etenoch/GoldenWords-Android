package ca.goldenwords.gwandroid.model;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class Issue {
    public String jsonString;
    public Set<Node> nodes;

    public int volume;
    public int issue;

    public Issue(){

    }

    public static Issue fromJson(String jsonString) throws JSONException{
        Issue issue = new Issue();
        issue.jsonString = jsonString;

        JSONObject obj = new JSONObject(jsonString);
        Node newNode;
        issue.nodes = new HashSet<>();

        Iterator<?> keys = obj.keys();

        while( keys.hasNext() ) {
            String key = (String)keys.next();
            if ( obj.get(key) instanceof JSONObject )
                issue.nodes.add(Node.fromJson( (JSONObject)obj.get(key) ));

        }


        return issue;
    }

}
