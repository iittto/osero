package reversi2;
import java.util.Deque;
import java.util.Queue;
import java.util.Scanner;
public class Board_Data {

	static private int masusize = 8;

	//範囲外アクセスを防ぐために外枠をつくっておく　実際のオセロ盤面はboard[1][1]〜board[8][8]
	static public int[][] board = new int[masusize+2][masusize+2];
	public static int board_eval[][] = new int[masusize+2][masusize+2];//評価関数
	
	private static final int	TRANS = 0;
	private static final int	BLACK = 1;
	private static final int	WHITE = 2;

	private static final int	CENTER = 4;
	private static final int[][] dir	={	{-1,-1},{-1, 0},{-1, 1},
												{ 0,-1},{ 0, 0},{ 0, 1},
												{ 1,-1},{ 1, 0},{ 1, 1}};
/*
	→y
↓		[0] [1] [2]
x		[3] [4] [5]
		[6]	[7] [8]
		CENTER = 4
*/

/*	
	private Deque<int[]> PutHistDeq;		//石を置いた履歴 [x][y][ひっくり返した個数]
	private Queue<int[]> PutHistQue;
	private Deque<int[]> RevHistDeq;	//ひっくり返った石の履歴	[x][y]
	private Queue<int[]> RevHistQur;
*/
	
	static int[][] PutHist = new int[100][3];		//石を置いた履歴 [x][y][ひっくり返した個数]
	static int[][] ReverceHist = new int[300][2];	//ひっくり返った石の履歴	[x][y]
	static private int PHistP;	//ポインタの代わり
	static private int RHistP;	
	
	public Board_Data(){   	
	}
	
	//boardの初期化
	static void init(){
		PHistP = 0;
		RHistP = 0;
		PutHist[0][2] = -1;
		
	
		for(int i=0; i<masusize+2; i++){
			for(int j=0; j<masusize+2; j++){
				if(i==0 || j==0)	board[i][j] = -1;				//置けない場所（ボードの外枠）
				else							board[i][j] =  TRANS;		//おける場所の初期値0（石なし）
			}
		}
   	
		board[masusize/2][masusize/2] = WHITE;
		board[masusize/2+1][masusize/2] = BLACK;
		board[masusize/2][masusize/2+1] = BLACK;
		board[masusize/2+1][masusize/2+1] = WHITE;
	}
	
	//評価値
	static void eval_init(){
		
		for(int i = 0;i < masusize; i++){
			for(int j = 0;j < masusize; j++){
				if((i == 0 || i == 7) && (j == 0 || j == 7))
					board_eval[i][j] = 30;
				else if((i == 1 || i == 6) && (j == 1 || j == 6))
					board_eval[i][j] = -15;
				else if((i == 0 || i == 1 || i == 6 || i == 7) && (j == 0 || j == 1 || j == 6 || j == 7) && i != j)
					board_eval[i][j] = -12;
				else if((i == 1 || i == 6) && (j >= 2 && j <= 5))
					board_eval[i][j] = -3;
				else if((j == 1 || j == 6) && (i >= 2 && i <= 5))
					board_eval[i][j] = -3;
				else if((j == 3 || j == 4) || (i == 3 || i == 4))
					board_eval[i][j] = -1;
			}
		}//盤面評価値初期化

	}
	
	static int recolor(int color){
		if(color == BLACK)	return WHITE;
		else					return BLACK;
	}


	//置く場所があるかの確認用
	static int Pass_Check(int vector,int x, int y){
		int P1 = Master.Get_Turn();
		int P2 = recolor(P1);
		
		if(vector == CENTER){
				for(int i=0; i<9; i++){
					if(board[x+dir[i][0]][y+dir[i][1]] == P2){
						if(Pass_Check(i,x+dir[i][0],y+dir[i][1]) == 1)	return 1;
					}
				}
		}else{
				x += dir[vector][0];
				y += dir[vector][1];
				if(board[x][y] == P2){
					if(Pass_Check(vector,x,y) == 1)	return 1;
				}else if(board[x][y] == P1){
					return 1;
				}
		}
		return 0;
	}

	//配列返すよう
	static int Put_Check(int turn,int [][]node,int vector,int x,int y){
		int P1 = turn;
		int P2 = (turn == BLACK) ? WHITE:BLACK;
		
		int OK = 0;
		if(vector == CENTER){
			for(int i=0; i<9; i++){
				if(i!=4 && node[x+dir[i][0]][y+dir[i][1]] == P2){
					if(Put_Check(turn,node,i,x+dir[i][0],y+dir[i][1]) == 1){
						//board[x][y] = P1;
						//board[x+dir[i][0]][y+dir[i][1]] = P1;
						OK = 1;
						//							return 1;
					}
				}
			}
		}else{
			x += dir[vector][0];
			y += dir[vector][1];
			if(node[x][y] == P2){
				if(Put_Check(turn,node,vector,x,y) == 1){
					//board[x][y] = P1;
					return 1;
				}
			}else if(node[x][y] == P1){
				return 1;
			}
		}
		return OK;


	}

	static int Virtual_put(int turn,int [][]node,int vector,int x, int y){
		int P1 = turn;
		int P2 = (turn == BLACK)?WHITE:BLACK;
		int OK = 0;

		if(vector == CENTER){
			for(int i=0; i<9; i++){
				if(i!=4 && node[x+dir[i][0]][y+dir[i][1]] == P2){
					if(Virtual_put(turn,node,i,x+dir[i][0],y+dir[i][1]) == 1){
						node[x][y] = P1;
						node[x+dir[i][0]][y+dir[i][1]] = P1;
						OK = 1;
						//							return 1;
					}
				}
			}
		}else{
			x += dir[vector][0];
			y += dir[vector][1];
			if(node[x][y] == P2){
				if(Virtual_put(turn,node,vector,x,y) == 1){
					node[x][y] = P1;
					return 1;
				}
			}else if(node[x][y] == P1){
				return 1;
			}
		}
		return OK;
	}
	
	
	
	//まだ置く場所があるかどうか
	static int Check(){		
		for(int i=0; i<masusize; i++){
			for(int j=0; j<masusize; j++){
				if(board[i+1][j+1] == TRANS && Pass_Check(CENTER,i+1,j+1) == 1) return 1;
			}
		}
		return 0;
	}
	
	//クリックされた場所に石を置く、挟んだ石をひっくり返す(置ければ1,置けなければ0を返す)
	static int Put(int vector,int x, int y){
		int P1 = Master.Get_Turn();
		int P2 = recolor(P1);
		int OK = 0;

		if(vector == CENTER){
				int Histnum = RHistP;
				for(int i=0; i<9; i++){
					if(i!=4 && board[x+dir[i][0]][y+dir[i][1]] == P2){
						if(Put(i,x+dir[i][0],y+dir[i][1]) == 1){
							board[x+dir[i][0]][y+dir[i][1]] = P1;
							
							//履歴に追加
							ReverceHist[RHistP][0] = x+dir[i][0];
							ReverceHist[RHistP][1] = y+dir[i][1];
							RHistP++;
							OK = 1;
						}
					}
				}
				if(OK == 1){
					board[x][y] = P1;
					PutHist[PHistP][0] = x;
					PutHist[PHistP][1] = y;
					PutHist[PHistP][2] = RHistP - Histnum;
					PHistP++;
				}
		}else{
				x += dir[vector][0];
				y += dir[vector][1];
				if(board[x][y] == P2){
					if(Put(vector,x,y) == 1){
						board[x][y] = P1;
						
						//履歴に追加
						ReverceHist[RHistP][0] = x;
						ReverceHist[RHistP][1] = y;
						RHistP++;

						return 1;
					}
				}else if(board[x][y] == P1){
					return 1;
				}
		}
		return OK;
	}
	
	//ボードデータ全部を渡す
	static void Get(int[][] viewboard){
		for(int i=0; i<masusize+2; i++){
			for(int j=0; j<masusize+2; j++){
				viewboard[i][j] = board[i][j];
			}
		}
	}
	
	//１つのマスの情報を渡す
	static int Get(int i,int j){
		return board[i][j];
	}
		
	static int HistDelete(){
		int x,y;
		int num = 0;
		
		if(PHistP == 0 && RHistP == 0){
			return 1;
		}else{
			PHistP--;
			int delnum = PutHist[PHistP][2];
			/*
			for(int i=0; i<PHistP; i++){
				java.lang.System.out.println(i+"手目"+PutHist[i][0]+ " " + PutHist[i][1]);
				for(int j=0; j<=PutHist[i][2];j++){
					java.lang.System.out.println(ReverceHist[j][0]+ " " + ReverceHist[j][1]);
				}
			}		
		*/
		if(PutHist[PHistP][0] == -1 && PutHist[PHistP][1] == -1){
			return 2;
		}
			
		for(int i=0; i<delnum; i++){
			if(RHistP > 0) RHistP--;
			
			x = ReverceHist[RHistP][0];
			y =	ReverceHist[RHistP][1];
			if(board[x][y] == BLACK)	board[x][y] = WHITE;
			else						board[x][y] = BLACK;

		}

		x = PutHist[PHistP][0];
		y = PutHist[PHistP][1];
		board[x][y] = TRANS;

		return 0;
		}
	}
	
	static void pass(){
		PutHist[PHistP][0] = -1;
		PutHist[PHistP][1] = -1;
		PutHist[PHistP][2] = 0;
		PHistP++;
	}
/*
	static void Set(int x, int y,int color){
		board[x][y] = color;
	}
*/
/*	
	static int Get_Turn(){
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
	static int win(){
		int white = 0;
		int black = 0;
		for(int i=0; i<masusize; i++){
			for(int j=0; j<masusize; j++){
				if(board[i+1][j+1] == 1)black++;
				if(board[i+1][j+1] == 2)white++;
			}
		}
		
		if(black > white){
			return BLACK;
		}else{
			return WHITE;
		}
	}
	
	//----履歴追加------------------------//
	//history受け渡し関数
	static int[][] GetHistoryData(){
		return PutHist;
	}
	//履歴数受け渡し関数
	static int GetTimeData(){
		return PHistP;
	}
	//----------------------------------//
}