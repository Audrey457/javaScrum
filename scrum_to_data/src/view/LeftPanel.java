package view;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LeftPanel extends JPanel {
	
	private JComboBox<String> opeChoice;
	private JTextField userInput;
	private JButton validate;
	private GridLayout layout;
	
	public LeftPanel(){
		super();
		this.initialise();
		this.setLayout(this.layout);
		this.addListeners();
		this.position();
	}
	
	public void initialise(){
		layout = new GridLayout(0, 1, 10, 10);
		opeChoice = new JComboBox<>();
		opeChoice.addItem(" ");
		
		userInput = new JTextField(20);
		userInput.setVisible(false);
		validate = new JButton("ok");
		validate.setVisible(false);
	}
	
	public void addListeners(){
		opeChoice.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox cb = (JComboBox) e.getSource();
				int selectedIndex = cb.getSelectedIndex();
				if(selectedIndex != 0){
					
				}else{
					setInputVisible(false);;
				}
			}
		});
		
	}
	
	public void setInputVisible(boolean inputNeeded){
		userInput.setVisible(inputNeeded);
		validate.setVisible(inputNeeded);
	}
	
	public void position(){
		JLabel comboBoxLabel = new JLabel("Choose operation");
		
		this.add(comboBoxLabel);
		this.add(opeChoice);
		this.add(this.userInput);
		this.add(this.validate);
	}

}
