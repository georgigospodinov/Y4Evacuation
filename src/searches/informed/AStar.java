package searches.informed;

import problem.Intersection;
import problem.Node;
import searches.general.GraphSearch;

import java.util.Collection;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.function.Consumer;

public class AStar extends GraphSearch {
    private PriorityQueue<Node> frontier;

    public AStar(Consumer<GraphSearch> statusUpdater) {
        super(statusUpdater);
    }

    @Override
    protected void createFrontier() {
        frontier = new PriorityQueue<>((a, b) -> {
            double ah = a.getHeuristicToGoal() + a.getHeuristicFromInit();
            double bh = b.getHeuristicToGoal() + b.getHeuristicFromInit();
            return Double.compare(ah, bh);
        });
        frontier.add(new Node(Intersection.init));
    }

    @Override
    protected void addToFrontier(Node child) {
        frontier.add(child);
    }

    @Override
    protected Node getNext() {
        Node next = frontier.poll();
        if (next != null) {
            LinkedList<Node> nodesToRemove = new LinkedList<>();
            frontier.forEach(n -> {
                if (n.intersection.equals(next.intersection))
                    nodesToRemove.add(n);
            });

            nodesToRemove.forEach(frontier::remove);
        }
        return next;
    }

    @Override
    public double heuristic(Node n) {
        if (n == null) return super.heuristic(n);
        return n.getHeuristicToGoal() + n.getHeuristicFromInit();
    }

    @Override
    protected Collection<Node> getFrontier() {
        return frontier;
    }
}
