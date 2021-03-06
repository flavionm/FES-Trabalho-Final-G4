package Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import Model.Client;
import Model.Rent;
import Model.Vehicle;
import java.sql.SQLException;

public class RentController {
	
	private Connection conn;
	private PreparedStatement stmt;
	private Statement st;
	private ResultSet rs;
	
	public RentController() {
		conn = new ConnectionDB().getConection();
	}
	
	public void start(Rent rent) throws SQLException {
		String sql = "INSERT INTO rent (client_id, vehicle_id, start_date, end_date) VALUES (?, ?, ?, ?);";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, rent.getClient_id());
			stmt.setInt(2, rent.getVehicle_id());
			stmt.setDate(3, java.sql.Date.valueOf(rent.getStart_date()));
			stmt.setDate(4, java.sql.Date.valueOf(rent.getEnd_date()));
			stmt.execute();
			stmt.close();
		} catch(Exception e) {
			throw e;
		}
	}
	
	public void end(Rent rent) {
		String sql = "UPDATE rent SET completed = TRUE where id = ?;";
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, rent.getId());
			stmt.execute();
			stmt.close();
		} catch(Exception e) {
			throw new RuntimeException("Erro no end de rent: " + e);
		}
	}
	
	public ArrayList<Rent> readAll() {
		String sql = "SELECT * FROM rent;";
		ArrayList<Rent> data = new ArrayList<>();
		try {
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while(rs.next()) {
				Rent rent = createRent(rs);
				data.add(rent);
			}
			st.close();
		}catch(Exception e) {
			throw new RuntimeException("Erro no readAll de rent:" + e);
		}
		return data;
	}
	
	public ArrayList<Rent> readAllFromClient(Client client) {
		String sql = "SELECT * FROM rent where client_id = ? ORDER BY start_date DESC;";
		ArrayList<Rent> data = new ArrayList<>();
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, client.getId());
			
			rs = stmt.executeQuery();
			while(rs.next()) {
				Rent rent = createRent(rs);
				data.add(rent);
			}
			stmt.close();
		}catch(Exception e) {
			throw new RuntimeException("Erro no readAll de rent:" + e);
		}
		return data;
	}
	
	private Rent createRent(ResultSet rs) throws SQLException {
		Rent rent = new Rent();
		rent.setId(rs.getInt("id"));
		rent.setClient_id(rs.getInt("client_id"));
		rent.setVehicle_id(rs.getInt("vehicle_id"));
		rent.setStart_date(rs.getDate("start_date").toLocalDate());
		rent.setEnd_date(rs.getDate("end_date").toLocalDate());
		rent.setCompleted(rs.getBoolean("completed"));
		return rent;
	}
	
}