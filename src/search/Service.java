package search;

public class Service {

	String name;
	int min_price, max_price, service_time;
	String description;

	public void setName(String name) {
		this.name = name;
	}

	public void setMinPrice(int min_price) {
		this.min_price = min_price;
	}

	public void setMaxPrice(int max_price) {
		this.max_price = max_price;
	}

	public void setServiceTime(int service_time) {
		this.service_time = service_time;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}