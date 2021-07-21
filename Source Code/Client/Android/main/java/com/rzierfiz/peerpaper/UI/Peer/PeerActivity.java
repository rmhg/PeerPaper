package com.rzierfiz.peerpaper.UI.Peer;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.rzierfiz.peerpaper.R;
import com.rzierfiz.peerpaper.UI.Peer.AddPeer.AddPeerAdapter;
import com.rzierfiz.peerpaper.UI.Peer.PeerConversation.PeerConversationActivity;
import com.rzierfiz.peerpaper.backend.Global.GlobalData;
import com.rzierfiz.peerpaper.backend.PeerConnection.PeerConversation;


public class PeerActivity extends AppCompatActivity {

    RecyclerView rcv = null;
    static PeerAdapter adapter = null;
    FloatingActionButton fab = null;
    ViewPager2 addPeerView = null;
    TabLayout tabLayout = null;
    String[] tabtitle = {"Main", "IP", "Discover"};
    LinearLayout linearLayout = null;
    boolean visi = false;

    public PeerActivity(){
        super(R.layout.peer_menu);

    }

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        init();



    }



    public void init(){
        GlobalData.curractivity = this;
        getSupportActionBar().setTitle(GlobalData.userInfo.loginCred.uid);
        addPeerView = (ViewPager2) findViewById(R.id.addpeerpmenu);
        linearLayout = (LinearLayout) findViewById(R.id.rootaddpeerpmenu);
        rcv = findViewById(R.id.rcvpmenu);
        fab = findViewById(R.id.fabpmenu);
        tabLayout = findViewById(R.id.tabaddpeerpmenu);

        adapter = new PeerAdapter();

        rcv.setLayoutManager(new LinearLayoutManager(this));
        rcv.setAdapter(adapter);


        addPeerView.setAdapter(new AddPeerAdapter(this));
        linearLayout.setVisibility(View.INVISIBLE);
        linearLayout.setAlpha(0.0f);



        adapter.BindPeer(this);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(AddPeerAdapter.init && tab.getPosition() == (addPeerView.getAdapter().getItemCount() - 1)){
                    GlobalData.discover.Discover();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        fab.setOnClickListener(this::onFabClick);
        fab.setRippleColor(Color.RED);

        new TabLayoutMediator(tabLayout, addPeerView, (tab, pos) -> {
            int c = addPeerView.getAdapter().getItemCount();
            int cpos = (1 - c/AddPeerAdapter.MAX_COUNT) + pos;
            tab.setText(tabtitle[cpos]);
        }).attach();
    }


    public void onFabClick(View v) {
        if(visi) {
            linearLayout.animate().translationY(2 * addPeerView.getHeight()).alpha(0.0f).setDuration(300).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    linearLayout.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }else{

            linearLayout.setTranslationY(2 * linearLayout.getHeight());
            linearLayout.animate().translationY(0).alpha(1.0f).setDuration(300).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    linearLayout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animator) {

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
        }
        visi = !visi;
        //addPeerView.setVisibility(visi);
        //tabLayout.setVisibility(visi);
    }
    public void ChangeToPC(String puid){
        Intent intent = new Intent(this, PeerConversationActivity.class);
        intent.putExtra("puid", puid);
        startActivity(intent);
    }

    public static void updateList(){
        adapter.peers = GlobalData.peers.values().toArray();
        GlobalData.curractivity.runOnUiThread(()->{adapter.notifyDataSetChanged();});
    }

}
