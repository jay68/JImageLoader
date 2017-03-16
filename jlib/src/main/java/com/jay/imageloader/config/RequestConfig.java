package com.jay.imageloader.config;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jay.imageloader.cache.DiskCacheStrategy;
import com.jay.imageloader.cache.DoubleCacheStrategy;
import com.jay.imageloader.cache.JCacheStrategy;
import com.jay.imageloader.cache.NoneCacheStrategy;

import java.io.File;
import java.net.URL;

/**
 * 参数设置类
 */

public class RequestConfig {
    private Context mContext;
    //加载地址
    private String mAddress;
    private JCacheStrategy mCacheStrategy;
    //图片未加载（加载失败）时的占位图
    private Drawable mPlaceHolder;
    private Drawable mErrorHolder;

    private RequestConfig() {
    }

    public Context getContext() {
        return mContext;
    }

    public Drawable getErrorHolder() {
        return mErrorHolder;
    }

    public Drawable getPlaceHolder() {
        return mPlaceHolder;
    }

    public String getAddress() {
        return mAddress;
    }

    public JCacheStrategy getCacheStrategy() {
        return mCacheStrategy;
    }

    public static class Builder {
        private RequestConfig mConfig = new RequestConfig();
        //缓存目录
        private String mCacheDir;

        public Builder(@NonNull Context context) {
            mConfig.mContext = context;
        }

        public RequestConfig build() {
            JCacheStrategy cacheStrategy = mConfig.getCacheStrategy();
            if (cacheStrategy == null)
                mConfig.mCacheStrategy = DoubleCacheStrategy.getInstance();
            //对磁盘或双缓存设置缓存目录
            if (cacheStrategy instanceof DiskCacheStrategy) {
                if (mCacheDir == null)
                    mCacheDir = mConfig.mContext.getExternalCacheDir().getPath();
                ((DiskCacheStrategy) cacheStrategy).setCacheDir(mCacheDir);
            } else if (cacheStrategy instanceof DoubleCacheStrategy) {
                if (mCacheDir == null)
                    mCacheDir = mConfig.mContext.getExternalCacheDir().getPath();
                ((DoubleCacheStrategy) cacheStrategy).setCacheDir(mCacheDir);
            }
            return mConfig;
        }

        public Builder error(Drawable drawable) {
            mConfig.mErrorHolder = drawable;
            return this;
        }

        public Builder error(@DrawableRes int id) {
            error(mConfig.mContext.getResources().getDrawable(id));
            return this;
        }

        public Builder placeHolder(Drawable drawable) {
            mConfig.mPlaceHolder = drawable;
            return this;
        }

        public Builder placeHolder(@DrawableRes int id) {
            placeHolder(mConfig.mContext.getResources().getDrawable(id));
            return this;
        }

        public Builder cacheStrategy(@Nullable JCacheStrategy cacheStrategy) {
            if (cacheStrategy == null)
                mConfig.mCacheStrategy = NoneCacheStrategy.getInstance();
            else
                mConfig.mCacheStrategy = cacheStrategy;
            return this;
        }

        public Builder from(URL url) {
            mConfig.mAddress = url.toString();
            return this;
        }

        public Builder from(String address) {
            mConfig.mAddress = address;
            return this;
        }

        public Builder from(File file) {
            from(file.getPath());
            return this;
        }

        public Builder cacheDir(String cacheDir) {
            mCacheDir = cacheDir;
            return this;
        }
    }
}