package ru.iu3.fclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("mbedcrypto");
        //initRng();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.btnClickMe);
        btn.setOnClickListener((View v) -> { onButtonClick(v);});


        int res = initRng();
        Log.i("fclient", "Init Rng = " + res);
        byte[] v = randomBytes(10);


        /*byte[] rnd = randomBytes(16);
        byte[] data = {1,2,3,4,5,6,7,8};
        byte[] encText = encrypt(rnd, data);
        byte[] decText = decrypt(rnd, encText);*/



        // Example of a call to a native method
       /*TextView tv = findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());*/

    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */

   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setContentView(R.layout.activity_main);

        if (requestCode == 0)
        {
            if (resultCode == RESULT_OK || data != null)
            {
                String pin = data.getStringExtra("pin");
                Toast.makeText(this, pin, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    public static byte[] StringToHex(String s)
    {
        byte[] hex;
        try
        {
            hex = Hex.decodeHex(s.toCharArray());
        }
        catch (DecoderException ex)
        {
            hex = null;
        }
        return hex;
    }

    protected void onButtonClick(View v)
    {
        //A.
        //Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
        //B.
        /*byte[] key = StringToHex("0123456789ABCDEF0123456789ABCDE0");
        byte[] enc = encrypt(key, StringToHex("000000000000000102"));
        byte[] dec = decrypt(key, enc);
        String s = new String(Hex.encodeHex(dec)).toUpperCase();
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();*/
        //C.
        /*Intent it = new Intent(this, PinpadActivity.class);
        startActivity(it);*/
        //D.
        /*Intent it = new Intent(this, PinpadActivity.class);
        startActivityForResult(it, 0);*/
        TestHttpClient();
    }

    protected void TestHttpClient()
    {
        new Thread(()->{
            try {
                HttpURLConnection uc = (HttpURLConnection) (new URL("http://10.0.2.2:8081/api/v1/title").openConnection());
                InputStream inputStream = uc.getInputStream();
                String html = IOUtils.toString(inputStream);
                String title = getPageTitle(html);
                runOnUiThread(()->{
                    Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
                });
            } catch (Exception ex) {
                Log.e("fapptag", "Http client fails", ex);
            }
        }).start();
    }

    protected String getPageTitle(String html)
    {
        Pattern pattern = Pattern.compile("<title>(.+?)</title>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(html);
        String p;
        if (matcher.find())
            p = matcher.group(1);
        else
            p = "Not found";
        return p;
        /*int pos = html.indexOf("<title");
        String p = "not found";
        if (pos >= 0)
        {
            int pos2 = html.indexOf("<", pos + 1);
            if (pos >= 0)
                p = html.substring(pos + 7, pos2);
        }
        return p;*/
    }


    public native String stringFromJNI();
    public static native int initRng();
    public static native byte[] randomBytes(int no);
    public static native byte[] encrypt(byte[] key, byte[] data);
    public static native byte[] decrypt(byte[] key, byte[] data);
}