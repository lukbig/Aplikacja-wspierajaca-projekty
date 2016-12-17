package com.bigos.awp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * Created by bigos on 04.12.16.
 */

public interface TemplateService<T> {
    T save(T entity);

    T findOne(long entityId);

    void delete(T entity);

    void delete(long entityId);

    void delete(List<T> entities);

    List<T> findAll(Sort sort);

    List<T> findAll(List<Long> entityIds);

    List<T> findAll();

    Page<T> findAll(final int page, final int size);
}
