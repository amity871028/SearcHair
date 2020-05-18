package hairMatch;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ImgurAPI {
    String SERVER = "https://api.imgur.com";
    public static final String AUTH = "2458a7d522073c6";

    @Headers("Authorization: Client-ID " + AUTH)
    @POST("/3/upload")
    Call<ImageResponse> postImage(
            @Body RequestBody image
    );
    
}