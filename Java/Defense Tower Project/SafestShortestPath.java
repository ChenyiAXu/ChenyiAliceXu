package finalproject;


import java.util.ArrayList;
import java.util.LinkedList;


import finalproject.system.Tile;
import finalproject.system.TileType;
import finalproject.tiles.MetroTile;


public class SafestShortestPath extends ShortestPath {
	public int health;
	public Graph costGraph;
	public Graph damageGraph;
	public Graph aggregatedGraph;

	//TODO level 8: finish class for finding the safest shortest path with given health constraint
	public SafestShortestPath(Tile start, int health) {
		super(start);
		this.health = health;
		generateGraph();
	}


	public void generateGraph() {
		// TODO Auto-generated method stub
		//cost graph --> distance cost
		//damage graph --> damage cost
		//aggregated graph --> damage cost
		ArrayList<Tile> tiles = BFS(source);
		costGraph = new Graph(tiles);
		for (Tile t : tiles) {
			for (Tile neighbor : t.neighbors) {
				if (neighbor.isWalkable()) {
					if (t.type == TileType.Metro && neighbor.type == TileType.Metro) {
						MetroTile m = (MetroTile) t;
						m.fixMetro(neighbor);
						costGraph.addEdge(t, neighbor, m.metroDistanceCost);
					} else {
						costGraph.addEdge(t, neighbor, neighbor.distanceCost);
					}


				}
			}
		}
		damageGraph = new Graph(tiles);
		for (Tile t : tiles) {
			for (Tile neighbor : t.neighbors) {
				if (neighbor.isWalkable()) {

					damageGraph.addEdge(t, neighbor, neighbor.damageCost);
				}

			}
		}
		aggregatedGraph = new Graph(tiles);
		for(Tile t: tiles){
			for(Tile neighbor: t.neighbors){
				if(neighbor.isWalkable()){
					aggregatedGraph.addEdge(t, neighbor, neighbor.damageCost);
				}
			}
		}
	}

	private double getDamageCostOfPath( ArrayList<Tile> path )
	{
		double sum = 0;
		for(int i = 0; i < path.size()-1; i++){
			sum += damageGraph.getEdge(path.get(i), path.get(i+1)).weight;
		}
		return sum;
	}

	private double getDistanceCostOfPath( ArrayList<Tile> path )
	{
		double sum = 0;
		for(int i = 0; i < path.size()-1; i++){
			sum += costGraph.getEdge(path.get(i), path.get(i+1)).weight;
		}
		return sum;
	}
	private double getAggregatedCostOfPath( ArrayList<Tile> path )
	{
		double sum = 0;
		for(int i = 0; i < path.size()-1; i++){
			sum += aggregatedGraph.getEdge(path.get(i), path.get(i+1)).weight;
		}
		return sum;
	}
	public ArrayList<Tile> findPath(Tile startNode, LinkedList<Tile> waypoints) {
		g = costGraph;
		ArrayList<Tile> shortestPath = super.findPath(startNode, waypoints);
		double damageCost_sp = getDamageCostOfPath(shortestPath);
		double distanceCost_sp = getDistanceCostOfPath(shortestPath);
		if (damageCost_sp < health) {
			return shortestPath;
		}
		g = damageGraph;
		ArrayList<Tile> safestPath = super.findPath(startNode, waypoints);
		double damageCost_safest = getDamageCostOfPath(safestPath);
		double distanceCost_safest = getDistanceCostOfPath(safestPath);
		if (damageCost_safest >= health) {
			return null; //no path found
		}
		g = aggregatedGraph;
		while (true) {
			double lambda = (distanceCost_sp - distanceCost_safest) / (damageCost_safest - damageCost_sp);
			for (int i = 0; i < g.getAllEdges().size() - 1; i++) {
				Graph.Edge e = g.getAllEdges().get(i);
				e.weight = e.getEnd().distanceCost + lambda * (e.getEnd().damageCost);
			}

			ArrayList<Tile> aggregatedPath = super.findPath(startNode, waypoints);
			double aggregated_cost_pr = getAggregatedCostOfPath(aggregatedPath);
			double aggregated_cost_sp = getAggregatedCostOfPath(shortestPath);
			double damageCost_aggregated = getDamageCostOfPath(aggregatedPath);

			if (aggregated_cost_pr == aggregated_cost_sp) {
				return safestPath;
			} else if (damageCost_aggregated <= health) {
				safestPath = aggregatedPath;
				damageCost_safest = getDamageCostOfPath(safestPath);
				distanceCost_safest = getDistanceCostOfPath(safestPath);
			} else {
				shortestPath = aggregatedPath;
				damageCost_sp = getDamageCostOfPath(shortestPath);
				distanceCost_sp = getDistanceCostOfPath(shortestPath);
			}

		}
	}

}
