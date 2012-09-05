package org.domino.engine.utility.sso;

/*
 * Stupid class who store all tte LtapToken cookie generation config
 */
public class LtpaTokenConfig {

	private String ltpaToken;
	private String cookieName = "LtpaToken";
	private String cookieDomain = "";
	private String dominoSecret;
	private long dominoSessionExpirtation;

	public long getDominoSessionExpirtation() {
		return dominoSessionExpirtation;
	}

	public void setDominoSessionExpirtation(long dominoSessionExpirtation) {
		this.dominoSessionExpirtation = dominoSessionExpirtation;
	}

	public String getCookieDomain() {
		return cookieDomain;
	}

	public void setCookieDomain(String cookieDomain) {
		this.cookieDomain = cookieDomain;
	}

	public String getCookieName() {
		return cookieName;
	}

	public void setCookieName(String cookieName) {
		this.cookieName = cookieName;
	}

	public String getDominoSecret() {
		return dominoSecret;
	}

	public void setDominoSecret(String dominoSecret) {
		this.dominoSecret = dominoSecret;
	}

	public String getLtpaToken() {
		return ltpaToken;
	}

	public void setLtpaToken(String ltpa_token) {
		ltpaToken = ltpa_token;
	}

}
