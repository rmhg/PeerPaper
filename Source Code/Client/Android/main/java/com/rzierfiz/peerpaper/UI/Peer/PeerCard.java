package com.rzierfiz.peerpaper.UI.Peer;


import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rzierfiz.peerpaper.R;
import com.rzierfiz.peerpaper.backend.Global.PeerInfo;


public class PeerCard extends RecyclerView.ViewHolder {
    TextView puidtv = null;
    TextView lstmsgtv = null;
    TextView stattv = null;
    ImageView dotstativ = null;
    View rootView = null;
    GradientDrawable dotstatus = null;

    public PeerCard(View itemView) {
        super(itemView);
        puidtv = itemView.findViewById(R.id.puidpcard);
        lstmsgtv = itemView.findViewById(R.id.lstmsgpcard);
        stattv = itemView.findViewById(R.id.statpcard);
        dotstativ = itemView.findViewById(R.id.dotstatpcard);
        dotstatus = (GradientDrawable) itemView.getResources().getDrawable(R.drawable.dotstatus);
        rootView = itemView.findViewById(R.id.rootpcard);

    }

    public void setPUID(String puid){
        puidtv.setText(puid);
    }
    public void setLastMsg(String lstmsg){
        lstmsgtv.setText(lstmsg);
    }
    public void setStatus(PeerInfo.STATUS status){
        int BACK_COLOR = 0;
        int STAT_COLOR = 0;
        String sstatus = "";
        switch (status){
            case CONNECTED: BACK_COLOR = R.color.conn_back_peer;STAT_COLOR = R.color.conn_stat_peer;sstatus = "Connected";
                break;
            case DISCONNECTED: BACK_COLOR = R.color.dis_back_peer;STAT_COLOR = R.color.dis_stat_peer;sstatus = "Disconnected";
                break;
            case PENDING: BACK_COLOR = R.color.pend_back_peer;STAT_COLOR = R.color.pend_stat_peer;sstatus = "Pending";
                break;
            case REQUESTED: BACK_COLOR = R.color.req_back_peer;STAT_COLOR = R.color.req_stat_peer;sstatus = "Requested";
                break;
        }
        rootView.setBackgroundResource(BACK_COLOR);
        dotstatus.setColor(itemView.getResources().getColor(STAT_COLOR));
        dotstativ.setImageDrawable(dotstatus);
        stattv.setText(sstatus);
    }

}
