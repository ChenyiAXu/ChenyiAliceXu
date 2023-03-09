package finalproject;

import finalproject.system.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import finalproject.TilePriorityQ;
import finalproject.Graph;







public abstract class PathFindingService {
    Tile source;
    Graph g;

    public PathFindingService(Tile start) {
        this.source = start;
    }

    public abstract void generateGraph();

    //TODO level 4: Implement basic dijkstra's algorithm to find a path to the final unknown destination
    public ArrayList<Tile> findPath(Tile startNode) {
        // Perform a breadth-first search to get a list of all the tiles
        ArrayList<Tile> bfs_list = BFS(startNode);
        // Initialize the priority queue with the tiles from the BFS
        // Initialize the cost estimates and predecessors for all the tiles
        init_source(bfs_list, startNode);

        TilePriorityQ q = new TilePriorityQ(bfs_list);

        // Create a list to store the path
        ArrayList<Tile> path = new ArrayList<Tile>();

        // Set the current tile to the starting node
        Tile destNode = null;
        // Keep track of whether the destination has been reached
        boolean destinationReached = false;
        // Loop until the priority queue is empty or the destination has been reached
        while (!q.priorityQ.isEmpty() ) {
            // Remove the minimum element from the priority queue
            Tile u = q.removeMin();
            // Loop through the neighbors of the minimum element
            for (Tile neighbor : g.getNeighbors(u)) {
                // Relax the edge between the minimum element and the neighbor
                relax(u, neighbor, g.getEdge(u, neighbor).weight, q);
            }
            if ( u.isDestination ) {
                destNode = u;
                break;
            }
        }
        // Add the tiles to the path list in the correct order
        for ( Tile current = destNode; current != null ; current = current.predecessor)
        {
            path.add(current);
        }
        // Reverse the list and return it
        ArrayList<Tile> reversedPath = new ArrayList<Tile>();
        for (int i = path.size() - 1; i >= 0; i--) {
            reversedPath.add(path.get(i));
        }
        return reversedPath;
    }
    public void relax(Tile u, Tile v, double weight, TilePriorityQ q) {
        // Relax the edge between u and v
        if (v.costEstimate > u.costEstimate + weight) {
            q.updateKeys( v, u, u.costEstimate + weight);
        }
    }
    public void init_source(ArrayList<Tile> vertices, Tile source) {
        // Initialize all vertices in the graph
        for (Tile t : vertices) {
            t.costEstimate = Double.MAX_VALUE;
            t.predecessor = null;
        }
        source.costEstimate = 0;
    }



    //TODO level 5: Implement basic dijkstra's algorithm to path find to a known destination
    public ArrayList<Tile> findPath(Tile start, Tile end) {

        // Perform a breadth-first search to get a list of all the tiles
        ArrayList<Tile> bfs_list = BFS(start);
        // Initialize the cost estimates and predecessors for all the tiles
        init_source(bfs_list, start);
        // Initialize the priority queue with the tiles from the BFS
        TilePriorityQ q = new TilePriorityQ(bfs_list);

        // Create a list to store the path
        ArrayList<Tile> path = new ArrayList<Tile>();

        // Set the current tile to the starting node
        Tile destNode = null;
        // Keep track of whether the destination has been reached
        boolean destinationReached = false;
        // Loop until the priority queue is empty or the destination has been reached
        while (!q.priorityQ.isEmpty() ) {
            // Remove the minimum element from the priority queue
            Tile u = q.removeMin();
            // Loop through the neighbors of the minimum element
            for (Tile neighbor : g.getNeighbors(u)) {
                // Relax the edge between the minimum element and the neighbor
                relax(u, neighbor, g.getEdge(u, neighbor).weight, q);
            }
            if ( u==end ) {
                destNode = u;
                break;
            }
        }
        // Add the tiles to the path list in the correct order
        for ( Tile current = destNode; current != null ; current = current.predecessor)
        {
            path.add(current);
        }
        // Reverse the list and return it
        ArrayList<Tile> reversedPath = new ArrayList<Tile>();
        for (int i = path.size() - 1; i >= 0; i--) {
            reversedPath.add(path.get(i));
        }
        return reversedPath;
    }

    //TODO level 5: Implement basic dijkstra's algorithm to path find to the final destination passing through given waypoints
    public ArrayList<Tile> findPath(Tile start, LinkedList<Tile> waypoints){
        //segments: start, to way point 1
        //loop through linkedlist of waypoints
        //end of waypoint list, to end
        // special case: if waypoints is empty, just return the path from start to end
        if(waypoints.isEmpty()){
            return findPath(start);
        }
        ArrayList<Tile> path = new ArrayList<Tile>();
        //segment 1: start to first waypoint
       Tile current = start;
       path.add(current);
        //loop through waypoints
       for(Tile temp: waypoints) {
           //find path from current to temp
           ArrayList<Tile> tempPath = findPath(current, temp);
           //add tempPath to path
           for(int i =1; i<tempPath.size(); i++){
               path.add(tempPath.get(i));
           }
           //set current to temp
           current = temp;
       }
        //segment 3: last waypoint to end
        ArrayList<Tile> path2 = findPath(current);
        //add path3 to path
        for(int i = 1; i < path2.size(); i++){
            path.add(path2.get(i));
        }
        return path;
    }
    public static ArrayList<Tile> BFS(Tile s) {
        // Return a list of tiles in the order of BFS traversal
        ArrayList<Tile> queue = new ArrayList<Tile>();
        ArrayList<Tile> visited = new ArrayList<Tile>();
        // Add the starting node to the queue and visited list

        Boolean[] isVisited = new Boolean[100];
        // Initialize the array with default values of false
        for (int i = 0; i < 100; i++) {
            isVisited[i] = false;
        }
        queue.add(s);
        visited.add(s);
        isVisited[s.nodeID] = true;

        // Traverse the map using BFS
        while (!queue.isEmpty()) {
            Tile current = queue.remove(0);

            // Get the neighbors of the current node
            ArrayList<Tile> neighbors = current.neighbors;
            // Traverse the neighbors of the current node
            for (Tile neighbor : neighbors) {
                // If the neighbor has not been visited, add it to the queue and visited list
                if (!visited.contains(neighbor) && neighbor.isWalkable()) {
                    queue.add(neighbor);
                    visited.add(neighbor);
                    isVisited[neighbor.nodeID] = true;
                }
            }
        }

        // Return the list of visited nodes in order
        return visited;
    }



}

