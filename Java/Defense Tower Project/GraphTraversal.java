package finalproject;

import finalproject.system.Tile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

public class GraphTraversal
{


	//TODO level 1: implement BFS traversal starting from s
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



	//TODO level 1: implement DFS traversal starting from s
	public static ArrayList<Tile> DFS(Tile s) {
		// Return a list of tiles in the order of DFS traversal
		ArrayList<Tile> DFS_List = new ArrayList<Tile>();
		ArrayList<Tile> visited = new ArrayList<Tile>();


		// Add the starting node to the DFS list
		push(s, DFS_List);

		// Traverse the map using DFS
		while (!DFS_List.isEmpty()) {
			Tile current = pop(DFS_List);
			// Add the current node to the visited list
			if(!visited.contains(current)&&current.isWalkable()) {
				push(current, visited);
				for(Tile neighbor: current.neighbors) {
					if(!visited.contains(neighbor)&&neighbor.isWalkable()) {
						push(neighbor, DFS_List);
					}
				}
			}


		}

		// Return the list of visited nodes in order
		return visited;
	}


	private static void push(Tile s, ArrayList<Tile> list){
		list.add(s);
	}

	private static Tile pop(ArrayList<Tile> list) {
		// If the list is not empty, remove and return the last element in the list
		if (!list.isEmpty()) {
			int to_be_removed = list.size() - 1;
			return list.remove(to_be_removed);
		}
		// If the list is empty, return null
		return null;
	}
}  