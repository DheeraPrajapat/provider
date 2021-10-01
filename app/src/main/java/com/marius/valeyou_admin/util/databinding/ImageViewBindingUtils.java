package com.marius.valeyou_admin.util.databinding;

import androidx.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.marius.valeyou_admin.R;
import com.marius.valeyou_admin.data.beans.favourites.MyFavouritesModel;
import com.marius.valeyou_admin.data.beans.map.MapListModel;
import com.marius.valeyou_admin.data.beans.rejected_bids.RejectedModels;
import com.marius.valeyou_admin.data.remote.api.Urls;
import com.marius.valeyou_admin.di.module.GlideApp;
import com.marius.valeyou_admin.di.module.GlideRequest;

import java.util.List;

public class ImageViewBindingUtils {
    @BindingAdapter(value = {"profile_url"}, requireAll = false)
    public static void setProfileUrl(final ImageView imageView, String imageUrl) {
        GlideApp.with(imageView.getContext())
                .load(Urls.MEDIA_URL+imageUrl)
                .placeholder(R.drawable.placeholder)
                .into(imageView);
    }

    @BindingAdapter(value = {"image_src"}, requireAll = false)
    public static void setImageSrc(final ImageView imageView, String imageUrl) {
        GlideApp.with(imageView.getContext())
                .load(Urls.MEDIA_URL+imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    @BindingAdapter(value = {"profile_src"}, requireAll = false)
    public static void setProfilePicture(final ImageView imageView, String imageUrl) {
        GlideApp.with(imageView.getContext())
                .load(Urls.MEDIA_URL+imageUrl)
                .placeholder(R.drawable.placeholder)
                .into(imageView);
    }

    @BindingAdapter(value = {"image_src"}, requireAll = false)
    public static void setImage(final ImageView imageView, String imageUrl) {
        GlideApp.with(imageView.getContext())
                .load(Urls.MEDIA_URL+imageUrl)
                .placeholder(R.drawable.new_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }
    @BindingAdapter(value = {"profile_image"}, requireAll = false)
    public static void setProfileImage(final ImageView imageView, String imageUrl) {
        GlideApp.with(imageView.getContext())
                .load(Urls.MEDIA_URL+imageUrl)
                .placeholder(R.drawable.new_placeholder)
                .into(imageView);
    }

    @BindingAdapter(value = {"job_image_src"}, requireAll = false)
    public static void setJobImage(final ImageView imageView, List<MapListModel.OrderImagesBean> list) {
        if (list.size()>0) {
            GlideApp.with(imageView.getContext())
                    .load(Urls.MEDIA_URL+list.get(0).getImages())
                    .placeholder(R.drawable.new_placeholder)
                    .into(imageView);

        }else{
            imageView.setImageResource(R.drawable.new_placeholder);
        }
    }

    @BindingAdapter(value = {"job_fav_src"}, requireAll = false)
    public static void setFavJobImage(final ImageView imageView, List<MyFavouritesModel.OrderBean.OrderImagesBean> list) {
        if (list!=null) {
            if (list.size() > 0) {
                GlideApp.with(imageView.getContext())
                        .load(Urls.MEDIA_URL + list.get(0).getImages())
                        .placeholder(R.drawable.new_placeholder)
                        .into(imageView);

            }else{
                imageView.setImageResource(R.drawable.new_placeholder);
            }

        }else{
            imageView.setImageResource(R.drawable.new_placeholder);
        }
    }

    @BindingAdapter(value = {"rejected_job_image"}, requireAll = false)
    public static void setRejectedJobImage(final ImageView imageView, List<RejectedModels.OrderImagesBean> list) {
        if (list!=null) {
            if (list.size() > 0) {
                GlideApp.with(imageView.getContext())
                        .load(Urls.MEDIA_URL + list.get(0).getImages())
                        .placeholder(R.drawable.new_placeholder)
                        .into(imageView);

            }else{
                imageView.setImageResource(R.drawable.new_placeholder);
            }

        }else{
            imageView.setImageResource(R.drawable.new_placeholder);
        }
    }

    @BindingAdapter(value = {"image_url", "resize", "placeholder"}, requireAll = false)
    public static void setImageUrl(final ImageView imageView, String image_url, boolean resize, Drawable placeholder) {
        GlideRequest<Drawable> requests = GlideApp.with(imageView.getContext()).load(Urls.MEDIA_URL+image_url);
        if (resize)
            requests.override(120, 100);
        if (placeholder != null)
            requests.placeholder(placeholder);
        requests.centerCrop();
        requests.into(imageView);
    }

    @BindingAdapter(value = {"rectangle", "view_width", "view_height", "placeholder"}, requireAll = false)
    public static void rectangle(final ImageView imageView, String image_url, Integer view_width, Integer view_height, Drawable placeholder) {
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        if (view_width != null && view_height != null)
            options.override(view_width, view_height);
        if (placeholder != null)
            options.placeholder(placeholder);
        GlideApp.with(imageView.getContext()).load(image_url).apply(options).into(imageView);
    }


    @BindingAdapter(value = {"square", "placeholder"}, requireAll = false)
    public static void setSqure(final ImageView imageView, String image_url, Drawable placeholder) {
        GlideRequest<Drawable> requests = GlideApp.with(imageView.getContext()).load(image_url);
        requests.override(120);
        if (placeholder != null)
            requests.placeholder(placeholder);
        requests.centerCrop();
        requests.into(imageView);
    }

    @BindingAdapter(value = {"android:view_image"})
    public static void setViewImageUrl(final ImageView imageView, String image_url) {
        GlideRequest<Drawable> requests = GlideApp.with(imageView.getContext()).load(Urls.MEDIA_URL+image_url).fitCenter();
        requests.placeholder(R.drawable.new_placeholder);
        requests.into(imageView);
    }



    @BindingAdapter(value = {"simpleResourse"})
    public static void setStepGroupIcon(final ImageView imageView, int simpleResourse) {

        if (simpleResourse != -1) {
            imageView.setImageResource(simpleResourse);

        }
    }

    @BindingAdapter(value = {"chat_thumbnil", "corner"}, requireAll = false)
    public static void setThumnilUrl(final ImageView imageView, String image_url, String message) {
        GlideRequest<Drawable> requests = GlideApp.with(imageView.getContext()).load(image_url);
        requests.centerCrop();
        requests.into(imageView);
    }

}
