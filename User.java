package bankAtm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class User {
	private String firstName;
	private String lastName;
	// MD5 hash of the user
	private String uuid;
	private byte pinHash[];
	private ArrayList<Account>accounts;
	public User(String firstName, String lastName, String pin, Bank theBank) throws NoSuchAlgorithmException {
		
		this.firstName = firstName;
		this.lastName = lastName;
		
		// store the pin's MD5 hash, rather than the original value for security reasons
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			this.pinHash = md.digest(pin.getBytes());
		}
		catch(NoSuchAlgorithmException e){
			System.err.println("error, caught NoSuchAlgorithmException");
			e.printStackTrace();
			System.exit(1);
		}
		
		// get a new unique universal ID for the user
		this.uuid = theBank.getNewUserUUID();
		
		//create empty list of accounts
		this.accounts = new ArrayList<Account>();
		
		// print log message
		System.out.printf("New User %s, %s with ID %s created.\n",lastName,firstName,this.uuid);
	}
	public void addAccount(Account account) {
		this.accounts.add(account);
		
	}
	public String getUUID() {
		return this.uuid;
	}
	
	public boolean validate(String pin) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return MessageDigest.isEqual(md.digest(pin.getBytes()), this.pinHash);
		}
		catch(NoSuchAlgorithmException e){
			System.err.println("error, caught NoSuchAlgorithmException");
			e.printStackTrace();
			System.exit(1);
		}
		return false;
	}
	public String getName() {
		
		return this.firstName +" " + this.lastName;
	}
	public void printAccountSummary() {
		System.out.printf("\n\n%s's account summary: \n", this.getName());
		for (int i = 0; i < this.accounts.size(); i++) {
			System.out.printf("\n%d) %s",  i+1, this.accounts.get(i).getSummaryLine());
			
		}
		
	}
	public int numAccounts() {
		
		return this.accounts.size();
	}
	// print the history
	public void printAccountTransHistory(int accountIndex) {
		
		this.accounts.get(accountIndex).printHistory();
	}
	// print the balnace
	public double getAccountBalance(int accIndex) {
		
		return this.accounts.get(accIndex).getBalance();
	}
	// print the UUID
	public String getUUID(int acctIndex) {
		
		return this.accounts.get(acctIndex).getUUID();
	}
	// print the transaction
	public void addAccTransaction(int accIndex, double amount, String memo) {
		this.accounts.get(accIndex).addTransaction(amount,memo);
	}
	
	
}
