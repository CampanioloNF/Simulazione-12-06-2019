package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;


public class FoodDao {

	public List<Food> listAllFood(){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Food(res.getInt("food_id"),
							res.getInt("food_code"),
							res.getString("display_name"), 
							res.getInt("portion_default"), 
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"),
							res.getDouble("calories")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Condiment> listAllCondiment(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_id"),
							res.getInt("food_code"),
							res.getString("display_name"), 
							res.getString("condiment_portion_size"), 
							res.getDouble("condiment_calories")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}

	}
	
	

	public void loadGraph(Graph<Condiment, DefaultWeightedEdge> grafo, Map<Integer, Condiment> idCondimentMap,
			int calorie) {
		
		String sql = "SELECT c1.condiment_id, c2.condiment_id, COUNT(*) AS peso " + 
				"FROM condiment c1, condiment c2, food_condiment fc1, food_condiment fc2 " + 
				"WHERE c1.condiment_calories < ? AND c2.condiment_calories < ? " + 
				"AND fc1.food_code = fc2.food_code " + 
				"AND fc1.condiment_food_code = c1.food_code " + 
				"AND fc2.condiment_food_code = c2.food_code " + 
				"AND c2.condiment_id > c1.condiment_id " + 
				"GROUP BY  c1.condiment_id, c2.condiment_id  " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt(1, calorie);
			st.setInt(2, calorie);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
				if(idCondimentMap.containsKey(res.getInt("c1.condiment_id")) 
						&& idCondimentMap.containsKey(res.getInt("c2.condiment_id"))) {
					
					//aggiungo l'arco
					Graphs.addEdge(grafo, idCondimentMap.get(res.getInt("c1.condiment_id")), 
							idCondimentMap.get(res.getInt("c2.condiment_id")), res.getInt("peso"));
					
				}
				
				
			}
			
			conn.close();
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
		
		
	}

	public void loadVertex(Graph<Condiment, DefaultWeightedEdge> grafo, Map<Integer, Condiment> idCondimentMap,
			int calorie) {
		
		String sql = "SELECT condiment_id, food_code, display_name, condiment_portion_size, condiment_calories " + 
				"FROM condiment c " + 
				"WHERE c.condiment_calories < ? " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt(1, calorie);
			
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
				//controllo che la mappa contenga i vertici altrimenti li aggiungo
				if(!idCondimentMap.containsKey(res.getInt("condiment_id"))) {
					
					Condiment cond = new Condiment(res.getInt("condiment_id"), res.getInt("food_code"),
							res.getString("display_name"), res.getString("condiment_portion_size"), 
							res.getDouble("condiment_calories"));
					
					idCondimentMap.put(cond.getCondiment_id(), cond);
					grafo.addVertex(cond);
					
				}
				
			}
			
			conn.close();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
		
		
		
	}
}
