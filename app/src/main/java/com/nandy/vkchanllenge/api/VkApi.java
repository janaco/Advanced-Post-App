package com.nandy.vkchanllenge.api;

import android.graphics.Bitmap;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKAttachments;
import com.vk.sdk.api.model.VKPhotoArray;
import com.vk.sdk.api.model.VKWallPostResult;
import com.vk.sdk.api.photo.VKImageParameters;
import com.vk.sdk.api.photo.VKUploadImage;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yana on 15.09.17.
 */

public class VkApi {

    private static final int TARGET_GROUP = 60479154;

    public static Single<Void> uploadImageToWall(Bitmap image) {

        return Single.create((SingleOnSubscribe<Void>) e -> loadImage(image, e)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());


    }

    private static void loadImage(Bitmap image, SingleEmitter<Void> e) {
        VKRequest request = VKApi.uploadWallPhotoRequest
                (new VKUploadImage(image, VKImageParameters.pngImage()), 0, TARGET_GROUP);

        request.executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                image.recycle();
                VKApiPhoto photoModel = ((VKPhotoArray) response.parsedModel).get(0);
                makePost(new VKAttachments(photoModel), e);
            }

            @Override
            public void onError(VKError error) {
                if (error.httpError != null) {
                    e.onError(new Throwable(error.errorMessage));
                }
            }
        });
    }

    private static void makePost(VKAttachments attachments, SingleEmitter<Void> e) {
        VKRequest post = VKApi.wall().post(VKParameters.from(VKApiConst.OWNER_ID, "-" + TARGET_GROUP, VKApiConst.ATTACHMENTS, attachments, VKApiConst.MESSAGE, null));
        post.setModelClass(VKWallPostResult.class);
        post.executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                e.onSuccess(null);
            }

            @Override
            public void onError(VKError error) {
                e.onError(new Throwable(error.errorMessage));
            }
        });
    }
}
