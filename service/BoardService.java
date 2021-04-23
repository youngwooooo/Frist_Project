package service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import util.ScanUtil;
import util.View;
import controller.Controller;
import dao.BoardDao;


public class BoardService {

	
	SimpleDateFormat fm = new SimpleDateFormat("yyyy/MM/dd");
	
	/////////////////////////////////////////////
	private BoardService(){}
	private static BoardService instance;
	public static BoardService getInstance(){
		if(instance == null){
			instance = new BoardService();
		}
		return instance;
	}
	/////////////////////////////////////////////
	
	private BoardDao boardDao = BoardDao.getInstance();
	
	// 비회원, 회원 도서조회
		public void BookBoard(){
			List<Map<String, Object>> boardList = boardDao.BoardList();		
			
			System.out.println("▶=============================================================================◀");
			System.out.println("도서코드\t제목\t\t\t작가\t\t출판사\t\t가격");
			System.out.println("▶=============================================================================◀");
			for(Map<String, Object> board : boardList){
				System.out.println(board.get("BOOK_CODE")
						+ "\t" + ((String)board.get("BOOK_NAME")).substring(0,8)
						+ "\t\t" + board.get("BOOK_WRITER")
						+ "\t\t" + board.get("BOOK_PUBLISHER")
						+ "\t\t" + board.get("BOOK_PRICE"));
			}
			System.out.println("▶=============================================================================◀");
		}
	
	public int userMain(){ // 로그인 유저 메인화면
		System.out.println("▶=============================================================================◀");
		System.out.println("1.도서조회\t2.공지사항\t3.마이페이지\t0.로그아웃");
		System.out.println("▶=============================================================================◀");
		System.out.print("번호입력> ");
		int input = ScanUtil.nextInt();
		
		switch(input){
		case 1: BookBoard_Member(); break;
		case 2: Notice(); break;
		case 3: myPage(); break;		
		case 0:
			Controller.LoginUser = null;
			System.out.println("로그아웃 되었습니다.");	
			return View.HOME;
		default : System.out.println("다시 입력하세요.");
		}		
		return View.HOME;
	}
	
	// 회원 도서목록 조회
		public int BookBoard_Member(){
			BookBoard();
				System.out.println("▶=============================================================================◀");
				System.out.println("1.카테고리 검색\t2.키워드 검색\t3.도서선택\t0.뒤로가기(←)");
				System.out.println("▶=============================================================================◀");
				System.out.print("번호 선택> ");
				int input = ScanUtil.nextInt();
				System.out.println();
				switch(input){
				case 1 : Category_Search(); break;
				case 2 : KeyWord_Search(); break;
				case 3 : BookInfo(); break; 					case 0 : return MemberLogin(); 
					}
						
				return BookBoard_Member();
			}	
			
	// 비회원 도서조회
	public int BookBoard_NonMember(){		
		BookBoard(); //도서목록 출력
		System.out.println("▶=============================================================================◀");
		System.out.println("1.카테고리 검색\t2.키워드 검색\t3.도서선택\t0.뒤로가기(←)");
		System.out.println("▶=============================================================================◀");
		System.out.print("번호 선택> ");
		int input = ScanUtil.nextInt();
		System.out.println();		
		switch(input){
		case 1 : 
			Category_Search(); 
			break;
		case 2 : 
			KeyWord_Search(); 
			break;
		case 3 : 
			BookInfo(); 
			break;
		case 0 : ; break; 
		}
		return 0;
	}
	
	// 카테고리 검색
	int Category_input;
	public void Category_Search(){
		/*
		 * 	10	소설
			20	경제
			30	청소년
			40	과학
			50	자기개발
		 */		
		List<Map<String, Object>> category_list = boardDao.selectBoardList_CategoryKind();
		for (Map<String, Object> board : category_list) {
			System.out.print(board.get("BOOK_CATEGORY") + " " + board.get("CATEGORY_NAME") + "\t");
		}
		System.out.println("0.뒤로가기(←)");
		System.out.println();
		System.out.print("검색할 카테고리 번호를 입력하세요> ");
		Category_input = ScanUtil.nextInt();
		System.out.println();
		switch(Category_input){
		case 10 : Category_Search_Complete(); break;
		case 20 : Category_Search_Complete(); break;
		case 30 : Category_Search_Complete(); break;
		case 40 : Category_Search_Complete(); break;
		case 50 : Category_Search_Complete(); break;
		
		
		}		
	}
	
	// 카테고리 검색 - 카테고리 별 도서 목록
	public void Category_Search_Complete(){	
		List<Map<String, Object>> boardList = boardDao.selectBoardList2(Category_input);
		System.out.println("▶=============================================================================◀");
		System.out.println("도서코드\t제목\t\t\t작가\t\t출판사\t\t가격");
		System.out.println("▶=============================================================================◀");
		for(Map<String, Object> board : boardList){
			System.out.println(board.get("BOOK_CODE")
					+ "\t" + ((String)board.get("BOOK_NAME")).substring(0,8)
					+ "\t\t" + board.get("BOOK_WRITER")
					+ "\t\t" + board.get("BOOK_PUBLISHER")
					+ "\t\t" + board.get("BOOK_PRICE"));
		}
		System.out.println("▶=============================================================================◀");
		BookInfo_choice1();
	}
	// 비회원, 회원 카테고리 검색 - 카테고리 별 도서 목록 - 도서 상세조회
	public void BookInfo_choice1(){
		System.out.println("1.도서 상세조회\t0.뒤로가기(←)");
		System.out.print("번호 선택> ");
		int input = ScanUtil.nextInt();
		System.out.println();
		switch(input){
		case 1 : BookInfo(); break;
		case 0:
			if(Controller.LoginUser != null){
//				userMain();
				Category_Search();
			}else{
				BookBoard_NonMember();
			}
			break;
		}		
	}
	
	// 비회원이 선택한 도서 정보 출력
	String input_bookCode;
	public void BookInfo(){		
		System.out.print("도서코드 입력> ");
		input_bookCode = ScanUtil.nextLine();
		System.out.println();
		Map<String, Object> boardOne = boardDao.selectBoardList_InfoBook(input_bookCode);
		
		Controller.CurrentBook = boardOne;

        System.out.println("도서번호 : " + boardOne.get("BOOK_CODE"));
        System.out.println("도서명 : " + boardOne.get("BOOK_NAME"));
        System.out.println("작가명 : " + boardOne.get("BOOK_WRITER"));
        System.out.println("가격 : " + boardOne.get("BOOK_PRICE"));
        System.out.println("출판사 : " + boardOne.get("BOOK_PUBLISHER"));
        System.out.println("책소개 : " + boardOne.get("BOOK_INTRO"));
        readReview();
        System.out.println();
        if(Controller.LoginUser != null){
        	System.out.println("1.구매\t2.리뷰작성\t0.뒤로가기(←)");       
            int input0 = ScanUtil.nextInt();
            switch(input0){
            case 1 : 
            	boardDao.buy();
            	boardDao.minusbook();
            	break;
            case 2 : WriteReview(); break;
            case 0 : Category_Search_Complete(); break;    
            }
        }else{
        	System.out.println("0.뒤로가기(←)");       
    		int input1 = ScanUtil.nextInt();
    		switch(input1){
    		case 0 : Category_Search_Complete(); break;		
    		}
        }
        
	}
	
	//키워드 검색 선택 메소드
		public void KeyWord_Search() {
			System.out.println("▶=============================================================================◀");
			System.out.println("무엇으로 검색하시겠습니까?");
			System.out.println("▶=============================================================================◀");
			System.out.println("1.도서명\t2.작가이름");
			System.out.print("번호 선택> ");
			int input = ScanUtil.nextInt();
			switch(input){
			case 1 : KeyWord_Search_BookName(); break;
			case 2 : KeyWord_Search_AuthorName(); break;
			}		
		}
		
		//키워드 검색(도서명)
		public void KeyWord_Search_BookName(){
			System.out.print("도서명을 입력하세요> ");
			String input = ScanUtil.nextLine();
			
			List<Map<String, Object>> bookName = boardDao.selectBoardList3(input);
			
			System.out.println("▶=============================================================================◀");
			System.out.println("도서코드\t제목\t\t\t작가\t\t출판사\t\t가격");
			System.out.println("▶=============================================================================◀");
			for(Map<String, Object> board : bookName){
				System.out.println(board.get("BOOK_CODE")
						+ "\t" + ((String)board.get("BOOK_NAME")).substring(0,8)
						+ "\t\t" + board.get("BOOK_WRITER")
						+ "\t\t" + board.get("BOOK_PUBLISHER")
						+ "\t\t" + board.get("BOOK_PRICE"));
			}
			System.out.println("▶=============================================================================◀");
			BookInfo_choice1();
		}
		
		public void KeyWord_Search_AuthorName(){
			System.out.println("작가이름을 입력하세요> ");
			String input = ScanUtil.nextLine();
			
			List<Map<String, Object>> authorName = boardDao.selectBoardList4(input);
			
			System.out.println("▶=============================================================================◀");
			System.out.println("도서코드\t제목\t\t\t작가\t\t출판사\t\t가격");
			System.out.println("▶=============================================================================◀");
			for(Map<String, Object> board : authorName){
				System.out.println(board.get("BOOK_CODE")
						+ "\t" + ((String)board.get("BOOK_NAME")).substring(0,8)
						+ "\t\t" + board.get("BOOK_WRITER")
						+ "\t\t" + board.get("BOOK_PUBLISHER")
						+ "\t\t" + board.get("BOOK_PRICE"));
			}
			System.out.println("▶=============================================================================◀");
			BookInfo_choice1();
		}
		
		public int MemberLogin(){ 	
			System.out.println("▶=============================================================================◀");
			System.out.println("1.도서조회\t2.공지사항\t3.마이페이지\t0.로그아웃");
			System.out.println("▶=============================================================================◀");
			System.out.print("번호입력> ");
			int input = ScanUtil.nextInt();
			
			switch(input){
			case 1: return BookBoard_Member(); 
			case 2: return Notice(); //공지사항
			case 3: return myPage(); //마이페이지		 

			case 0: Controller.LoginUser = null; 
					System.out.println("로그아웃 되었습니다.");
			return View.HOME;
			default : System.out.println("다시 입력하세요.");
			}		
			return MemberLogin();
		} 
		
		
		//리뷰보기 메소드
		public void readReview(){		
			List<Map<String, Object>> boardlist = boardDao.selectBoardList5(input_bookCode);
			for(Map<String, Object>  boardOne : boardlist){
				System.out.println("▶=============================================================================◀");
		        System.out.println("리뷰내용 : " + boardOne.get("REVIEW_CONTENTS"));
				System.out.print("별점 : ");
				for(int i=0; i<((BigDecimal)boardOne.get("REVIEW_STAR")).intValue(); i++){
					System.out.print("★");
				}
				System.out.println();
				System.out.println("작성일 : " + boardOne.get("REVIEW_WRITEDATE"));
			}
		}
		
		//리뷰작성
		public void WriteReview(){
			System.out.println("▶=============================================================================◀");
			System.out.println("리뷰 작성> ");
			String writeReview = ScanUtil.nextLine();	
			
			System.out.print("별점(1~5점)> ");
			int starReview = ScanUtil.nextInt();		
			
			Object mem_Id = Controller.LoginUser.get("MEM_ID");
			Object bookCode = Controller.LoginUser.get("BOOK_CODE");
			
			int review = boardDao.updateList1(writeReview, starReview, mem_Id, bookCode);
			
		}
	
	public int choiceBook(){
		System.out.println("도서코드를 입력해주세요> ");
		String bookCode = ScanUtil.nextLine();
		
		Map<String, Object> selectBook = boardDao.seletBook(bookCode);
		System.out.println("▶=============================================================================◀");
	    System.out.println("도서코드\t: " + selectBook.get("BOOK_CODE"));
	    System.out.println("도서명\t: " + selectBook.get("BOOK_NAME"));
	    System.out.println("작가\t: " + selectBook.get("BOOK_WRITER"));
	    System.out.println("가격\t: " + selectBook.get("BOOK_PRICE"));
	    System.out.println("카테고리\t: " + selectBook.get("BOOK_CATEGORY"));
	    System.out.println("책 소개\t: " + selectBook.get("BOOK_INTRO"));
	    System.out.println("▶=============================================================================◀");
		System.out.println("1.구매\t2.리뷰작성\t3.리뷰보기\t0.뒤로가기(←)");
		System.out.print("번호 선택 > ");
		int num = ScanUtil.nextInt();
		
		switch(num){
			case 1:
				boardDao.buy();
				boardDao.minusbook();
				break;
			case 2:
//				insertReview();					
				break;
			case 3:
//				readReview();
				break;
			case 0:
				return BookBoard_Member();
				
		}
		return BookBoard_Member();
	}
	
	
	public void searchBook(){
		
	}

	// [2.공지사항] : 전체 목록 / 비회원
	public int Notice_nonMember(){
		List<Map<String, Object>> nsl = boardDao.NoticeMain();
		System.out.println("▶=============================================================================◀");
		System.out.println("번호\t제목\t작성일");
		System.out.println("▶=============================================================================◀");
		for(Map<String, Object> notice : nsl){
			System.out.println(notice.get("NOTICE_NUM")
					+ "\t" + notice.get("NOTICE_TITLE")
					+ "\t" + fm.format(notice.get("NOTICE_DATE")));
		}
		System.out.println("▶=============================================================================◀");
		System.out.println("1.공지사항 조회\t0.뒤로가기");
		System.out.print("번호 선택> ");
		int input = ScanUtil.nextInt();
		System.out.println("▶=============================================================================◀");
		
		switch(input){
			case 1:
				selectNotice();  // 공지사항 조회
				break;
			case 0:
				return View.HOME; // 뒤로가기
		}
		return View.HOME;
	}
	
	// 공지사항(회원)
	public int Notice(){
		List<Map<String, Object>> nsl = boardDao.NoticeMain();
		System.out.println("▶=============================================================================◀");
		System.out.println("번호\t제목\t작성일");
		System.out.println("▶=============================================================================◀");
		for(Map<String, Object> notice : nsl){
			System.out.println(notice.get("NOTICE_NUM")
					+ "\t" + notice.get("NOTICE_TITLE")
					+ "\t" + fm.format(notice.get("NOTICE_DATE")));
		}
		System.out.println("▶=============================================================================◀");
		System.out.println("1.공지사항 조회\t0.뒤로가기");
		System.out.print("번호 선택> ");
		int input = ScanUtil.nextInt();
		System.out.println("▶=============================================================================◀");
		
		switch(input){
			case 1:
				selectNotice();  // 공지사항 조회
				break;
			case 0:
				return userMain(); // 뒤로가기
		}
		return userMain();
	}
	
	// [2.공지사항] - [1.공지사항 조회]
	public int selectNotice(){
		System.out.print("조회할 공지사항 번호를 입력해주세요> ");
		int noticeNum = ScanUtil.nextInt();
		
		Map<String, Object> read = boardDao.selectNotice(noticeNum);
		System.out.println("▶=============================================================================◀");
	    System.out.println("번호\t: " + read.get("NOTICE_NUM"));
	    System.out.println("제목\t: " + read.get("NOTICE_TITLE"));
	    System.out.println("내용\t: " + read.get("NOTICE_CONTENTS"));
	    System.out.println("작성자\t: " + read.get("ADMIN_CODE"));
	    System.out.println("작성일\t: " + fm.format(read.get("NOTICE_DATE")));
	    System.out.println("▶=============================================================================◀");
	    
	    return Notice();
		}
	
	// [3.마이페이지]
	public int myPage(){
		System.out.println("▶=============================================================================◀");
		System.out.println("1.내 정보 수정\t2.주문내역 확인\t3.1:1문의\t0.뒤로가기(←)");
		System.out.println("▶=============================================================================◀");
		System.out.print("번호 선택> ");
		int num = ScanUtil.nextInt();
		
		switch(num){
			case 1:
				info();
				break;
			case 2:
				readOrder();
				break;
			case 3:
				qa();
				break;
			case 0:
				return userMain();
		}
		return userMain();
	}
	
	// [3.마이페이지] - [1.내 정보 수정]
	public int info(){
		System.out.println("▶=============================================================================◀");
		System.out.println("1.개인정보 수정\t2.비밀번호 변경\t0.뒤로가기");
		System.out.println("▶=============================================================================◀");
		System.out.print("번호 선택> ");
		int num = ScanUtil.nextInt();
		
		switch(num){
			case 1:
				boardDao.changeInfo();  // 개인정보 수정
				break;
			case 2:
				boardDao.changePassword();  // 비밀번호 변경
				break;
			case 0:
				return myPage();  // 뒤로가기
		}
		return info();
	}
	
	// [3.마이페이지] - [2.주문내역 조회]
	public int readOrder(){
		List<Map<String, Object>> orderList = boardDao.orderList();
		System.out.println("▶=============================================================================◀");
		System.out.println("번호\t주문번호\t\t처리상태\t구매일\t\t구입수량");
		System.out.println("▶=============================================================================◀");
		for(Map<String, Object> list : orderList){
			System.out.println(list.get("RN")
					+ "\t" + list.get("ORDER_CODE") 
					+ "\t" + list.get("PROCESS_STATUS") 
					+ "\t" + fm.format(list.get("ORDER_BUY_DATE")) 
					+ "\t" + list.get("ORDER_QTY"));	
		}
		System.out.println("▶=============================================================================◀");
		System.out.println("1.상세조회\t0.뒤로가기(←)");
		System.out.print("번호 선택> ");
		int num = ScanUtil.nextInt();
		
		switch(num){
			case 1:
				selectOdrer();
				break;
			case 0:
				return myPage();
		}
		return readOrder();
	}
	
	// [3.마이페이지] - [2.주문내역 조회] - [1.상세조회]
	public int selectOdrer(){
		Map<String, Object> order = boardDao.selectOrder();
		System.out.println("▶=============================================================================◀");
		System.out.println("번호\t: " + order.get("RN"));
		System.out.println("주문번호\t: " + order.get("ORDER_CODE"));
		System.out.println("처리상태\t: " + order.get("PROCESS_STATUS"));
		System.out.println("구매일\t: " + fm.format(order.get("ORDER_BUY_DATE")));
		System.out.println("도서명\t: " + order.get("BOOK_NAME"));
		System.out.println("수량\t: " + order.get("ORDER_QTY"));
		System.out.println("결제가격\t: " + order.get("PRICE"));
		System.out.println("▶=============================================================================◀");
		System.out.println("1.구매취소\t0.뒤로가기(←)");
		System.out.println("번호 입력> ");
		int input = ScanUtil.nextInt();
		
		switch(input){
			case 1:
				boardDao.plusbook();
				boardDao.deletOrder();
				break;
			case 0:
				return readOrder();
		}
		
		return readOrder();
	}
	
	// [3.마이페이지] - [3. 1:1문의]
	public int qa(){
		List<Map<String, Object>> qalist = boardDao.qaMain();
		System.out.println("▶=============================================================================◀");
		System.out.println("번호\t제목\t작성일\t\t작성자");
		System.out.println("▶=============================================================================◀");
		for(Map<String, Object> qa : qalist){
			System.out.println(qa.get("QA_NUM")
					+ "\t" + qa.get("QA_TITLE")
					+ "\t" + fm.format(qa.get("QA_WDATE"))
					+ "\t" + qa.get("MEM_NAME"));
		}
		System.out.println("▶=============================================================================◀");
		System.out.println("1.문의글 조회\t2.문의글 작성\t0.뒤로가기(←)");
		System.out.print("번호 선택> ");
		int num = ScanUtil.nextInt();
		
		switch(num){
			case 1:
				selectQa();  // 문의글 선택
				break;
			case 2:
				boardDao.insertQa();  // 문의글 작성
				break;
			case 0:
				return myPage();  // 돌아가기
		}
		return myPage();
	}
	
	public int selectQa(){
		System.out.print("조회할 문의글 번호를 입력해주세요> ");
		int qaNum = ScanUtil.nextInt();
		
		Map<String, Object> read = boardDao.selectQa(qaNum);
		System.out.println("▶=============================================================================◀");
	    System.out.println("번호\t: " + read.get("QA_NUM"));
	    System.out.println("제목\t: " + read.get("QA_TITLE"));
	    System.out.println("내용\t: " + read.get("QA_CONTENTS"));
	    System.out.println("내용\t: " + fm.format(read.get("QA_WDATE")));
	    System.out.println("관리자\t: " + read.get("ADMIN_NAME"));
	    System.out.println("답변\t: " + read.get("QA_REPLY"));
	    System.out.println("답변일\t: " + read.get("QA_REPLYDATE"));
	    System.out.println("▶=============================================================================◀");
	    
	    return qa();
		}
		
}
		

	





















