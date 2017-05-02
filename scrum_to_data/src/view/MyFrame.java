package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyFrame extends JFrame {

	//the curv JPanel needs to display a curve : see JFreeChart !
	private JPanel disp, dispCurvCard, dispTabCard, curv;
	private JTable viewTab;
	private JButton dbManagement, dispTab, dispCurv, quit;
	private JComboBox opeChoice;
	private JFileChooser fc;
	private String textCombo[] = { "No Operation !" };

	public MyFrame() {
		super();
		setTitle("Scrum data");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.initialise();
		this.position();
		this.addListeners();
		pack();
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
	}

	/**
	 * Initialize all components
	 */
	private void initialise() {
		disp = new JPanel(new CardLayout());
		dispTabCard = new JPanel();
		dispCurvCard = new JPanel();
		dbManagement = new JButton("Database management");
		dispTab = new JButton("Table");
		dispCurv = new JButton("Curve");
		quit = new JButton("Quit");
		
		//just an example !!!!!
		viewTab = new JTable();
		
		fc = new JFileChooser();
		opeChoice = new JComboBox(textCombo);
		opeChoice.setEnabled(false);
	}

	
	/**
	 * To put all components on the right place
	 */
	private void position() {
		/*
		 * Left panel
		 */
		JPanel left = new JPanel();

		left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
		left.setPreferredSize(new Dimension(400, 800));
		left.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));

		// dbManagement button
		JPanel leftTop = new JPanel();
		leftTop.setLayout(new BoxLayout(leftTop, BoxLayout.PAGE_AXIS));
		dbManagement.setAlignmentX(Component.LEFT_ALIGNMENT);
		leftTop.add(dbManagement);
		leftTop.add(Box.createRigidArea(new Dimension(0, 30)));


		//opeChoice comboBox
		JPanel leftBottom = new JPanel();
		leftBottom.setLayout(new BoxLayout(leftBottom, BoxLayout.Y_AXIS));
		leftBottom.setPreferredSize(new Dimension(400, 400));

		opeChoice.setAlignmentX(Component.LEFT_ALIGNMENT);
		leftBottom.add(Box.createRigidArea(new Dimension(0, 100)));
		leftBottom.add(new JLabel("Choose operation:"));
		leftBottom.add(opeChoice);
		leftBottom.add(Box.createRigidArea(new Dimension(0, 20)));

		// ajout de tous les panels dans gauche
		leftTop.setAlignmentX(Component.LEFT_ALIGNMENT);
		leftBottom.setAlignmentX(Component.LEFT_ALIGNMENT);

		left.add(leftTop);
		left.add(leftBottom);

		/*
		 * right panel
		 */
		disp.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(),
				BorderFactory.createEmptyBorder(10, 10, 10, 10)));

		JScrollPane tab = new JScrollPane(viewTab);
		tab.setPreferredSize(new Dimension(1000, 500));
		dispTabCard.add(tab);
		disp.add(dispTabCard, "Table");

		curv = new JPanel();
		dispCurvCard.add(curv);
		disp.add(dispCurvCard, "Curve");

		// bottom panel
		JPanel bottom = new JPanel();
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.PAGE_AXIS));
		bottom.setPreferredSize(new Dimension(1100, 200));
		dispTab.setAlignmentY(Component.TOP_ALIGNMENT);
		dispCurv.setAlignmentY(Component.TOP_ALIGNMENT);
		dispTab.setAlignmentX(Component.CENTER_ALIGNMENT);
		dispCurv.setAlignmentX(Component.CENTER_ALIGNMENT);
		JPanel ctnSaveQuit = new JPanel();
		JPanel ctnChoice = new JPanel();
		ctnSaveQuit.setLayout(new BoxLayout(ctnSaveQuit, BoxLayout.LINE_AXIS));
		ctnChoice.setLayout(new BoxLayout(ctnChoice, BoxLayout.LINE_AXIS));
		quit.setAlignmentX(Component.RIGHT_ALIGNMENT);
		ctnSaveQuit.add(quit);
		ctnChoice.add(Box.createRigidArea(new Dimension(0, 100)));
		ctnChoice.add(dispTab);
		ctnChoice.add(Box.createRigidArea(new Dimension(10, 0)));
		ctnChoice.add(dispCurv);
		bottom.add(ctnChoice);
		bottom.add(ctnSaveQuit);

		JPanel right = new JPanel();
		right.setLayout(new BoxLayout(right, BoxLayout.PAGE_AXIS));
		right.setPreferredSize(new Dimension(1100, 800));
		right.add(disp);
		right.add(bottom);

		// add all panel in the main frame
		this.add(left, BorderLayout.LINE_START);
		this.add(right, BorderLayout.CENTER);
	}

	// add listeners
	private void addListeners() {
		dbManagement.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				dbManagementActionPerformed(evt);
			}
		});

		opeChoice.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				opeChoiceActionPerformed(evt);
			}
		});

		dispTab.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				dispTabActionPerformed(evt);
			}
		});

		dispCurv.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				dispCurvActionPerformed(evt);
			}
		});

		quit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				quitActionPerformed(evt);
			}
		});

	}
	
	private void dbManagementActionPerformed(ActionEvent e){
		//open an another frame (DBManagementFrame)
	}

	private void dispTabActionPerformed(ActionEvent e) {
		CardLayout cl = (CardLayout) (disp.getLayout());
		cl.show(disp, "Table");
	}

	private void dispCurvActionPerformed(ActionEvent e) {
		CardLayout cl = (CardLayout) (disp.getLayout());
		cl.show(disp, "Curve");
	}

	private void opeChoiceActionPerformed(ActionEvent e){
		//todo
	}

	private void quitActionPerformed(ActionEvent evt) {
		this.dispose();
	}
}