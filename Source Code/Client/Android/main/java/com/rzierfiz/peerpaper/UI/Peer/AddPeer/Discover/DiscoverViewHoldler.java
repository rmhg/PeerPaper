package com.rzierfiz.peerpaper.UI.Peer.AddPeer.Discover;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rzierfiz.peerpaper.R;
import com.rzierfiz.peerpaper.UI.Peer.AddPeer.DiscoverAddPeerFragment;

public class DiscoverViewHoldler extends RecyclerView.ViewHolder {
    TextView id = null;
    public DiscoverViewHoldler(View view){
        super(view);
        id = view.findViewById(R.id.peeriddpeer);

    }

    public void setPUID(String uid){
        id.setText(uid);
    }
}
