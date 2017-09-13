package reversi2;

import java.awt.event.* ;

import javax.swing.* ;

import java.awt.Font;
import java.awt.Graphics ;
import java.awt.Color ;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class Board extends JPanel implements MouseListener {
/********************sampleに入ってたやつ 多分いらない*********************
  final static int lim = 256*2;//256だと追い越され得るから
  static int[] bX = new int[lim]; //位置情報格納配列
  static int[] bY = new int[lim]; //位置情報格納配列
  static int[] bColor = new int[lim]; //色　情報格納配列
  static int sta = 0; //配列最前列位置（後に描画）
  static int end = 0; //配列最後尾位置（先に描画）
  int r = 30;                           // 円の半径
  final static Color bc = new Color(237,237,237);  // 背景色
  final static Color dc = Color.green;  // 描画色
/************************************************************************/
	static int BOARD_SIZE = 400;	//オセロ盤の大きさ
	static int BOARD_X = 30;			//オセロ盤の描画開始座標
	static int BOARD_Y = 110;
	
	//石の種類
	private static final int	TRANS = 0;	//空
	private static final int	BLACK = 1;	
	private static final int	WHITE = 2;
	
	JPanel p = new JPanel();
	
	private int masusize = 8;		//マスの数 デフォルト８×８
	private int[][] board = new int[masusize+2][masusize+2];	//ボード情報を格納する配列　描画用
	
	//履歴追加----------------------------------------//
	private int[][] history = new int[100][2];//履歴描画用
	private int time = 0;//指した回数保持用
	//------------------------------------------------//
	
	
  public Board() {
	  Master.init();
	  addMouseListener(this);
  }

  public void paintComponent(Graphics g) {
	  super.paintComponent(g);
	  int color,black=0,white=0;
	  int x,y;
	  int page = 0;
	  int player1,player2;
	  int masu = BOARD_SIZE / masusize;	//１マスの大きさ
	  String[] bnum = {"A","B","C","D","E ","F","G","H","I","J"};
	  String[] Pkind = {"　P1　","　P2　","CPlv."};
	  
	  page = Master.Get_Page();
	  player1 = Master.Get_CPlv(BLACK);
	  player2 = Master.Get_CPlv(WHITE);
	  
	  //アンチエイリアス処理
	  ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	  ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	  
	  //文字フォント指定
	  Font font1 = new Font("Arial", Font.BOLD, 30);
	  Font font2 = new Font("Arial", Font.BOLD, 15);
	  Font font3 = new Font("Arial", Font.BOLD, 50);
	  Font font4 = new Font("Arial", Font.BOLD, 25);
	  g.setFont(font1);
	  
	  //-----------------------------------オセロ盤表示--------------------------------------
	  //盤の色・場所・サイズ
	  g.setColor(Color.GREEN);
	  x = BOARD_X;
	  y = BOARD_Y;
	  g.fillRect(x,y,BOARD_SIZE,BOARD_SIZE);
	  
	  //ボード情報を取得・描画
	  Master.Get(board);
	  g.setColor(new Color(0,0,0));
	  for(int i=0; i<masusize; i++){
		//マス番号(縦)表示
		g.drawString(Integer.toString(i+1), x-masu/2, y+(i+1)*masu-15);
    	for(int j=0; j<masusize; j++){
    		//各マスの枠の表示
    		g.drawRect(x+i*masu,y+j*masu, masu, masu);
    		
    		//マス番号(横)表示
    		if(j==0){
    			g.drawString(bnum[i], x+i*masu+masu/3, y+j*masu - 5);
    		}
    		
    		//boardのデータ格納範囲はboard[1][1]~boad[8][8]
    		if(board[i+1][j+1] == 1 || board[i+1][j+1] == 2){
    		  if(board[i+1][j+1] == 1){
    			  color = 0;
    			  black++;
    		  }else{
    			  color = 255;
    			  white++;
    		  }
    		  
    		  //石の表示
    		  g.setColor(new Color(color,color,color));
    		  g.fillOval(x+i*masu+5, y+j*masu+5, masu-10, masu-10);
   			  g.setColor(new Color(0,0,0));
    		  g.drawOval(x+i*masu+5, y+j*masu+5, masu-10, masu-10);
    		}
    	}
	  }
	  
	  //-------------------------盤面下表示（手番・スコア)----------------------
	  g.setFont(font2);	  
	  x = BOARD_X + masu/2;
	  y = BOARD_Y + BOARD_SIZE + masu/4;
	  
	  //ページによって更新　画面一番下のボタン表示
	  if(page == 0){
		  g.drawString("ゲーム終了", x+masu, (int)(y+masu*2.6));
		  g.drawString(" まった！ ", x+masu*4, (int)(y+masu*2.6));
		  g.drawLine(x+masu-10, (int)(y+masu*2.7),x+masu*3-10, (int)(y+masu*2.7));
		  g.drawLine(x+masu*4-10, (int)(y+masu*2.7),x+masu*6-10, (int)(y+masu*2.7));
	  }else{
		  g.fillRect(x-20, (int)(y+masu*2.1),masu*7+40, (int)(masu*0.8));
		  g.setColor(new Color(255,255,255));
		  if(page == 1){
			  g.drawString("　終了する　", x+masu, (int)(y+masu*2.6));
			  g.drawString("　 続ける 　", x+masu*4, (int)(y+masu*2.6));
		  }else if(page == 2){
			  g.drawString("　終了する　", x+masu, (int)(y+masu*2.6));
			  g.drawString("　リトライ　", x+masu*4, (int)(y+masu*2.6));
		  }else if(page == 3){
			  g.drawString(" 同じレベル ", x+masu, (int)(y+masu*2.6));
			  if(Master.Get_Winner() == 0){
				  g.drawString(" 次のレベル ", x+masu*4, (int)(y+masu*2.6));
			  }else{
				  g.drawString(" 前のレベル ", x+masu*4, (int)(y+masu*2.6));
			  }
		  }
		  g.setColor(new Color(0,0,0));
	  }
	  
	  //手番枠表示
	  g.drawRect(x, y, masu*2, masu/2);
	  g.drawRect(x, y, masu*2, masu*2);
	  g.drawString("  手番", x+ masu/2, y+masu/2-5);
	  
	  //スコア枠表示
	  g.drawRect(x+masu*3, y, masu*4, masu/2);
	  g.drawRect(x+masu*3, y, masu*2, masu*2);
	  g.drawRect(x+masu*5, y, masu*2, masu*2);
	  if(player1 == 0){
		  g.drawString("　P1:黒　", (int)(x+masu*3.3), y+masu/2-5);
	  }else{
		  g.drawString("CPlv."+player1+":黒", (int)(x+masu*3.3), y+masu/2-5);
	  }
	  if(player2 == 0){
		  g.drawString("　P2:白　", (int)(x+masu*5.3), y+masu/2-5);
	  }else{
		  g.drawString("CPlv."+player2+":白", (int)(x+masu*5.3), y+masu/2-5);
	  }
	  
	  
	  //手番中身表示
	  g.setFont(font3);
	  if(Master.Get_Turn() == 1){
		  g.drawString("黒", x+masu/2, y+masu+30);
	  }else{
		  g.drawString("白", x+masu/2, y+masu+30);
	  }
	  
	  //スコア中身表示
	  int position;	//スコア表示の微調節用(2桁表示のときにずれないように)
	  if(black > 9)	position = 20;
	  else				position = 0;
	  g.drawString(Integer.toString(black), (int)(x+masu*3.7)-position, y+masu+30);
	  if(white > 9)	position = 20;
	  else				position = 0;
	  g.drawString(Integer.toString(white), (int)(x+masu*5.7)-position, y+masu+30);
	  
	  
	  //----------------------------盤面上表示----------------------------------------------
	  x = BOARD_X;
	  y = BOARD_Y - masu*2;
	  g.fillRect(x-masu/2, y, BOARD_SIZE+masu, (int)(masu*1.3));
	  g.setColor(new Color(255,255,255));
	  
	  if(page == 0){
	  /*プレイヤークラスのオブジェクトからint型で情報をもらえる関数player1.PCorUser(仮)をつくる
	  if(player1 == 0 && player2 == 0){
	  	g.drawString("   P1 vs P2   ", x+masu/2, y+masu);
	  }else if(player1 == 0 && player2 > 0){
	  	g.drawString("P1 vs CP", x+masu/2, y+masu);
	  	g.setFont(font1);
	  	g.drawString("(Lv." + player2 + ")", (int)(x+masu*5.4), y+masu);
	  }else if(player1 > 0 && player2 == 0){
	  	g.drawString("CP       vs P2", x+masu/2+2, y+masu);
	  	g.setFont(font1);
	  	g.drawString("(Lv." + player1 + ")", x+masu*2, y+masu);
	  }else if(player1 > 0 && player2 > 0){
	  	g.drawString("CP     vsCP", x+masu/2-18, y+masu);
	  	g.setFont(font1);
	  	g.drawString("Lv." + player1, x+masu*2-23, y+masu+7);
	  	g.drawString("Lv." + player2, x+masu*6, y+masu+7);
	  }
	  */
		  	g.drawString("CP     vs CP", x+masu/2-18, y+masu);
		  	g.setFont(font1);
		  	g.drawString("Lv." + 5, x+masu*2-23, y+masu+7);
		  	g.drawString("Lv." + 5, x+masu*6+15, y+masu+7);
	  
	  }else if(page == 2){
		  if(white < black){
			  g.drawString("黒の勝利", x+masu/2, y+masu);
		  }else{
			  g.drawString("白の勝利", x+masu/2, y+masu);
		  }
	  }
	  	//-----------------履歴表示---------------------------------//
		int hist = 0;
		int hist_x = 0;
		int hx,hy;				//直近の指し手表示用
	  	g.setFont(font4);  	//フォントサイズ:25
		g.setColor(new Color(0,0,0));
		history = Master.Get_History();
		time = Master.Get_Time();
		
		g.drawString("履歴",460,25);
		
		//履歴の枠
		g.drawRect(460, 2, 180, 660);
		g.drawRect(460, 50, 180, 612);
		g.drawRect(490, 28, 150, 634);
		g.drawRect(565, 28, 75, 634);

		g.setFont(font2);  //フォントサイズ:15
		if(player1 == 0){
		  g.drawString("　 ●P1 　", 490,49);
		}else{
		  g.drawString(" ●CPlv."+player1, 490,49);
		}
		if(player2 == 0){
			g.drawString("　 ○P2 　", 565,49);
		}else{
			g.drawString(" ○CPlv."+player2, 565,49);
		}
		
		for(int i=0;i<time;i++){
			if(i > time-2){
				g.setColor(new Color(255,0,0));
				g.setFont(font2);
				hx = BOARD_X + history[i][0] * masu - masu/2 - 8;
				hy = BOARD_Y + history[i][1] * masu - masu/3 + 2;
				g.drawString(Integer.toString((int)((time+1)/2)), hx, hy);
				g.setFont(font2);
			}
			if(i%2==0 && i/2+1<10){
				g.drawString(Integer.toString(i/2+1), 470,50+(i/2+1)*15);
			}
			else if(i%2==0 && i/2+1>=10){
				g.drawString(Integer.toString(i/2+1), 466,50+(i/2+1)*15);
			}
			for(int j=0;j<2;j++){
				//1Pの履歴
				if(i%2==0){
					hist_x = 549;
				}else if(i%2==1){
					hist_x = 624;
				}
				//列番号の表示
				if(j==0){
					hist = history[i][j];
					if(hist > 0)	g.drawString(bnum[hist-1],hist_x,50+(i/2+1)*15);
				}
				//行番号の表示
				else if(j==1){
                     if(history[i][j]==-1){
                    	 	g.drawString("パス", hist_x-19,50+(i/2+1)*15);
                      	}
						else{
							g.drawString(Integer.toString(history[i][j]),hist_x-11,50+(i/2+1)*15);
						}
				}
			}
		}
		//---------------------------------------------------------//
		Master.CPturn();
  }

  public void mouseClicked(MouseEvent e) { }
  public void mouseEntered(MouseEvent e) { }
  public void mouseExited(MouseEvent e) { }

  //どのマス/ボタンを押されたかをマスターに送る
  //(正の数,正の数)はマス番号、(-1,1or2）は下のボタン
  public void mousePressed(MouseEvent e){
    int x,y,i,j;
    int masu = BOARD_SIZE / masusize;
    
    	x = e.getX() - BOARD_X;
    	y = e.getY() - BOARD_Y;
    	
    	if(x > 0 && y > 0 && x < BOARD_SIZE && y < BOARD_SIZE){
    		i = x/masu + 1;
    		j = y/masu + 1;
    		Master.Send_Press(i,j,0);
    	}else{
    		y -= BOARD_SIZE;
    		if(x > masu*4.4 && x < masu*6 && y > masu*2.5 && y < masu*3){
    			Master.Send_Press(-1,1,0);
    		}else if(x > masu+20 && x < masu*3+5 && y > masu*2.5 && y < masu*3){
    			Master.Send_Press(-1,2,0);
    		}
    	}
/*    }else{
    	x = e.getX() - BOARD_X;
    	y = e.getY() - BOARD_Y;
		y -= BOARD_SIZE;
		if(x > masu*4.4 && x < masu*6 && y > masu*2.5 && y < masu*3){
			Master.Send_Press(-1, 1, 0);
		}else if(x > masu+20 && x < masu*3+5 && y > masu*2.5 && y < masu*3){
			Master.Send_Press(-1, 2, 0);
		}
    }
*/
  }
  
  public void mouseReleased(MouseEvent e) { }

}