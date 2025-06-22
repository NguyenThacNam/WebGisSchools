package com.humg.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.humg.entity.Location;
import com.humg.repository.LocationRepository;

@Service
public class LocationService {
    @Autowired
    private LocationRepository locaRepo;
    
    // Thư mục lưu ảnh (có thể thay đổi tùy vào hệ thống)
    private final String IMAGE_DIR = "images/";

    public List<Location> getAll() {
        return locaRepo.findAll();
    }

    // Thêm mới địa điểm
    public void saveLocation(Location location, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = saveImageFile(imageFile);
            location.setImageUrl("/images/" + fileName); // Đảm bảo lưu đường dẫn đúng
        }
        locaRepo.save(location);
    }

    // Xóa địa điểm
    public void deleteById(int id) {
        locaRepo.deleteById(id);
    }

    // Lấy địa điểm theo ID
    public Optional<Location> getById(Integer id) {
        return locaRepo.findById(id);
    }

    // Cập nhật địa điểm
    public void updateLocation(Location location) {
        locaRepo.save(location);
    }

    public void updateLocation(Location location, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = saveImageFile(imageFile);
            location.setImageUrl("/images/" + fileName);
        }
        locaRepo.save(location);
    }

    // Tìm kiếm địa điểm
    public List<Location> searchLocations(String keyword) {
        return locaRepo.findByNameContaining(keyword);
    }

    // Phân trang
    public Page<Location> getPaginatedLocation(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return locaRepo.findAll(pageable);
    }

    // Lưu ảnh vào thư mục
    private String saveImageFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
        Path uploadPath = Paths.get(IMAGE_DIR);
        
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        return fileName;
    }
}
