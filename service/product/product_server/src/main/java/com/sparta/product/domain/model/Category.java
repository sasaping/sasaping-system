package com.sparta.product.domain.model;

import com.sparta.common.domain.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "P_CATEGORY")
@Getter
@NoArgsConstructor
@SQLRestriction("is_deleted = false")
@SQLDelete(sql = "UPDATE p_category SET is_deleted = true where category_id = ?")
public class Category extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long categoryId;

  @Column(nullable = false)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private Category parent;

  @Column private boolean isDeleted = false;

  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Category> subCategories = new ArrayList<>();

  public Category(String name, Category parent) {
    this.name = name;
    this.parent = parent;
  }

  public void update(String name, Category category) {
    this.name = name;
    this.parent = category;
  }

  public void addSubCategory(Category subCategory) {
    this.subCategories.add(subCategory);
  }
}
