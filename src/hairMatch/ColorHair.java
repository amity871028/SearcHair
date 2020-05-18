package hairMatch;

import java.io.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ColorHair {
	boolean first = true;

	public String createFolder(String path, String name) throws IOException {
		String userPath = path + "/" + name;
		try {
			File file = new File(userPath);
			if (!file.exists())
				file.mkdir();// 建立資料夾
			else
				first = false;
		} catch (Exception e) {
			System.out.println("'" + path + "'此資料夾不存在");
		}
		return userPath; // 回傳使用者的資料夾路徑
	}

	public File getColorPicture(String path, String color) {
		int red = Integer.valueOf(color.substring(0, 2), 16);
		int green = Integer.valueOf(color.substring(2, 4), 16);
		int blue = Integer.valueOf(color.substring(4, 6), 16);
		int width = 400, height = 400;
		BufferedImage colorImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2d = (Graphics2D) colorImage.getGraphics();
		graphics2d.clearRect(0, 0, width, height);
		graphics2d.setPaint(new Color(red, green, blue));
		graphics2d.fillRect(0, 0, width, height);

		File file = new File(path + "/" + color + ".png");
		try {
			ImageIO.write(colorImage, "png", file);
		} catch (IOException e) {
			System.out.println("IOException " + e);
		}
		return file;
	}

	public String getColorHair(String hairstyleFolderRealPath, String folder, String path, String picture,
			File colorFile) throws IOException {
		String hairPath = hairstyleFolderRealPath + "/" + folder + "/" + picture;
		File hairFile = new File(hairPath);
		BufferedImage hairImg = ImageIO.read(hairFile);
		BufferedImage colorImg = ImageIO.read(colorFile);
		Graphics2D g2d = hairImg.createGraphics();
		int Width = colorImg.getWidth();
		int Height = colorImg.getHeight();
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.5f)); // 頭髮照片與顏色照片合併
		g2d.drawImage(colorImg, 0, 0, Width, Height, null);
		g2d.dispose();

		colorFile.delete(); // 輸出照片後刪掉顏色照片
		String fileName = null;
		if (first) // 第一次使用這功能 第一次建立資料夾
			fileName = CodeGenerator.getRandomCode(6); // 隨機產生一個檔案名稱
		else { // 已有個人資料夾
			deleteUserPicture(path); // 刪除資料夾內的照片
			fileName = CodeGenerator.getRandomCode(6); // 隨機產生一個檔案名稱
		}
		String newFileName = fileName + ".png";
		File newFile = new File(path + "/" + newFileName);
		ImageIO.write(hairImg, "png", newFile); // 產生合成照片
		return fileName + ".png";
	}

	public static void deleteUserPicture(String path) {
		File user = new File(path);
		String[] filenames;
		filenames = user.list(); // 回傳資料夾內所有檔案的檔名(含副檔名)
		File noFile = new File(path + "/" + filenames[0]);
		noFile.delete(); // 輸出照片後刪掉顏色照片
	}
}