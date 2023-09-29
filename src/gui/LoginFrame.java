package gui;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import shopPackage.Shop;
import users.*;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import java.awt.SystemColor;

public class LoginFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginFrame frame = new LoginFrame();
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
	public LoginFrame() {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		List<User> users=Shop.getUsersList();
		setBounds(100, 100, 450, 300);
		setTitle("Login");
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		//contentPane.setBackground(new Color(102, 102, 102));
		
		//showing the window in the center
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x=dim.width/2-this.getSize().width/2,y=dim.height/2-this.getSize().height/2;
		this.setLocation(x,y-(y*30/100));
		
		//the listbox containing available users list
		JComboBox usersCmbox = new JComboBox();
		usersCmbox.setBounds(76, 85, 278, 20);
		usersCmbox.setModel(new DefaultComboBoxModel<String>());
		contentPane.add(usersCmbox);
		
		
		JButton btnLogin = new JButton("login");
		btnLogin.setBounds(159, 124, 117, 29);
		btnLogin.addActionListener(new ActionListener() {
			//switch to the adminframe or customerFrame depending on which user is selected
			public void actionPerformed(ActionEvent arg0) {
				
				dispose();
				User user=users.get(usersCmbox.getSelectedIndex());
				
				if(user instanceof AdminUser) {
					new AdminFrame((AdminUser) user);
				}
				else {
					//new CustomerFrame1((Customer) user);
					new CustomerFrame((Customer) user);
				}
				
			}
		});
		contentPane.setLayout(null);
		contentPane.add(btnLogin);
		
		
		
		//populating users list
		DefaultComboBoxModel<String> model= (DefaultComboBoxModel<String>) usersCmbox.getModel();
		model.removeAllElements();
		for (int i=0;i<users.size();i++) {
			String type=(users.get(i) instanceof AdminUser)? "admin":"customer";
			model.addElement(users.get(i).getUsername()+" ("+type+")");
			
		}
	}
}
