package com.example.project.View.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.project.View.Fragments.ImageFragment;

import java.util.List;

public class ImageSliderAdapter extends FragmentStatePagerAdapter {

    private List<String> imageList;

    public ImageSliderAdapter(FragmentManager fm, List<String> imageList) {
        super(fm);
        this.imageList = imageList;
    }

    @Override
    public Fragment getItem(int position) {
        return ImageFragment.newInstance(imageList.get(position));
    }

    @Override
    public int getCount() {
        return imageList.size();
    }
}

