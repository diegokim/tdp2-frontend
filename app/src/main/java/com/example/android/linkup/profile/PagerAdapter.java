package com.example.android.linkup.profile;


        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                PhotosFragment photosFragment = new PhotosFragment();
                return photosFragment;
            case 1:
                InterestsFragment interestsFragment = new InterestsFragment();
                return interestsFragment;
            case 2:
                DescriptionFragment descriptionFragment = new DescriptionFragment();
                return descriptionFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
