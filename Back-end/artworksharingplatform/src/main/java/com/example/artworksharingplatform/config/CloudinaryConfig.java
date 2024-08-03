package com.example.artworksharingplatform.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary getCloudinary() {
        Map config = new HashMap();
        config.put("cloud_name", "diak7ssve");
        config.put("api_key", "921466739317785");
        config.put("api_secret", "dZ5EZms4AjY-44krn67szJbjmSw");
        config.put("secure", true);

        return new Cloudinary(config);
    }

}
