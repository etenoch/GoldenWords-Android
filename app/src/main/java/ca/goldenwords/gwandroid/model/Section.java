package ca.goldenwords.gwandroid.model;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Section {
    public String jsonString;
    public Set<Node> nodes;


    public static Section fromJson(String jsonString) throws JSONException {
        Section section = new Section();
        section.jsonString = jsonString;

        JSONObject obj = new JSONObject(jsonString);
        section.nodes = new HashSet<>();

        Iterator<?> keys = obj.keys();

        while( keys.hasNext() ) {
            String key = (String)keys.next();
            if ( obj.get(key) instanceof JSONObject )
                section.nodes.add(Node.fromJson( (JSONObject)obj.get(key) ));

        }


        return section;
    }

}
