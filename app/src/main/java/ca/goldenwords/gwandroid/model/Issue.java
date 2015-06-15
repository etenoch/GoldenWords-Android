package ca.goldenwords.gwandroid.model;


public class Issue {
    String temp;

    public Issue(String temp){
        this.temp = temp;
    }

    public String getData(){
        return temp;
    }


    public static Issue fromJson(String json){
        return new Issue(json);
    }
}
