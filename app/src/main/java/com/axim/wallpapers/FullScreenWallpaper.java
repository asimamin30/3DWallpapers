package com.axim.wallpapers;

import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.IOException;


public class FullScreenWallpaper extends AppCompatActivity {
    String originalUrl="";
    PhotoView photoView;
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_wallpaper);

        getSupportActionBar().hide();

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-2175088912031648/7787499255", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error

                        mInterstitialAd = null;
                    }
                });

        Intent intent =  getIntent();
        originalUrl = intent.getStringExtra("originalUrl");
        photoView = findViewById(R.id.photoView);
        Glide.with(this).load(originalUrl).into(photoView);

        Handler mhandler= new Handler();
        mhandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showBottomSheetDialog();
            }
        },3000);

        TastyToast.makeText(getApplicationContext(),"Please wait! just a sec", TastyToast.LENGTH_LONG,TastyToast.WARNING).show();
    }

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_layout);

        LinearLayout setwall = bottomSheetDialog.findViewById(R.id.setwall);
        LinearLayout setlock = bottomSheetDialog.findViewById(R.id.setlock);
        LinearLayout setboth = bottomSheetDialog.findViewById(R.id.setboth);
        LinearLayout savegall = bottomSheetDialog.findViewById(R.id.savegall);

        bottomSheetDialog.show();

        setwall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                Bitmap bitmap  = ((BitmapDrawable)photoView.getDrawable()).getBitmap();
                try {
                    wallpaperManager.setBitmap(bitmap);
                    TastyToast.makeText(getApplicationContext(),"Wallpaper Set", TastyToast.LENGTH_LONG,TastyToast.SUCCESS).show();


                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(FullScreenWallpaper.this);
                } else {

                }

                bottomSheetDialog.dismiss();
            }
        });

        setlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WallpaperManager wm = WallpaperManager.getInstance(getApplicationContext());
                Bitmap bitmap  = ((BitmapDrawable)photoView.getDrawable()).getBitmap();

                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        wm.setBitmap(bitmap,null,true,WallpaperManager.FLAG_LOCK);//For Lock screen
                        TastyToast.makeText(getApplicationContext(),"Wallpaper Set On Lock Screen", TastyToast.LENGTH_LONG,TastyToast.SUCCESS).show();
                    }
                    else{
                        TastyToast.makeText(getApplicationContext(),"Not Supported", TastyToast.LENGTH_LONG,TastyToast.ERROR).show();
                    }
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(FullScreenWallpaper.this);
                } else {

                }
                bottomSheetDialog.dismiss();

            }
        });
        setboth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                Bitmap bitmap  = ((BitmapDrawable)photoView.getDrawable()).getBitmap();
                try {
                    wallpaperManager.setBitmap(bitmap);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        wallpaperManager.setBitmap(bitmap,null,true,WallpaperManager.FLAG_LOCK);//For Lock screen
                    }
                    TastyToast.makeText(getApplicationContext(),"Wallpaper Set on Both", TastyToast.LENGTH_LONG,TastyToast.SUCCESS).show();


                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(FullScreenWallpaper.this);
                } else {

                }

                bottomSheetDialog.dismiss();
            }

        });
        savegall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadManager downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(originalUrl);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                downloadManager.enqueue(request);

                TastyToast.makeText(getApplicationContext(),"Downloading Start", TastyToast.LENGTH_LONG,TastyToast.SUCCESS).show();
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(FullScreenWallpaper.this);
                } else {

                }
                bottomSheetDialog.dismiss();
            }

        });

    }


}