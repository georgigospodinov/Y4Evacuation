package searches.uninformed;

import problem.Intersection;
import problem.Node;
import searches.general.GraphSearch;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Consumer;

public class BreadthFirst extends GraphSearch {
    private LinkedList<Node> frontier;

    public BreadthFirst(Consumer<GraphSearch> statusUpdater) {
        super(statusUpdater);
    }

    @Override
    protected void createFrontier() {
        frontier = new LinkedList<>();
        frontier.add(new Node(Intersection.init));
    }

    @Override
    protected void addToFrontier(Node child) {
        boolean shouldNotAdd = false;
        for (Node n : frontier)
            if (n.intersection.equals(child.intersection)) {
                shouldNotAdd = true;
                break;
            }

        if (shouldNotAdd) return;
        frontier.addLast(child);
    }

    @Override
    protected Node getNext() {
        return frontier.pollFirst();
    }

    @Override
    protected Collection<Node> getFrontier() {
        return frontier;
    }
}
