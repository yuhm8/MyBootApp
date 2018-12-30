package com.example.demo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

@Repository
public class MyDataDaoImpl implements MyDataDao<MyData> {
	private static final long serialVersionUID = 1L;

	private EntityManager entityManager;

	public MyDataDaoImpl() {
		super();
	}
	public MyDataDaoImpl(EntityManager manager) {
		this();
		entityManager = manager;
	}

	@Override
	public List<MyData> getAll() {
		List<MyData> list = null;
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MyData> query = builder.createQuery(MyData.class);
		Root<MyData> root = query.from(MyData.class);
		query.select(root);
		list = (List<MyData>)entityManager.createQuery(query).getResultList();
		entityManager.close();
		return list;
	}
	@Override
	public MyData findById(long id) {
		return (MyData)entityManager.createQuery("from MyData where id = "
				+  id).getSingleResult();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<MyData> findByName(String name) {
		return (List<MyData>)entityManager.createQuery("from MyData where name =" + name ).getResultList();

	}
	@SuppressWarnings("unchecked")
	@Override
	public List<MyData> find(String fstr) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MyData> query = builder.createQuery(MyData.class);
		Root<MyData> root = query.from(MyData.class);
		query.select(root).where(builder.equal(root.get("name"),fstr));
		List<MyData> list = null;
		list = (List<MyData>)entityManager.createQuery(query).getResultList();
		entityManager.close();
		return list;

	}

}
