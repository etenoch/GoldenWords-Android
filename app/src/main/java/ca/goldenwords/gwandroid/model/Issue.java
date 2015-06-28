package ca.goldenwords.gwandroid.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.goldenwords.gwandroid.data.DataSource;

public class Issue {
    public String jsonString;
    public Set<Node> nodes;

    public int volume_id;
    public int issue_id;

    public Issue(){

    }

    public static Issue fromJson(String jsonString) throws JSONException{
        Issue issue = new Issue();
        issue.jsonString = jsonString;

        JSONObject obj = new JSONObject(jsonString);
        issue.nodes = new HashSet<>();

        Iterator<?> keys = obj.keys();
        Node n=null;
        while( keys.hasNext() ) {
            String key = (String)keys.next();
            if ( obj.get(key) instanceof JSONObject ){
                n = Node.fromJson((JSONObject) obj.get(key));
                issue.nodes.add(n);
            }
        }

        n =issue.nodes.iterator().next();
        issue.issue_id =n.issue_id;
        issue.volume_id =n.volume_id;

        return issue;
    }

}
