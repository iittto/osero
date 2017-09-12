package reversi2;

//package reversi2;

import java.awt.event.* ;
import javax.swing.* ;
import java.awt.Graphics ;
import java.awt.Color ;

public class osero {
public osero() {
	}
	
	public static void main(String[] args) {

  JFrame frame = new JFrame();
  frame.setSize(650, 700);
  frame.setTitle("りばーし");
//  JLabel label1 = new JLabel("今日の日付：");
//  label1.setFont(new Font("ＭＳ ゴシック", Font.BOLD, 12));
//  setBounds(100, 100, 300, 250);
//  frame.add(label1);
  frame.addWindowListener(new WindowAdapter() { 
    public void windowClosing(WindowEvent e) { System.exit(0); }
  });
  frame.getContentPane().add( new Board() );
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