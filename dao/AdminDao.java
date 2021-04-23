package dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import util.JDBCUtil;


public class AdminDao {

	
	private AdminDao(){}
	private static AdminDao instance;
	public static AdminDao getInstance(){
		if(instance == null){
			instance = new AdminDao();
		}
		return instance;
	}
	
	private JDBCUtil jdbc = JDBCUtil.getInstance();
	
	public Map<String, Object> selectAdmin(String adminId, String password) {
		String sql = "SELECT ADMIN_CODE, ADMIN_PW "
				+ "FROM ADMIN "
				+ "WHERE ADMIN_CODE = ? "
				+ "AND ADMIN_PW = ? ";
		
		List<Object> param = new ArrayList<>();
		param.add(adminId);
		param.add(password);
		
		return jdbc.selectOne(sql, param);
	}
	
	//매장관리관리
	
	
	public List<Map<String, Object>> select_Admin_Stock_All(){
		String sql = "SELECT *  FROM BOOK_PROD ";
		
		
		
		
		return jdbc.selectList(sql);
	}
	
	public List<Map<String, Object>> selectAdminSales(String BOOK_CATEGORY){
		String sql = "SELECT *  FROM BOOK_PROD WHERE BOOK_CATEGORY = ? ";
		
		List<Object> param = new ArrayList<>();
		param.add(BOOK_CATEGORY);
		
		return jdbc.selectList(sql, param);
	}
	

	
	
	//매출조회
	
	//일별 조회
	public List<Map<String, Object>> select_Admin_Sales_DAY(String ORDER_BUY_DATE1){
		String sql = " SELECT BO.ORDER_CODE, BO.MEM_CODE, BO.PROCESS_STATUS, BO.ORDER_BUY_DATE, BO.BOOK_CODE, SUM(BO.ORDER_QTY*BP.BOOK_PRICE) AS HAP "+
					 " FROM BOOK_ORDER BO, BOOK_PROD BP " +
					 " WHERE BO.BOOK_CODE = BP.BOOK_CODE "+ 
					 " AND BO.ORDER_BUY_DATE = TO_DATE(?) "+
					 " GROUP BY BO.ORDER_CODE, BO.MEM_CODE, BO.PROCESS_STATUS, BO.ORDER_BUY_DATE, BO.BOOK_CODE " +
					 " ORDER BY BO.ORDER_CODE, BO.MEM_CODE, BO.PROCESS_STATUS, BO.ORDER_BUY_DATE, BO.BOOK_CODE ";
		
		List<Object> param = new ArrayList<>();
		
		param.add(ORDER_BUY_DATE1);
		
		
		return jdbc.selectList(sql, param);
	}
	
	
	// 매출전체조회
	public List<Map<String, Object>> select_Admin_Sales_All(){
		String sql = " SELECT BO.ORDER_CODE, BO.MEM_CODE, BO.PROCESS_STATUS, BO.ORDER_BUY_DATE, BO.BOOK_CODE, SUM(BO.ORDER_QTY*BP.BOOK_PRICE) AS HAP "+
				 " FROM BOOK_ORDER BO, BOOK_PROD BP " +
				 " WHERE BO.BOOK_CODE = BP.BOOK_CODE "+ 
				 " GROUP BY BO.ORDER_CODE, BO.MEM_CODE, BO.PROCESS_STATUS, BO.ORDER_BUY_DATE, BO.BOOK_CODE " +
				 " ORDER BY BO.ORDER_CODE, BO.MEM_CODE, BO.PROCESS_STATUS, BO.ORDER_BUY_DATE, BO.BOOK_CODE ";
		return jdbc.selectList(sql);
	}
	
	
	
	
	//재고관리
	public List<Map<String, Object>> select_Admin_Stock_Book(){
		
		String sql = "SELECT * FROM BOOK_PROD";

		return jdbc.selectList(sql);
	}
	public List<Map<String, Object>> select_Admin_Stock_Book_Code(String BOOK_CODE){
		
		String sql = "SELECT * FROM BOOK_PROD WHERE BOOK_CODE = ?";
		
		List<Object> param = new ArrayList<>();
		param.add(BOOK_CODE);
		return jdbc.selectList(sql,param);
	}
	
	
	
	
	
	
	//공지사항관리
	
	//공지사항 조회
	
		public List<Map<String, Object>> NOTICE_Admin(){//공지사항조회
			String sql = "SELECT *  FROM NOTICE ORDER BY NOTICE_NUM DESC";
			return jdbc.selectList(sql);
		}
	
	//공지사항 내용조회
		public Map<String, Object> NOTICE_Admin_contents(int NOTICE_NUM){//공지사항내역조회
		
			String sql = "SELECT *  FROM NOTICE WHERE NOTICE_NUM = ?";
			List<Object> param = new ArrayList<>();
			param.add(NOTICE_NUM);
			return jdbc.selectOne(sql,param);
		}
	
	//공지사항 입력
		public int insertNotice(String NOTICE_TITLE, String NOTICE_CONTENTS){
			String sql = "INSERT INTO NOTICE VALUES((SELECT MAX(NOTICE_NUM)+1 FROM NOTICE),?,?,'AD01',sysdate)";
			List<Object> param = new ArrayList<>();
			param.add(NOTICE_TITLE);
			param.add(NOTICE_CONTENTS);
			
			return jdbc.update(sql ,param);
			
		}
	//공지사항 업데이트
		
		//제목수정
		public int updateNoticeTitle(int  NOTICE_NUM,String NOTICE_TITLE){
			String sql = "UPDATE NOTICE SET NOTICE_TITLE = ?  WHERE NOTICE_NUM = ? ";
			
			List<Object> param = new ArrayList<>();
			param.add(NOTICE_TITLE);
			param.add(NOTICE_NUM);
			return jdbc.update(sql, param);
		}
		//내용수정
		public int updateNoticeContents(int  NOTICE_NUM,String NOTICE_CONTENTS){
			String sql = "UPDATE NOTICE SET NOTICE_CONTENTS = ?  WHERE NOTICE_NUM = ? ";
			
			List<Object> param = new ArrayList<>();
			param.add(NOTICE_CONTENTS);
			param.add(NOTICE_NUM);
			return jdbc.update(sql, param);
		}
		
	//공지사항 삭제
		public int deleteNotice(int NOTICE_NUM){
			String sql = "DELETE FROM NOTICE WHERE NOTICE_NUM = ? ";
			
			List<Object> param = new ArrayList<>();
			param.add(NOTICE_NUM);
			return jdbc.update(sql, param);
			
		}
	
	
	
	
	
	
	
	
	
	
	//주문내역관리----------------------------------------------------------------------------------------
	
	public List<Map<String, Object>> select_Order_Admin_All(){//회원내역조회
		String sql = "select * from book_order ORDER BY ORDER_BUY_DATE DESC ";
		return jdbc.selectList(sql);
	}
	
	
	public List<Map<String, Object>> select_Order_Admin_Date(String ORDER_BUY_DATE){//날짜 별 내역조회
		String sql = "select * from book_order where ORDER_BUY_DATE = TO_DATE(?) ORDER BY ORDER_BUY_DATE DESC ";
		
		List<Object> param = new ArrayList<>();
		param.add(ORDER_BUY_DATE);
		
		return jdbc.selectList(sql,param);
	}
	
	
	public List<Map<String, Object>> select_Order_Admin_Mem(String MEM_CODE){//회원 별 내역조회
		String sql = "select * from book_order WHERE MEM_CODE = ? ORDER BY ORDER_BUY_DATE DESC ";
		List<Object> param = new ArrayList<>();
		param.add(MEM_CODE);
		
		return jdbc.selectList(sql,param);
	}
	
	
	
	
	
	
	
	
	
	
	
	//회원관리----------------------------------------------------------------------------------------
	
	//회원조회
	
	public List<Map<String, Object>> MEM_Admin(){//회원내역조회
		String sql = "SELECT *  FROM BOOKSTORE_MEMBER WHERE MEM_CODE != 'ADMIN' ORDER BY MEM_JOINDATE DESC";
		return jdbc.selectList(sql);
	}
	
	//회원업데이트
	
	//1.pw
			public int updateMemPW(String MEM_PW,String MEM_CODE){//
				String sql = "UPDATE BOOKSTORE_MEMBER SET MEM_PW = ? WHERE MEM_CODE = ? ";
				
				List<Object> param = new ArrayList<>();
				param.add(MEM_PW);
				param.add(MEM_CODE);
				
				return jdbc.update(sql, param);
			}
	
	//2.회원이름
	public int updateMemName(String MEM_NAME,String MEM_CODE){//
		String sql = "UPDATE BOOKSTORE_MEMBER SET MEM_NAME = ? WHERE MEM_CODE = ?";
		
		List<Object> param = new ArrayList<>();
		param.add(MEM_NAME);
		param.add(MEM_CODE);
		
		return jdbc.update(sql, param);
	}
	
	//3.전화번호
		public int updateMemNum(String MEM_NUMBER,String MEM_CODE){//
			String sql = "UPDATE BOOKSTORE_MEMBER SET MEM_NUMBER = ? WHERE MEM_CODE = ?";
			
			List<Object> param = new ArrayList<>();
			param.add(MEM_NUMBER);
			param.add(MEM_CODE);
			
			return jdbc.update(sql, param);
		}
		
		//4.생년월일
		public int updateMemBir(String MEM_BIR,String MEM_CODE){//
			String sql = "UPDATE BOOKSTORE_MEMBER SET MEM_BIR = ? WHERE MEM_CODE = ?";
			
			List<Object> param = new ArrayList<>();
			param.add(MEM_BIR);
			param.add(MEM_CODE);
			
			return jdbc.update(sql, param);
		}
		
		//5.등급
		public int updateMemGrade(String MEM_GRADE,String MEM_CODE){//
			String sql = "UPDATE BOOKSTORE_MEMBER SET MEM_GRADE = ? WHERE MEM_CODE = ?";
			
			List<Object> param = new ArrayList<>();
			param.add(MEM_GRADE);
			param.add(MEM_CODE);
			
			return jdbc.update(sql, param);
		}
		
		//6.마일리지
		public int updateMemMileage(String MEM_MILEAGE,String MEM_CODE){//
			String sql = "UPDATE BOOKSTORE_MEMBER SET MEM_MILEAGE = ? WHERE MEM_CODE = ?";
			
			List<Object> param = new ArrayList<>();
			param.add(MEM_MILEAGE);
			param.add(MEM_CODE);
			
			return jdbc.update(sql, param);
		}
		
		//7.회원주소
		public int updateMemAddress1(String MEM_ADDRESS1,String MEM_CODE){//
			String sql = "UPDATE BOOKSTORE_MEMBER SET MEM_ADDRESS1 = ? WHERE MEM_CODE = ?";
			
			List<Object> param = new ArrayList<>();
			param.add(MEM_ADDRESS1);
			param.add(MEM_CODE);
			
			return jdbc.update(sql, param);
		}
		
		//8.상세주소
		public int updateMemAddress2(String MEM_ADDRESS2,String MEM_CODE){//
			String sql = "UPDATE BOOKSTORE_MEMBER SET MEM_ADDRESS2 = ? WHERE MEM_CODE = ?";
			
			List<Object> param = new ArrayList<>();
			param.add(MEM_ADDRESS2);
			param.add(MEM_CODE);
			
			return jdbc.update(sql, param);
		}
		
		//9.계좌
		public int updateMemAccount(String MEM_ACCOUNT,String MEM_CODE){//
			String sql = "UPDATE BOOKSTORE_MEMBER SET MEM_ACCOUNT = ? WHERE MEM_CODE = ?";
			
			List<Object> param = new ArrayList<>();
			param.add(MEM_ACCOUNT);
			param.add(MEM_CODE);
			
			return jdbc.update(sql, param);
		}
		
		
		
		
	
	
	//---------------------------
	//회원내역 삭제
	public int deleteMem(String MEM_CODE){
		String sql = "DELETE FROM BOOKSTORE_MEMBER WHERE MEM_CODE =?";
		
		List<Object> param = new ArrayList<>();
		param.add(MEM_CODE);
		return jdbc.update(sql, param);
		
	}
	
	
	//------------------------------------------------------
	
	//1:1QA_all
	public List<Map<String, Object>>  select_List_Qa(){
		String sql = "Select  QA_NUM, QA_TITLE,QA_CONTENTS,QA_WDATE  from QA ORDER BY QA_NUM DESC";
		
		return jdbc.selectList(sql);
	}
	//1:1QA_choice
	public List<Map<String, Object>>  select_List_Qa_Choice(int QA_CODE){
		String sql = "Select QA_NUM, QA_TITLE,QA_CONTENTS,QA_WDATE,NVL(QA_REPLY,'답글이 없습니다.') AS QA_REPLY,QA_REPLYDATE FROM QA WHERE QA_NUM = ? ORDER BY QA_NUM DESC ";
		List<Object> param = new ArrayList<>();
		param.add(QA_CODE);
		return jdbc.selectList(sql,param);
	}
	
	//1:1QA_답글 작성
	public int select_List_Qa_Choice_Reply_update(String QA_REPLY , int QA_CODE){
		String sql = "UPDATE QA SET QA_REPLY = ?,QA_REPLYDATE = sysdate, ADMIN_CODE ='AD001' WHERE QA_NUM = ?  ";
		List<Object> param = new ArrayList<>();
		param.add(QA_REPLY);
		param.add(QA_CODE);
		
		return jdbc.update(sql, param);
	}
	
}
