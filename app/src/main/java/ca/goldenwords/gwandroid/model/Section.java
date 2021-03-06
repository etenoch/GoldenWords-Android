package ca.goldenwords.gwandroid.model;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import ca.goldenwords.gwandroid.data.DataCache;
import ca.goldenwords.gwandroid.utils.GWUtils;
import ca.goldenwords.gwandroid.utils.RevisionDateComparator;
import ca.goldenwords.gwandroid.utils.Sections;

public class Section {
    public String jsonString;
    public Set<Node> nodes;
    public String name;
    public Sections articleCategory;

    public static Section fromJson(String jsonString,boolean addToCache) throws JSONException {
        Section section = new Section();
        section.jsonString = jsonString;

        JSONObject obj = new JSONObject(jsonString);
        section.nodes = new TreeSet<>(new RevisionDateComparator());

        Iterator<?> keys = obj.keys();

        while( keys.hasNext() ) {
            String key = (String)keys.next();
            if ( obj.get(key) instanceof JSONObject ){
                Node n = Node.fromJson((JSONObject) obj.get(key),addToCache);
                section.nodes.add(n);
                if(section.name==null){
                    section.name=n.article_category;
                    section.articleCategory = n.articleCategoryMachine;
                }
            }
        }

        if (addToCache) DataCache.addToCache(section);
        return section;
    }

}
