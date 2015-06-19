package ca.goldenwords.gwandroid.utils;

import java.util.Comparator;

import ca.goldenwords.gwandroid.model.Node;


public class RevisionDateComparator implements Comparator<Node> {

    @Override public int compare(Node n1, Node n2) {
        int r = -1;
        if (n1.revision<n2.revision) r = 1;
        return r;
    }
}
