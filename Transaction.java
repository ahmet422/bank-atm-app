package bankAtm;

import java.util.Date;

public class Transaction {
	private double amount;
	private Date timestap;
	private String memo;
	private Account inAccount;
	
	public Transaction(double amount, String memo, Account inAccount) {
		
		// call the two argument ctor first
		this(amount, inAccount);
		
		// set the memo
		this.memo = memo;
	}
	public Transaction(double amount, Account inAccount) {

		this.amount = amount;
		this.inAccount = inAccount;
		this.memo = "";
		this.timestap = new Date();
	}
	
	public double getAmount() {
		return this.amount;
	}
	public String getSummaryLine() {
		if(this.amount >= 0) {
			return String.format("%s : $%.02f : %s", this.timestap.toString(), this.amount, this.memo);
		}else {
			return String.format("%s : $(%.02f) : %s", this.timestap.toString(), -this.amount, this.memo);
		}
		
	}

}
