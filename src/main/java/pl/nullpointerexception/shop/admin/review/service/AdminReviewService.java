package pl.nullpointerexception.shop.admin.review.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.nullpointerexception.shop.admin.review.model.AdminReview;
import pl.nullpointerexception.shop.admin.review.repository.AdminReviewRepository;

@Service
@RequiredArgsConstructor
public class AdminReviewService {

    private final AdminReviewRepository adminReviewRepository;


    public Page<AdminReview> getReviews(Pageable pageable) {
        return adminReviewRepository.findAll(pageable);
    }

    public void deleteReview(Long id) {
        adminReviewRepository.deleteById(id);
    }

    @Transactional
    public void moderate(Long id) {
        adminReviewRepository.moderate(id);
    }
}
