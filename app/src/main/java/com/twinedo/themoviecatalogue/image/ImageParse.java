package com.twinedo.themoviecatalogue.image;

import android.net.Uri;

import com.twinedo.themoviecatalogue.api.ServiceURL;

public class ImageParse {

    public static Uri movieUrl(String size, String imagePath) {
        imagePath = imagePath.replace("/", "");

        return Uri.parse(ServiceURL.IMAGE_URL).buildUpon()
                .appendPath(size)
                .appendPath(imagePath)
                .build();
    }
}