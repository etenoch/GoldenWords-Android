package ca.goldenwords.gwandroid.utils;


public class IssueVolumeKey {

    private final int x;
    private final int y;

    public IssueVolumeKey(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IssueVolumeKey)) return false;
        IssueVolumeKey key = (IssueVolumeKey) o;
        return x == key.x && y == key.y;
    }

    @Override public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

}