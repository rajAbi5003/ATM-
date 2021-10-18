package com.projectAtm.org;

import java.text.DecimalFormat;
import java.util.Scanner;

public class Account 
{
	static Scanner input = new Scanner(System.in);
	static DecimalFormat moneyFormat = new DecimalFormat("##,###.##");
	private static String customerID;
	private static String customerPin;
	public static double currentBalance;
	public static double savingBalance;
	
	public static String generateAccountNumber() 
	{
		int idCustomer = (int)(Math.random() * 505452 + 165465);
		String convert = String.valueOf(idCustomer);
		return convert;
	}

	public static double getSavingBalance()
	{
		return savingBalance;
	}
	
	public static double getCurrentBalance()
	{
		return currentBalance;
	}
	
	public static double calcCurrentWithdraw(double amount)
	{
		currentBalance = currentBalance-amount;
		return currentBalance;
	}
	
	public static double calcSavingWithdraw(double amount) 
	{
		savingBalance = savingBalance-amount;
		return savingBalance;
	}
	
	public static double calcCurrentDeposit(double amount) 
	{
		currentBalance = currentBalance + amount;
		return currentBalance;	
	}
	
	public static double calcSavingDeposit(double amount) 
	{
		savingBalance = savingBalance + amount;
		return savingBalance;		
	}
	
	static double transferAccountCurrentBalance;
	public static void getCurrentTransferInput(double amount)
	{
		if(currentBalance-amount >= 0)
		{
			System.out.println("Amount Transfer is being processed!");
			currentBalance=currentBalance-amount;
			transferAccountCurrentBalance=transferAccountCurrentBalance+amount;
			System.out.println("Transfer Successfull!");
			System.out.println("Updated Current Account Balance : Rs." + currentBalance);
		}
		else
			System.out.println("Insufficient Current Balance!");
	}
	
	public static void getCurrentWithdrawInput() 
	{
		System.out.println("Current Account Balance: Rs."+ moneyFormat.format(currentBalance));
		System.out.println("Amount you want to withdraw from current account(Rupees): ");
		double amount = input.nextDouble();
		
		if(currentBalance-amount >= 0)
		{
			double checkBalance = calcCurrentWithdraw(amount);
			System.out.println("Updated Current Account Balance : Rs." + checkBalance);
		}
		else
			System.out.println("Insufficient Balance!");
	}
	
	public static void getCurrentDepositInput() 
	{
		System.out.println("Current Account Balance: Rs."+ moneyFormat.format(currentBalance));
		System.out.println("Amount you want to deposit in current account(Rupees): ");
		double amount = input.nextDouble();
		
		if(currentBalance+amount >= 0)
		{
			double checkBalance = calcCurrentDeposit(amount);
			System.out.println("Updated Current Account Balance : Rs." + moneyFormat.format(checkBalance));
		}
	}
	
	public static void getSavingDepositInput()
	{
		System.out.println("Saving Account Balance : Rs." + moneyFormat.format(savingBalance));
		System.out.println("Amount you want to deposit in savings account(Rupees): ");
		double amount = input.nextDouble();
		
		if(savingBalance+amount >= 0)
		{
			double saveBalance = calcSavingDeposit(amount);
			System.out.println("Updated Savings Account Balance: Rs." + moneyFormat.format(saveBalance));		
		}
	}
	
	public static void getSavingWithdrawInput() 
	{
		System.out.println("Saving Account Balance: Rs."+ moneyFormat.format(savingBalance));
		System.out.println("Amount you want to withdraw from savings account(Rupees): ");
		double amount = input.nextDouble();
		
		if(savingBalance-amount >= 0)
		{
			double saveBalance = calcSavingWithdraw(amount);
			System.out.println("Updated Savings Account Balance : Rs." + moneyFormat.format(saveBalance));
		}
		else
			System.out.println("Insufficient Balance!");
	}
}
