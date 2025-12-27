CREATE TABLE post_categories (
                                 post_id BIGINT NOT NULL,
                                 category_id BIGINT NOT NULL,
                                 PRIMARY KEY (post_id, category_id),
                                 CONSTRAINT fk_post_categories_post FOREIGN KEY (post_id) REFERENCES t_post (id) ON DELETE CASCADE,
                                 CONSTRAINT fk_post_categories_category FOREIGN KEY (category_id) REFERENCES t_category (id) ON DELETE CASCADE
);