package com.rzierfiz.peerpaper.UI.Peer;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.rzierfiz.peerpaper.R;
import com.rzierfiz.peerpaper.backend.Global.GlobalData;
import com.rzierfiz.peerpaper.backend.Global.PeerInfo;
import com.rzierfiz.peerpaper.backend.PeerConnection.Message.Message;
import com.rzierfiz.peerpaper.backend.PeerConnection.Message.PayloadType;
import com.rzierfiz.peerpaper.backend.PeerConnection.Message.TextMessage;
import com.rzierfiz.peerpaper.backend.PeerConnection.PeerConnection;
import com.rzierfiz.peerpaper.backend.PeerConnection.PeerConversation;

import org.w3c.dom.Text;

import static com.rzierfiz.peerpaper.backend.PeerConnection.Message.PayloadType.TEXT;


public class PeerAdapter extends RecyclerView.Adapter<PeerCard> {

    public Object[] peers = GlobalData.peers.values().toArray();
    PeerActivity peerActivity = null;
    @Override
    public PeerCard onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.peer_card, parent, false);
        return new PeerCard(view);
    }

    @Override
    public void onBindViewHolder(PeerCard holder, int position) {
        PeerConversation pc = ((PeerConnection)peers[position]).peerConversation;
        PeerInfo pinfo = pc.dst;
        holder.setPUID(pinfo.puid);
        holder.setLastMsg("");
        if(pc.msgs.size() > 0) {
            Message msg = pc.msgs.lastElement();
            if(msg.getPayloadType() == TEXT){
                holder.setLastMsg((TextMessage.fromMessage(msg)).getText());
            }else{
                holder.setLastMsg(msg.getPayloadType().name());
            }
        }
        holder.setStatus(pinfo.getStatus());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                peerActivity.ChangeToPC(pinfo.puid);
            }
        });

    }
    public void BindPeer(PeerActivity pa){
        peerActivity = pa;
    }


    @Override
    public int getItemCount() {
        return peers.length;
    }
}
