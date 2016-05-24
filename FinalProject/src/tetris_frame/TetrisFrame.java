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
package tetris_frame;
import java.awt.Color;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Observable;
import java.util.Observer;
import java.awt.event.ItemEvent;
import javax.swing.JFrame;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import tetris_panel.TetrisPanel;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;


public class TetrisFrame extends JFrame implements ActionListener
{
	private String username;
	
	private JMenu fileMenu;
	private JMenuItem startItem;
	private JMenuItem exitItem;
	
	private String HOST;
	
	private JMenu helpMenu;
	private JMenuItem aboutItem;
	private JMenuItem helpItem;
	
	private JMenu userMenu;
	
	private JMenuItem leaderboardItem;
	
	private JMenuBar bar;
	
	private TetrisPanel tetrisPanel;
	
	private boolean login;
	
	private int bestscore;
	
	public TetrisFrame(String host, String username,int bestscore) 
	{
		super( "Tetris Login["+ username + "]");	  
		this.bestscore = bestscore;
		this.username = username;
		this.HOST = host;
		tetrisPanel = new TetrisPanel();
		tetrisPanel.setInformation(HOST,username,bestscore);
		
		this.add(tetrisPanel,BorderLayout.CENTER);
		
		login = false;
		
		fileMenu = new JMenu( "File" );
		startItem = new JMenuItem( "New Game" ); 
		fileMenu.add( startItem ); 
		startItem.addActionListener(this); 
		exitItem = new JMenuItem( "Exit" ); 
		fileMenu.add( exitItem ); 
		exitItem.addActionListener(this); 
		
		helpMenu = new JMenu( "Help" ); 
		aboutItem = new JMenuItem( "About..." );
		helpMenu.add( aboutItem ); 
		aboutItem.addActionListener(this); 
		helpItem = new JMenuItem( "Control" );
		helpMenu.add( helpItem ); 
		helpItem.addActionListener(this); 
		
		userMenu = new JMenu( "Score" );
		
		leaderboardItem = new JMenuItem("Leaderboard");
		leaderboardItem.addActionListener(this); 
		userMenu.add(leaderboardItem);
		
		bar = new JMenuBar(); 	
		bar.add( fileMenu ); 
		bar.add(userMenu);
		bar.add( helpMenu ); 
		this.setJMenuBar(bar);
}

	
	
	public static void main( String[] args ) throws SQLException
	{ 
		TetrisFrame tetrisFrame = new TetrisFrame("localhost","1234",100); 
		tetrisFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		tetrisFrame.pack();
		tetrisFrame.setVisible(true);
		tetrisFrame.setResizable(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource()==exitItem)
		{
			System.exit(0);
		} else 
		if (e.getSource()==startItem)
		{
			tetrisPanel.gamestart();
		} else
		if (e.getSource()==aboutItem)
		{		
			JOptionPane.showMessageDialog( TetrisFrame.this,
					"This is a Tetris game made by\nJamesir Yao and Jerry Shin",
					"About", JOptionPane.PLAIN_MESSAGE );
		} else
		if (e.getSource()==helpItem)
		{
			JOptionPane.showMessageDialog( TetrisFrame.this,
					"Up for Rotate\nLeft/Right for Move\nP/Space for Pause\na/s for Speed",
					"Keyboard Control",
					JOptionPane.PLAIN_MESSAGE );
		} else
		if (e.getSource()==leaderboardItem)
		{
			String nameString[] = new String[10];
			String scoreString[] = new String[10];
			Connection myConn = null;
			Statement myStmt = null;
			ResultSet myRs = null;
			try {
				myConn = DriverManager.getConnection("jdbc:mysql://"+HOST+":3306/tetris", "tetris" , "tetris");
				System.out.println("Database connection successful!\n");
				myStmt = myConn.createStatement();
				myRs = myStmt.executeQuery("select * from score order by score desc");
				int a=0;
				while (myRs.next() && a<10) 
				{
					login = true;
					nameString[a] = myRs.getString("username");
					scoreString[a] = myRs.getString("score");
					System.out.println(myRs.getString("username")+" "+myRs.getString("score"));
					a++;
				} 
				String str = "";
				for (int i=0;i<a;i++)
				str = str + nameString[i] + " : "+ scoreString[i] + "\n";
				JOptionPane.showMessageDialog( TetrisFrame.this,
						str,
						"Leaderboard", JOptionPane.PLAIN_MESSAGE );
			}
			catch (Exception exc) 
			{
				exc.printStackTrace();
			}
		} 
		
	}

} 

