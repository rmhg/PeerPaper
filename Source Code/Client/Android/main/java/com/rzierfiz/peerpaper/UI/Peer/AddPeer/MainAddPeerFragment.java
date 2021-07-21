package com.rzierfiz.peerpaper.UI.Peer.AddPeer;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.rzierfiz.peerpaper.R;
import com.rzierfiz.peerpaper.backend.Global.FindPeer;
import com.rzierfiz.peerpaper.backend.Global.GlobalData;
import com.rzierfiz.peerpaper.backend.PeerConnection.PeerConnection;

public class MainAddPeerFragment extends Fragment {

    static EditText puidet = null;
    EditText msget = null;
    static TextView puidtv = null, statustv = null, name = null, about = null;

    Button clear = null, find = null, conn = null;

    static LinearLayout rootFindpeer = null;

    public MainAddPeerFragment() {
        super(R.layout.add_peer);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        puidet = view.findViewById(R.id.puidapeer);
        msget = view.findViewById(R.id.msgapeer);
        puidtv = view.findViewById(R.id.puidtvapeer);
        statustv = view.findViewById(R.id.statusapeer);
        name = view.findViewById(R.id.nameapeer);
        about = view.findViewById(R.id.aboutapeer);
        rootFindpeer = view.findViewById(R.id.res_findapeer);

        clear = view.findViewById(R.id.clearapeer);
        find = view.findViewById(R.id.findapeer);
        conn = view.findViewById(R.id.connectapeer);

        clear.setOnClickListener(v -> {
            onClear();
        });
        find.setOnClickListener(v -> {
            onFind(puidet.getText().toString());
        });
        conn.setOnClickListener(v -> {
            onConnect(puidet.getText().toString(), msget.getText().toString());
        });
    }

    void onClear() { puidet.setText("");msget.setText(""); rootFindpeer.setVisibility(View.INVISIBLE); }

    void onFind(String puid) {
        if(puid.length() > 0) GlobalData.sops.findpeerReq(puid);
    }

    void onConnect(String puid, String msg) {
        if(puid.length() > 0) {
            PeerConnection peerConnection = GlobalData.peers.get(puid);
            GlobalData.sops.connectReq(puid, msg);
        }
    }

    public static void updateFind() {

        if (rootFindpeer != null) {
            rootFindpeer.setVisibility(View.VISIBLE);
            puidtv.setText(puidet.getText().toString());
            if(GlobalData.findPeer.status == FindPeer.STATUS.NOT_FOUND){
                statustv.setText("NOT FOUND");
                statustv.setTextColor(Color.RED);
                name.setVisibility(View.INVISIBLE);
                about.setVisibility(View.INVISIBLE);
            }
            else if(GlobalData.findPeer.status == FindPeer.STATUS.FOUND){
                statustv.setText("FOUND");
                statustv.setTextColor(Color.GREEN);
                name.setVisibility(View.VISIBLE);
                about.setVisibility(View.VISIBLE);
                name.setText(GlobalData.findPeer.pname);
                about.setText(GlobalData.findPeer.pabout);
            }
        }
        AddPeerAdapter.update();
    }

}
