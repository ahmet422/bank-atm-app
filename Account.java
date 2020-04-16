package bankAtm;

import java.util.ArrayList;

public class Account {
	private String name;
	private String uuid;
	private User holder;
	private ArrayList<Transaction> transactions;
	public Account(String name, User holder, Bank theBank) {
		
		this.name = name;
		this.holder = holder;
		this.uuid = theBank.getNewAccountUUID();
		
		// init transactions
		this.transactions = new ArrayList<Transaction>();

	}
	public String getUUID() {
		// TODO Auto-generated method stub
		return this.uuid;
	}
	public String getSummaryLine() {
		//get the balance
		double balance = this.getBalance();
		
		// format summary line depending on whether the balance is negative
		if(balance >= 0) {
			return String.format("%s : $%.02f : %s", this.name,balance,this.uuid);
		} else {
			return String.format("%s : $%(.02f) : %s", this.name,balance,this.name);
		}
	}
	double getBalance() {
		double balance = 0;
		for (Transaction t : this.transactions) {
			balance = balance + t.getAmount();
			
		}
		return balance;
	}
	public void printHistory() {
		System.out.printf("\nTransaction history for account %s\n",this.uuid);
		for(int t = this.transactions.size()-1; t >= 0; t-- ){
			System.out.println(this.transactions.get(t).getSummaryLine());
		}
		System.out.println();
		
	}
	public void addTransaction(double amount, String memo) {
		
		Transaction tr = new Transaction(amount,memo,this);
		this.transactions.add(tr);
		
	}

	
}
