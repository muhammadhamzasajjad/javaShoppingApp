package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import shopPackage.BasketItem;
import shopPackage.Keyboard;
import shopPackage.KeyboardLayout;
import shopPackage.Mouse;
import shopPackage.Product;
import shopPackage.Shop;
import users.Customer;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;

import javax.swing.JCheckBox;import javax.swing.JButton;
import javax.swing.JComboBox;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.JTabbedPane;

public class CustomerFrame extends JFrame {
	
	//to go to the user operations just find (Ctrl+f) "###"
	
	
	private Customer customerUser;
	private Shop shop;
	private JPanel contentPane;
	
	
	private JTabbedPane tabbedPane;
	private JPanel itemsPanel;
	private JPanel switchPanel;
	private JButton switchBtn;
	private JScrollPane scrollPaneItems;
	private JTable productsTable;
	private JPanel optionsPanel;
	private JPanel addToBasketPanel;
	private JTextField txtQuantity;
	private JButton addToBasketBtn;
	private JPanel basketPanel;
	private JLabel totaLabelBasket;
	private JPanel showBasketPanel;
	private JTable basketTable;
	private JPanel basketOptionsPanel;
	private JButton buyAllBtn;
	private JButton saveAllBtn;
	private JButton deleteAllBtn;
	private JScrollPane scrollPaneBasket;
	private JPanel showPaymentPanel;
	private JPanel paymentTypePanel;
	private JPanel creditCardPanel;
	private JPanel paypalPanel;
	
	private JPanel statusPanel;
	private JLabel statusLabel;
	
	private Timer timer;
	private TimerTask statusTask;
	
	private HashMap<Integer, TimerTask> compTasks;
	

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CustomerFrame1 frame = new CustomerFrame1();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the frame.
	 */
	public CustomerFrame(Customer customer) {
		this.customerUser=customer;
		setTitle("Customer");
		shop=new Shop();
		timer=new Timer();
		compTasks=new HashMap<Integer, TimerTask>();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 663, 475);
		
		//creating and setting mainpanel
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		//panel to switch user
		switchPanel =new JPanel(new BorderLayout());
		switchBtn=new JButton();
		switchBtn.setText("Switch user");
		//###switch user operation
		switchBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new LoginFrame();
			}
		});
		switchPanel.add(switchBtn,BorderLayout.EAST);
		
		//label displaying username
		JLabel usernameLbl=new JLabel("Username: "+customer.getUsername());
		usernameLbl.setFont(new Font("", Font.BOLD+Font.ITALIC, 15));
		switchPanel.add(usernameLbl,BorderLayout.WEST);
		
		contentPane.add(switchPanel,BorderLayout.NORTH);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane,BorderLayout.CENTER);
		
		populateItemsTab();
		
		populateBasketTab();
		
		//creating and setting the status bar
		statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		contentPane.add(statusPanel,BorderLayout.SOUTH);
		statusPanel.setPreferredSize(new Dimension(this.getWidth(), 30));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		statusLabel = new JLabel();
		statusPanel.add(statusLabel);
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		
		//showing the window in the middle of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x=dim.width/2-this.getSize().width/2,y=dim.height/2-this.getSize().height/2;
		this.setLocation(x,y-(y*30/100));
		this.setVisible(true);
		
		showStatusMessage("Welcome "+customerUser.getUsername(), Color.green, 3000);
		
		
		
	}
	
	private void showStatusMessage(String message, Color color, int millis) {
		statusLabel.setText(message);
		statusPanel.setBackground(color);
		
		//resetting the previous statusTast so that the new status is displayed for all the required duration
		if(statusTask!=null) {
			statusTask.cancel();
		}
		
		statusTask=new TimerTask() {
			
			@Override
			public void run() {
				statusPanel.setBackground(contentPane.getBackground());
				statusLabel.setText("Status bar");
			}
		};
		
		timer.schedule(statusTask, millis);
	}
	
	private void setCompColor(Component comp,Color newcolor,Color originalColor, int millis) {
		
		comp.setBackground(newcolor);
		if(compTasks.get(comp.hashCode())!=null) {
			compTasks.get(comp.hashCode()).cancel();
		}
		TimerTask task= new TimerTask() {
			
			@Override
			public void run() {
				comp.setBackground(originalColor);
				
			}
		};
		compTasks.put(comp.hashCode(), task);
		//
		timer.schedule(task, millis);
		
	}
	
	
	private void createItemTabOptions() {
		
		//panel on the right side containing the add to basket button and textbox quantity
		optionsPanel=new JPanel(new BorderLayout());
		addToBasketPanel=new JPanel();
		addToBasketPanel.setLayout(new GridLayout(3,1,5,5));
		addToBasketPanel.add(new JLabel("Quantity"));
		txtQuantity=new JTextField(15);
		txtQuantity.setText("1");
		optionsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		
		addToBasketPanel.add(txtQuantity);
		
		optionsPanel.add(addToBasketPanel,BorderLayout.NORTH);
		
		addToBasketBtn=new JButton("Add to basket");
		addToBasketPanel.add(addToBasketBtn);
		
		//###add to basket operation
		addToBasketBtn.addActionListener(new ActionListener() {
			//allowing the user to add item to the basket when the button is pressed
			@Override
			public void actionPerformed(ActionEvent e) {
				if(productsTable.getSelectedRowCount()==0) {
					showStatusMessage("Select an item please.", Color.red, 2000);
					setCompColor(addToBasketBtn, Color.RED, null, 2000);
				}
				else if(!isInteger(txtQuantity.getText())||Integer.parseInt(txtQuantity.getText())<=0) {
					showStatusMessage("Quantity must be a positive integer value", Color.red, 2000);
					setCompColor(addToBasketBtn, Color.RED, null, 2000);
					txtQuantity.grabFocus();
				}
				else {
					try {
						customerUser.addToBasket(shop, Integer.parseInt(productsTable.getModel().getValueAt(productsTable.getSelectedRow(), 1).toString()), Integer.parseInt(txtQuantity.getText()));
						refreshBasketTable(); 
						showStatusMessage(productsTable.getModel().getValueAt(productsTable.getSelectedRow(), 1).toString()+" added to basket successfully", Color.green, 2000);
						setCompColor(addToBasketBtn, Color.GREEN, null, 2000);
					}catch (Exception e1) {
						
						showStatusMessage(e1.getMessage(), Color.red, 2000);
						setCompColor(addToBasketBtn, Color.RED, null, 2000);
					}
				}
				
				
			}
		
			});
		
		itemsPanel.add(optionsPanel,BorderLayout.EAST);
		
	}
	
	private boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	private void createItemsTable() {
		scrollPaneItems = new JScrollPane();
		itemsPanel.add(scrollPaneItems,BorderLayout.CENTER);
		
		productsTable=new JTable();
		scrollPaneItems.setViewportView(productsTable);
		
		//initializing table model by setting titles and making it readonly
		productsTable.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Product", "Barcode", "brand", "Type", "Colour", "Connectivity", "Price", "Buttons/Layout"
				}
			) {
				public boolean isCellEditable(int row, int column) {
					return false;
				}
		});
		productsTable.setRowHeight(25);
	
		
		refreshItemsTable(shop.cloneProducts(customerUser));
	}
	
	private void populateItemsTab() {
		itemsPanel = new JPanel(new BorderLayout());
		
		tabbedPane.addTab("Items", null, itemsPanel, null);

		createItemTabOptions();
		
		createSearchPanel();
		
		createItemsTable();
		
	}
	
	//creates the search panel containing the filer options
	private void createSearchPanel() {
		JPanel searchPanel=new JPanel();
		searchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
		itemsPanel.add(searchPanel,BorderLayout.NORTH);
		
		//filter by brand option
		JCheckBox filterByBrandCheckBox=new JCheckBox("Brand");
		searchPanel.add(filterByBrandCheckBox);
		JComboBox<String> filterByBrandCmBox=new JComboBox<String>();
		searchPanel.add(filterByBrandCmBox);
		DefaultComboBoxModel<String> model=(DefaultComboBoxModel<String>) filterByBrandCmBox.getModel();
		
		Collection<String> brandsCollection=shop.getBrandsCollection();
		for (String brand : brandsCollection) {
			model.addElement(brand);
		}
		
		//filter by layout option
		JCheckBox filterByLayoutCheckBox=new JCheckBox("Layout");
		searchPanel.add(filterByLayoutCheckBox);
		JComboBox<String> filterByLayoutCmBox=new JComboBox<String>();
		searchPanel.add(filterByLayoutCmBox);
		model=(DefaultComboBoxModel<String>) filterByLayoutCmBox.getModel();
		KeyboardLayout[] layouts=KeyboardLayout.values();
		for (KeyboardLayout layout : layouts) {
			model.addElement(layout.toString());
		}
		
		searchPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		JButton	filterBtn=new JButton("Filter");
		searchPanel.add(filterBtn);
		//###filter operation
		filterBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshItemsTable(shop.searchProducts(customerUser, filterByBrandCmBox.getSelectedItem().toString(), filterByBrandCheckBox.isSelected(), KeyboardLayout.valueOf(filterByLayoutCmBox.getSelectedItem().toString()), filterByLayoutCheckBox.isSelected()));
			}
		});
		
		
		//searchPanel.add(new )
	}
	
	//the function reloads all the rows  of the items table
	private void refreshItemsTable(List<Product> products) {
		DefaultTableModel productsTableModel=(DefaultTableModel) productsTable.getModel();
		
		removeAllRows(productsTableModel);
		
		for (Product product : products) {
			if(product instanceof Mouse) {
				Mouse mouse=(Mouse) product;
				productsTableModel.addRow(new Object[] {"Mouse",mouse.getBarcode(),mouse.getBrand(),mouse.getType(),mouse.getColour(),mouse.getConnectivity(),mouse.retailPriceToString(),mouse.getButtons()});
			}
			else {
				Keyboard keyboard=(Keyboard) product;
				productsTableModel.addRow(new Object[] {"Keyboard",keyboard.getBarcode(),keyboard.getBrand(),keyboard.getType(),keyboard.getColour(),keyboard.getConnectivity(),keyboard.retailPriceToString(),keyboard.getLayout()});
	
			}
			
		}
	}
	
	//the function removes all the rows of a given table model
	private void removeAllRows(DefaultTableModel model) {
		for(int i=model.getRowCount()-1;i>=0;i--) {
			model.removeRow(i);
			
		}
	}
	
	//reloads all the rows of the basket table
	private void refreshBasketTable() {
		DefaultTableModel basketTableModel=(DefaultTableModel) basketTable.getModel();
		
		removeAllRows(basketTableModel);
		
		Collection<BasketItem> basketItems= customerUser.cloneBasketItems();
		int row=0;
		for (BasketItem item : basketItems) {
			String prod="";
			Product product=item.getProduct();
			if(product instanceof Mouse) {
				prod="Mouse";
			}
			else {
				prod="Keyboard";
				
			}
			basketTableModel.addRow(new Object[] {prod,product.getBarcode(),product.getBrand(),product.retailPriceToString(),item.getQuantity()});
			//product.reta
			row++;
		}
		
		tabbedPane.setTitleAt(1, "Basket("+basketItems.size()+")");
		
		//showing the total bill
		totaLabelBasket.setText(String.format("Total %.2f £", customerUser.calculateBill()));
		if(basketItems.size()==0) {
			basketOptionsPanel.setVisible(false);
		}
		else {
			basketOptionsPanel.setVisible(true);
		}
		
	}
	
	private void createBasketTable() {
		
		//creating scrollpane and jtable
		scrollPaneBasket = new JScrollPane();
		showBasketPanel.add(scrollPaneBasket,BorderLayout.CENTER);
		basketTable=new JTable();
		basketTable.getModel();
		
		scrollPaneBasket.setViewportView(basketTable);
		
		
		basketTable.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Product", "Barcode", "brand", "Price", "Quantity"
				}
			) {
				boolean[] columnEditables = new boolean[] {
					false, false, false, false, false, false, false, false
				};
				public boolean isCellEditable(int row, int column) {
					return false;
				}
		});
		basketTable.setRowHeight(25);
		refreshBasketTable();
	}
	
	private void populateBasketTab() {
		basketPanel = new JPanel(new BorderLayout());
		
		//setting basket icon
		ImageIcon icon=new ImageIcon("files/shoppingcart.png");
		Image image = icon.getImage(); // transform it 
		Image img1 = image.getScaledInstance(45, 30,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
		icon = new ImageIcon(img1);
		tabbedPane.addTab("Basket", icon, basketPanel, null);
		
		//basket panel containing the basket table
		showBasketPanel=new JPanel(new BorderLayout());
		basketPanel.add(showBasketPanel,BorderLayout.CENTER);
		basketOptionsPanel=new JPanel();
		showBasketPanel.add(basketOptionsPanel,BorderLayout.NORTH);
		
		//basket operations buttons
		totaLabelBasket=new JLabel();
		basketOptionsPanel.add(totaLabelBasket);
		buyAllBtn=new JButton("Buy all");
		basketOptionsPanel.add(buyAllBtn);
		saveAllBtn=new JButton("Save All");
		basketOptionsPanel.add(saveAllBtn);
		deleteAllBtn=new JButton("Delete All");
		basketOptionsPanel.add(deleteAllBtn);
		
		createBasketTable();
		
		//###save all operation
		saveAllBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int opt=JOptionPane.showConfirmDialog(null, "Are you sure you want to save all basket items?");
				if(opt==JOptionPane.YES_OPTION) {
					customerUser.saveBasket(shop);
					refreshBasketTable();
				}
			}
		});
		//###delete all operation
		deleteAllBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int opt=JOptionPane.showConfirmDialog(null, "Are you sure you want to cancel basket items?","Warning",JOptionPane.YES_NO_OPTION);
				//int opt=JOptionPane.showInternalConfirmDialog(null, "hi");
				if(opt==JOptionPane.YES_OPTION) {
					customerUser.cancelBasket(shop);
					refreshBasketTable();
				}
				
			}
		});
		
		showPaymentPanel=new JPanel(new BorderLayout(50,50));
		
		JPanel gobackPanel=new JPanel(new BorderLayout(5, 5));
		showPaymentPanel.add(gobackPanel,BorderLayout.NORTH);
		JButton backToBasketBtn=new JButton("<<Back");
		gobackPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		gobackPanel.add(backToBasketBtn,BorderLayout.WEST);
		backToBasketBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				showPaymentPanel.setVisible(false);
				showBasketPanel.setVisible(true);
				basketPanel.remove(showPaymentPanel);
				basketPanel.add(showBasketPanel,BorderLayout.CENTER);
				
			}
		});
		
		paymentTypePanel=new JPanel(new BorderLayout());
		showPaymentPanel.add(paymentTypePanel,BorderLayout.CENTER);
		JComboBox<String> paymentTypecmBox=new JComboBox<String>();
		paymentTypecmBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Paypal","Credit Card"}));
		//paymentTypePanel.add(new JLabel("Payment Type: "));
		
		//total and payment methods when payAll is clicked
		JPanel paymentInfoJPanel=new JPanel();
		GridBagConstraints c=new GridBagConstraints();
		JLabel totalLbl=new JLabel("Total: ");
		totalLbl.setFont(new Font("", Font.BOLD, 14));
		c.gridx=0;c.gridy=0;c.ipadx=20;
		paymentInfoJPanel.add(totalLbl,c);
		showPaymentPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		c.gridx=2;c.gridy=0;
		paymentInfoJPanel.add(new JLabel("Method: "));
		c.gridx=3;c.gridy=0;
		paymentInfoJPanel.add(paymentTypecmBox);
		paymentTypecmBox.addActionListener(new ActionListener() {
			
			//when the selected payment type changes switch to the related combobox containing it's details
			@Override
			public void actionPerformed(ActionEvent e) {
				if(paymentTypecmBox.getSelectedIndex()==0) {
					creditCardPanel.setVisible(false);
					paymentTypePanel.remove(creditCardPanel);
					if(!Arrays.asList(paymentTypePanel).contains(paypalPanel)) {
						paymentTypePanel.add(paypalPanel,BorderLayout.CENTER);
					}
					
					
					paypalPanel.setVisible(true);
				}
				else {
					paypalPanel.setVisible(false);
					paymentTypePanel.remove(paypalPanel);
					if(!Arrays.asList(paymentTypePanel).contains(creditCardPanel)) {
						paymentTypePanel.add(creditCardPanel,BorderLayout.CENTER);
					}
					
					
					creditCardPanel.setVisible(true);
				}
				
			}
		});
		
		//showing paypal fields
		paymentTypePanel.add(paymentInfoJPanel,BorderLayout.NORTH);
		paypalPanel=new JPanel(new FlowLayout());
		paypalPanel.add(new JLabel("email"));
		JTextField emailtxt=new JTextField(20);
		paypalPanel.add(emailtxt);
		JButton payByPayPalBtn=new JButton("Confirm payment");
		paypalPanel.add(payByPayPalBtn);
		
		//###buy all using paypal operation
		payByPayPalBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					HashMap<Integer, BasketItem> items= customerUser.buyAllWithPaypal(shop, emailtxt.getText());					
					showPaymentPanel.setVisible(false);
					showBasketPanel.setVisible(true);
					basketPanel.remove(showPaymentPanel);
					basketPanel.add(showBasketPanel);
					showStatusMessage(totalLbl.getText().split(" ")[1]+" £ paid using PayPal", Color.green, 3500);
					setCompColor(payByPayPalBtn, Color.GREEN, null, 2000);
					refreshBasketTable();
					refreshItemsTable(shop.cloneProducts(customerUser));
				} catch (Exception e1) {
					showStatusMessage(e1.getMessage(), Color.red, 2000);
					setCompColor(payByPayPalBtn, Color.RED, null, 2000);
					
				}
			}
		});
		
		paymentTypePanel.add(paypalPanel,BorderLayout.CENTER);
		
		//###buy all operations
		buyAllBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				showBasketPanel.setVisible(false);
				/*if(!Arrays.asList(basketPanel.getComponents()).contains(showPaymentPanel)) {
					basketPanel.add(showPaymentPanel,BorderLayout.CENTER);
				}*/
				showPaymentPanel.setVisible(true);
				basketPanel.remove(showBasketPanel);
				basketPanel.add(showPaymentPanel);
				
				totalLbl.setText("Total: "+String.format("%.2f £", customerUser.calculateBill()));
			}
		});
		
		//showing credit card fields
		creditCardPanel=new JPanel(new FlowLayout());
		creditCardPanel.add(new JLabel("Card NO."));
		JTextField cardNotxt=new JTextField(16);
		creditCardPanel.add(cardNotxt);
		creditCardPanel.add(new JLabel("Security code"));
		JTextField securityCodetxt=new JTextField(3);
		creditCardPanel.add(securityCodetxt);
		JButton payByCreditCardBtn=new JButton("Confirm payment");
		creditCardPanel.add(payByCreditCardBtn);
		
		//###buy all using credit card operation
		payByCreditCardBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					HashMap<Integer, BasketItem> items= customerUser.buyAllWithCreditCard(shop, cardNotxt.getText(), securityCodetxt.getText());
					showStatusMessage(totalLbl.getText().split(" ")[1] +" £ paid using Credit Card", Color.green, 3500);
					setCompColor(payByCreditCardBtn, Color.GREEN, null, 2000);
					showPaymentPanel.setVisible(false);
					showBasketPanel.setVisible(true);
					basketPanel.remove(showPaymentPanel);
					basketPanel.add(showBasketPanel);
					refreshBasketTable();
					refreshItemsTable(shop.cloneProducts(customerUser));
				} catch (Exception e1) {
					showStatusMessage(e1.getMessage(), Color.red, 2000);
					setCompColor(payByCreditCardBtn, Color.red, null, 2000);
				}
			}
		});
		
	}
	
	
}
