import java.util.Scanner;
public class Board{
				
				public Board(){
				}
        public static void init(){
            for(int i=0;i<L;i++){
                board[0][i] =
                board[9][i] = WALL;
            }
            for(int i=1;i<L-1;i++){
                board[i][0] =
                board[i][9] = WALL;
            }
            board[4][4] =
            board[5][5] = WHITE;
            board[4][5] =
            board[5][4] = BLACK;
        }

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
    }
