package com.yztcedu.downloadprogressretrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Tiffany.V.Chou on 2016/10/17.
 */
public interface MyRetrofitApi {

    @Streaming
    @GET
    Call<ResponseBody> downloadFile(@Url()String url);
}
