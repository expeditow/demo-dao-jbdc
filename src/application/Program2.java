package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
		DepartmentDao depDao = DaoFactory.creacteDepartmentDao();
		
		System.out.println("=== TEST 1: department findById ====");
		System.out.println("Enter the ID you want to search for ");
		int idt = sc.nextInt();
		Department dep = depDao.findById(idt);
		System.out.println(dep);
		
		System.out.println("\n=== TEST 2: department findAll ====");
		List<Department> list = depDao.findAll();
		
		for(Department obj : list){
			System.out.println(obj);
		}
		
		System.out.println("\n=== TEST 3: department insert ====");
		Department newDep = new Department(null, "Cloaths");
		depDao.insert(newDep);
		System.out.println("Inserted! New id = " + newDep.getId());
		
		System.out.println("\n=== TEST 4: department update ====");
		dep = depDao.findById(8);
		dep.setName("Lebron James");
		depDao.update(dep);
		System.out.println("Update completed!");
		
		System.out.println("\n=== TEST 5: department delete ====");
		System.out.println("Enter the Id to delete");
		idt = sc.nextInt();
		depDao.deleteById(idt);
		System.out.println("Delete completed");
		
		
		sc.close();
	}

}
