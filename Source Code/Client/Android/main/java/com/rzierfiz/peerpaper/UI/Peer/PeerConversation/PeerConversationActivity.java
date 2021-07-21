package com.rzierfiz.peerpaper.UI.Peer.PeerConversation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rzierfiz.peerpaper.R;
import com.rzierfiz.peerpaper.UI.Peer.PeerActivity;
import com.rzierfiz.peerpaper.backend.Global.GlobalData;
import com.rzierfiz.peerpaper.backend.Global.PeerInfo;
import com.rzierfiz.peerpaper.backend.PeerConnection.Message.ImageMessage;
import com.rzierfiz.peerpaper.backend.PeerConnection.Message.Message;
import com.rzierfiz.peerpaper.backend.PeerConnection.Message.TextMessage;
import com.rzierfiz.peerpaper.backend.PeerConnection.PeerConnection;
import com.rzierfiz.peerpaper.backend.PeerConnection.PeerConversation;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.microedition.khronos.opengles.GL11ExtensionPack;

public class PeerConversationActivity extends AppCompatActivity {

    public static PeerConnection peerConnection = null;
    RecyclerView rcv = null;
    EditText inputet = null;
    Button send = null;
    Button attach = null;
    ActivityResultLauncher<Intent> activityResultLauncher = null;
    static PeerConversationAdapter adapter = null;
    public PeerConversationActivity(){
        super(R.layout.peer_conversation);
    }
    byte[] imgdata = null;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        activityResultLauncher = registerForActivityResult(new ActivityResultContract<Intent, Uri>() {
            @Override
            public Intent createIntent(Context context, Intent input) {
                return input;
            }

            @Override
            public Uri parseResult(int resultCode,Intent intent) {

                return intent.getData();
            }
        }, new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri uri) {
                String path = uri.getPath();
                try {
                    InputStream is = getContentResolver().openInputStream(uri);
                    int size = is.available();
                    if(size > 0){
                        imgdata = new byte[size];
                        int x = is.read(imgdata);
                        Log.d("Exp", "Size IMg : " + size + ", " + path + ", " + x);
                        String text = "";
                        for(int i = 0; i < 20;i++) text += imgdata[i];
                        String btext = "";
                        for(int i = 0; i < 20;i++) text += imgdata[imgdata.length - i - 1];
                    }
                    is.close();
                }catch (Exception e){
                    GlobalData.showTextShort(e + "");
                }
            }
        });
        GlobalData.curractivity = this;
        String puid = getIntent().getStringExtra("puid");
        peerConnection = GlobalData.peers.get(puid);
        getSupportActionBar().setTitle(puid);
        int color = 0;
        switch(peerConnection.peerConversation.dst.getStatus()){
            case CONNECTED: color = R.color.conn_back_peer;
            break;
            case DISCONNECTED: color = R.color.dis_back_peer;
                break;
            case PENDING: color = R.color.pend_back_peer;
                break;
            case REQUESTED: color = R.color.req_back_peer;
                break;
        }
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(getResources().getColor(color));
        getSupportActionBar().setBackgroundDrawable(gd);
        adapter = new PeerConversationAdapter();
        rcv = findViewById(R.id.rcvpconv);
        inputet = findViewById(R.id.inputpconv);
        send = findViewById(R.id.sendpconv);
        attach = findViewById(R.id.attachment);
        rcv.setLayoutManager(new LinearLayoutManager(this));
        rcv.setAdapter(adapter);

        send.setOnClickListener(v -> {onSend();});
        attach.setOnClickListener(v -> {attachment();});
    }

    void attachment(){

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("*/*").addCategory(Intent.CATEGORY_OPENABLE);
            intent = Intent.createChooser(intent, "Choose A File");
            activityResultLauncher.launch(intent);




    }

    void onSend(){
        String msg = inputet.getText().toString();
        if(peerConnection.peerConversation.dst.getStatus() == PeerInfo.STATUS.REQUESTED){
            if(GlobalData.current == GlobalData.CURRENT.LOGGED){
                GlobalData.sops.connectReq(peerConnection.peerConversation.dst.puid, "Hello");
            }
        }else{
            if(msg.length() > 0){
                peerConnection.sendMsg(new TextMessage(true, msg));
                inputet.setText("");
                updateList();
            }else if(imgdata != null){
                peerConnection.sendMsg(new ImageMessage(true, imgdata));
                imgdata = null;
                updateList();
            }
        }
    }

    public static void updateList(){
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        GlobalData.showTextShort("Activity Result");
        if(resultCode == RESULT_OK){
            Uri uri = data.getData();
            GlobalData.showTextShort(uri.toString());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        PeerActivity.updateList();
    }
    
}

