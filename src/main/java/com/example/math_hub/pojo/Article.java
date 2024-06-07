package com.example.math_hub.pojo;

import com.example.math_hub.anno.State;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;
@Data
public class Article {

    private Integer id;//主键ID
    @NotEmpty
    @Pattern(regexp = "^\\S{1,10}$")
    private String title;
    @NotEmpty
    private String content;

    @URL
    private String cover_img;
    @State
    private String state;//已发布|草稿
    @NotNull
    private Integer category_id;//文章分类id
    private Integer create_user;//创建人ID
    private LocalDateTime create_time;
    private LocalDateTime update_time;
}
