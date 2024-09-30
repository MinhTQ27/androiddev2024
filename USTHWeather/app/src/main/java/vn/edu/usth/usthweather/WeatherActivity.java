package vn.edu.usth.usthweather;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.io.InputStream;


public class WeatherActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;

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
}
