package com.yunsoo.dao;

import java.util.List;
import com.yunsoo.model.Product;

public interface ProductDao {
	public Product getById(int id);
    public void save(Product product);
    public void update(Product product);
    public void delete(Product product);
    public List<Product> getAllProducts();

}
