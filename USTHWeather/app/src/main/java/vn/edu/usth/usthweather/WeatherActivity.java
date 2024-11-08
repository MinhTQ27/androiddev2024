package vn.edu.usth.usthweather;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class WeatherActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;
    private ImageView logoImageView;
    Handler handler;

    public static final String TAG = "Weather";
    public static final String NETWORK_RESPONSE = "NETWORK_RESPONSE";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewPager viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sugar_lyrics);
        mediaPlayer.start();

        logoImageView = findViewById(R.id.usth_logo);
        if (logoImageView == null) {
            Log.e(TAG, "logoImageView is null. Check the layout file.");
        }
        // Start the AsyncTask to download the logo
        new DownloadImageTask().execute();
        // Start AsyncTask for network request simulation
        new RequestNetworkTask().execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(WeatherAndForecastFragment.newInstance(), "Ha Noi,Viet Nam");
        adapter.addFragment(WeatherAndForecastFragment.newInstance(), "Paris,France");
        adapter.addFragment(WeatherAndForecastFragment.newInstance(), "Thuong Hai, China ");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_icon:
                Toast.makeText(this, "Refreshing", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.more_icon:
                Intent intent = new Intent(WeatherActivity.this, PrefActivity.class);
                Toast.makeText(this, "New Activity", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // «Upgrade» the previous AsyncTask
    private class RequestNetworkTask extends AsyncTask<Void, Void, String> {
        // sleep() in doInBackground
        @Override
        protected String doInBackground(Void... voids) {
            try {
                // wait for 1 seconds to simulate a long network access
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Request Network";
        }

        // Toast in onPostExecute()
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(WeatherActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    }

    // Perform a real network request to USTH’s server
    private class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {
        // Download USTH logo
        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap bitmap = null;
            try {
                // Initialize URL
                URL url = new URL("https://cdn.haitrieu.com/wp-content/uploads/2022/11/Logo-Truong-Dai-hoc-Khoa-hoc-va-Cong-nghe-Ha-Noi.png");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                // Receive response
                int response = connection.getResponseCode();
                Log.i("USTHWeather", "The response is: " + response);
                if (response == HttpURLConnection.HTTP_OK) { // Check if response is OK
                    InputStream is = connection.getInputStream();
                    // Process image response
                    bitmap = BitmapFactory.decodeStream(is);
                    is.close(); // Close the InputStream
                }
                connection.disconnect();
            } catch (Exception e) {
                Log.e("USTHWeather", "Error downloading image: " + e.getMessage());
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null && logoImageView != null) {
                logoImageView.setImageBitmap(bitmap); // Set the bitmap to the ImageView
                Toast.makeText(WeatherActivity.this, "Image Loaded", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(WeatherActivity.this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}