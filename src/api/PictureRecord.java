package api;

public class PictureRecord {
		int id;
		String account;
		String description;
		String picture;
		String time;
		
		
		public void setId(int id) {
			this.id = id;
		}

		public void setAccount(String account) {
			this.account = account;
		}

		public void setDescription(String description) {
			this.description = description;
		}
		
		public void setPictrue(String picture) {
			this.picture = picture;
		}
		
		public void setTime(String time) {
			this.time = time;
		}
		
		// get
		public int getId() {
			return id;
		}
		
		public String getAccount() {
			return account;
		}

		public String getDescription() {
			return description;
		}
		
		public String getPicture() {
			return picture;
		}
		
		public String getTime() {
			return time;
		}
}
