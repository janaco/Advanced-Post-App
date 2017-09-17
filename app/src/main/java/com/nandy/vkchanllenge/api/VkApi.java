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

/**
 * Created by yana on 15.09.17.
 */

public class VkApi {

    private Callback callback;

    public VkApi(Callback callback) {
        this.callback = callback;
    }

    private VKRequest request;

    public void uploadImageToWall(Bitmap image) {
        request = VKApi.uploadWallPhotoRequest
                (new VKUploadImage(image, VKImageParameters.pngImage()), 0, 0);

        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                image.recycle();
                VKApiPhoto photoModel = ((VKPhotoArray) response.parsedModel).get(0);
                makePost(new VKAttachments(photoModel));
            }

            @Override
            public void onError(VKError error) {
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
                callback.onResult(true);
            }

            @Override
            public void onError(VKError error) {
                callback.onResult(false);
            }
        });
    }

    public void cancelRequest() {
        if (request != null) {
            request.cancel();
        }
    }

    public interface Callback {

        void onResult(boolean success);
    }

}
