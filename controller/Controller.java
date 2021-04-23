package controller;

import java.util.Map;

import service.AdminService;
import service.BoardService;
import service.UserService;
import util.ScanUtil;
import util.View;


public class Controller {


	public static void main(String[] args) {
		new Controller().start();
	}
	
	public static Map<String, Object> LoginUser;
	public static Map<String, Object> CurrentBook;
	
	private UserService userService = UserService.getInstance();
	private BoardService boardService = BoardService.getInstance();
	private AdminService adminService = AdminService.getInstance();
	
	private void start(){		
		int view = View.HOME;		
		
		while(true){
			switch(view){
			case View.HOME : 
				view = home();
				break;
			case View.LOGIN : 
				view = userService.Login();
				break;
			case View.SIGNUP : 
				view = userService.SignUp();
				break;
			case View.BookBoard_NonMember : 
				view = boardService.BookBoard_NonMember();
				break;
			}
		}
	}
	
	private int home(){
		// 메인화면(비회원)
		System.out.println("▶=============================================================================◀");
		System.out.println("1.도서조회\t2.공지사항 조회\t3.로그인\t\t4.회원가입\t0.프로그램 종료");
		System.out.println("▶=============================================================================◀");
		System.out.print("번호 입력> ");
		int input = ScanUtil.nextInt();
		
		switch(input){
		case 1 : return View.BookBoard_NonMember; 
		case 2 : return boardService.Notice_nonMember(); 
		case 3 : return loginMenu();
		case 4 : return View.SIGNUP;
		case 0 : System.out.println("프로그램이 종료되었습니다."); System.exit(0); break;
		default : System.out.println("다시 입력하세요.");
		}
		return View.HOME;
	}
	
	public int loginMenu(){
		System.out.println("▶=============================================================================◀");
		System.out.println("1.회원 로그인\t2.관리자 로그인");
		System.out.println("▶=============================================================================◀");
		System.out.print("번호 선택 > ");
		int num = ScanUtil.nextInt();
		
		if(num == 1){
			return View.LOGIN;
		}
		else if(num == 2){
			return adminService.adminLogin();
		}
		return View.HOME;
	}

}

















