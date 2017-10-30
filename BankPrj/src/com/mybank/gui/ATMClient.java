package com.mybank.gui;

import com.mybank.domain.*;
import com.mybank.data.DataSource;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.IOException;

public class ATMClient {

	private static final String USAGE = "USAGE: java com.mybank.gui.ATMClient <dataFilePath>";

	public static void main(String[] args) {

		// Retrieve the dataFilePath command-line argument
		if (args.length != 1) {
			System.out.println(USAGE);
		} else {
			String dataFilePath = args[0];

			try {
				System.out.println("Reading data file: " + dataFilePath);
				// Create the data source and load the Bank data
				DataSource dataSource = new DataSource(dataFilePath);
				dataSource.loadData();

				// Run the ATM GUI
				ATMClient client = new ATMClient();
				client.launchFrame();

			} catch (IOException ioe) {
				System.out.println("Could not load the data file.");
				System.out.println(ioe.getMessage());
				ioe.printStackTrace(System.err);
			}
		}
	}

	// PLACE YOUR GUI CODE HERE

	// GUI domain object instance variables
	Customer customer;
	Account account;

	// GUI lifecycle instance variables
	private enum ATMCycle {
		ENTER_CUST_ID, PERFORM_OPERATION
	}

	private ATMCycle cycle = ATMCycle.ENTER_CUST_ID;

	private enum ATMOperation {
		UNKNOWN, GET_BALANCE, DEPOSIT, WITHDRAW
	}

	private ATMOperation operation = ATMOperation.UNKNOWN;

	// GUI component instance variables
	private JFrame frame;
	private JPanel pLeftHalf;
	private JPanel pRightHalf;
	private JButton[] actionButtons;
	private JTextField entryField;
	private JTextField messageField;
	private JTextArea outputArea;

	public ATMClient() {
		frame = new JFrame("The First Java Bank ATM");
		frame.addWindowListener(new CloseHandler());
		initializeFrameComponents();
	}

	public void launchFrame() {
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
		performCycle(ATMCycle.ENTER_CUST_ID);
	}

	private void enableActionButtons(boolean enableFlag) {
		for (JButton b : actionButtons) {
			b.setEnabled(enableFlag);
		}
	}

	private void performCycle(ATMCycle cycle) {
		switch (cycle) {
		case ENTER_CUST_ID: {
			outputArea.setText(ENTER_CUSTOMER_TEXT);
			enableActionButtons(false);
			break;
		}
		case PERFORM_OPERATION: {
			performOperation();
			break;
		}
		}
	}

	private static final String ENTER_CUSTOMER_TEXT = "Enter your customer ID into the key pad and press the ENTER button.\n";
	private static final String PERFORM_OPERATION_TEXT = "";

	private void performOperation() {
		double amount = -1.0;
		switch (operation) {
		case GET_BALANCE: {
			outputArea.append("Your account balance is: " + account.getBalance() + "\n");
			break;
		}
		case DEPOSIT: {
			try {
				amount = Double.parseDouble(entryField.getText());
				account.deposit(amount);
				outputArea.append("Your deposit of " + amount + " was successful.\n");
				this.operation = ATMOperation.GET_BALANCE;
				performOperation();
			} catch (NumberFormatException nfe) {
				outputArea.append("Deposit amount is not a number: " + entryField.getText());
			}
			break;
		}
		case WITHDRAW: {
			try {
				amount = Double.parseDouble(entryField.getText());
				account.withdraw(amount);
				outputArea.append("Your withdrawal of " + amount + " was successful.\n");
				this.operation = ATMOperation.GET_BALANCE;
				performOperation();
			} catch (OverdraftException nfe) {
				outputArea.append("Your withdrawal of " + amount + " was unsuccessful.\n");
			} catch (NumberFormatException nfe) {
				outputArea.append("Deposit amount is not a number: " + entryField.getText());
			}
			break;
		}
		case UNKNOWN: {
			assert false;
			break;
		}
		}
	}

	private class CloseHandler extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			System.exit(0);
		}
	}

	private class KeyPadHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			JButton b = (JButton) event.getSource();
			entryField.setText(entryField.getText() + b.getText());
		}
	}

	private class EntryActionHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			switch (cycle) {
			case ENTER_CUST_ID: {
				int customerID = -1;
				try {
					customerID = Integer.parseInt(entryField.getText());
					customer = Bank.getCustomer(customerID);
					if (customer == null) {
						outputArea.append("Customer ID is not valid for this Bank: " + entryField.getText());
					} else {
						account = customer.getAccount(0);
						outputArea.append("Welcome " + customer.getFirstName() + " " + customer.getLastName() + "\n\n");
						cycle = ATMCycle.PERFORM_OPERATION;
						enableActionButtons(true);
					}
				} catch (NumberFormatException nfe) {
					outputArea.append("Customer ID is not a number: " + entryField.getText());
				}
				entryField.setText("");
				break;
			}
			case PERFORM_OPERATION: {
				performOperation();
				break;
			}
			}
		}
	}

	private void initializeFrameComponents() {
		initLeftHalf();
		initRightHalf();
	}

	private void initLeftHalf() {
		pLeftHalf = new JPanel();
		pLeftHalf.setLayout(new GridLayout(2, 1));
		initTopLeft();
		initBottomLeft();
		frame.add(pLeftHalf, BorderLayout.WEST);
	}

	private void initTopLeft() {
		JButton b;
		JPanel topLeftPanel = new JPanel();
		topLeftPanel.setLayout(new GridLayout(3, 1));
		actionButtons = new JButton[3];
		b = new JButton("Display account balance");
		actionButtons[0] = b;
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				operation = ATMOperation.GET_BALANCE;
				entryField.setText("");
				performOperation();
			}
		});
		topLeftPanel.add(b);
		b = new JButton("Make a deposit");
		actionButtons[1] = b;
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				operation = ATMOperation.DEPOSIT;
				outputArea.append("Enter the amount to deposit into the" + " key pad and press the ENTER button.\n");
				entryField.setText("");
			}
		});
		topLeftPanel.add(b);
		b = new JButton("Make a withdrawal");
		actionButtons[2] = b;
		b.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				operation = ATMOperation.WITHDRAW;
				outputArea.append("Enter the amount to withdraw into the" + " key pad and press the ENTER button.\n");
				entryField.setText("");
			}
		});
		topLeftPanel.add(b);
		pLeftHalf.add(topLeftPanel);
	}

	private void initBottomLeft() {
		// Initialize entry text field and keypad grid panel
		JPanel entryKeyPadPanel = new JPanel();
		entryKeyPadPanel.setLayout(new BorderLayout());
		// Create and add entry text field
		entryField = new JTextField(10);
		entryKeyPadPanel.add(entryField, BorderLayout.NORTH);
		// Create keypad grid and buttons
		JPanel keyPadGrid = new JPanel();
		keyPadGrid.setLayout(new GridLayout(4, 3));
		KeyPadHandler keyPadHandler = new KeyPadHandler();
		JButton[] keyPadButtons = new JButton[] { new JButton("1"), new JButton("2"), new JButton("3"),
				new JButton("4"), new JButton("5"), new JButton("6"), new JButton("7"), new JButton("8"),
				new JButton("9"), new JButton("0"), new JButton(".") };
		for (JButton b : keyPadButtons) {
			b.addActionListener(keyPadHandler);
			keyPadGrid.add(b);
		}
		JButton enterButton = new JButton("ENTER");
		enterButton.addActionListener(new EntryActionHandler());
		keyPadGrid.add(enterButton);
		entryKeyPadPanel.add(keyPadGrid, BorderLayout.SOUTH);
		// Add entry/keypad panel to left-half panel
		pLeftHalf.add(entryKeyPadPanel);
	}

	private void initRightHalf() {
		pRightHalf = new JPanel();
		pRightHalf.setLayout(new BorderLayout());
		outputArea = new JTextArea(10, 75);
		pRightHalf.add(outputArea, BorderLayout.CENTER);
		messageField = new JTextField(75);
		pRightHalf.add(messageField, BorderLayout.SOUTH);
		// pRightHalf.setEnabled(false);
		frame.add(pRightHalf, BorderLayout.EAST);
	}
}
