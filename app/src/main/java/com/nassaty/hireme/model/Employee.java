package com.nassaty.hireme.model;

public class Employee {

	private String employee_uid;
	private String employer_uid;

	public Employee() {
	}

	public String getEmployee_uid() {
		return employee_uid;
	}

	public void setEmployee_uid(String employee_id) {
		this.employee_uid = employee_id;
	}

	public String getEmployer_uid() {
		return employer_uid;
	}

	public void setEmployer_uid(String employer_uid) {
		this.employer_uid = employer_uid;
	}
}
