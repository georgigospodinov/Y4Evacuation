package main;

import searches.general.GraphSearch;
import searches.uninformed.BreadthFirst;
import searches.uninformed.DepthFirst;

public class Search1 extends MainSearch {

    @Override
    public void instantiateAlgsArray() {
        algs = new GraphSearch[]{new BreadthFirst(dis::updateStatus), new DepthFirst(dis::updateStatus)};
    }

    public static void main(String[] args) {
        Search1 s = new Search1();
        s.boot();
    }
}
