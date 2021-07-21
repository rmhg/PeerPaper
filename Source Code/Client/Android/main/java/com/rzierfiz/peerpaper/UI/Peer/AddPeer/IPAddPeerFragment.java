package com.rzierfiz.peerpaper.UI.Peer.AddPeer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.rzierfiz.peerpaper.R;
import com.rzierfiz.peerpaper.UI.Peer.PeerActivity;
import com.rzierfiz.peerpaper.UI.Peer.PeerAdapter;
import com.rzierfiz.peerpaper.backend.Global.GlobalData;
import com.rzierfiz.peerpaper.backend.Global.PeerInfo;
import com.rzierfiz.peerpaper.backend.PeerConnection.PeerConnection;
import com.rzierfiz.peerpaper.backend.PeerConnection.PeerConversation;

import org.w3c.dom.Text;

public class IPAddPeerFragment extends Fragment {

    TextView bindporttv = null;
    EditText ipet = null, portet = null;
    Button clear = null, cont = null;

    public IPAddPeerFragment() {
        super(R.layout.add_peer_ip);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ipet = view.findViewById(R.id.ipapeerip);
        portet = view.findViewById(R.id.portapeerip);
        clear = view.findViewById(R.id.clearapeerip);
        cont = view.findViewById(R.id.continueapeerip);
        bindporttv = view.findViewById(R.id.bindportapeerip);

        bindporttv.setText("Binded Port : " + GlobalData.BindPort);

        clear.setOnClickListener(v -> {
            onClear();
        });
        cont.setOnClickListener(v -> {
            String ip = ipet.getText().toString();
            if(ip.length() == 0) ip = "192.168.43.205";
            if(ip.length() > 0){
                int port = 5000;
                try{
                    port = Integer.parseInt(portet.getText().toString());
                }catch (Exception e){}
                onContinue( ip, port);
            }
        });

    }

    void onClear() {
        ipet.setText("");
        portet.setText("");
    }

    void onContinue(String ip, int port) {
        PeerInfo pinfo = new PeerInfo();
        pinfo.ip = ip;pinfo.port = port;
        pinfo.isconnectable = true;
        pinfo.isvalid = false;
        pinfo.puid = ip + ":" + port;
        PeerConnection peerConnection = GlobalData.peers.get(pinfo.puid);
        if(peerConnection == null) {
            peerConnection = new PeerConnection(new PeerConversation(pinfo));
            GlobalData.peers.put(pinfo.puid, peerConnection);
        }
        peerConnection.Connect();
        PeerActivity.updateList();
    }


}
