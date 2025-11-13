package com.pradeep;

import java.util.List;

public interface StudentDAO {
	boolean insert(Student student) throws Exception;
	boolean update(Student student) throws Exception;
	boolean delete(int id) throws Exception;
	Student getById(int id) throws Exception;
	List<Student> getAll() throws Exception;
}
