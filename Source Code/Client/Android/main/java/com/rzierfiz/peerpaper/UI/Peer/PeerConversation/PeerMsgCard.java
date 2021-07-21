package com.rzierfiz.peerpaper.UI.Peer.PeerConversation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.DrawableUtils;
import androidx.recyclerview.widget.RecyclerView;

import com.rzierfiz.peerpaper.R;
import com.rzierfiz.peerpaper.backend.PeerConnection.Message.Message;
import com.rzierfiz.peerpaper.backend.PeerConnection.Message.PayloadType;
import com.rzierfiz.peerpaper.backend.PeerConnection.Message.TextMessage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;

public class PeerMsgCard extends RecyclerView.ViewHolder {
    TextView text = null;
    ImageView imageView = null;
    RelativeLayout rootMsg = null;

    public PeerMsgCard(View itemView) {
        super(itemView);
        text = itemView.findViewById(R.id.textmcard);
        imageView = itemView.findViewById(R.id.imgmcard);

        text.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        rootMsg = itemView.findViewById(R.id.rootmcard);
    }

    public void setPeerMessage(Message pm) {
        if (pm.getIsMe()) {
            rootMsg.setHorizontalGravity(Gravity.START);
        } else {
            rootMsg.setHorizontalGravity(Gravity.END);
        }


        PayloadType payloadType = pm.getPayloadType();
        switch(payloadType){
            case TEXT:
                text.setText(TextMessage.fromMessage(pm).getText());
                text.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                break;
            case IMG:
                byte[] payloadData = pm.getPayloadData();;
                Context context = itemView.getContext();
                int num = (int) (Math.random() * 10 + 0);


                BitmapFactory.Options opts = new BitmapFactory.Options();
                opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
                opts.inSampleSize = 8;
                Bitmap bm = BitmapFactory.decodeByteArray(payloadData, 0, payloadData.length, opts);

                imageView.setImageBitmap(bm);
                imageView.setVisibility(View.VISIBLE);
                text.setVisibility(View.GONE);
                break;
        }

    }

}
