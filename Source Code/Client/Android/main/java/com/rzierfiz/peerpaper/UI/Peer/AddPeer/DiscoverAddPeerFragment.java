package com.rzierfiz.peerpaper.UI.Peer.AddPeer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rzierfiz.peerpaper.R;
import com.rzierfiz.peerpaper.UI.Peer.AddPeer.Discover.DiscoverAdapter;
import com.rzierfiz.peerpaper.backend.Global.GlobalData;

public class DiscoverAddPeerFragment extends Fragment {
    RecyclerView rcv = null;
    Button dis = null;
    DiscoverAdapter discoverAdapter = null;

    public DiscoverAddPeerFragment(){ super(R.layout.add_peer_dis); }


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int orientation = GridLayoutManager.HORIZONTAL;
        discoverAdapter = new DiscoverAdapter();
        rcv = view.findViewById(R.id.rcvdpeer);
        dis = view.findViewById(R.id.disdpeer);
        rcv.setLayoutManager(new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false));
        rcv.setAdapter(discoverAdapter);

        dis.setOnClickListener((v)->{onClick(v);});
    }

    public void onClick(View v){
        GlobalData.discover.Discover();
    }
}
