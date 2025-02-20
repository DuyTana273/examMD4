package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.model.ProductStatus;
import com.example.demo.model.dto.ProductDTO;
import com.example.demo.service.ICategoryService;
import com.example.demo.service.IProductService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private IProductService iProductService;
    @Autowired
    private ICategoryService iCategoryService;

    @GetMapping("/list")
    public String showAllProducts(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "price", required = false) BigDecimal price,
            @RequestParam(name = "categoryId", required = false) Long categoryId,
            Model model) {

        Pageable pageable = PageRequest.of(page, 5);
        Page<Product> products;

        // Nếu có bất kỳ điều kiện tìm kiếm nào, gọi searchProducts; nếu không thì dùng findAll.
        if ((name != null && !name.isEmpty()) || price != null || categoryId != null) {
            products = iProductService.searchProducts(name, price, categoryId, pageable);
        } else {
            products = iProductService.findAll(pageable);
        }

        List<Category> categories = iCategoryService.findAll();

        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("name", name);
        model.addAttribute("price", price);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("currentPage", products.getNumber() + 1);
        model.addAttribute("totalPages", products.getTotalPages());
        return "product/list";
    }


    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new ProductDTO());
        model.addAttribute("categories", iCategoryService.findAll());
        model.addAttribute("statuses", ProductStatus.values());
        return "product/add";
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute("product") ProductDTO productDTO,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        if (iProductService.existsByName(productDTO.getName())) {
            bindingResult.rejectValue("name", "", "Đã tồn tại sản phẩm này!");
            model.addAttribute("categories", iCategoryService.findAll());
            model.addAttribute("statuses", ProductStatus.values());
        }

        new ProductDTO().validate(productDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", iCategoryService.findAll());
            model.addAttribute("statuses", ProductStatus.values());
            model.addAttribute("product", productDTO);
            return "product/add";
        }
        Product product = new Product();
        BeanUtils.copyProperties(productDTO, product);
        iProductService.save(product);
        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Thêm mới sản phẩm thành công");

        return "redirect:/product/list";
    }

    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        ProductDTO productDTO = new ProductDTO();
        BeanUtils.copyProperties(iProductService.getProductById(id), productDTO);
        model.addAttribute("product", productDTO);
        model.addAttribute("categories", iCategoryService.findAll());
        model.addAttribute("statuses", ProductStatus.values());
        return "product/edit";
    }

    @PostMapping("/edit")
    public String updateProduct(@Valid @ModelAttribute("product") ProductDTO productDTO,
                                BindingResult bindingResult,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        Product product = iProductService.getProductById(productDTO.getId());

        if (!product.getName().equals(productDTO.getName()) && iProductService.existsByName(productDTO.getName())) {
            bindingResult.rejectValue("name", "", "Sản phẩm này đã tồn tại");
            model.addAttribute("statuses", ProductStatus.values());
        }
        new ProductDTO().validate(productDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", iCategoryService.findAll());
            model.addAttribute("statuses", ProductStatus.values());
            model.addAttribute("product", productDTO);
            return "product/edit";
        }

        BeanUtils.copyProperties(productDTO, product);
        iProductService.save(product);
        redirectAttributes.addFlashAttribute("messageType", "success");
        redirectAttributes.addFlashAttribute("message", "Cập nhật sản phẩm thành công");

        return "redirect:/product/list";
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public String deleteSelectedProducts(@RequestParam("ids") String ids) {
        // Tách chuỗi các id theo dấu phẩy
        String[] idArray = ids.split(",");
        for (String idStr : idArray) {
            Long id = Long.parseLong(idStr.trim());
            // Xóa từng sản phẩm theo id
            iProductService.deleteById(id);
        }
        return "Deleted successfully!";
    }

}
