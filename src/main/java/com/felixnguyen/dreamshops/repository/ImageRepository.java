package com.felixnguyen.dreamshops.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.felixnguyen.dreamshops.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
