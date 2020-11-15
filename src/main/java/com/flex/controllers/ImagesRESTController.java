package com.flex.controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.flex.dao.ImageDao;
import com.flex.models.ExtendedUserDetails;
import com.flex.models.ImageModel;
import com.flex.services.ImageUploadingService;
import com.flex.viewModels.ImageUploadingViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
public class ImagesRESTController {

    private final ImageDao dao;

    @Autowired
    private ImageUploadingService service;

    @Autowired
    public ImagesRESTController(ImageDao dao) {
        this.dao = dao;
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        ExtendedUserDetails user = (ExtendedUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return ResponseEntity.ok("User id: " + user.getId());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ImageModel>> search(String name) {
        List<ImageModel> images = dao.findByName(name);
        images.forEach(ImageModel::makeExtendedUrl);
        return ResponseEntity.ok(images);
    }

    @PostMapping("/")
    public ResponseEntity<ImageModel> uploadImage(ImageUploadingViewModel image) {
        Object user = SecurityContextHolder.getContext().getAuthentication().getDetails();
        try {
            ImageModel model = service.uploadImage(image, (ExtendedUserDetails) user);
            return ResponseEntity.ok().body(model);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/last")
    public ResponseEntity<List<ImageModel>> lastUploadedImages(int count) {
        if(count > 0) {
            return ResponseEntity.ok().body(dao.getLastImages(count));
        }
        return ResponseEntity.badRequest().body(null);
    }
}