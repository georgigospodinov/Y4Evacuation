package main;

import searches.general.GraphSearch;
import searches.uninformed.Bidirectional;

public class Search3 extends MainSearch {

    @Override
    public void instantiateAlgsArray() {
        algs = new GraphSearch[]{new Bidirectional(dis::updateStatus)};
    }

    public static void main(String[] args) {
        Search3 s = new Search3();
        s.boot();
    }
}
