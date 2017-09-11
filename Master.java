//package reversi2;

import java.util.Scanner;
public class Master{
	
	static public int turnflag;
	static private int masusize = 8;
	
	private static final int	TRANS = 0;
	private static final int	BLACK = 1;
	private static final int	WHITE = 2;
	
	private static final int	CENTER = 4;
		
	public Master(){
	}
	
	//初期化
	static void init(){
		Board_Data.init();
		turnflag = BLACK;
	}

	static int Get_Turn(){
		return turnflag;
	}
	
	static void Get(int[][] viewboard){
		Board_Data.Get(viewboard);
	}
	
	//画面からマス番号を受け取って処理
	static void Send_Press(int x,int y){
		if(x == -1 && y == -1){
			ReTake();
		}else{
			if(Board_Data.Get(x,y) == 0){
				if(turnflag == BLACK){
//					if(Board_Data.Put_Check)
//					Board_Data.Set(x,y,BLACK);
					if(Board_Data.Put(CENTER,x,y) == 1)	turnflag = WHITE;
				}else{
//					Board_Data.Set(x,y,WHITE);
					if(Board_Data.Put(CENTER,x,y) == 1)	turnflag = BLACK;
				}
			}
			pass();
		}
	}
	
	static void pass(){
		if(Board_Data.Check() == 0){
			if(turnflag == BLACK)	turnflag = WHITE;
			else						turnflag = BLACK;
			
			if(Board_Data.Check() == 0){
				finish();
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
