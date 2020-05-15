package hairMatch;

import java.io.*;
import java.nio.channels.FileChannel;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ToImgur {
	static final ImgurAPI imgurApi = createImgurAPI();

	public static String getImgur(String data, File newFile, String path) throws IOException {

		String fileName = null;
		fileName = CodeGenerator.getRandomCode(6); // 隨機產生檔案名稱
		String newFileName = fileName + ".png";
		File outputfile = new File(path + "/" + newFileName);
		if (data != null) {
			String base64Image = data.split(",")[1];
			byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
			BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
			ImageIO.write(img, "png", outputfile);
		}
		if (newFile != null)
			copyFileUsingFileChannels(newFile, outputfile); // 把newFile複製到outputfile

		RequestBody request = RequestBody.create(MediaType.parse("image/*"), outputfile);
		Call<ImageResponse> call = imgurApi.postImage(request);
		Response<ImageResponse> res = call.execute();
		outputfile.delete(); // 刪除檔案
		return res.body().data.link;
	}

	static ImgurAPI createImgurAPI() {
		Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
				.baseUrl(ImgurAPI.SERVER).build();
		return retrofit.create(ImgurAPI.class);
	}

	private static void copyFileUsingFileChannels(File source, File dest) throws IOException {
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		try {
			inputChannel = new FileInputStream(source).getChannel();
			outputChannel = new FileOutputStream(dest).getChannel();
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
		} finally {
			if (inputChannel != null)
				inputChannel.close();
			if (outputChannel != null)
				outputChannel.close();
		}
	}
}
