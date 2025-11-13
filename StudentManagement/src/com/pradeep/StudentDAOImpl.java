package com.pradeep;

import java.util.*;
import java.sql.*;

public class StudentDAOImpl implements StudentDAO{

	@Override
	public boolean insert(Student student) throws Exception {
		String sql = "INSERT INTO Student (id,name,branch) VALUES (?,?,?)";
		try(Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)){
			ps.setInt(1,student.getId());
			ps.setString(2,student.getName());
			ps.setString(3,student.getBranch());
			int rows = ps.executeUpdate();
			return rows > 0;
		}
	}

	@Override
	public boolean update(Student student) throws Exception {
		String sql = "UPDATE student SET name=?,branch=? WHERE id=?";
		try(Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)){
			ps.setString(1,student.getName());
			ps.setString(2,student.getBranch());
			ps.setInt(3,student.getId());
			int rows = ps.executeUpdate();
			return rows > 0;
		}
	}

	@Override
	public boolean delete(int id) throws Exception {
		String sql = "DELETE FROM Student WHERE id=?";
		try(Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)){
			ps.setInt(1,id);
			int rows = ps.executeUpdate();
			return rows>0;
		}
	}

	@Override
	public Student getById(int id) throws Exception {
		String sql = "SELECT id,name,branch FROM student WHERE id=?";
		try(Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql)){
			ps.setInt(1,id);
			try(ResultSet rs = ps.executeQuery()){
				if(rs.next()) {
					return mapRowToStudent(rs);
				}
				return null;
			}
		}
	}
	
	private Student mapRowToStudent(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String branch = rs.getString("branch");
        return new Student(id, name, branch);
    }

	@Override
	public List<Student> getAll() throws Exception {
		String sql = "SELECT id,name,branch FROM student";
		List<Student> list = new ArrayList<>();
		try(Connection con = DBConnection.getConnection();
				PreparedStatement ps = con.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()){
			while(rs.next()) {
				list.add(mapRowToStudent(rs));
			}
		}
		return list;
	}

}
