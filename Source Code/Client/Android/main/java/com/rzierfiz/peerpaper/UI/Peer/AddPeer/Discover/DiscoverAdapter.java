package com.rzierfiz.peerpaper.UI.Peer.AddPeer.Discover;

import android.animation.LayoutTransition;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.rzierfiz.peerpaper.R;
import com.rzierfiz.peerpaper.UI.Peer.PeerActivity;
import com.rzierfiz.peerpaper.backend.Global.GlobalData;
import com.rzierfiz.peerpaper.backend.Global.PeerInfo;
import com.rzierfiz.peerpaper.backend.PeerConnection.Discovery.DiscoverPeerInfo;
import com.rzierfiz.peerpaper.backend.PeerConnection.PeerConnection;
import com.rzierfiz.peerpaper.backend.PeerConnection.PeerConversation;

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverViewHoldler> {


    static Object[] peers = GlobalData.dpeers.values().toArray();
    static DiscoverAdapter discoverAdapter = null;

    public DiscoverAdapter(){ super(); discoverAdapter = this; }


    @Override
    public DiscoverViewHoldler onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dis_peer, parent, false);
        return new DiscoverViewHoldler(view);
    }

    public static void update(){
        peers = GlobalData.dpeers.values().toArray();
        if(GlobalData.curractivity instanceof PeerActivity  && discoverAdapter != null)
            GlobalData.curractivity.runOnUiThread(()->{discoverAdapter.notifyDataSetChanged();});
    }

    @Override
    public void onBindViewHolder(DiscoverViewHoldler holder, int position) {
            DiscoverPeerInfo discoverPeerInfo = (DiscoverPeerInfo)(peers[position]);
            holder.setPUID(discoverPeerInfo.puid);
            holder.itemView.setOnClickListener((v)->{
                GlobalData.discover.Request(discoverPeerInfo.puid);
            });
    }


    @Override
    public int getItemCount() {
        return peers.length;
    }
}
