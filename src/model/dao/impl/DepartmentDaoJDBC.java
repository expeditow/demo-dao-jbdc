package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao{
	
	private Connection conn;
	
	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		
		Department dep = new Department();
		dep.setId(rs.getInt("Id"));
		dep.setName(rs.getString("Name"));
		return dep;
	}
	
	@Override
	public void insert(Department obj) {
		
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"INSERT INTO department (Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getName());
			
			int rowsAff = st.executeUpdate();
			
			if(rowsAff > 0) {
				
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Department obj) {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement("UPDATE department SET Name = ? Where Id = ?");
			
			st.setString(1, obj.getName());
			st.setInt(2, obj.getId());
			
			st.executeUpdate();
			
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement("DELETE FROM department\r\n WHERE Id = ?");
			
			st.setInt(1, id);
			
			int rows = st.executeUpdate();
			
			if(rows == 0) {
				throw new DbException("No line with the corresponding ID.");
			}
			
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Department findById(Integer id) { // Deu certo!
		
		PreparedStatement st = null;
		ResultSet rs = null;
	
		try {
			// Preparando o comando
			st = conn.prepareStatement(
					"SELECT department.* "
					+ "FROM department "
					+ "WHERE department.id = ?");
			
			// 1 representa a posição do ?, e o segundo parâmetro é por qual valor vamos inserir
			st.setInt(1, id); 
			
			// Realizamos o comando
			rs = st.executeQuery();
			
			// Ele começa do 0 então fazemos isso p verificar se há elementos da busca
			if(rs.next()) { 
				Department dep = instantiateDepartment(rs);
				return dep;
			}
			return null;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Department> findAll() {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			
			st = conn.prepareStatement("SELECT department.* FROM department ORDER BY Name ");
			rs = st.executeQuery();
			
			List<Department> dep = new ArrayList<>();
			Department d;
			
			while(rs.next()) {
				d = instantiateDepartment(rs);
				dep.add(d);
			}
			
			return dep;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

}
