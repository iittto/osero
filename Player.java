package reversi2;
import java.util.Random;

public class Player{
	
	static private int masusize = 8;
	static private int CENTER = 4;
	static private int BLACK = 1;
	static private int WHITE = 2;
	static private int MAX_SIZE = 64;
	
	Random rnd = new Random();
	
	int findsize = 0;
//	boolean is_computer;
	
	int my_color;
	
	int cp_level;		//0->user　1~10->CPlv
	int recursion;	//探索の深さ
	//public static int board_eval[][] = new int[masusize+2][masusize+2];//盤面の評価値
	
	static int p,t;	//次の最善の指し手座標

	int[][] evalf1 =
		{ {	100, -20, 20,  5,  5, 20, -20, 100},
		  {	-20, -40, -5, -5, -5, -5, -40, -20},
		  {	 20,  -5, 15,  3,  3, 15,  -5,  20},
		  {   5,  -5,  3,  3,  3,  3,  -5,   5},
		  {   5,  -5,  3,  3,  3,  3,  -5,   5},
		  {	 20,  -5, 15,  3,  3, 15,  -5,  20},
		  {	-20, -40, -5, -5, -5, -5, -40, -20},
		  {	100, -20, 20,  5,  5, 20, -20, 100}};

	int[][] evalf2 =
		{ {	 300, -100,  0, -10, -10,  0, -100,  300},
		  {	-100, -150, -30, -30, -30, -30, -150, -100},
		  {	  0,   -30,  0, -10, -10,  0,  -30,   0},
		  {	 -10,   -30, -10, -10, -10, -10,  -30,  -10},
		  {	 -10,   -30, -10, -10, -10, -10,  -30,  -10},		  
		  {	  0,   -30,  0, -10, -10,  0,  -30,   0},		  
		  {	-100, -15, -30, -30, -30, -30, -150, -100},
		  {	 300, -100,  0, -10, -10,  0, -100,  300}};

	int[][] evalf3 =
		{ {	 -20, 12,  -50, -50, -50,  -50, 12,  -20},
		  {	12, 50, 10, 10, 10, 10, 50, 12},
		  {	  -5,  10,  0, 20, 20,  0,  10,  -5},
		  {	 -5,   10, 20, 20, 20, 20,  10,  -5},
		  {	 -5,  10, 20, 20, 20, 20,  10,  -5},		  
		  {	  -5,  10,  0, 20, 20,  0,  10,  -5},		  
		  {	12, 50, 10, 10, 10, 10, 50, 12},
		  {	 -200, 12,  -50, -50, -50,  -50, 12,  -20}};

	Player(int lv,int color){
		findsize = 0;
		cp_level = lv;
		my_color = color;
		switch (lv){
		case 1:
			recursion = 3;
			break;
		case 2:
			recursion = 1;
			break;
		case 3:
			recursion = 1;
			break;
		case 4:
			recursion = 1;
			break;
		case 5:
			recursion = 3;
			break;
		}
	}
	
	public int Get_Level(){
		return cp_level;
	}
	
	void Thought(){
		int [][]t = new int[MAX_SIZE][2];
		int i = 0;
		//t = this.find(Board_Data.board);
		// while(t[i][0] != 0 && t[i][1] != 0){
		// 	System.out.println(t[i][0]+","+t[i][1]);
		// 	i++;
		// }
		//System.out.print("\n");
	}
	
	/**********************************************************************************************/
    // (i, j)に石を置いた結果の盤面がboardとなったとき、その手はどれだけ優先すべきか評価
    //
    // [1] ＜優先度0＞角は最優先。
    // [2] ＜優先度1＞角以外の辺については、そこから角に繋がる全部の石が自分のになるなら優先。
    //     例えば下の図で、「＋」については黒石は優先的に置くが、「×」はそうではない。
    //     ●○○○＋×・・
    //     ●・・・・○・・
    //     ●・・・・●・・
    //     ＋○●・・・・・
    //     ○・・・・・・・
    //     ×○●・・・・・
    //     ・・・・・・・・
    //     ・・・・・・・・
    // [3] ＜優先度3＞角の隣は、対応する角が自分の石でないならば、優先度最低
    // [4] ＜優先度2＞以上に該当しないものは優先度同一
    int calc_priority(int [][]board, int i, int j){
    	int enemy_color = (my_color == BLACK)?WHITE:BLACK;
    	int flag = 0;
    	
        // 角。最優先
        if((i == 1 || i == 8) && (j == 1 || j == 8)){
            return 0;
        }
        
        
        // 辺。
        // どちらかの角まで自分の石が続いていれば優先(1)
        // そうでなければ先に進む
        if(i == 1 || i == 8){
            int c;
            
            flag = 0;
            // 右端か左端の列に置いた場合
            for(c = j-1; c >= 0; --c){
                // 置いた場所から上に見て行って、角まで自分の石が続いていれば1 for最後までいっていればc=-1
            	//空があればNG
            	if(board[i][c] == 0){
            		flag = 1;
            		break;
            	}
            	
            	if(flag == 0 && board[i][c] == enemy_color){
            	  	flag = 1;
              }else if(flag == 1 && board[i][c] == my_color){
            	  	flag = 2;
              }else if(flag == 2 && board[i][c] == enemy_color){
            	  	flag = 1;
            	  	break;
              	}
              }
            if(flag == 0 || flag == 2) return 1;
            
            flag = 0;
            for(c = j+1; c < j; ++c){
                // 置いた場所から下に見て行って、角まで自分の石が続いていれば
            	if(board[i][c] == 0){
            		flag = 1;
            		break;
            	}
            	
            	if(flag == 0 && board[i][c] == enemy_color){
            	  	flag = 1;
              }else if(flag == 1 && board[i][c] == my_color){
            	  	flag = 2;
              }else if(flag == 2 && board[i][c] == enemy_color){
            	  	flag = 1;
            	  	break;
              	}
              }
            if(flag == 0 || flag == 2) return 1;
        }
        
        if(j == 1 || j == 8){
            int c;
            
            flag = 0;
            // 上端か下端の列に置いた場合
            for(c = i-1; c >= 0; --c){
                // 置いた場所から左に見て行って、角まで自分の石が続いていれば
            	if(board[c][j] == 0){
            		flag = 1;
            		break;
            	}
            	
            	if(flag == 0 && board[c][j] == enemy_color){
            	  	flag = 1;
              }else if(flag == 1 && board[c][j] == my_color){
            	  	flag = 2;
              }else if(flag == 2 && board[c][j] == enemy_color){
            	  	flag = 1;
            	  	break;
              	}
              }
            if(flag == 0 || flag == 2) return 1;
            
            flag = 0;
            for(c = i+1; c <= 9; ++c){
                // 置いた場所から右に見て行って、角まで自分の石が続いていれば
            	if(board[c][j] == 0){
            		flag = 1;
            		break;
            	}
            	
            	if(flag == 0 && board[c][j] == enemy_color){
            	  	flag = 1;
              }else if(flag == 1 && board[c][j] == my_color){
            	  	flag = 2;
              }else if(flag == 2 && board[c][j] == enemy_color){
            	  	flag = 1;
            	  	break;
              	}
              }
            if(flag == 0 || flag == 2) return 1;
        }
        
        // 角の隣。該当する角が自分の石でなければ優先度最低
        int corner_i = -1, corner_j = -1; // 「該当する角」の座標
        
        if(i <= 1) corner_i = 0;
        if(i >= 7) corner_i = 8;
        if(j <= 1) corner_j = 0;
        if(j >= 7) corner_j = 8;
        if(corner_i != -1 && corner_j != -1 && board[corner_i][corner_j] != my_color){
            return 3;
        }
        
        return 2;
    }
    
    // 同じ優先度のもののなかでの比較に必要な指標。
    // そこに石を置いてみたとして、
    // 自分の石で以下のいずれかに該当するものが一番多くなるものを選ぶ：
    // 「角」「辺」「周囲八方に他の石がある石」
    int count_good_pieces(int[][] board){
        int good_pieces = 0;
        int enemy_color = (my_color == BLACK)?WHITE:BLACK;
        
        for(int i = 1; i < 9; ++i){
            if(board[i][0] == my_color) ++good_pieces;
            if(board[i][8] == my_color) ++good_pieces;
        }
        for(int j = 1; j < 8; ++j){
            if(board[0][j] == my_color) ++good_pieces;
            if(board[8][j] == my_color) ++good_pieces;
        }
        
        for(int i = 1; i < 9; ++i){
            for(int j = 1; j < 9; ++j){
                if(
                    board[i-1][j-1] != 0 &&
                    board[i][j-1] != 0 &&
                    board[i+1][j-1] != 0 &&
                    board[i-1][j]   != 0 &&
                    board[i][j]   == my_color &&
                    board[i+1][j]   != 0 &&
                    board[i-1][j+1] != 0 &&
                    board[i][j+1] != 0 &&
                    board[i+1][j+1] != 0
                ){
                    ++good_pieces;
                }
            }
        }
        
        return good_pieces;
}
    /******************************************************************************************************/
	
	
	int eval(int [][]board, int turn){
		int val = 0;
		int color = 1;
		int enemy_color = (my_color == BLACK)?WHITE:BLACK;
		int time = Master.Get_Time();
		int ran = rnd.nextInt(10);
/*
		if(enemy_color == turn){
			color = color * (-1);
		}
*/		
		for(int i = 1;i < masusize; i++){
			for(int j = 1;j < masusize; j++){
				if(board[i][j] == my_color){
					color = 1;
				}else if(board[i][j] == enemy_color){
					color = -1;
				}
				
					if(cp_level > 3){
						if(time < 45){
							val += color * evalf2[i-1][j-1];
						}else{
							val += color * evalf1[i-1][j-1];
						}
					}else if(cp_level == 3){
						ran = rnd.nextInt(10);
						val += ran;
					}else if(cp_level == 2){
						val += color * evalf1[i-1][j-1];
					}else if(cp_level == 1){
						val += color * evalf3[i-1][j-1];
				}
/*				}else{
					if(cp_level > 6){
						val += color * evalf2[i-1][j-1] * board[i][j];
					}else if(cp_level > 4){
						val += color * evalf1[i-1][j-1] * board[i][j];
					}else if(cp_level > 2){
						val += color * evalf1[i-1][j-1] * board[i][j];
					}else if(cp_level > 0){
						val += color * evalf2[i-1][j-1] * board[i][j];
					}
				}
*/			}
		}
		return val;
	}
	
	
	
	int [][]find(int turn,int [][]map,int s){
		int [][]possible = new int[MAX_SIZE][2];//[0]->x,[1]->y
		int size = 0;
		for(int i = 1;i < masusize+1; i++){
			for(int j = 1;j < masusize+1; j++){
				if( map[i][j] == 0 && Board_Data.Put_Check(turn,map,CENTER,i,j) == 1){
					possible[size][0] = i;
					possible[size][1] = j;
					size++;
				}
			}
		}
		s = size;
		return possible;
	}
	static void expand_node(int [][]map,int turn){


	}

	static void copy_board(int [][]src,int [][]dst){
		//dst = new int[masusize+2][masusize+2];
		for(int i = 0;i < masusize+2; i++){
			for(int j = 0; j < masusize+2; j++){
				dst[i][j] = src[i][j];
			}
		}
	}
	
	int Minimax(int [][]node,int turn,int depth){
		int [][]temp;
		int best = Integer.MIN_VALUE;
		int i = 0;
		int x=0,y=0;
		int val;
		int size = 0;
		int next_turn = (turn == BLACK)?WHITE:BLACK;
		//copy_board(node,temp);//再帰中の盤面の状態をコピー
		int [][]list = new int[MAX_SIZE][2];
		if(depth == this.recursion)
			return eval(node, turn);

		//expand_node(node,next_turn);
		list = find(turn,node,size);
		//	System.out.println("dd");

		while(list[i][0] != 0 && list[i][1] != 0){
			//System.out.println("ddd");
			// if(list[i][0] != 0 && list[i][1] != 0)
			// 	break;
			temp = new int[masusize+2][masusize+2];
			copy_board(node,temp);//現在の盤面をコピー
			//temp[list[i][0]][list[i][1]] = turn;
			Board_Data.Virtual_put(turn,temp,CENTER,list[i][0],list[i][1]);
			System.out.println(list[i][0]+","+list[i][1]);
			
			
			
			val = Minimax(temp,next_turn,depth + 1);
			if(turn == my_color && best <= val){
				best = val;
				x = list[i][0];
				y = list[i][1];
              //System.out.println(list[i][0]+","+list[i][1]);
			}
			if(turn != my_color && best <= -val){
				best = -val;
				x = list[i][0];
				y = list[i][1];
				//System.out.println(list[i][0]+","+list[i][1]);
			}
			
			i++;
		}
		if(depth == 0){
			p = x;
			t = y;
		}

		return best;


	}
	
	int Get_level(){
		return cp_level;
	}
	// 	System.out.println(list[i][0]+","+list[i][1]);
	// 	i++;
	// }
	// System.out.print("\n");

	
}
	