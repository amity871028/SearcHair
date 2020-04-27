package hairMatch;

import java.io.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import com.google.gson.Gson;

public class ColorHair {

	public String createFolder(String name) throws IOException {
		String project = ColorHair.class.getClassLoader().getResource("").toString();
		project = project.substring(6, project.length() - 90); // 取得專案根目錄的位置
		String path = "/SearcHair/WebContent/static/img/hair-match/user/" + name;
		try {
			File file = new File(project + path);
			if (!file.exists())
				file.mkdir();// 建立資料夾
		} catch (Exception e) {
			System.out.println("'" + path + "'此資料夾不存在");
		}
		return project + path + "/"; // 回傳使用者的資料夾路徑
	}

	public File getColorPicture(String path, String color) {
		int red = Integer.valueOf(color.substring(1, 3), 16);
		int green = Integer.valueOf(color.substring(3, 5), 16);
		int blue = Integer.valueOf(color.substring(5, 7), 16);
		int width = 400, height = 400;
		BufferedImage colorImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2d = (Graphics2D) colorImage.getGraphics();
		graphics2d.clearRect(0, 0, width, height);
		graphics2d.setPaint(new Color(red, green, blue));
		graphics2d.fillRect(0, 0, width, height);

		File file = new File(path + color + ".png");
		try {
			ImageIO.write(colorImage, "png", file);
		} catch (IOException e) {
			System.out.println("IOException " + e);
		}
		return file;
	}

	public String getColorHair(String path, String picture, File colorFile) throws IOException {
		String project = ColorHair.class.getClassLoader().getResource("").toString();
		project = project.substring(6, project.length() - 90);
		String hairPath = project + "/SearcHair/WebContent/" + picture;
		File hairFile = new File(hairPath);
		BufferedImage hairImg = ImageIO.read(hairFile);
		BufferedImage colorImg = ImageIO.read(colorFile);
		Graphics2D g2d = hairImg.createGraphics();
		int Width = colorImg.getWidth();
		int Height = colorImg.getHeight();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.5f)); // 頭髮照片與顏色照片合併
		g2d.drawImage(colorImg, 0, 0, Width, Height, null);
		g2d.dispose();

		File newFile = new File(path + "/current.png");
		ImageIO.write(hairImg, "png", newFile); // 產生合成照片
		colorFile.delete(); // 輸出照片後刪掉顏色照片
		String url = newFile.getPath();
		url = url.substring(url.indexOf("static"), url.length()); // 取相對路徑
		Hair hair = new Hair(url);
		Gson gson = new Gson();
		String ans = gson.toJson(hair); // 轉成JSON檔
		return ans;
	}
}