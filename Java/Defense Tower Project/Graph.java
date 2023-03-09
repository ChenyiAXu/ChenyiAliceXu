package finalproject;

import java.util.ArrayList;

import java.util.HashMap;

import finalproject.system.Tile;

public class Graph {

    // TODO level 2: Add fields that can help you implement this data type
    private ArrayList<Tile> vertices;
    private HashMap<String, Edge > edgesMap = new HashMap<>();

    // TODO level 2: initialize and assign all variables inside the constructor
    public Graph(ArrayList<Tile> vertices) {
        this.vertices = vertices;
    }

    // TODO level 2: add an edge to the graph
    public void addEdge(Tile origin, Tile destination, double weight){
        Edge e = new Edge(origin, destination, weight);
        String key = getMapKey(origin, destination);
        if ( edgesMap.containsKey(key))
            throw new AssertionError("edge already exists: from Tile " + origin+" to Tile "+ destination );
        edgesMap.put( key, e);
    }

    private String getMapKey(Tile origin, Tile destination) {
        return String.format("%d-%d", origin.getNodeID(), destination.getNodeID());
    }


    // TODO level 2: return a list of all edges in the graph
    public ArrayList<Edge> getAllEdges() {
        ArrayList result = new ArrayList<>();
        result.addAll(edgesMap.values());
        return result;
    }

    // TODO level 2: return list of tiles adjacent to t
    public ArrayList<Tile> getNeighbors(Tile t) {
        ArrayList<Tile> neighbors = new ArrayList<>();
        for (Edge edge : edgesMap.values()) {
            if (edge.getStart().equals(t)) {
                neighbors.add(edge.getEnd());
            }
        }
        return neighbors;
    }


    // TODO level 2: return total cost for the input path
    public double computePathCost(ArrayList<Tile> path) {
        //if path is empty, return -1
        if(path.isEmpty()) {
            throw new AssertionError("the path is empty");
        }
        //initialize cost to 0
        double cost = 0;
        //loop through the path
        for(int i = 0; i < path.size()-1; i++){
            Tile start = path.get(i);
            Tile end = path.get(i+1);
            Edge edge = getEdge( start,end);
            if(edge == null){
                throw new AssertionError("the path is broken");
            }
            cost += edge.weight;
        }
        return cost;
    }

    public Edge getEdge(Tile start, Tile end) {
        return edgesMap.get( getMapKey( start,end) );
    }


    public static class Edge{
        Tile origin;  //where the edge is originating from
        Tile destination;   //where the edge is directed to
        double weight;     //the weight associated to this edge

        // TODO level 2: initialize appropriate fields
        public Edge(Tile s, Tile d, double cost){
            this.origin = s;
            this.destination = d;
            this.weight = cost;
        }

        // TODO level 2: getter function 1
        public Tile getStart(){

            return this.origin;
        }


        // TODO level 2: getter function 2
        public Tile getEnd() {
            return this.destination;
        }

    }

}