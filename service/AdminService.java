package service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.xml.sax.HandlerBase;

import controller.Controller;
import util.ScanUtil;
import util.View;
import dao.AdminDao;
import dao.UserDao;

public class AdminService {
SimpleDateFormat fm = new SimpleDateFormat("yyyy/MM/dd");
	
	
	//싱글톤 패턴
	private AdminService(){}
	private static AdminService instance;
	public static AdminService getInstance(){
			if(instance == null){
				instance = new AdminService();
			}
			return instance;
	}
	
	private AdminDao adminDao = AdminDao.getInstance();
	
	public int adminLogin(){
		System.out.println("▶▶▶▶▶▶▶관리자 로그인◀◀◀◀◀◀◀");
		System.out.print("관리자ID\t: ");
		String adminId = ScanUtil.nextLine();
		System.out.print("비밀번호\t: ");
		String password = ScanUtil.nextLine();
		
		Map<String, Object> user = adminDao.selectAdmin(adminId, password);
		if(user == null){
			System.out.println("아이디 혹은 비밀번호를 잘못입력하셨습니다.");
			return adminLogin();
		}else{
			System.out.println("로그인 성공!");
			
			Controller.LoginUser = user;
			
			return admin_home();	
		}
		
	}
	
	//관리자 홈
	
	// 관리자 메인
	public int admin_home(){
		System.out.println("▶=============================================================================◀");
//		System.out.println("회원  : "+ LoginUser.get("MEM_ID"));
		System.out.println("1.매장관리\t2.공지사항 관리\t3.주문관리\t4.회원관리\t0.프로그램 종료");
		System.out.println("▶=============================================================================◀");
		System.out.print("번호 선택> ");
		int input = ScanUtil.nextInt();
		
		switch(input){
		
		case 1: return prod_manage() ;
		case 2: return notice_manage();
		case 3: return order_manage();
		case 4: return mem_manage();
		case 0:
		System.out.println("프로그램이 종료되었습니다.");
		System.exit(0);
		}
		
		return admin_home();
		
	}
	
	
	
	
	
	
	
	
	
	//관리자 관리 페이지	
	
	
	
 public int prod_manage(){
	while(true){
		System.out.println("▶=============================================================================◀");
		System.out.println("1.매출관리\t2.재고관리\t0.뒤로가기(←)");
		System.out.println("▶=============================================================================◀");
		System.out.print("번호 선택>");
	 int input = ScanUtil.nextInt();
	 switch(input){
	 case 1://매출관리
		    
		 	System.out.println("▶=============================================================================◀");
		 	System.out.println("1.일별조회\t2.전체조회\t0.뒤로가기(←)");
		 	System.out.println("▶=============================================================================◀");
		 		System.out.print("번호 선택>");
		 		input = ScanUtil.nextInt();
		 		switch(input){
		 		case 1://매출 일별 조회
		 			 select_sale_date();
		 			 break;
		 		case 2://매출 전체 조회
		 			 select_sale_All();
		 			 break;
		 		case 0://뒤로가기
		 			
		 			return prod_manage();
		 			
		 		}
		 		return prod_manage();
		 
		
		
	 case 2:	
		 //재고관리
		 System.out.println("재고관리 조회");
		 
		 		System.out.println("▶=============================================================================◀");
			 	System.out.println("1.재고관리 조회\t0.뒤로가기(←)");
			 	System.out.println("▶=============================================================================◀");
			 		System.out.print("번호 선택> ");
			 		input = ScanUtil.nextInt();
			 		switch(input){
			 		case 1://재고 조회
			 			select_stock_book();
			 			 break;
			 		
			 		case 0://뒤로가기
			 			return prod_manage();
			 		}
			 		
			 		return prod_manage();
		 
		 
	 case 0://뒤로가기
		 return admin_home();
	 }
	 
	}
	 
	
	 
 }
 
 
 //매출조회
 public int select_sale_All() {
	System.out.println("▶=============================================================================◀");
	 System.out.println("전체 매출을 조회합니다.");
	System.out.println("▶=============================================================================◀");
	 List<Map<String, Object>> sales_day_list = adminDao.select_Admin_Sales_All();
	 System.out.println("주문번호\t\t처리상태\t처리날짜\t\t회원코드\t매출");
	 for(Map<String, Object> sday : sales_day_list){
		 System.out.println(sday.get("ORDER_CODE")+"\t"+sday.get("PROCESS_STATUS")+"\t"+fm.format(sday.get("ORDER_BUY_DATE"))+"\t"+sday.get("MEM_CODE")+"\t"+sday.get("HAP"));
	 }
	 return 0;
 }
 
 public int select_sale_date() {
	System.out.println("▶=============================================================================◀");
	 System.out.print("검색하고 싶은 날짜를 입력해주세요>");
	System.out.println("▶=============================================================================◀");
	System.out.print("날짜 입력> ");
	 String str = ScanUtil.nextLine();
	 String ORDER_BUY_DATE1 = str.toUpperCase();
	 System.out.println("주문번호\t\t처리상태\t처리날짜\t\t회원코드\t매출");
	 List<Map<String, Object>> sales_day_list = adminDao.select_Admin_Sales_DAY(ORDER_BUY_DATE1);
	 for(Map<String, Object> sday : sales_day_list){
		 System.out.println(sday.get("ORDER_CODE")+"\t"+sday.get("PROCESS_STATUS")+"\t"+fm.format(sday.get("ORDER_BUY_DATE"))+"\t"+sday.get("MEM_CODE")+"\t"+sday.get("HAP"));
	 }
	
	 return 0;
 }

 
 
 
 
 
 
// System.out.println("재고관리 조건을입력해주세요");
// List<Map<String, Object>> Stock_All = adminDao.select_Admin_Stock_All();
// System.out.println("===============================");
//	System.out.println("책코드\t제목\t남은 수량");
//	System.out.println("-------------------------------");
//	for(Map<String, Object> board : Stock_All){
//		System.out.println(board.get("BOOK_CODE")
//				+ "\t"	+ board.get("BOOK_NAME")
//				+ "\t"	+ board.get("BOOK_TOTALSTOKE")
//				);
//	}
//	System.out.println("===============================");
 
 
 
 //재고관리 조회
 public int select_stock_book(){
	System.out.println("▶=============================================================================◀");
	 System.out.println("도서 재고 검색");
	System.out.println("▶=============================================================================◀");
//	 System.out.println("조회할 도서 코드입력");
//	 String BOOK_CODE = ScanUtil.nextLine();
	 List<Map<String, Object>> stock_book_list  = adminDao.select_Admin_Stock_Book();
	 System.out.println("주문코드\t도서제목\t\t재고");
	 for(Map<String, Object> sday : stock_book_list){
		 System.out.println(sday.get("BOOK_CODE")+"\t"+((String)sday.get("BOOK_NAME")).substring(0,2)+"..."+"\t\t"+sday.get("BOOK_TOTALSTOKE"));
	 }
	return 0;
	 
 }
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 /*
  * 
  * 
  * 
  * 
  */
 public int notice_manage(){//공지사항관리
	while(true){
	
		
	 
	System.out.println("▶=============================================================================◀");
	 System.out.println("1.조회\t0.뒤로가기(←)");
	System.out.println("▶=============================================================================◀");
	System.out.print("번호 선택> ");
	 
	 int input = ScanUtil.nextInt();
	 switch(input){
	 case 1://공지사항_조회
		 List<Map<String, Object>> noticeList = adminDao.NOTICE_Admin();
		 System.out.println("공지번호\t공지제목\t공지내용\t작성자\t작성일자");
		 for(Map<String,Object> notice : noticeList ){
				 System.out.println(notice.get("NOTICE_NUM")+
						 "\t"+((String) notice.get("NOTICE_TITLE")).substring(0,3)+
						 "\t"+((String) notice.get("NOTICE_CONTENTS")).substring(0,3)+
						 "\t"+notice.get("ADMIN_CODE")+
						 "\t"+fm.format(notice.get("NOTICE_DATE"))
						 );
		 }
		 return notice_manage_detail();
		 
		
	 case 0://뒤로가기
		 
		 return admin_home();
	 }
	 
	
	}
 }
 
 
 
 
 public int notice_manage_detail(){
while(true){
			System.out.println("▶=============================================================================◀");
			 System.out.println("1.조회\t2.작성\t3.수정\t4.삭제\t0.뒤로가기(←)");
			System.out.println("▶=============================================================================◀");
			System.out.print("번호 선택> ");
			 int input = ScanUtil.nextInt();
			 switch(input){
			 case 1://공지사항내용보기 
				 System.out.print("조회할 공지사항 번호를 선택 해주세요> ");
				 int NOTICE_NUM = ScanUtil.nextInt();
				 
				 Map<String, Object> noticeContents = adminDao.NOTICE_Admin_contents(NOTICE_NUM);
				System.out.println("▶=============================================================================◀");
				 System.out.println("공지 번호 : "+noticeContents.get("NOTICE_NUM"));
				 System.out.println("제목 : "+ noticeContents.get("NOTICE_TITLE"));
				 System.out.println("작성자 : "+ noticeContents.get("ADMIN_CODE"));
				 System.out.println("작성날짜 : "+ fm.format(noticeContents.get("NOTICE_DATE")));
				 System.out.println("내용 : "+ noticeContents.get("NOTICE_CONTENTS") );
				System.out.println("▶=============================================================================◀");
				 
				 return notice_manage();
				 
			 case 2://공지사항작성
				System.out.println("▶=============================================================================◀");
				 System.out.println("공지사항 작성입니다.");
				System.out.println("▶=============================================================================◀");
				 System.out.println("공지사항 제목을 입력해주세요> ");
				 String NOTICE_TITLE = ScanUtil.nextLine();
				 System.out.println("공지사항 내용을 입력해주세요> ");
				 String NOTICE_CONTENTS = ScanUtil.nextLine();
				 int result = adminDao.insertNotice(NOTICE_TITLE, NOTICE_CONTENTS);
				 if(0 < result){
						System.out.println("공지사항 입력 성공");
					
					}else{
						System.out.println("공지사항 입력  실패");
						
					}
				 return notice_manage();
			 
			 case 3://공지사항수정
				System.out.println("▶=============================================================================◀");
				 System.out.println("공지사항 수정");
				System.out.println("▶=============================================================================◀");
				 System.out.print("수정할 공지사항의 번호 입력> ");
				 NOTICE_NUM = ScanUtil.nextInt();
				 System.out.println("수정할 부분 선택");
				 System.out.println("1.제목\t2.내용");
				 System.out.print("번호 선택> ");
				 input = ScanUtil.nextInt();
				 switch(input){
								 case 1:
									 System.out.println("수정할 제목을 입력해주세요> ");
									 NOTICE_TITLE = ScanUtil.nextLine();
									 result = adminDao.updateNoticeTitle(NOTICE_NUM, NOTICE_TITLE);
									 if(0 < result){
											System.out.println("공지사항 입력 성공");
										
										}else{
											System.out.println("공지사항 입력  실패");
											
										}
									 return notice_manage();
								 case 2:
									 System.out.println("수정할 내용을 입력해주세요> ");
									 NOTICE_CONTENTS= ScanUtil.nextLine();
									 result = adminDao.updateNoticeContents(NOTICE_NUM, NOTICE_CONTENTS);
									 if(0 < result){
											System.out.println("공지사항 입력 성공");
										
										}else{
											System.out.println("공지사항 입력  실패");
											
										}
									 return notice_manage();
				 }
				 
			
			 case 4://공지사항삭제
				System.out.println("▶=============================================================================◀");
				 System.out.println("공지사항 삭제");
				System.out.println("▶=============================================================================◀");
				 System.out.print("삭제할 공지사항의 번호 입력> ");
				 	NOTICE_NUM = ScanUtil.nextInt();
				 	result = adminDao.deleteNotice(NOTICE_NUM);
				 	
				 	
				 	if(0 < result){
						System.out.println("공지사항 삭제 성공");
					
					}else{
						System.out.println("공지사항 삭제실패");
						
					}
				 	return notice_manage();
			
			 case 0://뒤로가기
				 return notice_manage();
			 }
			 
 }

}
 //------------------------------------------------------------------------------------------------------------------------
 
 
 public int order_manage(){//주문내역관리
	 
	 while(true){
		System.out.println("▶=============================================================================◀");
	 System.out.println("1.주문내역\t\t2.날짜별조회\t3.회원별조회\t0.뒤로가기(←)");
	System.out.println("▶=============================================================================◀");
	System.out.print("번호 선택> ");
	 int input = ScanUtil.nextInt();
	 switch(input){
	 case 1://주문내역
		 List<Map<String, Object>> oaa = adminDao.select_Order_Admin_All();
		 System.out.println("주문번호\t\t회원번호\t주문수량\t처리상태\t주문일자");
		 for(Map<String,Object> list : oaa ){
				 System.out.println(list.get("ORDER_CODE")+
						 "\t"+list.get("MEM_CODE")+
						 "\t"+list.get("ORDER_QTY")+
						 "\t"+list.get("PROCESS_STATUS")+
						 "\t"+fm.format(list.get("ORDER_BUY_DATE"))
						 		
						 );
		 }
		 
		 
		 
		 break;
	 case 2://주문내역 날짜
		System.out.println("▶=============================================================================◀");
		 System.out.print("조회할 날짜 입력> ");
		 String ORDER_BUY_DATE = ScanUtil.nextLine();
		System.out.println("▶=============================================================================◀");
		 
		 List<Map<String, Object>> oaa1 = adminDao.select_Order_Admin_Date(ORDER_BUY_DATE);
		 System.out.println("주문번호\t\t회원번호\t주문수량\t처리상태\t주문일자");
		 for(Map<String,Object> list : oaa1 ){
				 System.out.println(list.get("ORDER_CODE")+
						 "\t"+list.get("MEM_CODE")+
						 "\t"+list.get("ORDER_QTY")+
						 "\t"+list.get("PROCESS_STATUS")+
						 "\t"+fm.format(list.get("ORDER_BUY_DATE"))
						 		
						 );
		 }
		 
		 
		 
		 break;
	 case 3://주문내역 회운
		System.out.println("▶=============================================================================◀");
		 System.out.print("조회할 회원코드 입력> ");
		 String MEM_CODE = ScanUtil.nextLine();
		System.out.println("▶=============================================================================◀");
		 
		 List<Map<String, Object>> oaa2 = adminDao.select_Order_Admin_Mem(MEM_CODE);
		 System.out.println("주문번호\t\t회원번호\t주문수량\t처리상태\t주문일자");
		 for(Map<String,Object> list : oaa2 ){
				 System.out.println(list.get("ORDER_CODE")+
						 "\t"+list.get("MEM_CODE")+
						 "\t"+list.get("ORDER_QTY")+
						 "\t"+list.get("PROCESS_STATUS")+
						 "\t"+fm.format(list.get("ORDER_BUY_DATE"))
						 		
						 );
		 }
		 
		 
		 
		 break;
	 case 0://뒤로가기
		 return admin_home();
	 	}
	 
	 }
	
	
	 
 }
 
 
 
 
 /*
  * 
  * 
  * 
  * 
  * 
  * 
  * 
  * 
  * 
  */
 public int mem_manage(){//회원관리
	 while(true){
		System.out.println("▶=============================================================================◀");
	 System.out.println("1.회원내역조회\t2.1:1문의관리\t0.뒤로가기(←)");
	System.out.println("▶=============================================================================◀");
	System.out.print("번호 선택> ");
	 int input = ScanUtil.nextInt();
	 switch(input){//1.회원내역조회\t2.1:1문의관리\t0.뒤로이동
	 case 1://회원내역  
		 List<Map<String, Object>> memList = adminDao.MEM_Admin();
		 
		 System.out.println("회원 및 회원정보 내역");
		System.out.println("▶=============================================================================◀");
		 System.out.println("회원코드\t 아이디\t\t회원이름\t회원번호\t\t회원생일\t\t마일리지\t회원주소\t상세주소\t가입일자");
		System.out.println("▶=============================================================================◀");
		 
		 for(Map<String,Object> mem : memList ){
			 System.out.println(mem.get("MEM_CODE") +"\t"+mem.get("MEM_ID") +"\t\t"+
					 mem.get("MEM_NAME") +"\t"+mem.get("MEM_NUMBER") +"\t"+mem.get("MEM_BIR") +"\t"+
					 mem.get("MEM_MILEAGE") +"\t"+mem.get("MEM_ADDRESS1") +"\t"+
					 mem.get("MEM_ADDRESS2") +"\t"+fm.format(mem.get("MEM_JOINDATE")) 
					 );
		 }
		 
		System.out.println("▶=============================================================================◀");
		 System.out.println("0.뒤로가기(←)");
		System.out.println("▶=============================================================================◀");
		System.out.print("번호 선택> ");
		input = ScanUtil.nextInt();
		 switch(input){
		 case 1://회원수정
			 
			 //관리자 확인
			 System.out.println(Controller.LoginUser.get("MEM_ID"));			 
			System.out.println("▶=============================================================================◀");
			 System.out.println("관리자 비밀번호를 입력해주세요");
			 while(true){
				 System.out.print("비밀번호 입력> ");
			 String pw = ScanUtil.nextLine();
			 if(Controller.LoginUser.get("MEM_PW").equals(pw)){
				 System.out.println("확인되었습니다.");
				 break;
			 }else{
				 System.out.println("다시 입력해주세요.");
			 }
			 }
			System.out.println("▶=============================================================================◀");
			 
			 
			 
			System.out.println("▶=============================================================================◀");
			 System.out.println("원하시는 수정부분을 선택학세요");
			 System.out.println("1.PW\t2.회원이름\t3.전화번호\t4.생년월일\t\t5.회원등급\t6.마일리지\t7.회원주소\t8.회원상세주소\t0.뒤로가기(←)");
			 System.out.println("===============================");
			 System.out.print("입력>");
			 input = ScanUtil.nextInt();
			 switch(input){
			 case 1://비밀번호
			 	System.out.println("수정할 PW의 회원코드 입력>");
				String MEM_CODE = ScanUtil.nextLine();
				System.out.println("수정할 PW를 입력>");
				String MEM_PW = ScanUtil.nextLine();
				int result = adminDao.updateMemPW(MEM_PW, MEM_CODE);

			 	if(0 < result){
					System.out.println("수정 성공");
				
				}else{
					System.out.println("수정 실패");
					
				}
				return mem_manage();
			 	
		 	case 2://회원이름
		 		System.out.println("수정할 이름의 회원코드 입력>");
				MEM_CODE = ScanUtil.nextLine();
				System.out.println("수정할  이름을 입력>");
				String MEM_NAME = ScanUtil.nextLine();
				result = adminDao.updateMemName(MEM_NAME, MEM_CODE);

			 	if(0 < result){
					System.out.println("수정 성공");
				
				}else{
					System.out.println("수정 실패");
					
				}
				return mem_manage();
			 	
		 		
		 		
		 		
		 	case 3://전화번호	
		 		System.out.println("수정할 전화번호의 회원코드 입력>");
				MEM_CODE = ScanUtil.nextLine();
				System.out.println("수정할  전화번호를 입력>");
				String MEM_NUMBER = ScanUtil.nextLine();
				result = adminDao.updateMemNum(MEM_NUMBER, MEM_CODE);

			 	if(0 < result){
					System.out.println("수정 성공");
				
				}else{
					System.out.println("수정 실패");
					
				}
				return mem_manage();
			 	
		 		
		 	case 4://생년월일	
		 		System.out.println("수정할 생년월일의 회원코드 입력>");
				MEM_CODE = ScanUtil.nextLine();
				System.out.println("수정할  생년월일 입력>");
				String MEM_BIR = ScanUtil.nextLine();
				result = adminDao.updateMemBir(MEM_BIR, MEM_CODE);

			 	if(0 < result){
					System.out.println("수정 성공");
				
				}else{
					System.out.println("수정 실패");
					
				}
				return mem_manage();
			 	
		 	case 5://회원등급	
		 		System.out.println("수정할 등급의 회원코드 입력>");
				MEM_CODE = ScanUtil.nextLine();
				System.out.println("수정할  등급 입력>");
				String MEM_GRADE = ScanUtil.nextLine();
				result = adminDao.updateMemGrade(MEM_GRADE, MEM_CODE);

			 	if(0 < result){
					System.out.println("수정 성공");
				
				}else{
					System.out.println("수정 실패");
					
				}
				return mem_manage();
			 	
		 	case 6://마일리지	
		 		System.out.println("수정할 회원의 마일리지 코드 입력>");
				MEM_CODE = ScanUtil.nextLine();
				System.out.println("수정할  마일리지 입력>");
				String MEM_MILEAGE = ScanUtil.nextLine();
				result = adminDao.updateMemMileage(MEM_MILEAGE, MEM_CODE);

			 	if(0 < result){
					System.out.println("수정 성공");
				
				}else{
					System.out.println("수정 실패");
					
				}
				return mem_manage();
			 	
		 	case 7://회원주소	
		 		System.out.println("수정할 회원주소의 회원 코드 입력>");
				MEM_CODE = ScanUtil.nextLine();
				System.out.println("수정할  회원주소코드를 입력>");
				String MEM_ADDRESS1 = ScanUtil.nextLine();
				result = adminDao.updateMemAddress1(MEM_ADDRESS1, MEM_CODE);

			 	if(0 < result){
					System.out.println("수정 성공");
				
				}else{
					System.out.println( "수정 실패");
					
				}
				return mem_manage();
			 	
		 	case 8://상세주소	
		 		System.out.println("수정할 상세주소 코드의 회원 입력>");
				MEM_CODE = ScanUtil.nextLine();
				System.out.println("수정할  상세주소를 입력>");
				String MEM_ADDRESS2 = ScanUtil.nextLine();
				result = adminDao.updateMemAddress1(MEM_ADDRESS2, MEM_CODE);

			 	if(0 < result){
					System.out.println("수정 성공");
				
				}else{
					System.out.println( "수정 실패");
					
				}
				return mem_manage();
			 	
		
		 	case 0:
				 return admin_home();
				
			 }
		 
		 
		 //---------------------------------------------------------------------------------------------------------
		 case 2://회원 삭제
			 	System.out.println("삭제할 ID의 코드 입력>");
			 	String MEM_CODE = ScanUtil.nextLine();
			 	int result = adminDao.deleteMem(MEM_CODE);
			 	
			 	
			 	if(0 < result){
					System.out.println("삭제 성공");
				
				}else{
					System.out.println("삭제실패");
					
				}
				return mem_manage();
			 
		 
		 case 0:
			 return mem_manage();
		
		
		 }
		 
	//--------------------------------------------------------------
		 
	 case 2://1:1문의관리	 
		 
		 return QA_mem_Q();
		 
		 
	 case 0://뒤로가기
		 return admin_home();
	 }return admin_home();
 	}
 }
 
 //회원관리
	
 
 public int QA_mem_Q(){
	System.out.println("▶=============================================================================◀");
	 System.out.println("회원들의 문의사항");
	 System.out.println("1.전체조회\t\t0.뒤로가기(←)");
	System.out.println("▶=============================================================================◀");
	System.out.print("번호 선택> ");
	 
	 int input = ScanUtil.nextInt();
	 switch(input){
		
		case 1:
			System.out.println("문의번호\t문의제목\t문의시간");
				List<Map<String, Object>> qa_list = adminDao.select_List_Qa();
			 for(Map<String, Object> list :  qa_list){
				 System.out.println(list.get("QA_NUM")+"\t"+list.get("QA_TITLE")+"\t"+fm.format(list.get("QA_WDATE")));
			 }
			QA_mem_Q_detail();
				
			 break;
		case 0:
			return mem_manage();
			
		}
	 
	 return mem_manage();
	 
 }
 
 
 
 public int QA_mem_Q_detail(){
	 	System.out.println("▶=============================================================================◀");
	 	System.out.println("1.선택조회\t2.답글작성 \t0.뒤로가기(←)");
	 	System.out.println("▶=============================================================================◀");
	 	System.out.print("번호 선택> ");
	 		int input = ScanUtil.nextInt();
	 		switch(input){
	 		case 1://선택 조회
	 			 QA_mem_Q_detail_Choice();
	 			 break;

	 		case 2://답글 작성
	 			 QA_mem_Q_Reply();
	 			 break;

	 		case 0://뒤로가기
	 			return QA_mem_Q();
	 		}
	 	return QA_mem_Q();
	 	
 }
 
 public int QA_mem_Q_detail_Choice(){
	System.out.println("▶=============================================================================◀");
	 System.out.println("회원들의 문의글 선택");
	 System.out.print("회원 문의글 번호 입력> ");
	 int QA_CODE = ScanUtil.nextInt();
	System.out.println("▶=============================================================================◀");
	 System.out.println("문의번호\t문의제목\t문의시간");
	 
	System.out.println("▶=============================================================================◀");
		List<Map<String, Object>> qa_list = adminDao.select_List_Qa_Choice(QA_CODE);
	 for(Map<String, Object> list :  qa_list){
		 System.out.println("문의번호 : "+list.get("QA_NUM"));
		 System.out.println("문의 제목 : "+list.get("QA_TITLE"));
		 System.out.println("문의 내용: "+list.get("QA_CONTENTS"));
		 System.out.println("문의 날짜 : "+fm.format(list.get("QA_WDATE")));
		 System.out.println(list.get("QA_REPLY"));
		 System.out.println("--------------------------");
		 if(list.get("QA_REPLY").equals("답글이 없습니다.")){
		 System.out.println("답변 : "+list.get("QA_REPLY"));
		 }else{
			 System.out.println("답변 : "+list.get("QA_REPLY"));
			 System.out.println("답변 날짜 : "+fm.format(list.get("QA_REPLYDATE")));
		 }
	 }
	System.out.println("▶=============================================================================◀");
	QA_mem_Q_detail();
		
	  
	 
	 
	
	 return admin_home();
	 
 }
	 

 
 public int  QA_mem_Q_Reply(){
	System.out.println("▶=============================================================================◀");
	 System.out.println("답글을 작성할 회원들의 선택하기");
	 System.out.print("회원 문의글 번호 입력> ");
	 int QA_CODE = ScanUtil.nextInt();
	 System.out.println("답글  작성");
	 String QA_REPLY = ScanUtil.nextLine();
	System.out.println("▶=============================================================================◀");
	 int result = adminDao.select_List_Qa_Choice_Reply_update(QA_REPLY, QA_CODE);
	 	
	 	
	 	if(0 < result){
			System.out.println("답글 작성 성공");
		
		}else{
			System.out.println("답글 작성 실패");
			
		}
		return QA_mem_Q_detail();
	 
	 
	 
	 
	 
	 
 }
 
 
 
}//end
