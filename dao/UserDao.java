package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.Controller;
import util.JDBCUtil;


public class UserDao {
	
	//싱글톤 패턴
		private UserDao(){}
		private static UserDao instance;
		public static UserDao getInstance(){
			if(instance == null){
				instance = new UserDao();
			}
			return instance;
		}
		
		private JDBCUtil jdbc = JDBCUtil.getInstance();
		
		public int insertUser(Map<String, Object> param){
			String sql = "INSERT INTO BOOKSTORE_MEMBER "
					+ "VALUES((SELECT 'M'||LTRIM(NVL(TO_CHAR(MAX(SUBSTR(MEM_CODE,2) + 1),'000'), '001')) FROM BOOKSTORE_MEMBER), ?, ?, ?, ?, ?, '브론즈', 5000, ?, ?, SYSDATE)"; //회원가입 시 BOOKSTORE에 기입할 쿼리문(회원가입 데이터를 저장하기 위해서)
			
			List<Object> p = new ArrayList<>();
			p.add(param.get("MEM_ID"));			
			p.add(param.get("MEM_PW"));			
			p.add(param.get("MEM_NAME"));			
			p.add(param.get("MEM_NUMBER"));			
			p.add(param.get("MEM_BIR"));								
			p.add(param.get("MEM_ADDRESS1"));
			p.add(param.get("MEM_ADDRESS2"));
			
			return jdbc.update(sql, p);			
		}
		public Map<String, Object> selectUser(String userID, String userPassword){
			String sql = "SELECT MEM_ID, MEM_PW, MEM_CODE "
					+ "FROM BOOKSTORE_MEMBER "
					+ "WHERE MEM_ID = ? "
					+ "AND MEM_PW = ?"; //로그인 시 BOOKSTORE에서 가입된 회원 아이디, 패스워드를 조회해서 호출함
			
			List<Object> param = new ArrayList<>();
			param.add(userID);
			param.add(userPassword);
			
			return jdbc.selectOne(sql, param);
		}
		
}















