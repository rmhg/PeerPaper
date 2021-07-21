package com.rzierfiz.peerpaper.UI.Peer.PeerConversation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.rzierfiz.peerpaper.R;
import com.rzierfiz.peerpaper.backend.PeerConnection.PeerConversation;

public class PeerConversationAdapter extends RecyclerView.Adapter<PeerMsgCard> {


    PeerConversation peerConversation = PeerConversationActivity.peerConnection.peerConversation;

    @Override
    public PeerMsgCard onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msgcard, parent, false);
        return new PeerMsgCard(view);
    }

    @Override
    public void onBindViewHolder(PeerMsgCard holder, int position) {
        holder.setPeerMessage(peerConversation.msgs.get(position));
    }

    @Override
    public int getItemCount() {
        return peerConversation.msgs.size();
    }
}
