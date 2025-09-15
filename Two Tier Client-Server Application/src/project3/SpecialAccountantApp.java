/*
Name: Terence Wilchcombe
Course: CNT 4714 Spring 2024
Assignment title: Project 3 – A Specialized Accountant Application
Date: March 10, 2024
Class: SpecialAccountantApp
*/

package project3;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.mysql.cj.jdbc.MysqlDataSource;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.*;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.UIManager;
import javax.swing.JTextArea;
import java.sql.*;
import java.util.Properties;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JPasswordField;

public class SpecialAccountantApp extends JFrame implements ActionListener {
	
	
	JButton connectButton = new JButton("Connect to Database");
	JButton DisconnectButton = new JButton("Disconnect From Database");
	JButton ClearCommandButton = new JButton("Clear SQL Command");
	JButton ExecuteCommandButton = new JButton("Execute SQL Command");
	JButton btnNewButton_4 = new JButton("Clear Result Window");
	JComboBox userBox = new JComboBox();
	JComboBox URLBox = new JComboBox();
	public JScrollPane scrollPane = new JScrollPane();
	private boolean connectedToDatabase = false;

	private static final long serialVersionUID = 1L;
	public JPanel contentPane;
	public JTextField UsernameTF;
	public JPasswordField PasswordTF;
	public JTextField SQLCommandTF;
	MysqlDataSource dataSource = null;
	JLabel statusLabel = new JLabel("Connection Not Set");
	Connection connection = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SpecialAccountantApp frame = new SpecialAccountantApp();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SpecialAccountantApp() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 821, 591);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Connection Details");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel.setForeground(Color.BLUE);
		lblNewLabel.setBounds(10, 11, 111, 23);
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
		contentPane.add(lblNewLabel);
		
		
		connectButton.setForeground(new Color(0, 0, 0));
		connectButton.setBackground(Color.BLUE);
		connectButton.setBounds(10, 208, 173, 23);
		contentPane.add(connectButton);
		connectButton.addActionListener(this);
		
		
		DisconnectButton.setBackground(Color.RED);
		DisconnectButton.setForeground(new Color(0, 0, 0));
		DisconnectButton.addActionListener(this);
		DisconnectButton.setBounds(193, 208, 161, 23);
		contentPane.add(DisconnectButton);
		
		JLabel lblNewLabel_1 = new JLabel("DB URL Properties");
		lblNewLabel_1.setBackground(UIManager.getColor("Button.darkShadow"));
		lblNewLabel_1.setBounds(10, 45, 98, 14);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("User Properties");
		lblNewLabel_2.setBounds(10, 82, 86, 14);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Username");
		lblNewLabel_3.setBounds(10, 120, 57, 14);
		contentPane.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("Password");
		lblNewLabel_4.setBounds(10, 158, 57, 14);
		contentPane.add(lblNewLabel_4);
		
		UsernameTF = new JTextField();
		UsernameTF.setBounds(109, 117, 142, 20);
		contentPane.add(UsernameTF);
		UsernameTF.setColumns(10);
		
		PasswordTF = new JPasswordField();
		PasswordTF.setColumns(10);
		PasswordTF.setBounds(109, 155, 142, 20);
		contentPane.add(PasswordTF);
		
		
		URLBox.setBounds(109, 41, 142, 22);
		URLBox.addItem("operationslog.properties");
		contentPane.add(URLBox);
		
		
		userBox.setBounds(109, 78, 142, 22);
		userBox.addItem("theaccountant.properties");
	
		contentPane.add(userBox);
		
		JLabel lblNewLabel_5 = new JLabel("Specialized Accountant Application");
		lblNewLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_5.setBounds(131, 11, 223, 14);
		contentPane.add(lblNewLabel_5);
		
		JLabel lblNewLabel_6 = new JLabel("Enter An SQL Command");
		lblNewLabel_6.setForeground(Color.BLUE);
		lblNewLabel_6.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_6.setBounds(422, 11, 142, 14);
		contentPane.add(lblNewLabel_6);
		
		SQLCommandTF = new JTextField();
		SQLCommandTF.setBounds(422, 36, 356, 122);
		contentPane.add(SQLCommandTF);
		SQLCommandTF.setColumns(10);
		
		
		ClearCommandButton.setBackground(new Color(255, 215, 0));
		ClearCommandButton.setForeground(new Color(0, 0, 0));
		ClearCommandButton.setBounds(422, 184, 173, 23);
		contentPane.add(ClearCommandButton);
		ClearCommandButton.addActionListener(this);
		
		
		ExecuteCommandButton.setBackground(new Color(34, 139, 34));
		ExecuteCommandButton.setForeground(new Color(0, 0, 0));
		ExecuteCommandButton.setBounds(616, 184, 162, 23);
		contentPane.add(ExecuteCommandButton);
		ExecuteCommandButton.addActionListener(this);
		
		JLabel lblNewLabel_7 = new JLabel("SQL Execution Result Window");
		lblNewLabel_7.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_7.setForeground(new Color(0, 0, 255));
		lblNewLabel_7.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_7.setBounds(10, 294, 189, 23);
		contentPane.add(lblNewLabel_7);
		
		
		btnNewButton_4.setBackground(new Color(255, 215, 0));
		btnNewButton_4.setForeground(new Color(0, 0, 0));
		btnNewButton_4.addActionListener(this);
		btnNewButton_4.setBounds(10, 519, 142, 23);
		contentPane.add(btnNewButton_4);
		
		
		statusLabel.setBackground(new Color(0, 0, 0));
		statusLabel.setForeground(new Color(255, 0, 0));
		statusLabel.setBounds(40, 258, 710, 25);
		contentPane.add(statusLabel);
		
		
		scrollPane.setBounds(40, 328, 721, 180);
		contentPane.add(scrollPane);
	}
	
	 private void connectToDatabase() {
		    Properties properties = new Properties();
		    Properties URLproperties = new Properties();
		    String urlname = (String) URLBox.getSelectedItem();
		    String filename = (String) userBox.getSelectedItem();
		    String username = UsernameTF.getText();
		    String password = PasswordTF.getText();
		   
	        try (InputStream inputStream = new FileInputStream(filename)) {
	            properties.load(inputStream);
	        } catch (IOException ex) {
	            statusLabel.setText("Error loading database.properties file: " + ex.getMessage());
	            return;
	        }
	        try (InputStream inputStreamURL = new FileInputStream(urlname)) {
	            URLproperties.load(inputStreamURL);
	        } catch (IOException ex) {
	            statusLabel.setText("Error loading database.properties file: " + ex.getMessage());
	            return;
	        }
	        dataSource = new MysqlDataSource();

	        dataSource.setURL(URLproperties.getProperty("MYSQL_DB_URL"));
	    	dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));
	    	dataSource.setPassword(properties.getProperty("MYSQL_DB_PASSWORD")); 
	    	
	    	if ( (username.equals(dataSource.getUser())) && (password.equals(dataSource.getPassword())) ){
		        try {
		            Class.forName("com.mysql.cj.jdbc.Driver");
		            connection = dataSource.getConnection();
		         
		            String connectedDatabase = dataSource.getURL();
		            statusLabel.setText("Connected to: " + connectedDatabase);
		            
		            connectedToDatabase = true;
		         
		        } catch (ClassNotFoundException ex) {
		            statusLabel.setText("MySQL JDBC driver not found: " + ex.getMessage());
		        } catch (SQLException ex) {
		            statusLabel.setText("Error connecting to the database: " + ex.getMessage());
		        }
	    	}
	    	else {
	            statusLabel.setText("Username and password do not match the properties file.");
	    	}
	    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == connectButton) {
			
			connectToDatabase();
			
			
			
		}
		if (e.getSource() == DisconnectButton) {
			try {
				connection.close();
				statusLabel.setText("Manually disconnected from database");
				connectedToDatabase = false;
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		if (e.getSource() == ExecuteCommandButton) {
		    if (connectedToDatabase) {
		        String query = SQLCommandTF.getText();
		        if (query.trim().toLowerCase().startsWith("select")) {
		            try {
		                ResultSetTableModel tableModel = new ResultSetTableModel(connection, query);
		                tableModel.executeQuery(connection, query);
		                JTable resultTable = new JTable(tableModel);
		                scrollPane.setViewportView(resultTable);
		            } catch (SQLException ex) {
		                ex.printStackTrace();
		                JOptionPane.showMessageDialog(this, "Error executing query: " + ex.getMessage());
		            }
		        } else {
		            try {
		                ResultSetTableModel tableModel = new ResultSetTableModel(connection, query);
		                int rowsAffected = tableModel.executeUpdateQuery(connection, query);
		                JOptionPane.showMessageDialog(this, "Query executed successfully! Rows affected: " + rowsAffected);
		            } catch (SQLException ex) {
		                ex.printStackTrace();
		                JOptionPane.showMessageDialog(this, "Error executing query: " + ex.getMessage());
		            }
		        }
		    }
		}


		if (e.getSource()== ClearCommandButton) {
			SQLCommandTF.setText("");
		}
		if (e.getSource() == btnNewButton_4) {
		    scrollPane.setViewportView(null);
		}
			
		// TODO Auto-generated method stub
		
	}
}


	