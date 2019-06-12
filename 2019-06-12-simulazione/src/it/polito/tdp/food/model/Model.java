package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {

	private FoodDao dao;
	private Graph<Condiment, DefaultWeightedEdge> grafo;
	private Map<Integer, Condiment> idCondimentMap;
	private List<Condiment> dieta;
	private List<Condiment> vertici;
	private double cal;
	
	public Model() {
	
		this.dao = new FoodDao();
		
	}

	public void creaGrafo(int calorie) {
		
		this.idCondimentMap = new HashMap<Integer, Condiment>();
		this.grafo = new SimpleWeightedGraph<Condiment, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//carico i vertici
		dao.loadVertex(grafo, idCondimentMap, calorie);
		// carico il grafo
		dao.loadGraph(grafo, idCondimentMap, calorie);
		
	}
	
	
	public List<Condiment> getListCondiment(){
	
		if(grafo!= null) {
		
			List<Condiment> vertici = new LinkedList<Condiment>();
			
			for(Condiment con : grafo.vertexSet()) {
				
				int numFoods = 0;
				
				for(Condiment vicino : Graphs.neighborListOf(grafo, con)) {
					
					
					numFoods+= grafo.getEdgeWeight(grafo.getEdge(con, vicino));
					
				}
				
				con.setFoods(numFoods);
				vertici.add(con);
			}
			
			Collections.sort(vertici);
			
			return vertici;
			
		
		}return null;
	}
	
	public List<Condiment> dietaOttima(Condiment primo){
		
		if(grafo!=null) {
		
		this.cal = 0.0;
		
		Set<Condiment> parziale = new HashSet<Condiment>();
		parziale.add(primo);
		this.dieta = new ArrayList<Condiment>(parziale);
		
		// ho tutti i vertici
		this.vertici = new ArrayList<Condiment>(grafo.vertexSet());
	
		Set<Condiment> disponibili = new HashSet<Condiment>(grafo.vertexSet());
		
		disponibili.remove(primo);
		disponibili.removeAll(Graphs.neighborListOf(grafo, primo));
	 
		cerca(parziale, 0, disponibili);
		
		return dieta;
		
		}
		return null;
	}

	private void cerca(Set<Condiment> parziale, int L,  Set<Condiment> disponibili) {
	
	
		if(disponibili.isEmpty()) {
			
			double punti  = calcola(parziale);
			if(punti>cal) {
				cal=punti;
				dieta = new ArrayList<Condiment>(parziale);
			}
			
			return;
		}
		
		if(L==vertici.size())
			return;
		
		
		cerca(parziale, L+1, disponibili);
		
		parziale.add(vertici.get(L));
		
		if(disponibili.contains(vertici.get(L)))
		  disponibili.remove(vertici.get(L));
		for(Condiment c : Graphs.neighborListOf(grafo, vertici.get(L))) {
			if(disponibili.contains(c))
			  disponibili.remove(c);
		 
		}
		
		cerca(parziale, L+1, disponibili);
		
		parziale.add(vertici.get(L));
		
		if(!disponibili.contains(vertici.get(L)))
			  disponibili.add(vertici.get(L));
			for(Condiment c : Graphs.neighborListOf(grafo, vertici.get(L))) {
				if(!disponibili.contains(c))
				  disponibili.add(c);
			 
			}
		
	}

	private double calcola(Set<Condiment> parziale) {
		
		double punti = 0.0;
		
		for(Condiment c : parziale)
			punti+=c.getCondiment_calories();
		
		return punti;
	}
}
