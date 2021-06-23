package it.polito.tdp.borders.db;

import it.polito.tdp.borders.model.Adiacenza;
import it.polito.tdp.borders.model.Country;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BordersDAO {
	
	public List<Country> loadAllCountries(Map<Integer,Country> countriesMap) {
		
		String sql = 
				"SELECT ccode,StateAbb,StateNme " +
				"FROM country " +
				"ORDER BY StateAbb " ;

		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet rs = st.executeQuery() ;
			
			List<Country> list = new LinkedList<Country>() ;
			
			while( rs.next() ) {
				
				if(countriesMap.get(rs.getInt("ccode")) == null){
				
					Country c = new Country(
							rs.getInt("ccode"),
							rs.getString("StateAbb"), 
							rs.getString("StateNme")) ;
					countriesMap.put(c.getcCode(), c);
					list.add(c);
				} else 
					list.add(countriesMap.get(rs.getInt("ccode")));
			}
			
			conn.close() ;
			
			return list ;
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null ;
	}
	
	public List<Country> getCountriesFromYear(int anno,Map<Integer,Country> countriesMap) {
		String sql = "select * from country " + 
				"where CCode in ( " + 
				"select state1no " + 
				"from contiguity " + 
				"where year<=? and conttype=1)" ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery() ;
			
			List<Country> list = new LinkedList<Country>() ;
			
			while( rs.next() ) {
				
				if(countriesMap.get(rs.getInt("ccode")) == null){
					Country c = new Country(
							rs.getInt("ccode"),
							rs.getString("StateAbb"), 
							rs.getString("StateNme")) ;
					countriesMap.put(c.getcCode(), c);
					list.add(c);
				} else 
					list.add(countriesMap.get(rs.getInt("ccode")));
			}
			
			conn.close() ;
			
			return list ;
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null ;

	}
	
	public List<Adiacenza> getCoppieAdiacenti(int anno) {
		String sql = "select state1no, state2no " + 
				"from contiguity " + 
				"where year<=? " + 
				"and conttype=1 " + 
				"and state1no < state2no" ;
		
		List<Adiacenza> result = new ArrayList<>() ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt(1, anno);
			
			ResultSet rs = st.executeQuery() ;
			
			while(rs.next()) {
				result.add(new Adiacenza(rs.getInt("state1no"), rs.getInt("state2no"))) ;
			}
			
			conn.close();
			return result ;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
	}
	
	
	
	
}
