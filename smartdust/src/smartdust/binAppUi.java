package smartdust;

import java.awt.*;
import java.awt.event.*;
import javax.swing.BorderFactory;
import javax.swing.*;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class binAppUi extends JFrame{
	private JTextArea txArea;
	private Sserver ss;
	private JScrollPane scroll;
	private JPanel jpanel1;
	private JButton startButton;
	private JButton showdata;
	private JButton clearlog;
	private JButton changeadd;
	private JButton pre;
	/**
	 * 
	 */
	private static final long serialVersionUID = 42516066012575311L;

	public binAppUi() {
		this.setLocation(400, 100);
		addWindowListener(new WindowAdapter(){  
	           public void windowClosing(WindowEvent e) {
	        	   if(ss!=null)
	        	   	ss.destroy();
	               dispose();
	           }  
	       });
		setSize(650,400);//frame size 300 width and 300 height  
		setLayout(null);//no layout manager  
		createArea();
		createButton();
		this.setTitle("Smart Dustbin - blkCapHax");
		this.setResizable(false);
		setVisible(true);
	}
	
	private void createButton() {
		JPanel jpanel = new JPanel();
		jpanel.setLayout(null);
		jpanel.setBackground(Color.white);
		jpanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		jpanel.setBounds(500, 0, 150, 400);
		
		startButton = new JButton();
		if(ss==null || !ss.isAlive()) {
			startButton.setText("Start");;
		}
		
		Insets insets = jpanel.getInsets();
		Dimension dimension = jpanel.getPreferredSize();
		startButton.setBounds(20+insets.left, 10+insets.top, (int)Math.round((dimension.width)*0.7),(int)Math.round((dimension.height)*0.09));
		
		showdata = new JButton("Show Data");
		showdata.setBounds(20+insets.left, 60+insets.top, (int)Math.round((dimension.width)*0.7),(int)Math.round((dimension.height)*0.09));
		
		clearlog = new JButton("Clear Log");
		clearlog.setBounds(20+insets.left, 110+insets.top, (int)Math.round((dimension.width)*0.7),(int)Math.round((dimension.height)*0.09));
		
		pre = new JButton("Prefrence");
		pre.setBounds(20+insets.left, 160+insets.top, (int)Math.round((dimension.width)*0.7),(int)Math.round((dimension.height)*0.09));
		
		changeadd = new JButton("change add");
		changeadd.setBounds(20+insets.left, 210+insets.top, (int)Math.round((dimension.width)*0.7),(int)Math.round((dimension.height)*0.09));
		
		jpanel.add(startButton);
		jpanel.add(showdata);
		jpanel.add(pre);
		jpanel.add(clearlog);
		jpanel.add(changeadd);
		this.add(jpanel);
		
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(ss==null || !ss.isAlive()){
					ss = new Sserver(8070);
					ss.start();
					startButton.setText("Stop");
				}else {
					ss.destroy();
					startButton.setText("Start");
					ss = null;
				}
				
			}
			
		});
		
		showdata.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String str = "Bin statics...\n";
				str += " Bin Id \t Bin Status \n\n";
				str += " fdfdf \t vfvfvfv\n";
				smartbin.appui.tappend(str);
			}
			
		});
		
		clearlog.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				txArea.setText("Log...............\n");
			}
			
		});
		
		changeadd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				smartbin.db.changeadd();
			}
		});
	}
	
	private void createArea() {
		FlowLayout flowlayout = new FlowLayout(FlowLayout.LEFT);
		
		jpanel1 = new JPanel();
		jpanel1.setLayout(flowlayout);
		jpanel1.setBounds(0, 0, 500, 400);
		jpanel1.setBackground(Color.white);
		jpanel1.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		//jpanel1.setBackground(Color.CYAN);
		
		txArea = new JTextArea(20,43);
		txArea.setText("Log...............\n");
		txArea.setBackground(Color.white);
		Font font = new Font("Times New Roman", Font.PLAIN, 15);
		txArea.setFont(font);
		txArea.setBorder(BorderFactory.createLineBorder(Color.gray));
		txArea.setForeground(new Color(184,48,19));
		txArea.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		DefaultCaret cr = (DefaultCaret) txArea.getCaret();
		cr.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		txArea.setLineWrap(true);
		
		scroll = new JScrollPane(txArea); 
		
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setBounds(0,0,500,500);
		
		txArea.setEditable(false);
		jpanel1.add(scroll);
		this.add(jpanel1);
	}
	
	public void append(String str) {
		txArea.append(">> "+str+"\n");
		//scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
		txArea.setCaretPosition(txArea.getDocument().getLength());
	}
	public void tappend(String str) {
		txArea.append(">> "+str);
		//scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
		txArea.setCaretPosition(txArea.getDocument().getLength());
	}
	
	class scrollistener implements AdjustmentListener{

		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			// TODO Auto-generated method stub
			e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
		}
		
	}
}
