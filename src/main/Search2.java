package main;

import searches.general.GraphSearch;
import searches.informed.AStar;
import searches.informed.BestFirst;

public class Search2 extends MainSearch {

    @Override
    public void instantiateAlgsArray() {
        algs = new GraphSearch[]{new BestFirst(dis::updateStatus), new AStar(dis::updateStatus)};
    }

    public static void main(String[] args) {
        Search2 s = new Search2();
        s.boot();
    }
}
