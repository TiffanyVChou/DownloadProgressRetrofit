package com.yztcedu.downloadprogressretrofit;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private ProgressBar mProgressBar;
    private Button downloadBtn;

    String url = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1995946438,606134090&fm=116&gp=0.jpg";
    private MyRetrofitApi api;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        file = new File(getCacheDir().getAbsolutePath()+"tiffany.png");
        initView();
    }

    private void initView() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        downloadBtn = (Button) findViewById(R.id.btn_download);

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://www.baidu.com").build();
        api = retrofit.create(MyRetrofitApi.class);

        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MyTask().execute();
            }
        });


    }

    class MyTask extends AsyncTask<Void, Integer, Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            Call<ResponseBody> call = api.downloadFile(url);
            try {
                Response<ResponseBody> response = call.execute();
                long total = response.body().contentLength();
                Log.e("tag", "---------->total" + total);
                InputStream inputStream = response.body().byteStream();

                file.createNewFile();
                FileOutputStream outputStream = new FileOutputStream(file);

                byte[] data = new byte[200];
                int len;
                int length = 0;

                while ((len = inputStream.read(data))!= -1){
                    outputStream.write(data, 0, len);
                    length += len;
                    publishProgress((int)(length * 100/ total));//发送进度
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                outputStream.flush();
                outputStream.close();
                inputStream.close();
                Log.e("tag", "---------->下载完成");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            mProgressBar.setProgress(values[0]);
        }
    }
}
