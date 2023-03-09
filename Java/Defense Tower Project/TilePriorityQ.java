package finalproject;

import java.util.ArrayList;
import java.util.Arrays;


import finalproject.system.Tile;

public class TilePriorityQ {
	// TODO level 3: Add fields that can help you implement this data type
	ArrayList<Tile> priorityQ;
	// TODO level 3: implement the constructor for the priority queue
	public TilePriorityQ (ArrayList<Tile> vertices) {
		ArrayList<Tile> priorityQ = new ArrayList<Tile>();
		priorityList(vertices, priorityQ);
		this.priorityQ = priorityQ;
	}

	public ArrayList<Tile> priorityList(ArrayList<Tile> input, ArrayList<Tile> output) {
		for(int i = 0; i < input.size()-1; i++){
			for(int j=0; j<input.size()-i-1; j++){
				if(input.get(j).costEstimate > input.get(j+1).costEstimate){
					Tile temp = input.get(j);
					input.set(j, input.get(j+1));
					input.set(j+1, temp);
				}
			}
		}
		for(int i = 0; i < input.size(); i++){
			output.add(input.get(i));
		}

		return output;
	}

	
	// TODO level 3: implement remove min as seen in class
	public Tile removeMin() {
		if (priorityQ.isEmpty()) {
			return null;
		}
		return priorityQ.remove(0);
	}
	
	// TODO level 3: implement updateKeys as described in the pdf
	public void updateKeys(Tile t, Tile newPred, double newEstimate) {
		if(priorityQ.contains(t)) {
			t.predecessor = newPred;
			t.costEstimate = newEstimate;
		}
		ArrayList<Tile> temp = new ArrayList<Tile>();
		this.priorityQ= priorityList(priorityQ, temp);
	}
	
}
