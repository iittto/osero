import java.util.Scanner;
public class Board_Data {

	static private int masusize = 8;

	//範囲外アクセスを防ぐために外枠をつくっておく　実際のオセロ盤面はboard[1][1]〜board[8][8]
	static public int[][] board = new int[masusize+2][masusize+2];
	
	private static final int	PUTNG = -1;
	private static final int	TRANS = 0;
	private static final int	BLACK = 1;
	private static final int	WHITE = 2;

	private static final int	ULEFT 	= 0;
	private static final int	UP 			= 1;
	private static final int	URIGHT 	= 2;
	private static final int	LEFT 		= 3;
	private static final int	CENTER 	= 4;
	private static final int	RIGHT 	= 5;
	private static final int	DLEFT		= 6;
	private static final int	DOWN		= 7;
	private static final int	DRIGHT	= 8;
	
	private static final int[][] dir	= {{-1,-1},{-1, 0},{-1, 1},
																			 { 0,-1},{ 0, 0},{ 0, 1},
																			 { 1,-1},{ 1, 0},{ 1, 1}};
/*
	→y
↓	[0] [1] [2]
x	[3] [4] [5]
	[6]	[7] [8]
*/

	public Board_Data(){   	
	}
	
	static void init(){
		//boardの初期化
		for(int i=0; i<masusize+2; i++){
   		for(int j=0; j<masusize+2; j++){
   			if(i==0 || j==0)	board[i][j] = PUTNG;
   			else							board[i][j] = TRANS;
   		}
   	}
   	
   	board[masusize/2][masusize/2] = BLACK;
   	board[masusize/2+1][masusize/2] = WHITE;
   	board[masusize/2][masusize/2+1] = WHITE;
   	board[masusize/2+1][masusize/2+1] = BLACK;
	}
	
	static int recolor(int color){
		if(color == BLACK)	return WHITE;
		else								return BLACK;
	}
	
	static int Put_Check(int vector,int x, int y){
		int P1 = Master.get_turn();
		int P2 = recolor(P1);

		if(vector == CENTER){
				for(int i=0; i<7; i++){
					if(board[x+dir[i][0]][y+dir[i][1]] == P2){
						if(Put_Check(i,x+dir[i][0],y+dir[i][1]) == 1)	return 1;
					}
				}
/*			if(board[][j-1] == P2){	if( Put_Check(ULEFT ,i-1,j-1)==1 )	return 1;}
				if(board[i-1][j  ] == P2){	if( Put_Check(UP		,i-1,j  )==1 )	return 1;}
				if(board[i-1][j+1] == P2){	if( Put_Check(URIGHT,i-1,j+1)==1 )	return 1;}
				if(board[i  ][j-1] == P2){	if( Put_Check(LEFT	,i	,j-1)==1 )	return 1;}
				if(board[i  ][j+1] == P2){	if( Put_Check(RIGHT ,i	,j-1)==1 )	return 1;}
				if(board[i+1][j-1] == P2){	if( Put_Check(DLEFT ,i+1,j-1)==1 )	return 1;}
				if(board[i+1][j  ] == P2){	if( Put_Check(DOWN	,i+1,j  )==1 )	return 1;}
				if(board[i+1][j+1] == P2){	if( Put_Check(DRIGHT,i+1,j+1)==1 )	return 1;}
				return 0;
*/	}else{
				x += dir[vector][0];
				y += dir[vector][1];
				if(board[x][y] == P2){
					if(Put_Check(vector,x,y) == 1)	return 1;
				}else if(board[x][y] == P1){
					return 1;
				}
		}
		return 0;
	}
	
	static int Put(int vector,int x, int y){
		int P1 = Master.get_turn();
		int P2 = recolor(P1);
		int OK = 0;

		if(vector == CENTER){
				for(int i=0; i<9; i++){
					if(i!=4 && board[x+dir[i][0]][y+dir[i][1]] == P2){
						if(Put(i,x+dir[i][0],y+dir[i][1]) == 1){
							board[x][y] = P1;
							board[x+dir[i][0]][y+dir[i][1]] = P1;
							OK = 1;
//							return 1;
						}
					}
				}
		}else{
				x += dir[vector][0];
				y += dir[vector][1];
				if(board[x][y] == P2){
					if(Put(vector,x,y) == 1){
						board[x][y] = P1;
						return 1;
					}
				}else if(board[x][y] == P1){
					return 1;
				}
		}
		return OK;
	}
	
	static void Get(int[][] viewboard){
		for(int i=0; i<masusize+2; i++){
			for(int j=0; j<masusize+2; j++){
				viewboard[i][j] = board[i][j];
			}
		}
	}
	
	static int Get(int i,int j){
		return board[i][j];
	}
	
	static void Set(int x, int y,int color){
		board[x][y] = color;
	}
	
/*	
	static int get_turn(){
		return turnflag;
	}
*/
/*	
	static void Set_Board_Data(int i,int j,int n){
		board[i][j] = n;
	}
	
		
*/
/*
	        public static void show(){

            print(LINE);
            print("     ");
            for(int i=1;i<L-1;i++){
                print(i);
                if(i<L-2){print(" ");}
            }

            for(int i=0;i<L;i++){
                if(0<i && i<L-1){
                    print("\n %d ",i);
                }else {
                    print("\n   ");
                }
                for(int j=0;j<L;j++){
                    switch(board[i][j]){
                        case WALL  : {print("+"); break;}
                        case EMPTY : {print("_"); break;}
                        case WHITE : {print("o"); break;}
                        case BLACK : {print("*");       }
                    }
                    print(" ");
                }
                if(i<L-1){print(" ");}
                else     {print("\n") ;}
            }
            print(LINE);
        }
*/
}
