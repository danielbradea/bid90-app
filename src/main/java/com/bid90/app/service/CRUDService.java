package com.bid90.app.service;

public interface CRUDService<T> {
	T create(T t);
	T reade(Long i);
	T update(T t);
    void delete(T t);
}
