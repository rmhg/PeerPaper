package com.rzierfiz.peerpaper.UI.Peer.AddPeer;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.rzierfiz.peerpaper.backend.Global.FindPeer;
import com.rzierfiz.peerpaper.backend.Global.GlobalData;

import static androidx.viewpager.widget.PagerAdapter.POSITION_NONE;


public class AddPeerAdapter extends FragmentStateAdapter {

    Fragment[] peerlayout = {new MainAddPeerFragment(), new IPAddPeerFragment(), new DiscoverAddPeerFragment()};
    static FragmentStateAdapter fragmentStateAdapter = null;
    public static final int MAX_COUNT = 3;
    public static boolean init = false;
    public AddPeerAdapter(FragmentActivity activity) {
        super(activity);
        if(GlobalData.current == GlobalData.CURRENT.START){
            peerlayout = new Fragment[]{peerlayout[1], peerlayout[2]};
        }
        fragmentStateAdapter = this;
    }

    @Override
    public Fragment createFragment(int position) {
        init = true;return peerlayout[position];
    }

    public static void update(){
    }

    @Override
    public int getItemCount() {
        return peerlayout.length;
    }

}
