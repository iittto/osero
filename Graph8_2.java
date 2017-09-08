import java.awt.event.* ;
import javax.swing.* ;
import java.awt.Graphics ;
import java.awt.Color ;

public class Graph8_2 extends JPanel implements MouseListener {
  final static int lim = 256*2;//256だと追い越され得るから
  static int[] bX = new int[lim]; //位置情報格納配列
  static int[] bY = new int[lim]; //位置情報格納配列
  static int[] bColor = new int[lim]; //色　情報格納配列
  static int sta = 0; //配列最前列位置（後に描画）
  static int end = 0; //配列最後尾位置（先に描画）
  int r = 30;                           // 円の半径
  final static Color bc = new Color(237,237,237);  // 背景色
  final static Color dc = Color.green;  // 描画色
	
	static int BOARD_SIZE = 400;
	static int MASU_SIZE = 50;
	
	private static final int	TRANS = 0;
	private static final int	BLACK = 1;
	private static final int	WHITE = 2;
	
	private int masusize = 8;
	private int flag;
	private int pushx;
	private int pushy;
	

	private int[][] board = new int[masusize+2][masusize+2];
	
  public Graph8_2() {
    addMouseListener(this);
    setBackground(bc);
/*   	for(int i=0; i<masusize; i++){
   		for(int j=0; j<masusize; j++){
   			board[i][j] = 0;
   		}
   	}
   	
   	board[masusize/2][masusize/2] = 1;
   	board[masusize/2-1][masusize/2] = 2;
   	board[masusize/2][masusize/2-1] = 2;
   	board[masusize/2-1][masusize/2-1] = 1;
   	
   	flag = 1;
*/
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
		int color = 0;
		g.setColor(Color.GREEN);
    g.fillRect(0,0,BOARD_SIZE,BOARD_SIZE);
    
    Board_Data.Get(board);
    
    for(int i=0; i<masusize; i++){
    	for(int j=0; j<masusize; j++){
    	  g.setColor(new Color(0,0,0));
    		g.drawRect(i*MASU_SIZE,j*MASU_SIZE,MASU_SIZE,MASU_SIZE);
    		
    		//boardのデータ格納範囲はboard[1][1]~boad[8][8]
    		if(board[i+1][j+1] == 1 || board[i+1][j+1] == 2){
    		  if(board[i+1][j+1] == 1)	color = 0;
    		  else											color = 255;
    		  
    		  g.setColor(new Color(color,color,color));
          g.fillOval(i*MASU_SIZE+5,j*MASU_SIZE+5,40,40);
          g.setColor(new Color(0,0,0));
          g.drawOval(i*MASU_SIZE+5,j*MASU_SIZE+5,40,40);
        }
      }
    }
		
//		setBounds(600, 100, 600, 500);

/*
    if(bX[end]>=0) {
      if(sta != end && bColor[sta] == 237){bColor[sta] = 238;sta++;}
      if(sta >= lim ){sta = 0;}
        int i = sta;			
        while(true){
          if(bColor[i] < 237){
          bColor[i]++;
          g.setColor(new Color(bColor[i],bColor[i],bColor[i]));
          g.fillOval(bX[i]-r,bY[i]-r,r*2,r*2);
        }
        if(i == end){break;}
        i++;
        if(i >= lim){i = 0;}
      }
    }
*/
  }

  public void mouseClicked(MouseEvent e) { }
  public void mouseEntered(MouseEvent e) { }
  public void mouseExited(MouseEvent e) { }
  
  public void mousePressed(MouseEvent e) {
    int i,j;
    if(e.getX() < BOARD_SIZE && e.getY() < BOARD_SIZE){
	    i = e.getX()/50 + 1;
	    j = e.getY()/50 + 1;
	    Master.send_Press(i,j);
		}
  }
  
  public void mouseReleased(MouseEvent e) { }
  public static void main(String[] args) {
    
    Board_Data.init();
    Master.init();
    
    JFrame frame = new JFrame();
    frame.setTitle("りばーし");
    JPanel p = new JPanel();
		JLabel label1 = new JLabel("今日の天気：");
  	JLabel label2 = new JLabel("やっほー");
		p.add(label1);
    p.add(label2);
    frame.addWindowListener(new WindowAdapter() { 
      public void windowClosing(WindowEvent e) { System.exit(0); }
    });
    frame.getContentPane().add( new Graph8_2() );
    frame.setSize(500, 500);
    frame.setVisible(true);
       
    while(true){
      try{
        Thread.sleep(100);
      }catch(InterruptedException ee){
      }
      frame.repaint();
    }
  }
}
