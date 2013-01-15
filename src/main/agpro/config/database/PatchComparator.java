package agpro.config.database;

import java.util.Comparator;

public class PatchComparator implements Comparator<Patch> {
    public int compare(Patch o1, Patch o2) {
        return o1.version().compareTo(o2.version());
    }
}