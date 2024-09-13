package vn.edu.usth.usthweather;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class FragmentPagerAdapter extends androidx.fragment.app.FragmentPagerAdapter {
    private final int PAGE_COUNT = 3;
    private String titles[] = new String[] { "Hanoi", "Paris", "Toulouse" };

    public FragmentPagerAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT; // number of pages for a ViewPager
    }

    @NonNull
    @Override
    public Fragment getItem(int page) {
// returns an instance of Fragment corresponding to the specified page
        switch (page) {
            case 0: return WeatherAndForecastFragment.newInstance();
            case 1: return WeatherAndForecastFragment.newInstance();
            case 2: return WeatherAndForecastFragment.newInstance();
        }
        return new EmptyFragment(); // failsafe
    }
    @Override
    public CharSequence getPageTitle(int page) {
// returns a tab title corresponding to the specified page
        return titles[page];
    }
}