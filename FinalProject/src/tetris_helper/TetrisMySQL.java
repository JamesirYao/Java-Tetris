package tetris_helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JOptionPane;

import tetris_frame.LoginFrame;
import tetris_frame.TetrisFrame;

public class TetrisMySQL 
{
	public TetrisMySQL()
	{
		
	}
	
	public int login(String HOST,String username,String password)
	{
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		try {
			myConn = DriverManager.getConnection("jdbc:mysql://"+HOST+":3306/tetris", "tetris" , "tetris");
			System.out.println("Database connection successful!\n");
			myStmt = myConn.createStatement();
			myRs = myStmt.executeQuery("select * from score where username = '"+username+
					"' and password = '" + password + "'");
			
			if (myRs.next()) 
			{
				int score = myRs.getInt("score");
				return score;
			} else
			{
				return -1;
			}
		}
		catch (Exception exc) 
		{
			exc.printStackTrace();
		}
		return -1;
	}
	
	public boolean register(String HOST,String username,String password)
	{
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		try {
			myConn = DriverManager.getConnection("jdbc:mysql://"+HOST+":3306/tetris", "tetris" , "tetris");
			System.out.println("Database connection successful!\n");
			myStmt = myConn.createStatement();
			myStmt.executeUpdate("insert into tetris.score value('"+username+"','"+password+"','0');");
			return true;
		}
		catch (Exception exc) 
		{
			exc.printStackTrace();
		}
		return false;
	}
	
	public String showLeaderBoard(String HOST)
	{
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		String nameString[] = new String[10];
		String scoreString[] = new String[10];
		try {
			myConn = DriverManager.getConnection("jdbc:mysql://"+HOST+":3306/tetris", "tetris" , "tetris");
			System.out.println("Database connection successful!\n");
			myStmt = myConn.createStatement();
			myRs = myStmt.executeQuery("select * from score order by score desc");
			int a=0;
			while (myRs.next() && a<10) 
			{
				nameString[a] = myRs.getString("username");
				scoreString[a] = myRs.getString("score");
				System.out.println(myRs.getString("username")+" "+myRs.getString("score"));
				a++;
			} 
			String str = "";
			for (int i=0;i<a;i++)
			str = str + nameString[i] + " : "+ scoreString[i] + "\n";
			return str;
		}
		catch (Exception exc) 
		{
			exc.printStackTrace();
		}
		return "ERROR!";
	}
	
	public void updateScore(String HOST,String username,int bestscore)
	{
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
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
}
