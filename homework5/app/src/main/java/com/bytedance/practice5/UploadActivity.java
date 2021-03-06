package com.bytedance.practice5;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bytedance.practice5.model.UploadResponse;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.bytedance.practice5.Constants.BOUNDARY;
import static com.bytedance.practice5.Constants.END_FIX;
import static com.bytedance.practice5.Constants.PRE_FIX;
import static com.bytedance.practice5.Constants.STUDENT_ID;
import static com.bytedance.practice5.Constants.USER_NAME;
import static com.bytedance.practice5.Constants.token;
import static com.bytedance.practice5.Constants.BASE_URL;
public class UploadActivity extends AppCompatActivity {
    private static final String TAG = "chapter5";
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024;
    private static final int REQUEST_CODE_COVER_IMAGE = 101;
    private static final String COVER_IMAGE_TYPE = "image/*";
    private IApi api;
    private Uri coverImageUri;
    private SimpleDraweeView coverSD;
    private EditText toEditText;
    private EditText contentEditText ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initNetwork();
        setContentView(R.layout.activity_upload);
        coverSD = findViewById(R.id.sd_cover);
        toEditText = findViewById(R.id.et_to);
        contentEditText = findViewById(R.id.et_content);
        findViewById(R.id.btn_cover).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFile(REQUEST_CODE_COVER_IMAGE, COVER_IMAGE_TYPE, "????????????");
            }
        });


        findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        findViewById(R.id.btn_URLsubmit).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                submitMessageWithURLConnection();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_COVER_IMAGE == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                coverImageUri = data.getData();
                coverSD.setImageURI(coverImageUri);

                if (coverImageUri != null) {
                    Log.d(TAG, "pick cover image " + coverImageUri.toString());
                } else {
                    Log.d(TAG, "uri2File fail " + data.getData());
                }

            } else {
                Log.d(TAG, "file pick fail");
            }
        }
    }

    private void initNetwork() {
        //TODO 3
        // ??????Retrofit??????
        // ??????api??????
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        api = retrofit.create(IApi.class);
    }

    private void getFile(int requestCode, String type, String title) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(type);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        intent.putExtra(Intent.EXTRA_TITLE, title);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, requestCode);
    }

    private void submit() {
        byte[] coverImageData = readDataFromUri(coverImageUri);
        if (coverImageData == null || coverImageData.length == 0) {
            Toast.makeText(this, "???????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        String to = toEditText.getText().toString();
        if (TextUtils.isEmpty(to)) {
            Toast.makeText(this, "?????????TA?????????", Toast.LENGTH_SHORT).show();
            return;
        }
        String content = contentEditText.getText().toString();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "??????????????????TA?????????", Toast.LENGTH_SHORT).show();
            return;
        }

        if ( coverImageData.length >= MAX_FILE_SIZE) {
            Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(TAG, "hi");
        //TODO 5
        // ??????api.submitMessage()??????????????????
        // ???????????????????????????activity???????????????toast
        new Thread(new Runnable() {
            @Override
            public void run() {
                Call<UploadResponse> uploadresponse = api.submitMessage(STUDENT_ID,"",
                    MultipartBody.Part.createFormData("from",USER_NAME),MultipartBody.Part.createFormData("to",to),
                    MultipartBody.Part.createFormData("content",content),
                    MultipartBody.Part.createFormData("image","cover.png",RequestBody.create(MediaType.parse("multipart/form-data"),coverImageData)),token);
                uploadresponse.enqueue(new Callback<UploadResponse>() {
                    @Override
                    public void onResponse(Call<UploadResponse> call, Response<UploadResponse> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(UploadActivity.this, "?????????????????????", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        final UploadResponse upResponse = response.body();
                        if (upResponse == null) {
                            Toast.makeText(UploadActivity.this, "?????????????????????", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (upResponse.success) {
                            Log.d("UploadResponse", "Success.");
                            Toast.makeText(UploadActivity.this, "???????????????", Toast.LENGTH_SHORT).show();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    UploadActivity.this.finish();
                                }
                            });
                        } else {
                            Log.d("UploadResponse Error", upResponse.error);
                            Toast.makeText(UploadActivity.this, "????????????: " + upResponse.error, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UploadResponse> call, Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(UploadActivity.this, "????????????" + t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).start();
    }


    // TODO 7 ?????? ???URLConnection?????????????????????
    private void submitMessageWithURLConnection(){
        byte[] coverImageData = readDataFromUri(coverImageUri);
        if (coverImageData == null || coverImageData.length == 0) {
            Toast.makeText(this, "???????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        String to = toEditText.getText().toString();
        if (TextUtils.isEmpty(to)) {
            Toast.makeText(this, "?????????TA?????????", Toast.LENGTH_SHORT).show();
            return;
        }
        String content = contentEditText.getText().toString();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(this, "??????????????????TA?????????", Toast.LENGTH_SHORT).show();
            return;
        }

        if ( coverImageData.length >= MAX_FILE_SIZE) {
            Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream is = null;
                OutputStream os = null;
                String query = "?student_id=" + STUDENT_ID;

                String urlStr = String.format("https://api-android-camp.bytedance.com/zju/invoke/messages%s",query);
                UploadResponse RseponseResult = null;

                try {
                    URL url = new URL(urlStr);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(10 * 1000);
                    conn.setReadTimeout(10 * 1000);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("token", token);
                    conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
                    conn.setRequestProperty("Charset", "UTF-8");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.connect();

                    os = new DataOutputStream(conn.getOutputStream());
                    // ???body
                    // ??????????????????https://img-ask.csdn.net/upload/201805/09/1525863351_886440.png
                    StringBuilder contentBody = new StringBuilder();
                    contentBody.append(PRE_FIX);
                    contentBody.append("Content-Disposition: form-data; name=\"from\"" + "\r\n");
                    contentBody.append("\r\n");
                    contentBody.append(USER_NAME);

                    contentBody.append(PRE_FIX);
                    contentBody.append("Content-Disposition: form-data; name=\"to\"" + "\r\n");
                    contentBody.append("\r\n");
                    contentBody.append(to);

                    contentBody.append(PRE_FIX);
                    contentBody.append("Content-Disposition: form-data; name=\"content\"" + "\r\n");
                    contentBody.append("\r\n");
                    contentBody.append(content);

                    os.write(contentBody.toString().getBytes());

                    // ?????????
                    StringBuffer strBuf = new StringBuffer();
                    strBuf.append(PRE_FIX);
                    strBuf.append("Content-Disposition: form-data; name=\"image\"; filename=\"cover.jpg\"\r\n");
                    strBuf.append("Content-Type: image/jpeg" + "\r\n\r\n");
//                    strBuf.append(coverImageData);
                    os.write(strBuf.toString().getBytes());
                    os.write(coverImageData);
                    os.write(END_FIX.getBytes());
                    os.flush();
                    Log.e(TAG, Integer.toString(conn.getResponseCode()));
                    if (conn.getResponseCode() == 200) {
                        is = conn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                        RseponseResult = new Gson().fromJson(reader, new TypeToken<UploadResponse>() {
                        }.getType());
                        if (RseponseResult.success) {
                            Toast.makeText(UploadActivity.this, "???????????????", Toast.LENGTH_SHORT).show();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    UploadActivity.this.finish();
                                }
                            });
                        } else {
                        }
                        reader.close();
                        is.close();
                    } else {
                    }
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, "HttpURLConnection Failed with Network Exception: " + e.toString());
                    Toast.makeText(UploadActivity.this, "????????????" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }).start();
    }


    private byte[] readDataFromUri(Uri uri) {
        byte[] data = null;
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            data = Util.inputStream2bytes(is);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


}
