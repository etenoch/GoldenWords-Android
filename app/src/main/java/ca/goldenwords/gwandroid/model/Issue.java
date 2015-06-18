package ca.goldenwords.gwandroid.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        issue.nodes = new HashSet<>();

        Iterator<?> keys = obj.keys();

        while( keys.hasNext() ) {
            String key = (String)keys.next();
            if ( obj.get(key) instanceof JSONObject )
                issue.nodes.add(Node.fromJson( (JSONObject)obj.get(key) ));
        }

        Node n =issue.nodes.iterator().next();
        Matcher m = Pattern.compile("Issue\\s(\\d+)").matcher(n.issue);
        issue.issue = m.find() ? Integer.parseInt(m.group(1)) : 0;

        m = Pattern.compile("Volume\\s(\\d+)").matcher(n.volume);
        issue.volume =  m.find() ? Integer.parseInt(m.group(1)) : 0;

        return issue;
    }

}
