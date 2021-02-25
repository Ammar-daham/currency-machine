package model;

/**
 * @author Ammar Daham
 */
 
public class Valuutta {
	// Nämä rajoitteet tulevat tietokannan schemasta
	public final int TUNNUS_MAX = 3;
	public final int NIMI_MAX = 50;
	
	private String tunnus;
	private double vaihtokurssi;
	private String nimi;
	
	public Valuutta(String tunnus, double vaihtokurssi, String nimi) {
		// TODO Auto-generated constructor stub
		this.tunnus = tunnus;
		this.vaihtokurssi = vaihtokurssi;
		this.nimi = nimi;
	}
	
	public String getTunnus() {
		if(tunnus.length() > TUNNUS_MAX) {
			 return this.tunnus.substring(0, TUNNUS_MAX).toUpperCase();
			}
		return tunnus;
	}

	public void setTunnus(String tunnus) {
		this.tunnus = tunnus.toUpperCase();
	}

	public double getVaihtokurssi() {
		return vaihtokurssi;
	}

	public void setVaihtokurssi(double vaihtokurssi) {
		this.vaihtokurssi = vaihtokurssi;
	}

	public String getNimi() {
		if(nimi.length() > NIMI_MAX) {
			return  this.nimi.substring(0, NIMI_MAX);
		}
		return this.nimi;
	}

	public void setNimi(String nimi) {
		this.nimi = nimi;
	}	
	
}
