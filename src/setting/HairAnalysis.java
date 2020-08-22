package setting;

public class HairAnalysis {
	private String account;
	private boolean greasy;
	private boolean frizzy;
	private boolean sleek;
	private boolean tangled;
	private boolean perm;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public boolean isGreasy() {
		return greasy;
	}

	public void setGreasy(boolean greasy) {
		this.greasy = greasy;
	}

	public boolean isFrizzy() {
		return frizzy;
	}

	public void setFrizzy(boolean frizzy) {
		this.frizzy = frizzy;
	}

	public boolean isSleek() {
		return sleek;
	}

	public void setSleek(boolean sleek) {
		this.sleek = sleek;
	}

	public boolean isTangled() {
		return tangled;
	}

	public void setTangled(boolean tangled) {
		this.tangled = tangled;
	}

	public boolean isPerm() {
		return perm;
	}

	public void setPerm(boolean perm) {
		this.perm = perm;
	}
}
