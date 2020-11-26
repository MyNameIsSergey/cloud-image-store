package com.flex.services;

import com.flex.exeptions.ImageNotFoundException;
import com.flex.models.ExtendedUserDetails;
import com.flex.models.ImageModel;
import com.flex.viewModels.ImageUploadingViewModel;

import java.io.IOException;

public interface ImageUploadingService {

    /**
     * Store image on file server
     * Write row to database
     * @param image
     * @param user
     * @return image model with URLs.
     */
    ImageModel uploadImage(ImageUploadingViewModel image, ExtendedUserDetails user) throws IOException;

    void deleteImage(ImageModel model) throws IOException, ImageNotFoundException;
}
