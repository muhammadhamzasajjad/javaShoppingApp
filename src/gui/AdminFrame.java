package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;


import shopPackage.Connectivity;
import shopPackage.Keyboard;
import shopPackage.KeyboardLayout;
import shopPackage.KeyboardType;
import shopPackage.Mouse;
import shopPackage.MouseType;
import shopPackage.Product;
import shopPackage.Shop;
import users.AdminUser;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class AdminFrame extends JFrame {

	//to go to the user operations just find (Ctrl+f) "###"
	
	private Shop shop;
	private AdminUser adminUser;
	private JPanel contentPane;
	private JTable productsTable;
	private JPanel switchPanel;
	private JButton switchBtn;
	private JTabbedPane tabbedPane;
	private JPanel itemsPanel;
	private JScrollPane scrollPaneItems;
	
	private JPanel addItemPanel;
	private JLabel itemTypelbl;
	private JLabel lblNewLabel;
	private JTextField BarcodeTxt;
	private JComboBox itemNameCmbox;
	private JLabel lblNewLabel_1;
	private JComboBox itemTypeCmbox;
	private JLabel lblNewLabel_2;
	private JTextField brandTxt;
	private JLabel lblNewLabel_3;
	private JTextField colourTxt;
	private JLabel lblNewLabel_4;
	private JComboBox connectivityCmbox;
	private JLabel lblNewLabel_5;
	private JLabel lblNewLabel_6;
	private JTextField originalPriceTxt;
	private JLabel lblNewLabel_7;
	private JTextField retailPriceTxt;
	private JLabel lblLayoutButtons;
	private JButton btnAddItem;
	private JPanel LayoutButtonsPanel;
	private JComboBox layoutCmbox;
	private JTextField nButtonsTxt=new JTextField();
	
	private JPanel statusPanel;
	private JLabel statusLabel;
	
	private Timer timer;
	private TimerTask statusTask;	//task to reset the status after certain time
	private JTextField quantityTxt;
	
	private HashMap<Integer, TimerTask> compTasks;
	
	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminFrame frame = new AdminFrame();
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
	public AdminFrame(AdminUser adminUser){
		this.adminUser=adminUser;
		shop=new Shop();
		timer=new Timer();
		compTasks=new HashMap<Integer, TimerTask>();
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 663, 475);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		setTitle("Admin");
		
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
		JLabel usernameLbl=new JLabel("Username: "+adminUser.getUsername());
		usernameLbl.setFont(new Font("", Font.BOLD+Font.ITALIC, 15));
		switchPanel.add(usernameLbl,BorderLayout.WEST);
		
		contentPane.add(switchPanel,BorderLayout.NORTH);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane,BorderLayout.CENTER);
		
		populateItemsTab();
		populateAddItemTab();
		
		//creating status bar
		statusPanel = new JPanel();
		statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		contentPane.add(statusPanel,BorderLayout.SOUTH);
		statusPanel.setPreferredSize(new Dimension(this.getWidth(), 30));
		statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
		
		statusLabel = new JLabel();
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		statusPanel.add(statusLabel);
		
		
		showStatusMessage("Welcome "+adminUser.getUsername(), Color.green, 3000);
		
		//displaying the window in the center of the screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x=dim.width/2-this.getSize().width/2,y=dim.height/2-this.getSize().height/2;
		this.setLocation(x,y-(y*30/100));
		this.setVisible(true);
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
		//JOptionPane.showMessageDialog(null, comp.hashCode());
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
		timer.schedule(task, millis);
		
	}
	
	
	private void populateItemsTable() {
		scrollPaneItems = new JScrollPane();
		itemsPanel.add(scrollPaneItems,BorderLayout.CENTER);
		
		productsTable=new JTable();
		scrollPaneItems.setViewportView(productsTable);
		
		//creating the titles of the table and making it readonly
		productsTable.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Product", "Barcode", "brand", "Type", "Colour", "Connectivity", "Original Price","Retail price","Quantity", "Buttons/Layout"
				}
			) {
				public boolean isCellEditable(int row, int column) {
					return false;
				}
		});
		productsTable.setRowHeight(25);
		refreshItemsTable();
	}
	
	public void populateItemsTab(){
		itemsPanel = new JPanel(new BorderLayout());
		tabbedPane.addTab("Items", null, itemsPanel, null);
		
		populateItemsTable();
		
	}
	
	//the method reloads all the rows of the items table
	private void refreshItemsTable() {
		DefaultTableModel productsTableModel=(DefaultTableModel) productsTable.getModel();
		removeAllRows(productsTableModel);
		
		List<Product> products= shop.cloneProducts(adminUser);
		
		for (Product product : products) {
			if(product instanceof Mouse) {
				Mouse mouse=(Mouse) product;
				
				productsTableModel.addRow(new Object[] {"Mouse",mouse.getBarcode(),mouse.getBrand(),mouse.getType(),mouse.getColour(),mouse.getConnectivity(),mouse.originalPriceToString(),mouse.retailPriceToString(),mouse.getQuantityInStock(),mouse.getButtons()});
			}
			else {
				Keyboard keyboard=(Keyboard) product;
				productsTableModel.addRow(new Object[] {"Keyboard",keyboard.getBarcode(),keyboard.getBrand(),keyboard.getType(),keyboard.getColour(),keyboard.getConnectivity(),keyboard.originalPriceToString(),keyboard.retailPriceToString(),keyboard.getQuantityInStock(),keyboard.getLayout()});
	
			}
			
		}
		
		tabbedPane.setTitleAt(0, "Items("+products.size()+")");
		
	}
	
	//the method removes all the rows of a given table model
	private void removeAllRows(DefaultTableModel model) {
		for(int i=model.getRowCount()-1;i>=0;i--) {
			model.removeRow(i);
			
		}
	}
	
	private void populateAddItemTab() {
		
		//creating the panel containing all the fields needed to add a new Item and setting the gridbaglayout
		addItemPanel=new JPanel();
		addItemPanel.setBorder(new LineBorder(Color.black));
		tabbedPane.addTab("Add item",null, addItemPanel,null);
		GridBagLayout gbl_addItemPanel = new GridBagLayout();
		gbl_addItemPanel.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gbl_addItemPanel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 32, 0, 0, 0};
		gbl_addItemPanel.columnWeights = new double[]{0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_addItemPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		addItemPanel.setLayout(gbl_addItemPanel);
		
		//creating device name label
		itemTypelbl = new JLabel("Device name");
		itemTypelbl.setHorizontalAlignment(SwingConstants.TRAILING);
		GridBagConstraints gbc_itemTypelbl = new GridBagConstraints();
		gbc_itemTypelbl.anchor = GridBagConstraints.WEST;
		gbc_itemTypelbl.insets = new Insets(0, 0, 5, 5);
		gbc_itemTypelbl.gridx = 1;
		gbc_itemTypelbl.gridy = 1;
		addItemPanel.add(itemTypelbl, gbc_itemTypelbl);
		
		//combobox containing different available product types
		itemNameCmbox = new JComboBox();
		itemNameCmbox.setModel(new DefaultComboBoxModel(new String[] {"Keyboard", "Mouse"}));
		GridBagConstraints gbc_itemNameCmbox = new GridBagConstraints();
		gbc_itemNameCmbox.insets = new Insets(0, 0, 5, 5);
		gbc_itemNameCmbox.fill = GridBagConstraints.HORIZONTAL;
		gbc_itemNameCmbox.gridx = 2;
		gbc_itemNameCmbox.gridy = 1;
		addItemPanel.add(itemNameCmbox, gbc_itemNameCmbox);
		
		//when selected device type switches from keyboard to mouse or viceversa show the layout combobox or mouse button textbox
		itemNameCmbox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshItemTypeCmbBox();
				if(itemNameCmbox.getSelectedIndex()==0) {
					nButtonsTxt.setVisible(false);
					LayoutButtonsPanel.remove(nButtonsTxt);
					LayoutButtonsPanel.add(layoutCmbox);
					
					layoutCmbox.setVisible(true);
					lblLayoutButtons.setText("Layout");
					
				}
				else {
					layoutCmbox.setVisible(false);
					LayoutButtonsPanel.remove(layoutCmbox);
					LayoutButtonsPanel.add(nButtonsTxt,BorderLayout.CENTER);
					nButtonsTxt.setVisible(true);
					lblLayoutButtons.setText("# of Buttons");
					
				}
				
			}
		});
		
		
		//barcode label
		lblNewLabel = new JLabel("Barcode");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 5;
		gbc_lblNewLabel.gridy = 1;
		addItemPanel.add(lblNewLabel, gbc_lblNewLabel);
		
		//textfield containing the barcode
		BarcodeTxt = new JTextField();
		GridBagConstraints gbc_BarcodeTxt = new GridBagConstraints();
		gbc_BarcodeTxt.insets = new Insets(0, 0, 5, 0);
		gbc_BarcodeTxt.fill = GridBagConstraints.HORIZONTAL;
		gbc_BarcodeTxt.gridx = 6;
		gbc_BarcodeTxt.gridy = 1;
		addItemPanel.add(BarcodeTxt, gbc_BarcodeTxt);
		BarcodeTxt.setColumns(10);
		
		lblNewLabel_1 = new JLabel("Device type");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 3;
		addItemPanel.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		itemTypeCmbox = new JComboBox();
		GridBagConstraints gbc_itemTypeCmbox = new GridBagConstraints();
		gbc_itemTypeCmbox.insets = new Insets(0, 0, 5, 5);
		gbc_itemTypeCmbox.fill = GridBagConstraints.HORIZONTAL;
		gbc_itemTypeCmbox.gridx = 2;
		gbc_itemTypeCmbox.gridy = 3;
		addItemPanel.add(itemTypeCmbox, gbc_itemTypeCmbox);
		refreshItemTypeCmbBox();
		
		lblNewLabel_2 = new JLabel("Brand");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 5;
		gbc_lblNewLabel_2.gridy = 3;
		addItemPanel.add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		brandTxt = new JTextField();
		GridBagConstraints gbc_brandTxt = new GridBagConstraints();
		gbc_brandTxt.insets = new Insets(0, 0, 5, 0);
		gbc_brandTxt.fill = GridBagConstraints.HORIZONTAL;
		gbc_brandTxt.gridx = 6;
		gbc_brandTxt.gridy = 3;
		addItemPanel.add(brandTxt, gbc_brandTxt);
		brandTxt.setColumns(10);
		
		lblNewLabel_3 = new JLabel("Colour");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 1;
		gbc_lblNewLabel_3.gridy = 5;
		addItemPanel.add(lblNewLabel_3, gbc_lblNewLabel_3);
		
		colourTxt = new JTextField();
		GridBagConstraints gbc_colourTxt = new GridBagConstraints();
		gbc_colourTxt.insets = new Insets(0, 0, 5, 5);
		gbc_colourTxt.fill = GridBagConstraints.HORIZONTAL;
		gbc_colourTxt.gridx = 2;
		gbc_colourTxt.gridy = 5;
		addItemPanel.add(colourTxt, gbc_colourTxt);
		colourTxt.setColumns(10);
		
		lblNewLabel_4 = new JLabel("Connectivity");
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.gridx = 5;
		gbc_lblNewLabel_4.gridy = 5;
		addItemPanel.add(lblNewLabel_4, gbc_lblNewLabel_4);
		
		connectivityCmbox = new JComboBox();
		GridBagConstraints gbc_connectivityCmbox = new GridBagConstraints();
		gbc_connectivityCmbox.insets = new Insets(0, 0, 5, 0);
		gbc_connectivityCmbox.fill = GridBagConstraints.HORIZONTAL;
		gbc_connectivityCmbox.gridx = 6;
		gbc_connectivityCmbox.gridy = 5;
		addItemPanel.add(connectivityCmbox, gbc_connectivityCmbox);
		
		lblNewLabel_5 = new JLabel("Quantity");
		GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
		gbc_lblNewLabel_5.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_5.gridx = 1;
		gbc_lblNewLabel_5.gridy = 7;
		addItemPanel.add(lblNewLabel_5, gbc_lblNewLabel_5);
		
		quantityTxt = new JTextField();
		GridBagConstraints gbc_quantityTxt = new GridBagConstraints();
		gbc_quantityTxt.insets = new Insets(0, 0, 5, 5);
		gbc_quantityTxt.fill = GridBagConstraints.HORIZONTAL;
		gbc_quantityTxt.gridx = 2;
		gbc_quantityTxt.gridy = 7;
		addItemPanel.add(quantityTxt, gbc_quantityTxt);
		quantityTxt.setColumns(10);
		
		lblNewLabel_6 = new JLabel("Original Price");
		GridBagConstraints gbc_lblNewLabel_6 = new GridBagConstraints();
		gbc_lblNewLabel_6.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_6.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_6.gridx = 5;
		gbc_lblNewLabel_6.gridy = 7;
		addItemPanel.add(lblNewLabel_6, gbc_lblNewLabel_6);
		
		originalPriceTxt = new JTextField();
		GridBagConstraints gbc_originalPriceTxt = new GridBagConstraints();
		gbc_originalPriceTxt.insets = new Insets(0, 0, 5, 0);
		gbc_originalPriceTxt.fill = GridBagConstraints.HORIZONTAL;
		gbc_originalPriceTxt.gridx = 6;
		gbc_originalPriceTxt.gridy = 7;
		addItemPanel.add(originalPriceTxt, gbc_originalPriceTxt);
		originalPriceTxt.setColumns(10);
		
		lblNewLabel_7 = new JLabel("Retail Price");
		GridBagConstraints gbc_lblNewLabel_7 = new GridBagConstraints();
		gbc_lblNewLabel_7.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_7.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_7.gridx = 1;
		gbc_lblNewLabel_7.gridy = 9;
		addItemPanel.add(lblNewLabel_7, gbc_lblNewLabel_7);
		
		retailPriceTxt = new JTextField();
		GridBagConstraints gbc_retailPriceTxt = new GridBagConstraints();
		gbc_retailPriceTxt.insets = new Insets(0, 0, 5, 5);
		gbc_retailPriceTxt.fill = GridBagConstraints.HORIZONTAL;
		gbc_retailPriceTxt.gridx = 2;
		gbc_retailPriceTxt.gridy = 9;
		addItemPanel.add(retailPriceTxt, gbc_retailPriceTxt);
		retailPriceTxt.setColumns(10);
		
		lblLayoutButtons = new JLabel("Layout");
		GridBagConstraints gbc_lblLayoutButtons = new GridBagConstraints();
		gbc_lblLayoutButtons.anchor = GridBagConstraints.WEST;
		gbc_lblLayoutButtons.insets = new Insets(0, 0, 5, 5);
		gbc_lblLayoutButtons.gridx = 5;
		gbc_lblLayoutButtons.gridy = 9;
		addItemPanel.add(lblLayoutButtons, gbc_lblLayoutButtons);
		
		
		btnAddItem = new JButton("Add");
		//###add item operation
		btnAddItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addItem();
			}
		});
		
		LayoutButtonsPanel = new JPanel();
		GridBagConstraints gbc_LayoutButtonsPanel = new GridBagConstraints();
		gbc_LayoutButtonsPanel.insets = new Insets(0, 0, 5, 0);
		gbc_LayoutButtonsPanel.fill = GridBagConstraints.BOTH;
		gbc_LayoutButtonsPanel.gridx = 6;
		gbc_LayoutButtonsPanel.gridy = 9;
		addItemPanel.add(LayoutButtonsPanel, gbc_LayoutButtonsPanel);
		LayoutButtonsPanel.setLayout(new BorderLayout(0, 0));
		
		layoutCmbox = new JComboBox();
		LayoutButtonsPanel.add(layoutCmbox, BorderLayout.CENTER);
		GridBagConstraints gbc_btnAddItem = new GridBagConstraints();
		gbc_btnAddItem.gridwidth = 6;
		gbc_btnAddItem.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAddItem.gridx = 1;
		gbc_btnAddItem.gridy = 11;
		addItemPanel.add(btnAddItem, gbc_btnAddItem);
		
		
		loadLayoutCmbox();
		
		loadConnectivityCmbox();
		
		
		setPadding();
		
	}
	
	private boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private boolean isDouble(String value) {
		try {
			Double.parseDouble(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private void addItem() {
		//JOptionPane.showMessageDialog(null,	btnAddItem.getBackground());
		//cases covering all the possbilities when an invalid input is given
		
		if(!isInteger(BarcodeTxt.getText())||BarcodeTxt.getText().length()!=6) {
			BarcodeTxt.grabFocus();
			showStatusMessage("Barcode must be a six digit integer value", Color.red, 2000);
			setCompColor(btnAddItem, Color.RED, null, 2000);
			
		}
		else if(brandTxt.getText().trim().equals("")) {
			brandTxt.grabFocus();
			showStatusMessage("Insert a brand please", Color.red, 2000);
			setCompColor(btnAddItem, Color.RED, null, 2000);
			
		}
		else if (colourTxt.getText().trim().equals("")) {
			colourTxt.grabFocus();
			showStatusMessage("Insert a colour please", Color.red, 2000);
			setCompColor(btnAddItem, Color.RED, null, 2000);
		}
		else if(!isInteger(quantityTxt.getText().trim())) {
			quantityTxt.grabFocus();
			showStatusMessage("quantity must be an integer value", Color.red, 2000);
			setCompColor(btnAddItem, Color.RED, null, 2000);
			
			
		}
		else if (!isDouble(originalPriceTxt.getText().trim())) {
			originalPriceTxt.grabFocus();
			showStatusMessage("Original price must be a number", Color.red, 2000);
			setCompColor(btnAddItem, Color.RED, null, 2000);
		}
		else if (!isDouble(retailPriceTxt.getText().trim())) {
			retailPriceTxt.grabFocus();
			showStatusMessage("Retail price must be a number", Color.red, 2000);
			setCompColor(btnAddItem, Color.RED, null, 2000);
		}
		else if (itemNameCmbox.getSelectedIndex()==1&&!isInteger(nButtonsTxt.getText().trim())) {
			nButtonsTxt.grabFocus();
			showStatusMessage("Number of buttons must be an integer.", Color.red, 2000);
			setCompColor(btnAddItem, Color.RED, null, 2000);
		}
		else{
			//all the inputs are valid
			
			Product product;
			if(itemNameCmbox.getSelectedIndex()==0) {//keyboard
				product =new Keyboard(Integer.parseInt(BarcodeTxt.getText().trim()), brandTxt.getText().trim(), colourTxt.getText().trim(), Connectivity.valueOf(connectivityCmbox.getSelectedItem().toString()), Integer.parseInt(quantityTxt.getText().trim()), Double.parseDouble(originalPriceTxt.getText().trim()), Double.parseDouble(retailPriceTxt.getText().trim()), KeyboardType.valueOf(itemTypeCmbox.getSelectedItem().toString()), KeyboardLayout.valueOf(layoutCmbox.getSelectedItem().toString()));
			}
			else {//mouse
				product =new Mouse(Integer.parseInt(BarcodeTxt.getText().trim()), brandTxt.getText().trim(), colourTxt.getText().trim(), Connectivity.valueOf(connectivityCmbox.getSelectedItem().toString()), Integer.parseInt(quantityTxt.getText().trim()), Double.parseDouble(originalPriceTxt.getText().trim()), Double.parseDouble(retailPriceTxt.getText().trim()), MouseType.valueOf(itemTypeCmbox.getSelectedItem().toString()), Integer.parseInt(nButtonsTxt.getText().trim()));
			}
			
			try {
				shop.addProduct(product, adminUser);
				showStatusMessage("Item added successfully", Color.GREEN, 3000);
				setCompColor(btnAddItem, Color.GREEN, null, 2500);
				refreshItemsTable();
			} catch (Exception e) {
				showStatusMessage(e.getMessage(), Color.red, 2500);
				setCompColor(btnAddItem, Color.RED, null, 2500);
			}
		}
		
		
	}
	
	//sets the padding of the elements of add item tab
	private void setPadding() {
		Component[] components=addItemPanel.getComponents();
		GridBagLayout layout=(GridBagLayout) addItemPanel.getLayout();
		for (Component component : components) {
			JComponent jComponent=((JComponent) component);
			GridBagConstraints constraints=(GridBagConstraints) layout.getConstraints(component);
			addItemPanel.remove(component);
			constraints.insets=new Insets(10, 5, 10, 5);
			addItemPanel.add(component,constraints);
			
		}
		//BarcodeTxt.setMargin(new Insets(5, 5, 5, 5));
		
	}
	
	private void loadLayoutCmbox() {
		DefaultComboBoxModel<String> model=new DefaultComboBoxModel<String>();
		KeyboardLayout[] layouts=KeyboardLayout.values();
		for (int i = 0; i < layouts.length; i++) {
			model.addElement(layouts[i].toString());
		}
		layoutCmbox.setModel(model);
	}
	
	private void loadConnectivityCmbox() {
		DefaultComboBoxModel<String> model=new DefaultComboBoxModel<String>();
		Connectivity[] connTypes=Connectivity.values();
		for (int i = 0; i < connTypes.length; i++) {
			model.addElement(connTypes[i].toString());
		}
		connectivityCmbox.setModel(model);
		
	}
	
	private void refreshItemTypeCmbBox() {
		DefaultComboBoxModel<String> model=new DefaultComboBoxModel<String>();
		if(itemNameCmbox.getSelectedIndex()==0) {
			KeyboardType[] types=KeyboardType.values();
			for (int i = 0; i < types.length; i++) {
				model.addElement(types[i].toString());
			}
		}
		else {
			MouseType[] types=MouseType.values();
			for (int i = 0; i < types.length; i++) {
				model.addElement(types[i].toString());
			}
		}
		itemTypeCmbox.setModel(model);
	}
	
}
