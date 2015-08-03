package ca.goldenwords.gwandroid.utils;


public class VolumeIssueKey {

    private final int volume;
    private final int issue;

    public VolumeIssueKey(int x, int y) {
        this.volume = x;
        this.issue = y;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VolumeIssueKey)) return false;
        VolumeIssueKey key = (VolumeIssueKey) o;
        return volume == key.volume && issue == key.issue;
    }

    @Override public int hashCode() {
        int result = volume;
        result = 31 * result + issue;
        return result;
    }

    public int getVolume() {
        return volume;
    }

    public int getIssue() {
        return issue;
    }
}