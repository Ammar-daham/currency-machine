package model;
import java.sql.*;

/**
 * @author Ammar Daham
 */

public class ValuuttaAccessObject implements IValuuttaDAO {
	
	private Valuutta valuutta;
	private Valuutta[] valuutat;
	Connection conn;
	ResultSet rs;
	final String URL = "jdbc:mariadb://localhost/valuutta";
	final String USERNAME = "root";
	final String PWD = "Aljewary89";
	
	public ValuuttaAccessObject() {
		try {
			conn = DriverManager.getConnection(URL, USERNAME, PWD);
		}catch(SQLException e) {
			do {
				System.err.println("Viesti: " + e.getMessage());
				System.err.println("Virhekoodi: " + e.getErrorCode());
				System.err.println("SQL-tilakoodi: " + e.getSQLState());
			}while(e.getNextException() != null);
			System.exit(-1); //  lopetus heti virheen vuoksi
		}
	}

	@Override
	public boolean createValuutta(Valuutta valuutta) {
		try(PreparedStatement createStm = conn.prepareStatement("INSERT INTO valuutta VALUES (?, ?, ?);")){
			
			boolean hasValuutta = false;
			
			createStm.setString(1, valuutta.getTunnus());
			createStm.setDouble(2, valuutta.getVaihtokurssi());
			createStm.setString(3, valuutta.getNimi());
			
			Valuutta[] valuutatTemp = readValuutat();
			for(Valuutta v: valuutatTemp) {
				if(v.getTunnus().equals(valuutta.getTunnus())) {
					hasValuutta = true;
				}
			}
			
			if(hasValuutta) {
				System.out.println("Valuutta on jo tietokannassa.");
				hasValuutta = false;
				return false;
			}else {
				createStm.executeUpdate();
			}
			return true;
			
		}catch(SQLException e) {
			do {
				System.err.println("Valuutan luominen epäonnistui.");
			} while (e.getNextException() != null);
			
		}
		
		return false;
	}

	@Override
	public Valuutta readValuutta(String tunnus) {
		
		try(PreparedStatement readValuuttaStm = 
				conn.prepareStatement("SELECT * FROM valuutta WHERE tunnus = ?;")){
			
			readValuuttaStm.setString(1, tunnus);
			rs = readValuuttaStm.executeQuery();
			
			if(rs.next()) {
				String rsTunnus = rs.getString("tunnus");
				double rsVaihtokurssi = rs.getDouble("vaihtokurssi");
				String rsNimi = rs.getString("nimi");
				valuutta = new Valuutta(rsTunnus, rsVaihtokurssi, rsNimi);
			} else {
				return null;
			}
			
		}catch(SQLException e) {
			do{
				System.err.println("Viesti: " + e.getMessage());
				System.err.println("Virhekoodi: " + e.getErrorCode());
				System.err.println("SQL-tilakoodi: " + e.getSQLState());
			}while(e.getNextException() != null);
		}
		
		return valuutta;
	}

	@Override
	public Valuutta[] readValuutat() {
		try (PreparedStatement readValuuttaStatement = 
				conn.prepareStatement("SELECT * FROM valuutta")) {

			int rows = 0;
			int index = 0;

			rs = readValuuttaStatement.executeQuery();

			while (rs.next()) {
				rows++;
			}

			rs = readValuuttaStatement.executeQuery();
			valuutat = new Valuutta[rows];

			while (rs.next()) {
				String rsTunnus = rs.getString("tunnus");
				double rsVaihtokurssi = rs.getDouble("vaihtokurssi");
				String rsNimi = rs.getString("nimi");
				valuutat[index] = new Valuutta(rsTunnus, rsVaihtokurssi, rsNimi);
				index++;
			}
			

		} catch (SQLException e) {
			do {
				System.err.println("Tiedot   hakeminen epäonnistui.");
			} while (e.getNextException() != null);
		}
		return valuutat;
	}

	@Override
	public boolean updateValuutta(Valuutta valuutta) {
		try(PreparedStatement updateStm = conn.prepareStatement("UPDATE valuutta "
				+ "SET nimi = ?, vaihtokurssi = ? WHERE tunnus = ?;")){
			
			updateStm.setString(1, valuutta.getNimi());
			updateStm.setDouble(2, valuutta.getVaihtokurssi());
			updateStm.setString(3, valuutta.getTunnus());
			
			updateStm.executeUpdate();
			
			return true;
		}
		catch(SQLException e) {
			do {
				System.err.println("Tiedot päivitäminen epäonnistui.");
			} while (e.getNextException() != null);
		}
		return false;
	}

	@Override
	public boolean deleteValuutta(String tunnus) {
		try(PreparedStatement deleteStm = conn.prepareStatement("DELETE FROM valuutta WHERE tunnus = ?")){
			
			if(readValuutta(tunnus) != null) {
				deleteStm.setString(1, tunnus);
				deleteStm.executeUpdate();
				return true;
			}else {
				System.out.println("Valuutta ei löytynyt.");
				return false;
			}
			
		}catch(SQLException e) {
			do {
				System.err.println("Tiedot poistominen epäonnistui.");
			} while (e.getNextException() != null);
		}
		return false;
	}

}
