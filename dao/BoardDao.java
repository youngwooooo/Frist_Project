package dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.Controller;
import util.JDBCUtil;
import util.ScanUtil;


public class BoardDao {
	public static String orderCode;
	/////////////////////////////////////////////
	private BoardDao() {}
	private static BoardDao instance;
	public static BoardDao getInstance() {
		if (instance == null) {
			instance = new BoardDao();
		}
		return instance;
	}	
	/////////////////////////////////////////////
	
	private JDBCUtil jdbc= JDBCUtil.getInstance();
	
	// 카테고리 종류
	public List<Map<String, Object>> selectBoardList_CategoryKind(){
		String sql = "SELECT *"
				+ "FROM CATEGORY";
		
		return jdbc.selectList(sql);
	}
	
	
	//카테고리 검색용 DB메소드 
		public List<Map<String, Object>> selectBoardList2(int category){
			String sql = "SELECT BOOK_CODE, BOOK_NAME, BOOK_WRITER, BOOK_PUBLISHER, BOOK_PRICE, BOOK_CATEGORY "
	                     + "FROM BOOK_PROD "
	                     + "WHERE BOOK_CATEGORY = ? "
	                     + "ORDER BY BOOK_NAME";                     
			
			List<Object> param = new ArrayList<>();
			param.add(category);
			
			return jdbc.selectList(sql, param);
		}
		
		//도서 정보 출력용 DB메소드
		public Map<String, Object> selectBoardList_InfoBook(String bookNumber){
			String sql = "SELECT * FROM BOOK_PROD WHERE BOOK_CODE = ? ";
			
			List<Object> param = new ArrayList<>();
			param.add(bookNumber);
			
			return jdbc.selectOne(sql, param);
		}
		
		//키워드(도서명) 검색용 DB메소드
		public List<Map<String, Object>> selectBoardList3(String bookName){
			String sql ="SELECT BOOK_CODE, BOOK_NAME, BOOK_WRITER, BOOK_PUBLISHER, BOOK_PRICE, BOOK_CATEGORY "
	                + "FROM BOOK_PROD "
	                + "WHERE BOOK_NAME LIKE '%' || ? || '%' "
	                + "ORDER BY BOOK_NAME";
			
			List<Object> param = new ArrayList<>();
			param.add(bookName);
			
			return jdbc.selectList(sql, param);
		}
		
		//키워드(작가명) 검색용 DB메소드
		public List<Map<String, Object>> selectBoardList4(String authorName){
			String sql = "SELECT BOOK_CODE, BOOK_NAME, BOOK_WRITER, BOOK_PUBLISHER, BOOK_PRICE, BOOK_CATEGORY "
	                + "FROM BOOK_PROD "
	                + "WHERE BOOK_WRITER LIKE '%' || ? || '%' "
	                + "ORDER BY BOOK_NAME";
			
			List<Object> param = new ArrayList<>();
			param.add(authorName);
			
			return jdbc.selectList(sql, param);
		}
		
		//리뷰보기 DB메소드
		public List<Map<String, Object>> selectBoardList5(String bookCode){
			String sql = "SELECT REVIEW_CONTENTS, REVIEW_STAR, REVIEW_WRITEDATE " 
						  + "FROM REVIEW "
						  + "WHERE BOOK_CODE = ?";
			List<Object> param = new ArrayList<>();
			param.add(bookCode); //도서코드 받아와서 해당 도서 리뷰 보기
			
			return jdbc.selectList(sql, param); 
		}
		
		//리뷰작성 DB메소드
		public int updateList1(String content, int star, Object mem_Id, Object bookCode){
			String sql = "INSERT INTO REVIEW VALUES("
						+ "(SELECT NVL(MAX(REVIEW_NUM), 0) + 1 "
						  + "FROM REVIEW), ?, ?, SYSDATE, " 
	                       + "(SELECT MEM_CODE "
	                           + "FROM BOOKSTORE_MEMBER "
	                          + "WHERE MEM_ID = ?), "
	                       + "(SELECT BOOK_CODE "
	                           + "FROM BOOK_PROD "
	                          + "WHERE BOOK_CODE = ?))";		
			
			List<Object> param = new ArrayList<>();
			param.add(content); //내용
			param.add(star); //별점
			param.add(Controller.LoginUser.get("MEM_ID")); //회원아이디
			param.add(Controller.CurrentBook.get("BOOK_CODE")); //도서코드
			
			return jdbc.update(sql, param);
		}
		
		// [1.도서조회] : 도서 전체 목록
		public List<Map<String, Object>> BoardList(){
			String sql = "SELECT BOOK_CODE, BOOK_NAME, BOOK_WRITER, BOOK_PUBLISHER, BOOK_PRICE, BOOK_CATEGORY "
					+ "FROM BOOK_PROD "
					+ "ORDER BY BOOK_CODE "; 
			
			return jdbc.selectList(sql);
		}
		
		
	// [1.도서조회] - [1. 도서 선택]
	public Map<String, Object> seletBook(String bookCode){
		String sql = "SELECT BOOK_CODE, BOOK_NAME, BOOK_WRITER, BOOK_PUBLISHER, BOOK_PRICE, BOOK_CATEGORY, BOOK_INTRO "
				+ "FROM BOOK_PROD "
				+ "WHERE BOOK_CODE = ?";
		
		List<Object> param = new ArrayList<>();
		param.add(bookCode);
		
		
		return jdbc.selectOne(sql, param);
	}
	
	// [1.도서조회] - [1.도서선택] - [1.구매]
	public int buy(){
		System.out.print("도서코드를 입력해주세요 > ");
		String bookCode = ScanUtil.nextLine();
		System.out.print("수량을 입력해주세요 > ");
		int qty = ScanUtil.nextInt();
		System.out.println("구매가 완료되었습니다.");
	
		String sql = "INSERT INTO BOOK_ORDER "
				+ "VALUES ((SELECT TO_CHAR(SYSDATE, 'YYYYMMDD')||LTRIM(NVL(TO_CHAR(MAX(SUBSTR(ORDER_CODE,9) + 1),'00000'), '00001')) FROM BOOK_ORDER), "
				+ "?, '결제완료', SYSDATE, ?, ?)";
		
		List<Object> param = new ArrayList<>();
		param.add(Controller.LoginUser.get("MEM_CODE"));
		param.add(bookCode);
		param.add(qty);
		
		return jdbc.update(sql, param);
	}
	
	// [주문취소]
	public int deletOrder(){
		String sql = "DELETE FROM BOOK_ORDER WHERE ORDER_CODE = ?";
		
		List<Object> param = new ArrayList<>();
		param.add(orderCode);
		
		System.out.println("구매가 취소되었습니다.");
		
		return jdbc.update(sql, param);
	}
	
	// [구매 시 도서재고 감소]
	public int minusbook(){
		String sql = "UPDATE BOOK_PROD "
				+ "SET BOOK_TOTALSTOKE = BOOK_TOTALSTOKE - (SELECT ORDER_QTY FROM "
				+ "(SELECT ROWNUM RN, BO.* "
				+ "FROM BOOK_ORDER BO "
				+ "WHERE ROWNUM = 1 "
				+ "ORDER BY ORDER_CODE DESC)) "
				+ "WHERE BOOK_CODE = (SELECT BOOK_CODE "
				+ "FROM (SELECT ROWNUM RN, BO.* "
				+ "FROM BOOK_ORDER BO "
				+ "WHERE ROWNUM = 1 "
				+ "ORDER BY ORDER_CODE DESC))";
		return jdbc.update(sql);
	}
	// [구매취소 시 도서재고 증가]
	public int plusbook(){
		System.out.print("취소할 주문번호를 입력해주세요 > ");
		orderCode = ScanUtil.nextLine();
		
		String sql = "UPDATE BOOK_PROD "
				+ "SET BOOK_TOTALSTOKE = BOOK_TOTALSTOKE + (SELECT ORDER_QTY FROM "
				+ "(SELECT ROWNUM RN, BO.* "
				+ "FROM BOOK_ORDER BO "
				+ "WHERE ORDER_CODE = ? "
				+ "ORDER BY ORDER_CODE DESC)) "
				+ "WHERE BOOK_CODE = (SELECT BOOK_CODE "
				+ "FROM (SELECT ROWNUM RN, BO.* "
				+ "FROM BOOK_ORDER BO "
				+ "WHERE ORDER_CODE = ? "
				+ "ORDER BY ORDER_CODE DESC))";
		
		List<Object> param = new ArrayList<>();
		param.add(orderCode);
		param.add(orderCode);
		return jdbc.update(sql, param);
	}
	
	
	// [2. 공지사항] : 전체목록 조회
	public List<Map<String, Object>> NoticeMain(){
		String sql = "SELECT NOTICE_NUM, NOTICE_TITLE, NOTICE_DATE FROM NOTICE ORDER BY NOTICE_NUM DESC";
		
		return jdbc.selectList(sql);
	}
	
	// [2. 공지사항] - [1. 공지사항 조회] : 공지사항 세부내용
	public Map<String, Object> selectNotice(int noticeNum){
		
		String sql = "SELECT NOTICE_NUM, NOTICE_TITLE, NOTICE_CONTENTS, ADMIN_CODE, NOTICE_DATE FROM NOTICE WHERE NOTICE_NUM = ?";
		
		List<Object> param = new ArrayList<>();
		param.add(noticeNum);
		
		return jdbc.selectOne(sql, param);
	}
	
	// [4.마이페이지] - [1.내 정보 수정] - [1.개인정보 수정]
	public int changeInfo(){
		System.out.print("수정할 기본주소 > ");
		String address1 = ScanUtil.nextLine();
		System.out.print("수정할 상세주소 > ");
		String address2 = ScanUtil.nextLine();
		System.out.print("수정할 휴대폰번호 > ");
		String number = ScanUtil.nextLine();
		
		String sql = "UPDATE BOOKSTORE_MEMBER "
				+ "SET MEM_ADDRESS1 = ?, MEM_ADDRESS2 = ?, MEM_NUMBER = ? "
				+ "WHERE MEM_ID = ? "
				+ "AND MEM_PW = ?";
		
		List<Object> param = new ArrayList<>();
		param.add(address1);
		param.add(address2);
		param.add(number);
		param.add(Controller.LoginUser.get("MEM_ID"));
		param.add(Controller.LoginUser.get("MEM_PW"));
		
		return jdbc.update(sql, param);
	}
	
	// [4.마이페이지] - [1.내 정보 수정] - [2.비밀번호 수정]
	public int changePassword(){
		System.out.print("변경할 비밀번호를 입력해주세요 > ");
		String changepassword = ScanUtil.nextLine();
		
		String sql = "UPDATE BOOKSTORE_MEMBER SET MEM_PW = ? WHERE MEM_PW = ?";
		
		List<Object> param = new ArrayList<>();
		param.add(changepassword);
		param.add(Controller.LoginUser.get("MEM_PW"));
		
		return jdbc.update(sql, param);
	}
	
	// [4.마이페이지] - [2.주문내역 확인] : 주문 내역 전체 출력
	public List<Map<String, Object>> orderList(){
		String sql = "SELECT ROWNUM RN, A.ORDER_CODE, A.PROCESS_STATUS, A.ORDER_BUY_DATE, A.ORDER_QTY "
				+ "FROM (SELECT ORDER_CODE, PROCESS_STATUS, ORDER_BUY_DATE, ORDER_QTY "
				+ "FROM BOOK_ORDER "
				+ "WHERE MEM_CODE = ? "
				+ "ORDER BY ORDER_CODE DESC) A";
		
		List<Object> param = new ArrayList<>();
		param.add(Controller.LoginUser.get("MEM_CODE"));
		
		return jdbc.selectList(sql, param);
	}
	
	// [4.마이페이지] - [2.주문내역 확인] - [1.상세조회]
	public Map<String, Object> selectOrder(){
		System.out.print("상세조회할 주문내역 번호를 선택해주세요 > ");
		int num = ScanUtil.nextInt();
		
		String sql = "SELECT B.*"
				+ "FROM (SELECT ROWNUM RN, A.* "
				+ "FROM (SELECT BO.ORDER_CODE, BO.PROCESS_STATUS, BO.ORDER_BUY_DATE, BP.BOOK_NAME, BO.ORDER_QTY, SUM(BOOK_PRICE*ORDER_QTY) PRICE "
				+ "FROM BOOK_ORDER BO, BOOK_PROD BP "
				+ "WHERE BO.BOOK_CODE = BP.BOOK_CODE "
				+ "GROUP BY BO.ORDER_CODE, BO.PROCESS_STATUS, BO.ORDER_BUY_DATE, BP.BOOK_NAME, BO.ORDER_QTY "
				+ "ORDER BY BO.ORDER_CODE DESC) A "
				+ "WHERE ROWNUM <= 50) B "
				+ "WHERE B.RN = ?";
		
		List<Object> param =  new ArrayList<>();
		param.add(num);
		
		return jdbc.selectOne(sql, param);
	}
	
	// [4.마이페이지] - [3. 1:1문의] : 문의글 전체목록 조회
	public List<Map<String, Object>> qaMain(){
		String sql = "SELECT Q.QA_NUM, Q.QA_TITLE, Q.QA_WDATE, BM.MEM_NAME "
				+ "FROM QA Q, BOOKSTORE_MEMBER BM "
				+ "WHERE Q.MEM_CODE = BM.MEM_CODE "
				+ "AND Q.MEM_CODE = ?"
				+ "ORDER BY Q.QA_NUM DESC";
		
		List<Object> param = new ArrayList<>();
		param.add(Controller.LoginUser.get("MEM_CODE"));
		
		return jdbc.selectList(sql, param);
	}
	
	// [4.마이페이지] - [3. 1:1문의] - [1. 문의글 선택]
	public Map<String, Object> selectQa(int qaNum){
		
		String sql = "SELECT QA_NUM, QA_TITLE, QA_CONTENTS, QA_WDATE, ADMIN_CODE, QA_REPLY, QA_REPLYDATE "
				+ "FROM QA "
				+ "WHERE QA_NUM = ?";
		
		List<Object> param = new ArrayList<>();
		param.add(qaNum);
		
		return jdbc.selectOne(sql, param);
	}
	
	// [4.마이페이지] - [3. 1:1문의] - [2.문의글 작성]
	public int insertQa(){
		System.out.print("문의글 제목을 입력해주세요 > ");
		String qatitle = ScanUtil.nextLine();
		System.out.println("문의글 내용을 입력해주세요 > ");
		String qacontents = ScanUtil.nextLine();
		
		String sql = "INSERT INTO QA(QA_NUM, QA_TITLE, QA_CONTENTS, QA_WDATE, MEM_CODE) "
				+ "VALUES((SELECT NVL(MAX(QA_NUM), 0) + 1 FROM QA), "
				+ "?, ?, SYSDATE, ?)";
		
		List<Object> param = new ArrayList<>();
		param.add(qatitle);
		param.add(qacontents);
		param.add(Controller.LoginUser.get("MEM_CODE"));
		
		return jdbc.update(sql, param);
	}
}















