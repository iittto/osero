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
	
	static void init(){
		turnflag = BLACK;
	}

	static int get_turn(){
		return turnflag;
	}
	
	//画面からマス番号を受け取って処理
	static void send_Press(int x,int y){
		if(Board_Data.Get(x,y) == 0){
			if(turnflag == BLACK){
//				if(Board_Data.Put_Check)
//				Board_Data.Set(x,y,BLACK);
				if(Board_Data.Put(CENTER,x,y) == 1)	turnflag = WHITE;
			}else{
//				Board_Data.Set(x,y,WHITE);
				if(Board_Data.Put(CENTER,x,y) == 1)	turnflag = BLACK;
			}
		}
	}
}
