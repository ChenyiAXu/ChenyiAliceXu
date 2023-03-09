package finalproject;


import finalproject.system.Tile;
import finalproject.system.TileType;
import finalproject.tiles.MetroTile;

import java.util.ArrayList;

public class ShortestPath extends PathFindingService {
    //TODO level 4: find distance prioritized path
    public ShortestPath(Tile start) {
        super(start);
        generateGraph();
    }

    @Override
    public void generateGraph() {
        ArrayList<Tile> tiles = BFS(source);
        g = new Graph(tiles);
        for(Tile t : tiles){
            for(Tile neighbor : t.neighbors){
                if ( neighbor.isWalkable()){
                    if(t.type == TileType.Metro && neighbor.type == TileType.Metro){
                        MetroTile m = (MetroTile) t;
                         m.fixMetro(neighbor);
                        g.addEdge(t, neighbor, m.metroDistanceCost);}
                    else{
                    g.addEdge(t, neighbor, neighbor.distanceCost);}
                }
            }
        }
    }
}