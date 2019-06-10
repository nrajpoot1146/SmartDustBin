package smartdust;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;

import javax.swing.*;

public class prefrence extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7019879223457337530L;

	public prefrence(){
		this.setLocation(400, 100);
		this.setSize(300,300);
		createtxtv();
		this.setVisible(true);
	}
	
	public void createtxtv() {
		JPanel jpanel = new JPanel();
		jpanel.setLayout(new FlowLayout());
		jpanel.setBounds(0,0,200,300);
		
		Insets insets = jpanel.getInsets();
		Dimension dimension = jpanel.getPreferredSize();
		
		JTextField jtdbHost= new JTextField();
		jtdbHost.setBounds(20+insets.left, 110+insets.top, (int)Math.round((dimension.width)*0.8),(int)Math.round((dimension.height)*0.9));
		JTextField jtdbPort= new JTextField();
		jtdbPort.setBounds(20+insets.left, 110+insets.top, (int)Math.round((dimension.width)*0.7),(int)Math.round((dimension.height)*0.09));
		jtdbHost.setSize(300, 100);;
		jpanel.add(jtdbHost);
		jpanel.add(jtdbPort);
		this.add(jpanel);
		
	}
}
