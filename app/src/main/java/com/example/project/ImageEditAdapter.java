package com.example.project;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class ImageEditAdapter extends FragmentStatePagerAdapter {

    private List<String> imageList;

    public ImageEditAdapter(FragmentManager fm, List<String> imageList) {
        super(fm);
        this.imageList = imageList;
    }

    @Override
    public Fragment getItem(int position) {
        return EditImageFragment.newInstance(imageList.get(position));
    }

    @Override
    public int getCount() {
        return imageList.size();
    }
}

