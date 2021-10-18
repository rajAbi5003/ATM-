package com.projectAtm.org;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Scanner;

public class OptionMenu extends Account
{
	static Scanner in = new Scanner(System.in);
	static DecimalFormat money = new DecimalFormat("##,###.##");
	static int click;
	static boolean flag=true;
	static String key;
	static String value;
	static boolean signal = true;
	static String userName;
	public static void getLogin() throws IOException,InterruptedException, SQLException
	{
		int x=1;
		do
		{
			try 
			{
				System.out.println("--------------------------------Welcome to ATM-----------------------------------------");
				System.out.println("1.Create an Account");
				System.out.println("2.Login to an existing account");
				click = in.nextInt();
				if(click==1)
					createAccount();
				else if(click==2)
				{
					System.out.println("Enter your customer Id : ");
					key=in.next();
					System.out.println("Enter your 4-Digit Pin Number :");
					value=in.next();
				}
				else
				{
					flag=false;
					break;
				}
			}
			catch(Exception e)
			{ 
				flag=false;
				x=2;
			}
			if(flag)
			{
				retrieveCustomerTable(key);
				if(signal==true)
				{
					System.out.println("Hello " + AccHolder + ",");
					System.out.println("Welcome to ATM ");
					getAccountType();
				}
			}
		}while(x==1);
	}
	
	public static final String UPDATE_QUERY = "update customerdetails set currentbalance=? , savingbalance=? where accountnumber=?";
	
	public static void updateBalanceCustomerTable() throws SQLException
	{
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys","root","root");;
			PreparedStatement update = conn.prepareStatement(UPDATE_QUERY);
			update.setDouble(1,currentBalance);
			update.setDouble(2,savingBalance);
			update.setString(3,key);
			update.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println("Error Occured!");
		}
	}
	
	public static final String QUERY = "select username,pinnumber,currentbalance,savingbalance from customerdetails where accountnumber=?";
	static String AccHolder;
	public static void retrieveCustomerTable(String AccNumber) throws SQLException
	{
		String pinnumber = null; 
		try
		{
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys","root","root");
		PreparedStatement receive = conn.prepareStatement(QUERY);
		receive.setString(1,AccNumber);
		ResultSet execute = receive.executeQuery();
		if(!execute.next())
        {
            System.out.println("Account not found!");
            signal=false;
            return;
        }
		execute = receive.executeQuery();
		while(execute.next())
		{
			AccHolder = execute.getString("username");
			pinnumber = execute.getString("pinnumber");
			currentBalance = execute.getDouble("currentbalance");
			savingBalance = execute.getDouble("savingbalance");
		}
		if(value.equals(pinnumber))
		{
			System.out.println("Login Successful:)");
			signal=true;
			return;
		}
		else
		{
			System.out.println("Password Incorrect!");
			signal=false;
			getLogin();
		}
		receive.close();
		conn.close();    
		}
		catch(Exception e)
		{
			System.out.println("Account not found or Password Incorrect!");
		}
	}
	
	static String num;
	static String pinNumberVerify;
	
	public static void createAccount() throws IOException, InterruptedException, SQLException 
	{
		System.out.println("Customer ID is being generated..");
		num=generateAccountNumber();
		System.out.println("Customer ID : " + num);
		System.out.println("Note this Id for future usage!!");
		System.out.println("User Name : ");
		userName = in.next();
		System.out.println("Forwarding to generate a new Pin Number!!");
		System.out.println("Enter a 4-Digit Pin Number: ");
		String pinNumber = in.next();
		int length = pinNumber.length();
		if(length!=4)
		{
			System.out.println("Pin Number : 4 Digits Only!! \n   Session Expired!");
			getLogin();
			return;
		}
		System.out.println("Re-Enter again: ");
		pinNumberVerify = in.next();
		int newLength = pinNumberVerify.length();
		if(newLength!=4)
		{
			System.out.println("Pin Number : 4 Digits Only!! \n   Session Expired!");
			getLogin();
			return;
		}
		else if(!pinNumber.equals(pinNumberVerify))
			{
				System.out.println("Pin Numbers doesn't match!! \n  Session Expired!");
				getLogin();
				return;
			}
		
		if(pinNumber.equals(pinNumberVerify))
		{
			System.out.println("Account has been successfully created!");
			updateCustomerTable(num,userName,pinNumberVerify,0,0);
			try 
			{
				getLogin();
			} 
			catch(IOException e) 
			{
				System.out.println("Error!");
			}
		}
		else
		{
			System.out.println("Pin Numbers doesn't match !!");
			System.out.println("Session Ended!!!");
			try 
			{
				getLogin();
			} 
			catch(IOException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	public static final String INSERT_USERS_SQL = "insert into customerdetails"+"(accountnumber,username,pinnumber,currentbalance,savingbalance) values"+"(?,?,?,?,?);";

	public static void updateCustomerTable(String AccNumber,String AccHolder,String PinNumber,double currentbal,double savebal) throws SQLException 
	{
		try
		{
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys","root","root");;
		PreparedStatement send = conn.prepareStatement(INSERT_USERS_SQL);
		send.setString(1,AccNumber);
		send.setString(2, AccHolder);
		send.setString(3,PinNumber);
		send.setDouble(4,currentbal);
		send.setDouble(5,savebal);
		
		send.executeUpdate();
		System.out.println("Account details stored in database successfully:)");
		}
		catch(Exception e)
		{
			System.out.println("Error");
		}
	}
	
	public static void getAccountType() throws SQLException 
	{
		System.out.println("---------------------------------------------DASH BOARD-----------------------------------------------------------");
		System.out.println("1.Current Account");
		System.out.println("2.Savings Account");
		System.out.println("3.Transfer Money Online");
		System.out.println("4.Exit");
		System.out.println("Enter your choice..");
		int choice=in.nextInt();
		switch(choice)
		{
			case 1:
				getCurrent();
				break;
			case 2:
				getSaving();
				break;
			case 3:
				transferMoneyOnline();
				break;
			case 4:
				updateBalanceCustomerTable();
				System.out.println("Thank You ! Visit Again!!");
				break;
			default:
				System.out.println("Invalid Choice");
				getAccountType();
				break;
		}
	}
		
	public static void getCurrent() throws SQLException 
	{
		System.out.println("---------------------------------------------Current Account-----------------------------------------------------------");
		System.out.println("1.View Balance ");
		System.out.println("2.Withdraw Funds ");
		System.out.println("3.Deposit Funds ");
		System.out.println("4.Back");
		
		System.out.println("Enter your option..");
		int option = in.nextInt();
		
		switch(option)
		{
			case 1:
				System.out.println("Current Account Balance : Rs." + money.format(getCurrentBalance()));
				getAccountType();
				break;
			case 2:
				getCurrentWithdrawInput();
				getAccountType();
				break;
			case 3:
				getCurrentDepositInput();
				getAccountType();
				break;
			case 4:
				getAccountType();
				break;
			default:
				System.out.println("Invalid Option!!");
				getCurrent();
				break;
		}
	}
				
	public static void getSaving() throws SQLException
	{
		System.out.println("--------------------------------------------Savings Account----------------------------------------------------------");
		System.out.println("1.View Balance ");
		System.out.println("2.Withdraw Funds ");
		System.out.println("3.Deposit Funds ");
		System.out.println("4.Back");
		
		System.out.println("Enter your option..");
		int option = in.nextInt();
		
		switch(option)
		{
			case 1:
				System.out.println("Saving Account Balance : Rs." + money.format(getSavingBalance()));
				getAccountType();
				break;
			case 2:
				getSavingWithdrawInput();
				getAccountType();
				break;
			case 3:
				getSavingDepositInput();
				getAccountType();
				break;
			case 4:
				getAccountType();
				break;
			default:
				System.out.println("Invalid Option!!");
				getCurrent();
				break;
		}
		
	}
	
	public static String TRANSFER_ACCOUNT_QUERY = "select currentbalance from customerdetails where accountnumber=?";
	static boolean transferSignal = true;
	public static void retrieveTransferAccountDetails(String AccNumber) throws SQLException
	{
		try
		{
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys","root","root");;
		PreparedStatement obtain = conn.prepareStatement(TRANSFER_ACCOUNT_QUERY);
		obtain.setString(1,AccNumber);
		ResultSet execute = obtain.executeQuery();
		if(!execute.next())
        {
            System.out.println("Account not found!");
            transferSignal=false;
            return;
        }
		execute = obtain.executeQuery();
		while(execute.next())
			transferAccountCurrentBalance = execute.getDouble("currentbalance");
		System.out.println("Process started..");
		obtain.close();
		conn.close();    
		}
		catch(Exception e)
		{
			System.out.println("Account Not Found!");
			getAccountType();
		}
	}
	
	public static final String UPDATE_TRANSFER_QUERY = "update customerdetails set currentbalance=? where accountnumber=?";
	
	public static void updateTransferBalanceCustomerTable() throws SQLException
	{
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sys","root","root");;
			PreparedStatement update = conn.prepareStatement(UPDATE_TRANSFER_QUERY);
			update.setDouble(1,transferAccountCurrentBalance);
			update.setString(2,transferAccountNum);
			update.executeUpdate();
		}
		catch(Exception e)
		{
			System.out.println("Error Occured!");
		}
	}
	
	static String transferAccountNum;
	public static void transferMoneyOnline() throws SQLException
	{
		System.out.println("Enter the account number to which you wish to transfer : ");
		transferAccountNum = in.next();
		retrieveTransferAccountDetails(transferAccountNum);
		if(transferSignal)
		{
			System.out.println("Enter the amount you wish to transfer(Rupees) : ");
			double amount = in.nextDouble();
			getCurrentTransferInput(amount);
			updateTransferBalanceCustomerTable();
		}
		getAccountType();
	}
}
