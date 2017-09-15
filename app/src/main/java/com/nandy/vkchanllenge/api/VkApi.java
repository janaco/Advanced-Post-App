package com.nandy.vkchanllenge.api;

import android.graphics.Bitmap;
import android.util.Log;

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
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yana on 15.09.17.
 */

public class VkApi {

    private static final int TARGET_GROUP = -1;

    private Callback callback;

    public VkApi(Callback callback) {
        this.callback = callback;
    }

    private VKRequest request;

    public void uploadImageToWall(Bitmap image) {
        request = VKApi.uploadWallPhotoRequest
                (new VKUploadImage(image, VKImageParameters.pngImage()), 0, 0);
        Log.d("POST_", "uploadWallPhotoRequest: " + image);

        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                Log.d("POST_", "onComplete: " + response.json);
                image.recycle();
                VKApiPhoto photoModel = ((VKPhotoArray) response.parsedModel).get(0);
                makePost(new VKAttachments(photoModel));
            }

            @Override
            public void onError(VKError error) {
                Log.d("POST_", "onError: " + error);
                if (error.httpError != null) {
                    callback.onResult(false);
                }
            }
        });

    }

    private void makePost(VKAttachments attachments) {
        request = VKApi.wall().post(VKParameters.from(VKApiConst.ATTACHMENTS, attachments, VKApiConst.MESSAGE, null));
        request.setModelClass(VKWallPostResult.class);
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                Log.d("POST_", "success: " + response.json);
               callback.onResult(true);
            }

            @Override
            public void onError(VKError error) {
                callback.onResult(false);
            }
        });
    }

    public void cancelRequest() {
//        Log.d("POST_", "cancel: " + request);
        if (request != null) {
//            request.cancel();
        }
    }

    public interface Callback {

        void onResult(boolean success);
    }

}
