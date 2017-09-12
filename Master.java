package reversi2;

import java.util.Scanner;
public class Master{
	
	static public int turnflag;
	static private int masusize = 8;
	
	private static final int	TRANS = 0;
	private static final int	BLACK = 1;
	private static final int	WHITE = 2;
	
	private static final int	CENTER = 4;
	private static int[] Playre = new int[3];
	private static int page;		//ページの状態　ゲーム中:0 中断確認:1 結果画面:2
	
	
	public Master(){
	}
	
	//初期化
	static void init(){
		Playre[1] = 0;
		Playre[2] = 0;
		Board_Data.init();
		turnflag = BLACK;
		page = 0;
	}

	static int Get_Turn(){
		return turnflag;
	}
	
	static void Get(int[][] viewboard){
		Board_Data.Get(viewboard);
	}
	
	//画面からマス番号を受け取って処理
	static void Send_Press(int x,int y,int turn){
		if(x == -1 && y == -1 && page == 0){
			ReTake();
		}else{
			if(Board_Data.Get(x,y) == 0){
				//if( Playre[turnflag] == turn ){	//0:USER 1~10:CP
					if(turnflag == BLACK){
						if(Board_Data.Put(CENTER,x,y) == 1)	turnflag = WHITE;
					}else{
						if(Board_Data.Put(CENTER,x,y) == 1)	turnflag = BLACK;
					}
				//}
			}
			pass();
		}
	}
	
	static int Pose(){
		if(Playre[turnflag] == 0 && page == 0){
			return 0;
		}else{
			if(page > 0)	return page;
			else			return -1;
		}
	}
	
	static void pass(){
		if(Board_Data.Check() == 0){
			if(turnflag == BLACK)	turnflag = WHITE;
			else						turnflag = BLACK;
			
			if(Board_Data.Check() == 0){
				page = 2;
			}
		}
	}
	
	static void finish(){
		init();
	}
	
	static void ReTake(){
		for(int i=0; i<2; i++){
			if(1==Board_Data.HistDelete()){
				if(turnflag == BLACK)	turnflag = WHITE;
				else									turnflag = BLACK;
			}
		}
	}
}