package com.self.dao;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 基础Dao接口定义
 * @author jie
 * @date 2018-01-16
 */
@NoRepositoryBean
public interface BaseDao<E,ID extends Serializable>  extends JpaRepository<E,ID>, JpaSpecificationExecutor<E> {

}
