package finalproject;

import finalproject.system.Tile;

import java.util.ArrayList;

import finalproject.system.TileType;
import finalproject.tiles.MetroTile;

public class FastestPath extends PathFindingService {
    //TODO level 6: find time prioritized path
    public FastestPath(Tile start) {
        super(start);
        generateGraph();
    }

	@Override
	public void generateGraph() {
		// TODO Auto-generated method stub
        ArrayList<Tile> tiles = BFS(source);
        g = new Graph(tiles);
        for(Tile t : tiles){
            for(Tile neighbor : t.neighbors){
                if ( neighbor.isWalkable()) {
                if (t.type == TileType.Metro && neighbor.type == TileType.Metro) {
                        MetroTile m = (MetroTile) t;
                        m.fixMetro(neighbor);
                        g.addEdge(t, neighbor, m.metroTimeCost);
                    } else {
                        g.addEdge(t, neighbor, neighbor.timeCost);
                    }


                }
            }
        }
   }


}
