package com.example.demo.form;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
import java.util.*;

public class ReviewForm {
    @Size(max = 255)
    private String reviewer;
    @NotNull @Min(1) @Max(5)
    private Integer rating = 5;
    @Size(max = 2000)
    private String comment;
    public String getReviewer() { return reviewer; }
    public void setReviewer(String reviewer) { this.reviewer = reviewer; }
    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public boolean hasContent() {
        return (reviewer != null && !reviewer.isBlank()) || (comment != null && !comment.isBlank());
    }
    @AssertTrue(message = "กรุณากรอกชื่อผู้รีวิวเมื่อเขียนความคิดเห็น")
    public boolean isComplete() { return !hasContent() || (reviewer != null && !reviewer.isBlank()); }

}
