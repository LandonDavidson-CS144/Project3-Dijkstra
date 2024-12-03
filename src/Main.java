//ArrayDeque is only used for the traversal functions
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

public class Main {
    public static void main(String[] args) {
        Graph graph = new Graph();
        for (int i = 0; i < 38; i++) {
            graph.addVertex(i);
        }
        int[][] edges = {{20, 8, 1, 8, 2, 8, 7, 4, 14, 4}, {5, 8}, {6, 2, 3, 5}, {17, 1}, {7, 3, 5, 7, 8, 5},
                {8, 1, 6, 8, 25, 6}, {}, {11, 1}, {11, 8, 12, 7}, {12, 9, 10, 6, 13, 3}, {13, 5, 30, 4, 17, 2},
                {14, 6, 12, 2}, {13, 2, 15, 8, 16, 6}, {17, 9}, {}, {35, 7}, {17, 1}, {}, {}, {},
                {21, 1, 24, 3, 27, 5, 34, 1}, {24, 1}, {23, 8, 25, 1, 26, 8}, {26, 2, 37, 2}, {27, 2, 28, 6},
                {26, 7, 28, 1, 29, 6}, {29, 9, 30, 5}, {28, 5, 34, 7}, {31, 5, 32, 1}, {30, 1, 33, 5}, {37, 4},
                {32, 4, 34, 3, 35, 9}, {33, 4, 36, 1}, {36, 7}, {}, {36, 4, 37, 1}, {}, {}};
        for (int node = 0; node < edges.length; node++) {
            for (int j = 0; j < edges[node].length; j += 2) {
                graph.addEdge(node, edges[node][j], edges[node][j + 1]);
            }
        }

        graph.findDistances(1);
        graph.printDistance(37);

        graph.findDistances(14);
        graph.printDistance(23);
    }
}



class Graph {
    static class Edge {
        int weight;
        Node target;
        public Edge(Node target, int weight) {
            this.target = target;
            this.weight = weight;
        }
    }
    static class Node implements Comparable<Node> {
        int value;
        Node prev;
        int distance;
        ArrayList<Edge> neighbors = new ArrayList<>();
        public Node(int value) {
            this.value = value;
        }
        public void addEdge(Node target, int weight) {
            for (Edge e: neighbors) {
                if (e.target == target) return;
            }
            neighbors.add(new Edge(target, weight));
            target.neighbors.add(new Edge(this, weight));
        }
        public void removeEdge(Node target) {
            for (Edge e: neighbors) {
                if (e.target == target) {
                    neighbors.remove(e);
                    break;
                }
            }
            for (Edge e: target.neighbors) {
                if (e.target == this) {
                    target.neighbors.remove(e);
                    break;
                }
            }
        }
        public boolean isAdjacent(Node target) {
            for (Edge e: neighbors) {
                if (e.target == target) return true;
            }
            return false;
        }

        @Override
        public int compareTo(Node n) {
            return this.distance - n.distance;
        }
    }
    ArrayList<Node> nodes = new ArrayList<>();
    public void addVertex(int value) {
        nodes.add(new Node(value));
    }
    public void removeVertex(int value) {
        Node target = findNode(value);
        if (target == null) return;
        ArrayList<Edge> neighborsCopy = new ArrayList<>(target.neighbors);
        for (Edge e: neighborsCopy) {
            target.removeEdge(e.target);
        }
        nodes.remove(target);
    }
    public void addEdge(int startValue, int endValue, int weight) {
        Node start = findNode(startValue);
        Node end = findNode(endValue);
        if (start == null || end == null) return;
        start.addEdge(end, weight);
    }
    public void removeEdge(int startValue, int endValue) {
        Node start = findNode(startValue);
        Node end = findNode(endValue);
        if (start == null || end == null) return;
        start.removeEdge(end);
    }
    public Node findNode(int value) {
        for (Node n: nodes) {
            if (n.value == value) return n;
        }
        return null;
    }
    public boolean isAdjacent(Node start, Node end) {
        return start.isAdjacent(end);
    }
    public void breadthFirst(Node start) {
        ArrayDeque<Node> queue = new ArrayDeque<>();
        queue.add(start);
        ArrayList<Node> previousNodes = new ArrayList<>();
        while (!queue.isEmpty()) {
            Node curNode = queue.pop();
            if (previousNodes.contains(curNode)) continue;
            for (Edge e: curNode.neighbors) {
                queue.add(e.target);
            }
            previousNodes.add(curNode);
            System.out.println(curNode.value);
        }
    }
    public void depthFirst(Node start) {
        ArrayDeque<Node> stack = new ArrayDeque<>();
        stack.add(start);
        ArrayList<Node> previousNodes = new ArrayList<>();
        while (!stack.isEmpty()) {
            Node curNode = stack.pollLast();
            if (previousNodes.contains(curNode)) continue;
            for (Edge e: curNode.neighbors) {
                stack.add(e.target);
            }
            previousNodes.add(curNode);
            System.out.println(curNode.value);
        }
    }
    public void displayTraversals() {
        System.out.println("Breadth First Traversal");
        breadthFirst(nodes.getFirst());

        System.out.println("Depth First Traversal");
        depthFirst(nodes.getFirst());
    }
    public void printDistance(int targetValue) {
        Node curNode = findNode(targetValue);
        if (curNode ==  null) return;
        ArrayList<Integer> path = new ArrayList<>();
        path.add(curNode.value);
        int distance = curNode.distance;
        while (curNode.prev != null) {
            curNode = curNode.prev;
            path.add(curNode.value);
        }
        Collections.reverse(path);
        for (int node: path) {
            System.out.print(node);
            if (node == targetValue) {
                System.out.println("   Distance: " + distance);
                return;
            }
            System.out.print(" -> ");
        }
    }
    public void findDistances(int startValue) {
        Node start = null;
        for (Node n: nodes) {
            n.distance = Integer.MAX_VALUE;
            n.prev = null;
            if (n.value == startValue) {
                start = n;
            }
        }
        if (start == null) return;
        PriorityQueue<Node> queue = new PriorityQueue<>();
        ArrayList<Node> visitedNodes = new ArrayList<>();
        start.distance = 0;
        queue.add(start);
        while (!queue.isEmpty()) {
            Node curNode = queue.poll();
            if (visitedNodes.contains(curNode)) continue;
            visitedNodes.add(curNode);
            for (int i = 0; i < curNode.neighbors.size(); i++) {
                if (!queue.contains(curNode.neighbors.get(i).target)) {
                    queue.add(curNode.neighbors.get(i).target);
                }
                int prevDistance = curNode.neighbors.get(i).target.distance;
                int altDistance = curNode.distance + curNode.neighbors.get(i).weight;
                if (altDistance < prevDistance) {
                    curNode.neighbors.get(i).target.distance = altDistance;
                    curNode.neighbors.get(i).target.prev = curNode;
                }
                queue.add(curNode.neighbors.get(i).target);
            }
        }
    }
}
