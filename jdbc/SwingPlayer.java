package jdbc;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.day11.MessageBox;

public class SwingPlayer  extends JFrame{
	Connection con;
	private JTextField[] tf = new JTextField[4];
	
	public SwingPlayer() {
		dbCon(); // ��񿬰�
		setLayout(new GridLayout(2, 2));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container c = getContentPane();
		
		c.add(new PlayerPanel()); //1��1��
		
		JTextArea ta = new JTextArea(); //1��2��
		JScrollPane jsp = new JScrollPane(ta);
		c.add(jsp);
		
		JButton insertBtn = new JButton("�߰�");// 2��1��
		JButton viewBtn = new JButton("����");
		JButton updateBtn = new JButton("����");
		JButton deleteBtn = new JButton("����");
		JLabel lblNum = new JLabel("");
		lblNum.setVisible(false);
		JPanel p1= new JPanel();
		p1.add(insertBtn);
		p1.add(viewBtn);
		p1.add(updateBtn);
		p1.add(deleteBtn);
		p1.add(lblNum);
		c.add(p1);
		
		//�߰�
		insertBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String sql = "insert into player values(player_seq.nestval,?,?,?,?";
				try {
					PreparedStatement  ps = con.prepareStatement(sql);
					ps.setString(1, tf[0].getText());
					ps.setDouble(2, Double.parseDouble(tf[1].getText()));
					ps.setDouble(3,Double.parseDouble(tf[2].getText()));
					ps.setNString(4, tf[3].getText());
					ps.executeUpdate();
					viewBtn.doClick();
					
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				
				
			}
		});
		//����
		viewBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ta.setText("");
				String sql = "select * from player order by num desc";
				try {
					Statement st= con.createStatement();
					ResultSet rs = st.executeQuery(sql);
					while(rs.next()) {
						ta.append("��ȣ : " +rs.getInt("num")+"\n");
						ta.append("�̸� : " +rs.getString("name")+"\n");
						ta.append("���� : " +rs.getString("kind")+"\n\n");
					}
					
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
				
			}
		});
		//����
		updateBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String sql = "update player set name = ?,height = ?,weight =?,kind =?,where num = ?,";
				try {
					PreparedStatement ps = con.prepareStatement(sql);
					ps.setString(1, tf[0].getText());
					ps.setDouble(2, Double.parseDouble(tf[1].getText()));
					ps.setDouble(3, Double.parseDouble(tf[2].getText()));
					ps.setString(4, tf[3].getText());
					ps.setInt(5, Integer.parseInt(lblNum.getText()));
					ps.executeUpdate();
					viewBtn.doClick();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		//����
		deleteBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int num = Integer.parseInt(lblNum.getText());
				int i=JOptionPane.showConfirmDialog(null, "���� �����ұ��?","confirm",JOptionPane.YES_NO_OPTION);
				if (i==JOptionPane.NO_OPTION)return;
				String sql = "delete from player where num ="+num;
				try {
					Statement st =con.createStatement();
					st.executeUpdate(sql);
					viewBtn.doClick();
					
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		ta.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				try{
				lblNum.setVisible(true);
				lblNum.setText(ta.getSelectedText());
				int num = Integer.parseInt(lblNum.getText());
				String sql = "select * from player where num ="+num;
				Statement st =con.createStatement();
				ResultSet rs = st.executeQuery(sql);
				if(rs.next()) {
					tf[0].setText(rs.getString("name"));
					tf[1].setText(rs.getDouble("height")+"");
					tf[2].setText(rs.getDouble("weight")+"");
					tf[3].setText(rs.getString("kind"));
				}
				}catch (NumberFormatException | NullPointerException n) {
						 new MessageBox("����!!!", "��ȣ�� �����ϼ���");
					} catch (SQLException e1) {
						 e1.printStackTrace();
					}	
					
				}
			
		});
		JPanel p2 = new JPanel(); //2��2��
       JComboBox<String> jcb = new JComboBox<String>();
       jcb.addItem("�̸�");
       jcb.addItem("����");
       p2.add(jcb);
       JTextField tfSearch = new JTextField(10);
       p2.add(tfSearch);
       JButton searchBtn = new JButton("�˻�");
       p2.add(searchBtn);
		
		c.add(p2);
		searchBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				ta.setText("");
				if(tfSearch.getText().isEmpty()) {
					new MessageBox("�˻�","�˻�� �Է��ϼ���");
						return;
				}
//				String key = "";
//				if(jcb.getSelectedIndex()==0) {
//					key = "name";
//					
//				}else {
//					key = "kind";
//				}
				String key = jcb.getSelectedIndex() == 0?"name":"king";
				String sql = "select from player values where"+key+" like '%"+tfSearch.getText()+"%'";
				try {
					Statement st = con.createStatement();
					ResultSet rs = st.executeQuery(sql);
					while (rs.next()) {
						ta.append("��ȣ :"+rs.getInt("num")+"\n");
						ta.append("�̸� :"+rs.getString("name")+"\n");
						ta.append("Ű :"+rs.getDouble("height")+"\n");
						ta.append("������ :"+rs.getDouble("weight")+"\n");
						ta.append("���� :"+rs.getString("kind")+"\n");
						
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		
		
		setSize(600, 400);
		setVisible(true);
		
	}
   
	private void dbCon() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			String user = "scott";
			String pwd = "1234";
		   con =   DriverManager.getConnection(url, user, pwd);
		} catch (ClassNotFoundException e) {
				e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		
	}

	class PlayerPanel  extends JPanel{
		private String[] text = {"�̸�","Ű","������","����"};
	
		public PlayerPanel() {
			setLayout(null);
			for(int i =0 ;i<text.length;i++) {
				JLabel la = new JLabel(text[i]);
				la.setSize(50, 20);
				la.setLocation(30, 50+i*20);
				add(la);
				tf[i] = new JTextField(50);
				tf[i].setSize(150, 20);
				tf[i].setLocation(120, 50+i*20);
				add(tf[i]);
			}
		}
		
	}
	public static void main(String[] args) {
		new SwingPlayer();

	}
}