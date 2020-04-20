package hairMatch;

import java.io.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import com.google.gson.Gson;

public class ColorHair {
	
	public String newFolder(String name) {
		String path = "WebContent/static/img/hair-match/user/"+name;
        File file = new File(path);
        file.mkdir(); //建立資料夾
        return path+"/";
	}
	
	public File colorPicture(String path,String color) {
		int red= Integer.valueOf(color.substring(1,3),16);
		int green= Integer.valueOf(color.substring(3,5),16);
		int blue= Integer.valueOf(color.substring(5,7),16);
        int width=400,height=400;
        BufferedImage colorImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); 
        Graphics2D graphics2d=(Graphics2D)colorImage.getGraphics();
        graphics2d.clearRect(0, 0, width, height);      
        graphics2d.setPaint(new Color(red,green,blue));      
        graphics2d.fillRect(0, 0, width, height); 
        
        File file = new File(path+color+".png");    
        try {
            ImageIO.write(colorImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
	}
	
	public void colorHair(String path,String picture,String color,File colorFile) throws IOException {    
        File hairFile = new File(picture); 
    	String[] arr=picture.split("\\\\");//不限制元素個數
    	String pictureName=arr[6]; //拿到照片名稱
        BufferedImage hairImg = ImageIO.read(hairFile);
        BufferedImage colorImg = ImageIO.read(colorFile);
        Graphics2D g2d = hairImg.createGraphics();
        int Width = colorImg.getWidth();
        int Height = colorImg.getHeight();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.5f)); //頭髮照片與顏色照片合併
        g2d.drawImage(colorImg, 0, 0, Width, Height, null);
        g2d.dispose();	
        File newFile = new File(path+"/"+color+"_"+pictureName);
        ImageIO.write(hairImg, "png", newFile); //產生合成照片
        colorFile.delete(); //輸出照片後刪掉顏色照片
        String url=newFile.getPath();
		Hair hair = new Hair(url);
		Gson gson = new Gson();
		String ans = gson.toJson(hair);
		System.out.println(ans); //======這裡輸出JSON======	
	}
	
	public static void main(String args[]) throws IOException {
		String picture="WebContent\\static\\img\\hair-match\\頭髮素材\\男生短髮\\boy1.png";
		String color = "#FF7F50"; //選擇的顏色
		String userName = "ITZY"; //使用者名稱
		ColorHair colorHair = new ColorHair();
		String path = colorHair.newFolder(userName); //獲得使用者新建的個人資料夾路徑
		File colorFile = colorHair.colorPicture(path,color); //獲得使用者選的顏色照片
		colorHair.colorHair(path, picture,color, colorFile);
	}
}
