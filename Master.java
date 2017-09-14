package reversi2;

import java.util.Scanner;
public class Master{
	
	static public int turnflag;			//黒、白どちらのターンかを保持
	static private int masusize = 8;	//マスの数　8*8
	
	private static int CPtime = 1000;
	
	//石の種類
	private static final int	TRANS = 0;
	private static final int	BLACK = 1;
	private static final int	WHITE = 2;
	
	private static int page;		//ページの状態　ゲーム中:0 中断確認:1 結果画面:2 再戦選択:3

	private static final int	CENTER = 4;		//探索を行う際の真ん中を示す値
	static Player[] Player = new Player[2];
//	private static int[] Player = new int[2];	//プレイヤー
	private static int winner;					//プレイヤー配列番号と紐つけ　勝ったほう
	//実際用いるときはturnflagが1､2なのでPlayer[turnflag-1]
	
	public Master(){
	}
	
	//初期化
	static void init(int p1,int p2){
		Player[0] = new Player(p1,BLACK);
		Player[1] = new Player(p2,WHITE);
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
	
	static void CPturn(){
		if(Player[turnflag-1].Get_Level() != 0){
			Player[turnflag-1].Minimax(Board_Data.board,turnflag,0);
			System.out.println(turnflag+":"+Player[turnflag-1].p+","+Player[turnflag-1].t);
			Send_Press(Player[turnflag-1].p,Player[turnflag-1].t,Player[turnflag-1].Get_Level());
		}
	}
	
	//画面からマス番号を受け取って処理
	//(x,y) = (正の数,正の数)はマス番号、(-1,1or2）は下のボタン
	static void Send_Press(int x,int y,int flom){
		if(page == 0){
			if(x == -1){
				if(y == 1)		ReTake();
				if(y == 2)		page = 1;
			}else{
				if(Player[turnflag-1].Get_Level() == flom && Board_Data.Get(x,y) == 0){
					if(turnflag == BLACK){
						if(Board_Data.Put(CENTER,x,y) == 1)	turnflag = WHITE;
					}else if(turnflag == WHITE){
						if(Board_Data.Put(CENTER,x,y) == 1)	turnflag = BLACK;
					}
				}
				pass();
			}
		}else if(page == 1){
			if(x == -1){
				if(y == 1)		page = 0;	//ゲームに戻る
				if(y == 2)		finish();	//ゲーム終了
			}
		}else	if(page == 2){
			if(x == -1){
				if(y == 1){				//レベル選択へ
					if(Player[0] != Player[1] && (Player[0].Get_Level() == 0 || Player[1].Get_Level() == 0)){
							page = 3;
					}else	finish();
				}
				if(y == 2)		finish();	//ゲーム終了
			}
		}else if(page == 3){
			if(x == -1){
				if(y == 2){
					finish();//同じレベル init(今のレベル)
				}
				if(y == 1){				//次/前のレベル
					//ここにレベル設定を変更する
					if(Player[winner-1].Get_Level() == 0 && Player[0].Get_Level()+Player[1].Get_Level()<5){
						init(0,Player[0].Get_Level()+Player[1].Get_Level()+1);
					}else if(Player[winner-1].Get_Level() != 0 && Player[0].Get_Level()+Player[1].Get_Level()>1){
						init(0,Player[0].Get_Level()+Player[1].Get_Level()-1);
					}
				}
			}
		}
	}
	
	static int Get_Page(){
		return page;
	}
	
	static void pass(){
		if(Board_Data.Check() == 0){
			Board_Data.pass();
			if(turnflag == BLACK)	turnflag = WHITE;
			else						turnflag = BLACK;
			
			if(Board_Data.Check() == 0){
				page = 2;
				winner = Board_Data.win();
			}
		}
	}
	
	static int Get_CPlv(int color){
		return Player[color-1].Get_Level();
	}
	
	static int Get_Winner(){
		if(page < 1){
			return -1;
		}else{
			return Player[winner-1].Get_Level();
		}
	}
	
	static void finish(){
		init(Player[0].Get_Level(),Player[1].Get_Level());
	}
	
	static void ReTake(){
		int check;
		for(int i=0; i<2; i++){
			check = Board_Data.HistDelete();
			if(check == 1){
			}else if(check == 2){		//手番がパスだった場合
				if(i == 1)	i = -1;		//戻った1個めがパス(相手がパス)のときはそのまま次のパスへ
				//2個がパス(相手を1回戻した次の自ターンがパス)ならそこからさらに2手戻る
				if(turnflag == BLACK)	turnflag = WHITE;
				else						turnflag = BLACK;
			}else{
				if(turnflag == BLACK)	turnflag = WHITE;
				else						turnflag = BLACK;
			}
		}
	}
	
	//履歴取得
	static int[][] Get_History(){
		int[][] temp = new int[100][2]; //保持用
		temp = Board_Data.GetHistoryData();
		return temp;
	}
	//指し回数取得
	static int Get_Time(){
		int temp2 = 0; //保持用
		temp2 = Board_Data.GetTimeData();
		return temp2;
	}
	
}