package bankAtm;

import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class ATM {

	public static void main(String[] args) throws NoSuchAlgorithmException {
		
		Scanner scan = new Scanner(System.in);
		
		Bank theBank = new Bank("BOFA");
		
		User aUser = theBank.addUser("Ahmet", "Tachmuradov", "1234");
	
		Account newAccount = new Account("Checking", aUser, theBank);

		aUser.addAccount(newAccount);
		theBank.addAccount(newAccount);
		
		User curUser;
		while(true) {
			// stay in the login prompt until successful login
			curUser = ATM.mainMenuPrompt(theBank,scan);
			
			// stay in main menu until user quits
			ATM.printUserMenu(curUser,scan);
		}
	}
	private static User mainMenuPrompt(Bank theBank,Scanner sc) {
		String userID;
		String pin;
		User authUser;
		
		do {
			System.out.printf("\n\nWelcome to %s\n\n", theBank.getName());
			System.out.printf("Enter user ID: ");
			userID = sc.nextLine();
			System.out.printf("Enter pin: ");
			pin = sc.nextLine();
			
			authUser = theBank.userLogin(userID, pin);
			if(authUser == null)
				System.out.println("Incorrect user ID/pin combination. Please try again");
		}
		while(authUser == null);
		
		return authUser;
	}

	private static void printUserMenu(User theUser, Scanner sc) {
		theUser.printAccountSummary();
		
		int choice;
		
		do {
			System.out.printf("\nHello %s, what would you like to do?\n", theUser.getName());
			System.out.println("**********MENU************"
							+ "\n1 Show Account Transaction"
							+ "\n2 Withdraw"
							+ "\n3 Deposit"
							+ "\n4 Transfer"
							+ "\n5 Quit");
			choice = sc.nextInt();
			
			if(choice<1 || choice>5)
				System.out.println("Wrong input please select 1-5");
		}
		while(choice<1 || choice>5);
		
		switch(choice) {
		case 1: 
			ATM.showTransHistory(theUser, sc);
			break;
		case 2: 
			ATM.withdrawFunds(theUser,sc);
			break;
		case 3: 
			ATM.depositFunds(theUser,sc);
			break;
		case 4: 
			ATM.trasferFunds(theUser, sc);
			break;
		case 5:
			sc.nextLine();
			break;
		}
		if(choice!=5)
			ATM.printUserMenu(theUser, sc);
	}

	private static void depositFunds(User theUser, Scanner sc) {
		int toAcct;
		double amount;
		double accBalance;
		String memo;
		
		//get the account to transfer from
		
		do {
			System.out.printf("Enter the number (1-%d) of the account \nto deposit in : ",theUser.numAccounts());
			toAcct =sc.nextInt()-1;
			if(toAcct<0 || toAcct>= theUser.numAccounts())
				System.out.println("Invalid accountplease try again");
		}
		while(toAcct<0 || toAcct>= theUser.numAccounts());
		
		accBalance = theUser.getAccountBalance(toAcct);
		
		do {
			System.out.println("Enter the amount to deposit $");
			amount = sc.nextDouble();
			if(amount<0)
				System.out.println("Amount must be greater than zero");
		} while(amount < 0);
		
		// gobble up rest of previous input
		
		sc.nextLine();
		
		// get a memo
		System.out.print("Enter a memo");
		memo = sc.nextLine();
		
		// do withdraw
		theUser.addAccTransaction(toAcct, amount, memo);
		
	}

	private static void withdrawFunds(User theUser, Scanner sc) {
		int fromAcct;
		double amount;
		double accBalance;
		String memo;
		
		//get the account to transfer from
		
		do {
			System.out.printf("Enter the number (1-%d) of the account \nto withdraw from: ",theUser.numAccounts() );
			fromAcct =sc.nextInt()-1;
			if(fromAcct<0 || fromAcct>= theUser.numAccounts())
				System.out.println("Invalid accountplease try again");
		}
		while(fromAcct<0 || fromAcct>= theUser.numAccounts());
		
		accBalance = theUser.getAccountBalance(fromAcct);
		
		do {
			System.out.printf("Enter the amount to withdraw (max $%.02f): $", accBalance);
			amount = sc.nextDouble();
			if(amount<0)
				System.out.println("Amount must be greater than zero");
			else if (amount > accBalance)
				System.out.printf("Amount must not be greater than \n balance of $%.02f.\n", accBalance);
		} while(amount < 0 || amount > accBalance);
		
		// gobble up rest of previous input
		
		sc.nextLine();
		
		// get a memo
		System.out.println("Enter a memo");
		memo = sc.nextLine();
		
		// do withdraw
		theUser.addAccTransaction(fromAcct, -1*amount, memo);
	}

	private static void trasferFunds(User theUser, Scanner sc) {
		int fromAcct;
		int toAcct;
		double amount;
		double accBalance;
		
		//get the account to transfer from
		
		do {
			System.out.printf("Enter the number (1-%d) of the account \nto transfer from: ",theUser.numAccounts() );
			fromAcct =sc.nextInt()-1;
			if(fromAcct<0 || fromAcct>= theUser.numAccounts())
				System.out.println("Invalid accountplease try again");
		}
		while(fromAcct<0 || fromAcct>= theUser.numAccounts());
		
		accBalance = theUser.getAccountBalance(fromAcct);
		
		// acccount to transfer to
		
		do {
			System.out.printf("Enter the number (1-%d) of the account \nto transfer to: ",theUser.numAccounts() );
			toAcct =sc.nextInt()-1;
			if(toAcct<0 || toAcct>= theUser.numAccounts())
				System.out.println("Invalid accountplease try again");
		}
		while(toAcct<0 || toAcct>= theUser.numAccounts());
		
		// get the amount to transfer
		
		do {
			System.out.printf("Enter the amount to transfer (max $%.02f): $", accBalance);
			amount = sc.nextDouble();
			if(amount<0)
				System.out.println("Amount must be greater than zero");
			else if (amount > accBalance)
				System.out.printf("Amount must not be greater than \n balance of $%.02f.\n", accBalance);
		} while(amount < 0 || amount > accBalance);
		
		// do the transfer
		theUser.addAccTransaction(fromAcct, -1*amount, String.format("Transfer to account %s", theUser.getUUID(toAcct)));
		theUser.addAccTransaction(toAcct, amount, String.format("Transfer to account %s", theUser.getUUID(fromAcct)));
	}

	private static void showTransHistory(User theUser, Scanner sc) {
		int theAcct;
		
		do {
			System.out.printf("Enter the number (1-%d) of the account whose"
					+ " transactions you want to see: ", theUser.numAccounts());
			theAcct = sc.nextInt() -1 ;
			if(theAcct<0 || theAcct > theUser.numAccounts())
				System.out.println("Invalid account please try again");
		}
		while(theAcct<0 || theAcct > theUser.numAccounts());
		
		theUser.printAccountTransHistory(theAcct);
		
	}

	
}
