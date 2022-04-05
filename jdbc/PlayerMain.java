package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class PlayerMain {
	private Scanner sc = new Scanner(System.in);
	Connection con;
	
	public PlayerMain() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			String url = "jdbc:oracle:thin@localhost:1521:xe";
			String user = "scott";
			String pwd = "1234";
			con = DriverManager.getConnection(url, user, pwd);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void dbUse() {
		System.out.println("1.������� 2.�������� 3.����ã�� 4.���� 5.���� ���� ���� 6.����");
		int num = sc.nextInt();
		sc.nextLine();
		if(num == 1){
			playerInsert();
		}
		else if(num == 2){
			playerview();
		}
		else if(num == 3){
			System.out.println("ã�� ���� �̸� >> ");
			String name = sc.nextLine();
			playerSearch(name);
		}
		else if(num == 4){
			playerDelete();
		}
		else if(num == 5){
			playerUpdate();
		}
		else if(num == 6){
			System.out.println("����");
			System.exit(0);
		}
		else {
			System.out.println("�Է¿���");
		}
	}

	
	private void playerInsert() {
		System.out.println("�̸� >> ");
		String name = sc.nextLine();
		System.out.println("Ű >> ");
		double height = sc.nextDouble();
		System.out.println("ü�� >> ");
		double weight = sc.nextDouble();
		sc.nextLine();
		System.out.println("���� >> ");
		String kind = sc.nextLine();
		String sql = "insert into player values(player_seq.nextval,?,?,?,?)";
		try {
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, name);
			ps.setDouble(2, height);
			ps.setDouble(3, weight);
			ps.setString(4, kind);
			ps.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
	
	private void playerview() {
		String sql = "select * from player";
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				System.out.println("��ȣ : "+rs.getInt("num"));
				System.out.println("�̸� : "+rs.getString("name"));
				System.out.println("Ű : "+rs.getDouble("height"));
				System.out.println("������ : "+rs.getDouble("weight"));
				System.out.println("���� : "+rs.getString("kind"));
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	private void playerSearch(String name) {
		String sql = "select * from player where name like '%"+ name +"%'";
		
		try {
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			while(rs.next()) {
				System.out.println("��ȣ : "+rs.getInt("num"));
				System.out.println("�̸� : "+rs.getString("name"));
				System.out.println("Ű : "+rs.getDouble("height"));
				System.out.println("������ : "+rs.getDouble("weight"));
				System.out.println("���� : "+rs.getString("kind"));
				System.out.println();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private void playerDelete() {
		System.out.println("������ ���� �̸� >>");
		String name = sc.nextLine();
		playerSearch(name);
		System.out.println("������ ���� ��ȣ >>");
		int num = sc.nextInt();
		String sql = "delete form where num ="+num;
		try {
			Statement st = con.createStatement();
			st.executeUpdate(sql);
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	private void playerUpdate() {
		System.out.println("������ ���� �̸� >>");
		String name = sc.nextLine();
		playerSearch(name);
		System.out.println("������ ���� ��ȣ >>");
		int num = sc.nextInt();
		System.out.println("������ �׸� >>");
		System.out.println("1.�̸� 2.Ű 3.������ 4.����");
		int changeCol = sc.nextInt();
		sc.nextLine();
		String colName = "";
		switch (changeCol) {
		case 1: colName = "name" ; break;
		case 2: colName = "height" ; break;
		case 3: colName = "weight" ; break;
		case 4: colName = "kind" ; break;
		}
		System.out.println("�������� >>");
		String content = sc.nextLine();
		String sql = "update player set"+colName+"='"+content+"'where num ="+num;
		System.out.println(sql);
		
		try {
			Statement st = con.createStatement();
			st.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		
		
	}



	public static void main(String[] args) {
		PlayerMain pm = new PlayerMain();
		pm.dbUse();

	}

}