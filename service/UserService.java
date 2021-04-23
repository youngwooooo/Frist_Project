package service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import util.ScanUtil;
import util.View;
import controller.Controller;
import dao.UserDao;


public class UserService {	
	SimpleDateFormat fm = new SimpleDateFormat("yyyy/MM/dd");
	//싱글톤 패턴
	private UserService(){}
	private static UserService instance;
	public static UserService getInstance(){
		if(instance == null){
			instance = new UserService();
		}
		return instance;
	}
	
	private UserDao userDao = UserDao.getInstance();
	private AdminService adminService = AdminService.getInstance();
	private BoardService boardService = BoardService.getInstance(); 
		
	//사용자 회원가입 메서드
	public int SignUp() {
		Map<String, Object> param = new HashMap<>();
		boolean Routine = true;
		System.out.println("▶==============================회원가입==========================================◀");
		while (Routine) {
			System.out.println("[영문으로 시작 / '-'를 제외한 특수문자 불가 / 5~12글자]");
			System.out.print("회원 ID\t: ");
			String userID = ScanUtil.nextLine();
			boolean b0 = userID.matches("^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$");
			if (b0) {
				param.put("MEM_ID", userID);
				break;
			} else {
				System.out.println("다시 입력 해주세요.");
				continue;
			}
		}

		System.out.println();
		while (Routine) {
			System.out.println("[소문자, 0~9 숫자, 특수문자 8자리 이상]");
			System.out.print("비밀번호\t: ");
			String userPassword = ScanUtil.nextLine();
			boolean b1 = userPassword
					.matches("((?=.*[a-z])(?=.*[0-9])(?=.*[^a-zA-Z0-9]).{8,})");
			if (b1) {
				param.put("MEM_PW", userPassword);
				break;
			} else {
				System.out.println("다시 입력 해주세요.");
				continue;
			}
		}

		System.out.println();
		while (Routine) {
			System.out.println("[한글만 입력 가능, 글자 수는 상관 없음]");
			System.out.print("이름\t: ");
			String userName = ScanUtil.nextLine();
			boolean b2 = userName.matches("^[ㄱ-ㅎ가-힣]*$");
			if (b2) {
				param.put("MEM_NAME", userName);
				break;
			} else {
				System.out.println("다시 입력 해주세요.");
				continue;
			}
		}
		
		System.out.println();
		while (Routine) {
			System.out.println("[EX) 010-1234-5678]");
			System.out.print("연락처\t: "); // 0101234567
			String phoneNumber = ScanUtil.nextLine();
			boolean b5 = phoneNumber.matches("^01(?:0|1|[6-9])[-]?(\\d{3}|\\d{4})[-]?(\\d{4})$");
			if (b5) {
				param.put("MEM_NUMEBR", phoneNumber);
				break;
			} else {
				System.out.println("다시 입력 해주세요.");
				continue;
			}
		}

		System.out.println();
		while (Routine) {
			System.out.println("[EX) 1995/01/01]");
			System.out.print("생년월일\t: "); // 19950101
			String birthDay = ScanUtil.nextLine();
			boolean b6 = birthDay.matches("^[0-9]{4}[/](0[1-9]|1[0-2])[/](0[1-9]|[12][0-9]|[3][01])");
			if (b6) {
				param.put("MEM_BIR", birthDay);
				break;
			} else {
				System.out.println("다시 입력 해주세요.");
				continue;
			}
		}
		
		System.out.println();
		while (Routine) {
			System.out.println("[한글과 숫자만 입력 가능]");
			System.out.println("[EX) 대전광역시]");
			System.out.print("기본주소\t: ");
			String userAddress0 = ScanUtil.nextLine();
			boolean b3 = userAddress0.matches("^[ㄱ-ㅎ가-힣0-9]*$");
			if (b3) {
				param.put("MEM_ADDRESS1", userAddress0);
				break;
			} else {
				System.out.println("다시 입력 해주세요.");
				continue;
			}
		}

		System.out.println();
		while (Routine) {
			System.out.println("[한글과 숫자만 입력 가능]");
			System.out.println("[EX) 중구]");
			System.out.print("상세주소\t: ");
			String userAddress1 = ScanUtil.nextLine();
			boolean b4 = userAddress1.matches("^[ㄱ-ㅎ가-힣0-9]*$");
			if (b4) {
				param.put("MEM_ADDRESS2", userAddress1);
				break;
			} else {
				System.out.println("다시 입력 해주세요.");
				continue;
			}
		}
		System.out.println("▶=============================================================================◀");
		int result = userDao.insertUser(param);

		if (0 < result) {
			System.out.println("▶ 회원가입 성공!");
			System.out.println("▶=============================================================================◀");
		} else {
			System.out.println("▶ 회원가입 실패");
			System.out.println("▶=============================================================================◀");
		}
		return View.HOME;
	}	
	
	
	//사용자 로그인 메서드
	public int Login(){
		System.out.println("▶▶▶▶▶▶▶로그인◀◀◀◀◀◀◀");
		System.out.print("회원ID\t: ");
		String login_userID = ScanUtil.nextLine();
		System.out.print("비밀번호\t: ");
		String login_userPassword = ScanUtil.nextLine();
		
		Map<String, Object> user = userDao.selectUser(login_userID, login_userPassword);
		if(user == null){
			System.out.println("아이디 혹은 비밀번호를 잘못입력하셨습니다.");
		}else{
			System.out.println("로그인 성공!");
			
			Controller.LoginUser = user;
			
			return boardService.userMain();	
		}
		return View.LOGIN;

 }
	
}




















