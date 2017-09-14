package com.example.android.linkup.profile;


        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentStatePagerAdapter;

        import com.example.android.linkup.models.Profile;

public class ProfilePagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    private Profile profile;

    public ProfilePagerAdapter(FragmentManager fm, int NumOfTabs, Profile profile) {
        super(fm);
        this.profile = profile;
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                PhotosFragment photosFragment = new PhotosFragment(profile);
                return photosFragment;
            case 1:
                InterestsFragment interestsFragment = new InterestsFragment(profile);
                return interestsFragment;
            case 2:
                DescriptionFragment descriptionFragment = new DescriptionFragment(profile);
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
