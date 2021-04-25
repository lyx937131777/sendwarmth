package com.example.sendwarmth.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sendwarmth.InterestringActivityActivity;
import com.example.sendwarmth.db.Carousel;
import com.example.sendwarmth.db.InterestingActivity;
import com.example.sendwarmth.presenter.HomePresenter;
import com.example.sendwarmth.util.HttpUtil;
import com.example.sendwarmth.util.LogUtil;
import com.sun.banner.BannerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CarouselBannerAdapter extends BannerAdapter {
    private List<Carousel> carouselList;
    private List<ImageView> imageViewList;

    public CarouselBannerAdapter(List<Carousel> carouselList, final Context context, final HomePresenter homePresenter){
        this.carouselList = carouselList;
        imageViewList = new ArrayList<>();
        for(final Carousel carousel : carouselList){
            ImageView imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context,carousel.getLink(), Toast.LENGTH_LONG).show();
                    switch (carousel.getCarouselType()){
                        case "business":{
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(carousel.getLink()));
                            context.startActivity(intent);
                            break;
                        }
                        case "activity":{
                            homePresenter.startInterestingActivityActivity(carousel.getLink());
                            break;
                        }
                        case "topic":{
                            homePresenter.startHealthBroadcastActivity(carousel.getLink());
                            break;
                        }
                    }
                }
            });
            Glide.with(context).load(HttpUtil.getResourceURL(carousel.getCarouselPic())).into(imageView);
            imageViewList.add(imageView);
        }
    }

    @Override
    public View getView(int position, View converView) {
        View view = imageViewList.get(position);
        ViewGroup parent = (ViewGroup) view.getParent();
        if(parent != null){
            LogUtil.e("CarouselBannerAdapter","有父控件！！！");
            parent.removeAllViews();
        }
        return view;
    }

    @Override
    public int getCount() {
        return carouselList.size();
    }
}
