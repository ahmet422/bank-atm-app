package bankAtm;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Random;

public class Bank {
	private String name;
	private ArrayList<User> users;
	private ArrayList<Account>accounts;

	public Bank(String name) {
		this.name = name;
		this.users = new ArrayList<User>();
		this.accounts = new ArrayList<Account>() ;
	}

	public String getNewUserUUID() {
		String uuid;
		Random randomNum = new Random();
		int length = 6;
		boolean nonUnique;
		
		do {
			uuid = "";
			for (int i = 0; i < length; i++) {
				uuid += ((Integer)randomNum.nextInt(10)).toString();
			}
			nonUnique = false;
			for (User u : this.users) {
				if(uuid.compareTo(u.getUUID()) == 0){
					nonUnique = true;
					break;
				}
			}
		}
		while(nonUnique);
		return uuid;
	}
	
	public String getNewAccountUUID() {
		
		String uuid;
		Random randomNum = new Random();
		int length = 10;
		boolean nonUnique;
		
		do {
			uuid = "";
			for (int i = 0; i < length; i++) {
				uuid += ((Integer)randomNum.nextInt(10)).toString();
			}
			nonUnique = false;
			for (Account acc : this.accounts) {
				if(uuid.compareTo(acc.getUUID()) == 0){
					nonUnique = true;
					break;
				}
			}
		}
		while(nonUnique);
		
	return uuid;
	}

	public void addAccount(Account account) {
		this.accounts.add(account);
		
	}
	
	public User addUser(String firstName, String lastName, String pin) throws NoSuchAlgorithmException {
		// create new user
		User newUser = new User(firstName, lastName, pin, this);
		this.users.add(newUser);
		
		// create savings account for the new user
		Account savingAccount = new Account("Saving", newUser, this);
		
		newUser.addAccount(savingAccount);
		this.addAccount(savingAccount);
		return newUser;
	}
	
	public User userLogin(String userID, String pin) {
		for (User u : this.users) {
			if(u.getUUID().compareTo(userID) == 0 && u.validate(pin)) {
				return u;
			}
		}
		
		return null;
	}

	public String getName() {
		return this.name;
	}
}
