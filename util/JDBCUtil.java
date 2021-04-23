package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JDBCUtil {
	
	private JDBCUtil(){};
	private static JDBCUtil instance;
	
	public static JDBCUtil getInstance(){
		if(instance == null){
			instance = new JDBCUtil();
		}
		return instance;
	}
	
	String url = "jdbc:oracle:thin:@localhost:1521:xe";
	String user = "First_Project";
	String password = "java";
	
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	public Map<String, Object> selectOne(String sql){
		Map<String, Object> row = null;
		try {
			con = DriverManager.getConnection(url, user, password);
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			while(rs.next()){
				row = new HashMap<>();
				for(int i = 1; i <= columnCount; i++){
					String key = metaData.getColumnName(i);
					Object value = rs.getObject(i);
					row.put(key, value);
				}
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs != null) try{ rs.close(); } catch(Exception e){}
			if(ps != null) try{ ps.close(); } catch(Exception e){}
			if(con != null) try{ con.close(); } catch(Exception e){}
		}
		return row;
	}//Map<String, Object> selectOne(String sql)
	
	
	public Map<String, Object> selectOne(String sql, List<Object> param){
		Map<String, Object> row = null;
		
		try{
			con = DriverManager.getConnection(url, user, password);
			ps = con.prepareStatement(sql);
			for(int i = 0; i < param.size(); i++){
				ps.setObject(i + 1, param.get(i));
			}
			rs = ps.executeQuery();
			
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			while(rs.next()){
				row = new HashMap<>();
				for(int i = 1; i <= columnCount; i++){
					String key = metaData.getColumnName(i);
					Object value = rs.getObject(i);
					row.put(key, value);
				}
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			if(rs != null) try{ rs.close(); } catch(Exception e){};
			if(ps != null) try{ ps.close(); } catch(Exception e){};
			if(con != null) try{ con.close(); } catch(Exception e){};
		}
		return row;
	}//Map<String, Object> selectOne(String sql, List<Object> param)
	
	public List<Map<String, Object>> selectList(String sql){
		List<Map<String, Object>> list = new ArrayList<>();
		
		try {
			con = DriverManager.getConnection(url, user, password);
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			while(rs.next()){
				HashMap<String, Object> row = new HashMap<>();
				for(int i = 1; i <= columnCount; i++){
					String key = metaData.getColumnName(i);
					Object value = rs.getObject(i);
					row.put(key, value);
				}
				list.add(row);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs != null) try{ rs.close(); } catch(Exception e){ };
			if(ps != null) try{ ps.close(); } catch(Exception e){ };
			if(con != null) try{ con.close(); } catch(Exception e){ };
		}
		return list;
	}
	
	public List<Map<String, Object>> selectList(String sql, List<Object> param){
		List<Map<String, Object>> list = new ArrayList<>();
		
		try {
			con = DriverManager.getConnection(url, user, password);
			ps = con.prepareStatement(sql);
			for(int i = 0; i < param.size(); i++){
				ps.setObject(i + 1, param.get(i));
			}
			rs = ps.executeQuery();
			
			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			
			while(rs.next()){
				HashMap<String, Object> row = new HashMap<>();
				
				for(int i = 1; i <= columnCount; i++){
					String key = metaData.getColumnName(i);
					Object value = rs.getObject(i);
					row.put(key, value);
				}
				list.add(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs != null) try{ rs.close(); } catch(Exception e){} 
			if(ps != null) try{ ps.close(); } catch(Exception e){} 
			if(con != null) try{ con.close(); } catch(Exception e){} 
		}
		return list;
	}
	
	public int update(String sql){
		int result = 0;
		
		try {
			con = DriverManager.getConnection(url, user, password);
			ps = con.prepareStatement(sql);
			result = ps.executeUpdate();			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs != null) try{ rs.close(); } catch(Exception e){}
			if(ps != null) try{ ps.close(); } catch(Exception e){}
			if(con != null) try{ con.close(); } catch(Exception e){}
		}
		
		return result;
	}
	
	public int update(String sql, List<Object> param){
		int result = 0;
		
		try {
			con = DriverManager.getConnection(url, user, password);
			ps = con.prepareStatement(sql);
			for(int i =0; i < param.size(); i++){
				ps.setObject(i + 1, param.get(i));
			}
			result = ps.executeUpdate();	
			
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(rs != null) try{ rs.close(); } catch(Exception e){}
			if(ps != null) try{ ps.close(); } catch(Exception e){}
			if(con != null) try{ con.close(); } catch(Exception e){}
		}
		return result;
	}
	
}



























