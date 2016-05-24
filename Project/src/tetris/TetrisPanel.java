//                         Tetris Game By 
//                    Jamesir Yao and Jerry Shin
//                            _ooOoo_  
//                           o8888888o  
//                           88" . "88  
//                           (| -_- |)  
//                            O\ = /O  
//                        ____/`---'\____  
//                      .   ' \\| |// `.  
//                       / \\||| : |||// \  
//                     / _||||| -:- |||||- \  
//                       | | \\\ - /// | |  
//                     | \_| ''\---/'' | |  
//                      \ .-\__ `-` ___/-. /  
//                   ___`. .' /--.--\ `. . __  
//                ."" '< `.___\_<|>_/___.' >'"".  
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |  
//                 \ \ `-. \_ __\ /__ _/ .-` / /  
//         ======`-.____`-.___\_____/___.-`____.-'======  
//                            `=---='  
//                         佛祖保佑 永无BUG             
//         .............................................  
package tetris;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;


public class TetrisPanel extends JPanel implements Observer 
{
	private String HOST;
	private String username;
	private int bestscore;
	
	private TetrisControl control = null;
	private TetrisModel model = null;

	private JFrame mainFrame;
	private Canvas paintCanvas;
	private JLabel labelScore;
	private static final int nodeWidth = 21;
	private static final int nodeHeight = 21;
	private int canvasWidth ;
	private int canvasHeight ;
	
	private boolean started;
	private Thread game;
	
	public TetrisPanel()
	{
		super();
		this.setLayout(new BorderLayout());
		started = false;
		
		model = new TetrisModel(11, 19);
		model.addObserver(this);
		control = new TetrisControl(model);
		this.addKeyListener(control);
		
	    labelScore = new JLabel("   ");
	    labelScore.setHorizontalAlignment(SwingConstants.CENTER);
	    this.add(labelScore, BorderLayout.NORTH);

	    canvasWidth = (model.maxX+1) * nodeWidth ;
	    canvasHeight = (model.maxY+1) * nodeWidth ;
	    
	    paintCanvas = new Canvas();
	    paintCanvas.setSize(canvasWidth, canvasHeight);
	    paintCanvas.addKeyListener(control);
		this.setBackground(Color.WHITE);
		
	    this.add(paintCanvas, BorderLayout.CENTER);
	}

	public void gamestart()
	{
		if (!started)
		{
			this.setFocusable(true);
			game = new Thread(model);
			game.start();
			started = true;
		} else
		{
			model.reset();
		}
		
	}
	
	public void setInformation(String host,String username,int bestscore)
	{
		this.HOST = host;
		this.username = username;
		this.bestscore = bestscore;
		
		System.out.println(bestscore);
	}
	
	public void paint() 
	{
		Graphics g = paintCanvas.getGraphics();
		int i,j;
		
		for (i=1;i<model.maxX;i++)
			for (j=1;j<model.maxY;j++)
			{
				g.setColor(blockColor(model.map[i][j]));
				drawNode(g, i, j);
			}
		
	    g.setColor(blockColor(model.getKind()));
	    print(g,model.getKind(),model.getX(),model.getY());
	    
	    g.setColor(Color.DARK_GRAY);
		for (i=0;i<=model.maxX;i++)
		{
			drawNode(g,i,0);
			drawNode(g,i,model.maxY);
		}
		for (i=0;i<=model.maxY;i++)
		{
			drawNode(g,0,i);
			drawNode(g,model.maxX,i);
		}
	    updateScore();
	}

	private static final int[][][] BLOCK=
	{
		{},
		{{0,0,1},{0,0,2},{1,0,-1},{0,0,0}},
		{{0,0,0},{0,-1,0},{0,2,0},{0,1,0}},
		{{0,0,0},{0,1,0},{0,2,0},{0,0,1}},
		{{0,-1,0},{0,0,0},{0,0,1},{0,0,2}},
		{{0,0,0},{0,-1,0},{0,-2,0},{1,0,-1}},
		{{1,0,-1},{2,0,-2},{0,0,0},{0,1,0}},
		{{0,-1,0},{0,-2,0},{0,0,0},{0,0,1}},
		{{1,0,-1},{2,0,-2},{0,0,0},{0,-1,0}},
		{{0,1,0},{0,2,0},{0,0,0},{1,0,-1}},
		{{0,1,0},{0,0,0},{0,0,1},{0,0,2}},
		{{0,-1,0},{0,0,0},{0,1,0},{0,0,1}},
		{{0,-1,0},{0,0,0},{0,0,1},{1,0,-1}},
		{{1,0,-1},{0,1,0},{0,-1,0},{0,0,0}},
		{{0,1,0},{0,0,0},{0,0,1},{1,0,-1}},
		{{0,1,0},{0,0,1},{0,1,1},{0,0,0}},
		{{1,-1,0},{0,0,0},{0,0,1},{0,1,1}},
		{{0,-1,0},{1,0,-1},{0,-1,1},{0,0,0}},
		{{0,1,0},{0,0,1},{0,-1,1},{0,0,0}},
		{{0,1,0},{0,0,0},{1,0,-1},{0,1,1}}
	};
	
	private void print(Graphics g,int kind,int x,int y)
	{
		for (int i =0;i<4;i++)
			drawNode(g,x+BLOCK[kind][i][1],y+BLOCK[kind][i][2]);
	}
	
	private Color blockColor(int kind)
	{
		Color c = Color.WHITE;
		switch(kind)
		{
			case 1: case 2:
				c = Color.magenta;
				break;
			case 3: case 4: case 5: case 6:
				c = Color.red;
				break;
			case 7: case 8: case 9: case 10:
				c = Color.yellow;
				break;
			case 11: case 12: case 13: case 14:
				c = Color.orange;
				break;
			case 15:
				c = Color.PINK;
				break;
			case 16: case 17:
				c = Color.GREEN;
				break;
			case 18: case 19:
				c = Color.BLUE;
				break;
		}
		return c;
	}
	
	private void drawNode(Graphics g,int x,int y) 
	{
		if ((g.getColor()==Color.darkGray)||(y>0))
		g.fillRoundRect(x * nodeWidth,y * nodeHeight,nodeWidth - 1,nodeHeight - 1,4,4);
	}

	public void updateScore() 
	{
		if (model.getScore()>bestscore) 
		{
			Connection myConn = null;
			Statement myStmt = null;
			ResultSet myRs = null;
			bestscore = model.getScore();
			try {
				myConn = DriverManager.getConnection("jdbc:mysql://"+HOST+":3306/tetris", "tetris" , "tetris");
				System.out.println("Database connection successful!\n");
				myStmt = myConn.createStatement();
				myStmt.executeUpdate("update score set score='"+bestscore+"' where username = '"+username+"';");
			}
			catch (Exception exc) 
			{
				exc.printStackTrace();
			}
		}
		String s = "Best: "+ bestscore +"  Score: " + model.getScore();
	    labelScore.setText(s);
	}

	public void update(Observable o, Object arg) 
	{
		paint();
	}
	
	public static void main( String[] args )
	{ 
		JFrame frame = new JFrame(); 
		TetrisPanel tp = new TetrisPanel();
		frame.add(tp);
		tp.gamestart();
	    frame.pack();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setVisible(true);
	    frame.setResizable(false);
	} 

}
