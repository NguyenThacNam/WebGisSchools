package com.humg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.humg.entity.Location;
import com.humg.service.LocationService;

@Controller
@RequestMapping("admin/location")
public class LocationController {

    @Autowired
    private LocationService locaSv;

    // Thêm mới
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("location", new Location());
        return "admin/location/form";
    }

    //
    @PostMapping("/save")
    public String save(@ModelAttribute Location location,
                      @RequestParam(name = "imageFile", required = false) MultipartFile imageFile ,RedirectAttributes redirectAttributes) {
        try {
            locaSv.saveLocation(location, imageFile);
            redirectAttributes.addFlashAttribute("successMessage", "Đã thêm địa điểm mới thành công!");
            return "redirect:/admin/location";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/location/create?error=upload";
        }
    }

    // Xóa 
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        locaSv.deleteById(id);
        return "redirect:/admin/location";
    }

    // Sửa
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Location location = locaSv.getById(id).orElse(null);
        model.addAttribute("location", location);
        return "admin/location/form-edit";
    }

    // 
    @PostMapping("/update")
    public String updateLocation(@ModelAttribute Location location,
                                @RequestParam(name = "imageFile", required = false) MultipartFile imageFile ,RedirectAttributes redirectAttributes) {
        try {
            locaSv.updateLocation(location, imageFile);
            redirectAttributes.addFlashAttribute("successMessage", "Update thành công!");
            return "redirect:/admin/location";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/location/edit/" + location.getId() + "?error=upload";
        }
    }

    // Tìm kiếm 
    @GetMapping("/search")
    public String search(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        model.addAttribute("data", locaSv.searchLocations(keyword));
        model.addAttribute("keyword", keyword);
        return "admin/location/list";
    }

    // Phân trang
    @GetMapping("")
    public String list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {
        model.addAttribute("data", locaSv.getPaginatedLocation(page, size).getContent()); // lấy tất cả sản phẩm
        model.addAttribute("currentPage", page);// trang hiện tại
        model.addAttribute("totalPages", locaSv.getPaginatedLocation(page, size).getTotalPages()); // tổng trang
        return "admin/location/list";
    }
}
