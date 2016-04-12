package model;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.JTextField;
import javax.swing.ButtonGroup;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.JTabbedPane;
import javax.swing.JCheckBox;
import javax.swing.JToggleButton;

public class Display {
	
	private JFrame frame;
	private JPanel panelRR;
	private JPanel textpanel;
	private JTextArea textArea;
	private JTextArea PtextArea;
	private JScrollPane scroll;
	private JScrollPane Pscroll;
	private Connection con;
	private Controller c;
	private JRadioButton rdbtnRUT1;
	private JRadioButton rdbtnRCT1;	
	private JRadioButton rdbtnRRT1;
	private JRadioButton rdbtnST1;
	private ButtonGroup groupRRL;
	private ButtonGroup groupRRDR;
	private ButtonGroup groupRRWR;
	private JComboBox schemaCB;
	private JTextField ageField;
	private JComboBox memCB;
	private String type;
	private JCheckBox chckbxAbort;
	private JToggleButton tglbtnToggleDoubleRead;
	private JLabel lblIsolationLevels;
	private JRadioButton rdbtnRUDR;
	private JRadioButton rdbtnRCDR;
	private JRadioButton rdbtnRepReaDR;
	private JRadioButton rdbtnSDR;
	private JTextField ageField2;
	private JComboBox memCB2;
	private JComboBox schemaCB2;
	private JPanel panelWR;
	private JPanel panelProgText;
	private JLabel label_2;
	private JRadioButton rdbtnRUWR;
	private JRadioButton rdbtnRCWR;
	private JRadioButton rdbtnRRWR ;
	private JRadioButton rdbtnSWR;
	private JComboBox comboBoxLWR;
	private JLabel label_3;
	private JTextField textFieldLWR;
	private JLabel label_4;
	private JComboBox schemacbGWR;
	private JComboBox memcbGWR;
	private JLabel label_5;
	private JTextField textFieldGWR;
	private JButton btnGWR;
	private JCheckBox AbortWR;
	
	public Display(Controller c){
		
		this.c = c;
		type = c.getType();
		frame = new JFrame("ADVANDB MCO3");
		frame.setBounds(0, 0, 1300, 750);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		panelRR = new JPanel();
		panelRR.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panelRR.setBounds(6, 6, 310, 524);
		frame.getContentPane().add(panelRR);
		panelRR.setLayout(null);
		
		JButton btnTransaction = new JButton("Global Read");
		btnTransaction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {	
				clearText();
				c.readGlobal();
			}
		});
		btnTransaction.setBounds(97, 14, 141, 29);
		panelRR.add(btnTransaction);
		
		JLabel lblSelectIsolationLevel = new JLabel(type + " Isolation Levels");
		lblSelectIsolationLevel.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelectIsolationLevel.setBounds(78, 55, 175, 16);
		panelRR.add(lblSelectIsolationLevel);
		
		rdbtnRUT1 = new JRadioButton("Read Uncommitted");
		rdbtnRUT1.setBounds(78, 83, 160, 23);
		panelRR.add(rdbtnRUT1);
		
		rdbtnRCT1 = new JRadioButton("Read Committed");
		rdbtnRCT1.setBounds(78, 118, 141, 23);
		panelRR.add(rdbtnRCT1);
		
		rdbtnRRT1 = new JRadioButton("Repeatable Read");
		rdbtnRRT1.setBounds(78, 153, 141, 23);
		panelRR.add(rdbtnRRT1);
		
		rdbtnST1 = new JRadioButton("Serializable");
		rdbtnST1.setBounds(78, 188, 141, 23);
		panelRR.add(rdbtnST1);
		
		groupRRL = new ButtonGroup();
		groupRRL.add(rdbtnRUT1);
		groupRRL.add(rdbtnRCT1);
		groupRRL.add(rdbtnRRT1);
		groupRRL.add(rdbtnST1);
		
		JButton btnLocalReadRR = new JButton("Local Read");
		btnLocalReadRR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				clearText();
				
				if(groupRRL.getSelection() == null)
					c.readLocal(0);
				
				if(rdbtnRUT1.isSelected() == true){
					textArea.append("\nRead Uncommitted Selected");
					c.readLocal(1);
				}
				
				if(rdbtnRCT1.isSelected() == true){
					textArea.append("\nRead Committed Selected");
					c.readLocal(2);
				}
				
				if(rdbtnRRT1.isSelected() == true){
					textArea.append("\nRepeatable Read Selected");
					c.readLocal(3);
				}
				
				if(rdbtnST1.isSelected() == true){
					textArea.append("\nSerializable Selected");
					c.readLocal(4);
				}		
				
			}
		});
		btnLocalReadRR.setBounds(110, 223, 117, 29);
		panelRR.add(btnLocalReadRR);
		
		JButton btnDoubleRead = new JButton("Double Read");
		btnDoubleRead.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int t1 = 0;
				int t2 = 0;
				clearText();
				if(rdbtnRUT1.isSelected() == true){
					textArea.append("\nRead Uncommitted Selected");
					t1 = 1;
				}
				
				if(rdbtnRCT1.isSelected() == true){
					textArea.append("\nRead Committed Selected");
					t1 = 2;
				}
				
				if(rdbtnRRT1.isSelected() == true){
					textArea.append("\nRepeatable Read Selected");
					t1 = 3;
				}
				
				if(rdbtnST1.isSelected() == true){
					textArea.append("\nSerializable Selected");
					t1 = 4;
				}
				
				if(rdbtnRUDR.isSelected() == true){
					textArea.append("\nRead Uncommitted Selected");
					t2 = 1;
				}
				
				if(rdbtnRCDR.isSelected() == true){
					textArea.append("\nRead Committed Selected");
					t2 = 2;
				}
				
				if(rdbtnRepReaDR.isSelected() == true){
					textArea.append("\nRepeatable Read Selected");
					t2 = 3;
				}
				
				if(rdbtnSDR.isSelected() == true){
					textArea.append("\nSerializable Selected");
					t2 = 4;
				}	
				
				c.readDoubleLocal(t1, t2);
			}
		});
		btnDoubleRead.setBounds(102, 473, 117, 29);
		panelRR.add(btnDoubleRead);
		
		lblIsolationLevels = new JLabel(c.getOtherType() + " Isolation Levels");
		lblIsolationLevels.setHorizontalAlignment(SwingConstants.CENTER);
		lblIsolationLevels.setBounds(78, 264, 175, 16);
		panelRR.add(lblIsolationLevels);
		
		rdbtnRUDR = new JRadioButton("Read Uncommitted");
		rdbtnRUDR.setBounds(78, 292, 180, 23);
		panelRR.add(rdbtnRUDR);
		
		rdbtnRCDR = new JRadioButton("Read Committed");
		rdbtnRCDR.setBounds(78, 327, 175, 23);
		panelRR.add(rdbtnRCDR);
		
		rdbtnRepReaDR = new JRadioButton("Repeatable Read");
		rdbtnRepReaDR.setBounds(78, 362, 160, 23);
		panelRR.add(rdbtnRepReaDR);
		
		rdbtnSDR = new JRadioButton("Serializable");
		rdbtnSDR.setBounds(78, 397, 141, 23);
		panelRR.add(rdbtnSDR);
		
		groupRRDR = new ButtonGroup();
		groupRRDR.add(rdbtnRUDR);
		groupRRDR.add(rdbtnRCDR);
		groupRRDR.add(rdbtnRepReaDR);
		groupRRDR.add(rdbtnSDR);
		
		
		textpanel = new JPanel();
		textpanel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		textpanel.setBounds(648, 6, 629, 524);
		frame.getContentPane().add(textpanel);
		
		textArea = new JTextArea();
		textArea.setBounds(6, 5, 360, 460);
		textArea.setVisible(true);
		textpanel.setLayout(null);
		textArea.setEditable(false);
		
		scroll = new JScrollPane(textArea);
		scroll.setBounds(6, 5, 619, 513);
		textpanel.add(scroll);
		
		JPanel panelWW = new JPanel();
		panelWW.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panelWW.setBounds(6, 542, 630, 167);
		frame.getContentPane().add(panelWW);
		panelWW.setLayout(null);
		
		JButton btnWrite = new JButton("Write");
		btnWrite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearText();
				
				Thread T1 = new Thread(){
					  public void run () {
						  
						  Object selected = schemaCB.getSelectedItem();
							
							if(chckbxAbort.isSelected() == true){
								selected = "xxxxx";
							try {
								Thread.sleep(2000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							appendText("TRANSACTION ABORTED");}
								
							if(selected.toString().equals("Marinduque")){
								c.writeGlobal(0, (String) schemaCB2.getSelectedItem(), (Integer) memCB2.getSelectedItem(), Integer.parseInt(ageField2.getText()));}
							
							if(selected.toString().equals("Palawan")){
								c.writeGlobal(0, (String) schemaCB2.getSelectedItem(), (Integer) memCB2.getSelectedItem(), Integer.parseInt(ageField2.getText()));}
	
					  }
					};
				
					Thread T2 = new Thread(){
						  public void run () {
							  
							  Object selected = schemaCB2.getSelectedItem();
								
								if(chckbxAbort.isSelected() == true){
									selected = "xxxxx";
								try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								appendText("TRANSACTION ABORTED");}
									
								
								if(selected.toString().equals("Marinduque")){
									c.writeGlobal(0, (String) schemaCB2.getSelectedItem(), (Integer) memCB2.getSelectedItem(), Integer.parseInt(ageField2.getText()));}
								
								if(selected.toString().equals("Palawan")){
									c.writeGlobal(0, (String) schemaCB2.getSelectedItem(), (Integer) memCB2.getSelectedItem(), Integer.parseInt(ageField2.getText()));}
		
						  }
						};
			T1.start();
			T2.start();

			}
		});
		btnWrite.setBounds(255, 128, 117, 29);
		panelWW.add(btnWrite);
		
		JLabel lblSelectSchema = new JLabel("Select Schema");
		lblSelectSchema.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelectSchema.setBounds(117, 6, 117, 16);
		panelWW.add(lblSelectSchema);
		
		schemaCB = new JComboBox();
		schemaCB.setBounds(97, 23, 157, 27);
		panelWW.add(schemaCB);
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
                	appendProgress("\nSelect a region");
                }
                	
            }
        });
		
		memCB = new JComboBox();
		memCB.setEnabled(false);
		memCB.setBounds(97, 56, 157, 27);
		panelWW.add(memCB);
		
		ageField = new JTextField();
		ageField.setBounds(117, 103, 117, 26);
		panelWW.add(ageField);
		ageField.setColumns(10);
		
		JLabel lblNewAge = new JLabel("New Age");
		lblNewAge.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewAge.setBounds(145, 84, 61, 16);
		panelWW.add(lblNewAge);
		
		chckbxAbort = new JCheckBox("Abort");
		chckbxAbort.setBounds(281, 104, 67, 23);
		panelWW.add(chckbxAbort);
		
		ageField2 = new JTextField();
		ageField2.setColumns(10);
		ageField2.setBounds(393, 103, 117, 26);
		panelWW.add(ageField2);
		
		JLabel label = new JLabel("New Age");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(421, 84, 61, 16);
		panelWW.add(label);
		
		memCB2 = new JComboBox();
		memCB2.setEnabled(false);
		memCB2.setBounds(373, 56, 157, 27);
		panelWW.add(memCB2);
		
		schemaCB2 = new JComboBox();
		schemaCB2.setBounds(373, 23, 157, 27);
		panelWW.add(schemaCB2);
		schemaCB2.addItem("--");
		schemaCB2.addItem("Palawan");
		schemaCB2.addItem("Marinduque");
		schemaCB2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {

                JComboBox schemaCB = (JComboBox) event.getSource();

                Object selected = schemaCB.getSelectedItem();
                if(selected.toString().equals("Marinduque")){
                	memCB2.removeAllItems();
                	memCB2.setEnabled(true);
                	memCB2.addItem(1);
                	memCB2.addItem(2);
                	memCB2.addItem(3);
                	memCB2.addItem(4);
                	memCB2.addItem(5);
                }
                
                if(selected.toString().equals("Palawan")){
                	memCB2.removeAllItems();
                	memCB2.setEnabled(true);
                	memCB2.addItem(1);
                	memCB2.addItem(2);
                	memCB2.addItem(3);
                }
                
                if(selected.toString().equals("--")){
                	appendProgress("\nSelect a region");
                }
                	
            }
        });
		
		JLabel label_1 = new JLabel("Select Schema");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setBounds(393, 6, 117, 16);
		panelWW.add(label_1);
		
		panelWR = new JPanel();
		panelWR.setBorder(new LineBorder(Color.LIGHT_GRAY));
		panelWR.setBounds(328, 6, 308, 524);
		frame.getContentPane().add(panelWR);
		panelWR.setLayout(null);
		
		JLabel lblLocalReadwrite = new JLabel("Local Read/Write");
		lblLocalReadwrite.setBounds(106, 16, 106, 16);
		panelWR.add(lblLocalReadwrite);
		
		label_2 = new JLabel(c.getType() + " Isolation Levels");
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		label_2.setBounds(71, 44, 175, 16);
		panelWR.add(label_2);
		
		rdbtnRUWR = new JRadioButton("Read Uncommitted");
		rdbtnRUWR.setBounds(71, 72, 160, 23);
		panelWR.add(rdbtnRUWR);
		
		rdbtnRCWR = new JRadioButton("Read Committed");
		rdbtnRCWR.setBounds(71, 107, 141, 23);
		panelWR.add(rdbtnRCWR);
		
		rdbtnRRWR = new JRadioButton("Repeatable Read");
		rdbtnRRWR.setBounds(71, 142, 141, 23);
		panelWR.add(rdbtnRRWR);
		
		rdbtnSWR = new JRadioButton("Serializable");
		rdbtnSWR.setBounds(71, 177, 141, 23);
		panelWR.add(rdbtnSWR);
		
		groupRRWR = new ButtonGroup();
		groupRRWR.add(rdbtnRCWR);
		groupRRWR.add(rdbtnRUWR);
		groupRRWR.add(rdbtnRRWR);
		groupRRWR.add(rdbtnSWR);
		
		comboBoxLWR = new JComboBox();
		comboBoxLWR.setBounds(71, 212, 141, 27);
		panelWR.add(comboBoxLWR);
        if(c.getType().equals("Marinduque")){
        	comboBoxLWR.removeAllItems();
        	comboBoxLWR.setEnabled(true);
        	comboBoxLWR.addItem(1);
        	comboBoxLWR.addItem(2);
        	comboBoxLWR.addItem(3);
        	comboBoxLWR.addItem(4);
        	comboBoxLWR.addItem(5);
        }
        
        if(c.getType().equals("Palawan")){
        	comboBoxLWR.removeAllItems();
        	comboBoxLWR.setEnabled(true);
        	comboBoxLWR.addItem(1);
        	comboBoxLWR.addItem(2);
        	comboBoxLWR.addItem(3);
        }
        
        if(c.getType().equals("Central")){
        	comboBoxLWR.removeAllItems();
        	comboBoxLWR.setEnabled(true);
        	comboBoxLWR.addItem("Palawan");
        	comboBoxLWR.addItem(1);
        	comboBoxLWR.addItem(2);
        	comboBoxLWR.addItem(3);
        	comboBoxLWR.addItem(4);
        	comboBoxLWR.addItem(5);
        	comboBoxLWR.addItem("Marinduque");
        	comboBoxLWR.addItem(1);
        	comboBoxLWR.addItem(2);
        	comboBoxLWR.addItem(3);
        }
        
		
		label_3 = new JLabel("New Age");
		label_3.setHorizontalAlignment(SwingConstants.CENTER);
		label_3.setBounds(113, 244, 61, 16);
		panelWR.add(label_3);
		
		textFieldLWR = new JTextField();
		textFieldLWR.setColumns(10);
		textFieldLWR.setBounds(86, 263, 117, 26);
		panelWR.add(textFieldLWR);
		
		JButton btnLWR = new JButton("Local Read and Write");
		btnLWR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearText();
				Thread T1 = new Thread(){
					  public void run () {
						  	
							
							if(groupRRWR.getSelection() == null)
								c.readLocal(0);
							
							if(rdbtnRUWR.isSelected() == true){
								textArea.append("\nRead Uncommitted Selected");
								c.readLocal(1);
							}
							
							if(rdbtnRCWR.isSelected() == true){
								textArea.append("\nRead Committed Selected");
								c.readLocal(2);
							}
							
							if(rdbtnRRWR.isSelected() == true){
								textArea.append("\nRepeatable Read Selected");
								c.readLocal(3);
							}
							
							if(rdbtnSWR.isSelected() == true){
								textArea.append("\nSerializable Selected");
								c.readLocal(4);
							}
					  }
					};
				
					Thread T2 = new Thread(){
						  public void run () {
							  
							  String tempType = c.getType();
							  
								if(AbortWR.isSelected() == true){
									tempType = "xxxxx";
									try {
										Thread.sleep(2000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									appendText("TRANSACTION ABORTED");
									
								}
									
								
								if(tempType.toString().equals("Marinduque")){
									c.writeGlobal(0, "Marinduque", (Integer) comboBoxLWR.getSelectedItem(), Integer.parseInt(textFieldLWR.getText()));}
								
								if(tempType.toString().equals("Palawan")){
									c.writeGlobal(0, "Palawan", (Integer) comboBoxLWR.getSelectedItem(), Integer.parseInt(textFieldLWR.getText()));}
							  
								if(tempType.toString().equals("Central")){
									
									if((Integer) comboBoxLWR.getSelectedItem() <= 3)
									c.writeGlobal(0, "Marinduque", (Integer) comboBoxLWR.getSelectedItem(), Integer.parseInt(textFieldLWR.getText()));
									
								}
								
								if(tempType.toString().equals("Central")){
									
									if((Integer) comboBoxLWR.getSelectedItem() > 3)
									c.writeGlobal(0, "Palawan", (Integer) comboBoxLWR.getSelectedItem(), Integer.parseInt(textFieldLWR.getText()));
									
								}
								
						  }
						};
						
					T1.start();
					T2.start();
			}
		});
		btnLWR.setBounds(60, 295, 175, 29);
		panelWR.add(btnLWR);
		
		label_4 = new JLabel("Select Schema");
		label_4.setHorizontalAlignment(SwingConstants.CENTER);
		label_4.setBounds(91, 336, 117, 16);
		panelWR.add(label_4);
		
		schemacbGWR = new JComboBox();
		schemacbGWR.setBounds(71, 353, 157, 27);
		panelWR.add(schemacbGWR);
		schemacbGWR.addItem("--");
		schemacbGWR.addItem("Palawan");
		schemacbGWR.addItem("Marinduque");
		schemacbGWR.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {

                JComboBox schemacbGWR = (JComboBox) event.getSource();

                Object selected = schemacbGWR.getSelectedItem();
                if(selected.toString().equals("Marinduque")){
                	memcbGWR.removeAllItems();
                	memcbGWR.setEnabled(true);
                	memcbGWR.addItem(1);
                	memcbGWR.addItem(2);
                	memcbGWR.addItem(3);
                	memcbGWR.addItem(4);
                	memcbGWR.addItem(5);
                }
                
                if(selected.toString().equals("Palawan")){
                	memcbGWR.removeAllItems();
                	memcbGWR.setEnabled(true);
                	memcbGWR.addItem(1);
                	memcbGWR.addItem(2);
                	memcbGWR.addItem(3);
                }
                
                if(selected.toString().equals("--")){
                	appendProgress("\nSelect a region");
                }
                	
            }
        });
		
		memcbGWR = new JComboBox();
		memcbGWR.setEnabled(false);
		memcbGWR.setBounds(71, 382, 157, 27);
		panelWR.add(memcbGWR);
		
		label_5 = new JLabel("New Age");
		label_5.setHorizontalAlignment(SwingConstants.CENTER);
		label_5.setBounds(119, 409, 61, 16);
		panelWR.add(label_5);
		
		textFieldGWR = new JTextField();
		textFieldGWR.setColumns(10);
		textFieldGWR.setBounds(91, 428, 117, 26);
		panelWR.add(textFieldGWR);
		
		btnGWR = new JButton("Global Read and Write");
		btnGWR.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearText();
				Thread T1 = new Thread(){
					  public void run () {
							c.readGlobal();
					 }
				};
				
				Thread T2 = new Thread(){
					  public void run () {
						  
						  String tempType = c.getType();
						  
							if(AbortWR.isSelected() == true){
								tempType = "xxxxx";
								try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								appendText("TRANSACTION ABORTED");
								
							}
							
							Object selected = schemacbGWR.getSelectedItem();
							
							if(tempType.equals("Marinduque")){
								c.writeGlobal(0, (String) schemacbGWR.getSelectedItem(), (Integer) memcbGWR.getSelectedItem(), Integer.parseInt(textFieldGWR.getText()));}
							
							if(tempType.equals("Palawan")){
								c.writeGlobal(0, (String) schemacbGWR.getSelectedItem(), (Integer) memcbGWR.getSelectedItem(), Integer.parseInt(textFieldGWR.getText()));}
							
							if(tempType.equals("Central")){
								c.writeGlobal(0, (String) schemacbGWR.getSelectedItem(), (Integer) memcbGWR.getSelectedItem(), Integer.parseInt(textFieldGWR.getText()));}
							
					
							
					 }
				};
				
				T1.start();
				T2.start();
				
			}
		});
		btnGWR.setBounds(62, 482, 169, 29);
		panelWR.add(btnGWR);
		
		AbortWR = new JCheckBox("Abort");
		AbortWR.setBounds(110, 457, 76, 23);
		panelWR.add(AbortWR);

		panelProgText = new JPanel();
		panelProgText.setBounds(648, 542, 629, 167);
		frame.getContentPane().add(panelProgText);
		
		PtextArea = new JTextArea();
		PtextArea.setBounds(6, 5, 360, 460);
		PtextArea.setVisible(true);
		panelProgText.setLayout(null);
		PtextArea.setEditable(false);
		
		Pscroll = new JScrollPane(PtextArea);
		Pscroll.setBounds(6, 5, 619, 156);
		panelProgText.add(Pscroll);
		frame.setVisible(true);
		
	}
	
	public void appendText(String s){
		textArea.append(s);	
	}
	
	public void clearText(){
		textArea.setText("");
	}
	
	public void setCon(Controller controller){
		c = controller;
	}
	
	public void appendProgress(String s){
		PtextArea.append(s);
	}
}
