package com.media.streaming.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static java.lang.Math.min;
import org.springframework.core.io.FileUrlResource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.lang3.StringUtils;

@RestController
public class VideoController {

    private static final long CHUNK_SIZE = 1000000L;
    public static final String VideoUploadingDir =Paths.get(System.getProperty("user.dir")).getFileSystem().getRootDirectories().iterator().next().toString() + "home/ubuntu/Video";
    @GetMapping(value = "/video/{fileType}/{fileName}", produces = "application/octet-stream")
    public ResponseEntity<ResourceRegion> getVideo(@RequestHeader(value = "Range", required = false) String rangeHeader, @PathVariable("fileType") String fileType, @PathVariable("fileName") String fileName)
            throws IOException {

        if (!new File(VideoUploadingDir).exists()) {
            new File(VideoUploadingDir).mkdirs();
        }
        return getVideoRegion(rangeHeader, fileName);

    }

    public ResponseEntity<ResourceRegion> getVideoRegion(String rangeHeader, String fileName) throws IOException {
        FileUrlResource videoResource = new FileUrlResource(VideoUploadingDir + "/"+fileName+".mp4");
        ResourceRegion resourceRegion = getResourceRegion(videoResource, rangeHeader);

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(MediaTypeFactory.getMediaType(videoResource).orElse(MediaType.APPLICATION_OCTET_STREAM))
                .body(resourceRegion);
    }

    private ResourceRegion getResourceRegion(UrlResource video, String httpHeaders) throws IOException {
        ResourceRegion resourceRegion = null;

        long contentLength = video.contentLength();
        int fromRange = 0;
        int toRange = 0;
        if (StringUtils.isNotBlank(httpHeaders)) {
            String[] ranges = httpHeaders.substring("bytes=".length()).split("-");
            fromRange = Integer.valueOf(ranges[0]);
            if (ranges.length > 1) {
                toRange = Integer.valueOf(ranges[1]);
            } else {
                toRange = (int) (contentLength - 1);
            }
        }

        if (fromRange > 0) {
            long rangeLength = min(CHUNK_SIZE, toRange - fromRange + 1);
            resourceRegion = new ResourceRegion(video, fromRange, rangeLength);
        } else {
            long rangeLength = min(CHUNK_SIZE, contentLength);
            resourceRegion = new ResourceRegion(video, 0, rangeLength);
        }

        return resourceRegion;
    }

}