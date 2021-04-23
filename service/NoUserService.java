package service;

public class NoUserService { //비회원 서비스

	private NoUserService(){}
	private static NoUserService instance;
	public static NoUserService getInstance(){
		if(instance == null){
			instance = new NoUserService();
		}
		return instance;
	}
	
}
