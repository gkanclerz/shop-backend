package pl.nullpointerexception.shop.admin.review.controller;

import liquibase.pro.packaged.P;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import pl.nullpointerexception.shop.admin.review.model.AdminReview;
import pl.nullpointerexception.shop.admin.review.service.AdminReviewService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/reviews")
public class AdminReviewController {

    private final AdminReviewService adminReviewService;

    @GetMapping
    public Page<AdminReview> getReviews(Pageable pageable){
        return adminReviewService.getReviews(pageable);
    }

    @DeleteMapping("/{id}")
    public void deleteReview(@PathVariable Long id){
        adminReviewService.deleteReview(id);
    }

    @PutMapping("/{id}/moderate")
    public void moderate(@PathVariable Long id){
        adminReviewService.moderate(id);
    }

}
