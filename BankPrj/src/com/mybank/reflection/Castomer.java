package com.mybank.reflection;

import com.mybank.domain.*;

import java.util.List;
import java.util.ArrayList;

public class Castomer {

  private String firstName;
  private String lastName;

  private List<Account> accounts;

  public Castomer(String f, String l) {
    firstName = f;
    lastName = l;
    // initialize accounts array
    accounts = new ArrayList<Account>(10);
  }

  public Castomer() {
	// TODO Auto-generated constructor stub
}

public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void addAccount(Account acct) {
    accounts.add(acct);
  }

  public int getNumOfAccounts() {
    return accounts.size();
  }

  public Account getAccount(int account_index) {
    return accounts.get(account_index);
  }
}
