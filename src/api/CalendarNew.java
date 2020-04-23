package api;

public class CalendarNew {
	String account;
	String time;
	int category;
	String kind;
	int cost;
	String description;
	String color;

	public void setAccount(String account) {
		this.account = account;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	// get
	public String getAccount() {
		return account;
	}

	public String getTime() {
		return time;
	}

	public int getCategory() {
		return category;
	}

	public String getKind() {
		return kind;
	}

	public int getCost() {
		return cost;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getColor() {
		return color;
	}
}
