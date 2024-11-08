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
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

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
        new DownloadImageTask(this);
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
    // «Upgrade» previous AsyncTask HttpURLConnection to Volley
    private class DownloadImageTask  {
        private final Context context;
        private RequestQueue queue;
        public DownloadImageTask(WeatherActivity weatherActivity) {
            this.context = weatherActivity;
            this.queue = Volley.newRequestQueue(context);
            Log.d("USTHWeather", "Starting image request...");
            // Create the image request
            ImageRequest imageRequest = new ImageRequest(
                    "https://cdn.haitrieu.com/wp-content/uploads/2022/11/Logo-Truong-Dai-hoc-Khoa-hoc-va-Cong-nghe-Ha-Noi.png",
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            // Set the image in the ImageView
                            if (logoImageView != null) {
                                logoImageView.setImageBitmap(response);
                                Toast.makeText(context, "Image Loaded", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    0, 0, ImageView.ScaleType.CENTER,
                    Bitmap.Config.ARGB_8888,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("USTHWeather", "Error loading image: " + error.getMessage());
                            Toast.makeText(context, "Failed to load image", Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            Log.d("USTHWeather", "Adding image request to queue.");
            // Add the request to the RequestQueue
            queue.add(imageRequest);
        }
}
}