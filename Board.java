//package reversi2;

import java.awt.event.* ;
import javax.swing.* ;
import java.awt.Graphics ;
import java.awt.Color ;

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
	static int BOARD_X = 50;			//オセロ盤の描画開始座標
	static int BOARD_Y = 100;
	
	//石の種類
	private static final int	TRANS = 0;	//空
	private static final int	BLACK = 1;	
	private static final int	WHITE = 2;
	
	JPanel p = new JPanel();
	
	private int masusize = 8;			//マスの数 デフォルト８×８
//	private int flag;
//	private int pushx;
//	private int pushy;
	
	private int[][] board = new int[masusize+2][masusize+2];	//ボード情報を格納する配列　描画用
	
  public Board() {
	  Master.init();
    addMouseListener(this);
//    setBackground(bc);
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
		int color = 0;
		int masu = BOARD_SIZE / masusize;	//１マスの大きさ
		
		g.setColor(Color.GREEN);
    g.fillRect(BOARD_X,BOARD_Y,BOARD_SIZE,BOARD_SIZE);
    
    Master.Get(board);
    
    for(int i=0; i<masusize; i++){
    	for(int j=0; j<masusize; j++){
    	  g.setColor(new Color(0,0,0));
    		g.drawRect(BOARD_X + i*masu,BOARD_Y + j*masu,masu,masu);
    		
    		//boardのデータ格納範囲はboard[1][1]~boad[8][8]
    		if(board[i+1][j+1] == 1 || board[i+1][j+1] == 2){
    		  if(board[i+1][j+1] == 1)	color = 0;
    		  else											color = 255;
    		  
    		  g.setColor(new Color(color,color,color));
    		  g.fillOval(BOARD_X + i*masu,BOARD_Y + j*masu,masu,masu);
    		  g.setColor(new Color(0,0,0));
    		  g.drawOval(BOARD_X + i*masu,BOARD_Y + j*masu,masu,masu);
    		}
    	}
    }
  }

  public void mouseClicked(MouseEvent e) { }
  public void mouseEntered(MouseEvent e) { }
  public void mouseExited(MouseEvent e) { }
  
  public void mousePressed(MouseEvent e) {
    int x,y,i,j;
    int masu = BOARD_SIZE / masusize;
    
    x = e.getX() - BOARD_X;
    y = e.getY() - BOARD_Y;
    if(x > 0 && y > 0 && x < BOARD_SIZE && y < BOARD_SIZE){
	    i = x/masu + 1;
	    j = y/masu + 1;
	    Master.Send_Press(i,j);
		}else{
			Master.Send_Press(-1,-1);
		}
  }
  
  public void mouseReleased(MouseEvent e) { }

}
