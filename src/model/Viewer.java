package model;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class Viewer {
	
	private JFrame frame;
	private JPanel panelT1;
	private JPanel textpanel;
	private JTextArea textArea;
	private JScrollPane scroll;
	private static String username = "root";
	private static String password = "root";
	private static String schema = "sampledblocking";
	private Connection con;
	private Controller c;
	private JRadioButton rdbtnRUT1;
	private JRadioButton rdbtnRCT1;	
	private JRadioButton rdbtnRRT1;
	private JRadioButton rdbtnST1;
	private ButtonGroup group;
	private JComboBox schemaCB;
	private JTextField ageField;
	private JComboBox memCB;
	
	public Viewer(){
		
		con = getConnection();
		frame = new JFrame("ADVANDB MCO3");
		frame.setBounds(0, 0, 750, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		panelT1 = new JPanel();
		panelT1.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panelT1.setBounds(91, 6, 182, 213);
		frame.getContentPane().add(panelT1);
		panelT1.setLayout(null);
		
		JButton btnTransaction = new JButton("Transaction 1");
		btnTransaction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(group.getSelection() == null)
					textArea.append("\nSelect Isolation Levels");
				
				if(rdbtnRUT1.isSelected() == true){
					textArea.append("\nRead Uncommitted Selected");
//					c.TransactionOne(1);
				}
				
				if(rdbtnRCT1.isSelected() == true){
					textArea.append("\nRead Committed Selected");
//					c.TransactionOne(2);
				}
				
				if(rdbtnRRT1.isSelected() == true){
					textArea.append("\nRepeatable Read Selected");
//					c.TransactionOne(3);
				}
				
				if(rdbtnST1.isSelected() == true){
					textArea.append("\nSerializable Selected");
//					c.TransactionOne(4);
				}			
				
			}
		});
		btnTransaction.setBounds(31, 174, 117, 29);
		panelT1.add(btnTransaction);
		
		JLabel lblSelectIsolationLevel = new JLabel("Select Isolation Level");
		lblSelectIsolationLevel.setBounds(31, 6, 132, 16);
		panelT1.add(lblSelectIsolationLevel);
		
		rdbtnRUT1 = new JRadioButton("Read Uncommitted");
		rdbtnRUT1.setBounds(16, 34, 160, 23);
		panelT1.add(rdbtnRUT1);
		
		rdbtnRCT1 = new JRadioButton("Read Committed");
		rdbtnRCT1.setBounds(16, 69, 141, 23);
		panelT1.add(rdbtnRCT1);
		
		rdbtnRRT1 = new JRadioButton("Repeatable Read");
		rdbtnRRT1.setBounds(16, 104, 141, 23);
		panelT1.add(rdbtnRRT1);
		
		rdbtnST1 = new JRadioButton("Serializable");
		rdbtnST1.setBounds(16, 139, 141, 23);
		panelT1.add(rdbtnST1);
		
		group = new ButtonGroup();
		group.add(rdbtnRUT1);
		group.add(rdbtnRCT1);
		group.add(rdbtnRRT1);
		group.add(rdbtnST1);

		
		textpanel = new JPanel();
		textpanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		textpanel.setBounds(381, 6, 369, 466);
		frame.getContentPane().add(textpanel);
		
		textArea = new JTextArea();
		textArea.setBounds(6, 5, 360, 460);
		textArea.setVisible(true);
		textpanel.setLayout(null);
		textArea.setEditable(false);
		
		scroll = new JScrollPane(textArea);
		scroll.setBounds(6, 5, 360, 460);
		textpanel.add(scroll);
		
		JPanel MemH = new JPanel();
		MemH.setBorder(new LineBorder(Color.LIGHT_GRAY));
		MemH.setBounds(6, 231, 363, 241);
		frame.getContentPane().add(MemH);
		MemH.setLayout(null);
		
		JButton btnTransaction_1 = new JButton("Transaction 2");
		btnTransaction_1.setBounds(117, 194, 117, 29);
		MemH.add(btnTransaction_1);
		
		JLabel lblSelectSchema = new JLabel("Select Schema");
		lblSelectSchema.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelectSchema.setBounds(117, 17, 117, 16);
		MemH.add(lblSelectSchema);
		
		schemaCB = new JComboBox();
		schemaCB.setBounds(97, 45, 157, 27);
		MemH.add(schemaCB);
		schemaCB.addItem("--");
		schemaCB.addItem("Palawan");
		schemaCB.addItem("Marinduque");
		
		schemaCB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {

                JComboBox schemaCB = (JComboBox) event.getSource();

                Object selected = schemaCB.getSelectedItem();
                if(selected.toString().equals("Marinduque")){
                	memCB.removeAllItems();
                	memCB.setEnabled(true);
                	memCB.addItem(1);
                	memCB.addItem(2);
                	memCB.addItem(3);
                	memCB.addItem(4);
                	memCB.addItem(5);
                }
                
                if(selected.toString().equals("Palawan")){
                	memCB.removeAllItems();
                	memCB.setEnabled(true);
                	memCB.addItem(1);
                	memCB.addItem(2);
                	memCB.addItem(3);
                }
                
                if(selected.toString().equals("--")){
                	appendText("\nSelect a region");
                }
                	

            }
        });
		
		memCB = new JComboBox();
		memCB.setEnabled(false);
		memCB.setBounds(97, 84, 157, 27);
		MemH.add(memCB);
		
		ageField = new JTextField();
		ageField.setBounds(117, 156, 117, 26);
		MemH.add(ageField);
		ageField.setColumns(10);
		
		JLabel lblNewAge = new JLabel("New Age");
		lblNewAge.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewAge.setBounds(145, 128, 61, 16);
		MemH.add(lblNewAge);
		
		frame.setVisible(true);
		
	}
	
	public Connection getConnection(){
		
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + schema, username, password);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	
	public void appendText(String s){
		textArea.append(s);	
	}
	
	public void clearText(){
		textArea.setText("");
	}
	
	public void setCon(Controller control){
		c = control;
	}

}
