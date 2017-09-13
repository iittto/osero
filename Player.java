package reversi2;

public class Player{
	
	static private int masusize = 8;
	static private int CENTER = 4;
	static private int BLACK = 1;
	static private int WHITE = 2;
	static private int MAX_SIZE = 64;
//	boolean is_computer;
		
	int cp_level;		//0->user　1~10->CPlv
	int recursion;	//探索の深さ
	//public static int board_eval[][] = new int[masusize+2][masusize+2];//盤面の評価値
	
	static int p,t;	//次の最善の指し手座標

	
	Player(int lv){
		cp_level = lv;
		switch (lv){
		case 1:
			recursion = 4;
			break;
		case 2:
			recursion = 3;
			break;
		case 3:
			recursion = 2;
			break;
		case 4:	
			recursion = 1;
			break;
		case 5:
			recursion = 0;
			break;
		case 6:
			recursion = 1;
			break;
		case 7:
			recursion = 2;
			break;
		case 8:
			recursion = 3;
			break;
		case 9:
			recursion = 4;
			break;
		case 10:
			recursion = 5;
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
	// void init(){
	// 	for(int i = 1;i < masusize+1; i++){
	// 		for(int j = 1;j < masusize+1; j++){
	// 			if((i == 1 || i == 8) && (j == 1 || j == 8))
	// 				board_eval[i][j] = 30;
	// 			else if((i == 2 || i == 7) && (j == 2 || j == 7))
	// 				board_eval[i][j] = -15;
	// 			else if((i == 1 || i == 2 || i == 7 || i == 8) && (j == 1 || j == 2 || j == 7 || j == 8) && i != j)
	// 				board_eval[i][j] = -12;
	// 			else if((i == 2 || i == 7) && (j >= 3 && j <= 6))
	// 				board_eval[i][j] = -3;
	// 			else if((j == 2 || j == 7) && (i >= 3 && i <= 6))
	// 				board_eval[i][j] = -3;
	// 			else if((j == 4 || j == 5) || (i == 4 || i == 5))
	// 				board_eval[i][j] = -1;
	// 		}
	// 	}//盤面評価値初期化

	// }
	static int eval(int [][]board){
		int val = 0;
		for(int i = 1;i < masusize; i++){
			for(int j = 1;j < masusize; j++){
				val += Board_Data.board_eval[i][j] * board[i][j];
			}
		}
		return val;
	}
	int [][]find(int turn,int [][]map){
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
		int val;
		int next_turn = (turn == BLACK)?WHITE:BLACK;
		//copy_board(node,temp);//再帰中の盤面の状態をコピー
		int [][]list = new int[MAX_SIZE][2];
		if(depth == this.recursion)
			return eval(node);

		//expand_node(node,next_turn);
		list = find(turn,node);
		//	System.out.println("dd");

		while(list[i+1][0] != 0 && list[i+1][1] != 0){
			//System.out.println("ddd");
			// if(list[i][0] != 0 && list[i][1] != 0)
			// 	break;
			temp = new int[masusize+2][masusize+2];
			copy_board(node,temp);//現在の盤面をコピー
			//temp[list[i][0]][list[i][1]] = turn;
			Board_Data.Virtual_put(turn,temp,CENTER,list[i][0],list[i][1]);
			i++;
			System.out.println(list[i][0]+","+list[i][1]);
			
			val = Minimax(temp,next_turn,depth + 1);
			if(turn == BLACK && best < val){
				best = val;
				if(depth == 0 ){
				p = list[i][0];
				t = list[i][1];
				}
                //System.out.println(list[i][0]+","+list[i][1]);
			}
			if(turn == WHITE && best < -val){
				best = -val;
				if(depth == 0 ){
				p = list[i][0];
				t = list[i][1];
				}
				//System.out.println(list[i][0]+","+list[i][1]);
			}

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
	