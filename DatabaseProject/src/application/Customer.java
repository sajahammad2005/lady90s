package application;

import java.sql.PreparedStatement;
//
public class Customer {
public int cId;
private String name , phone , email , address;

public Customer() {}
public Customer(int cId , String name , String phone , String email , String address) {
	addToDB(cId, name, phone, email, address);
}
public void addToDB(int cId , String name , String phone , String email , String address) {
	String sql = "INSERT INTO customer (customer_id, name, phone, email , address) VALUES (?, ?, ?, ?, ?) ";
	try (PreparedStatement st = Main.conn.prepareStatement(sql) ){
		st.setInt(1, cId);
		st.setString(2, name);
		st.setString(3, phone);
		st.setString(4, email);
		st.setString(5, address);
		st.executeUpdate();
		
	}catch (Exception e) {
		return;
	}
	setcId(cId);
	setName(name);
	setPhone(phone);
	setEmail(email);
	setAddress(address);
}
public void updateDB(int cId , String name , String phone , String email , String address) {
	String sql = "UPDATE customer set name = ?, phone = ? , email = ?, address = ? WHERE customer_id = ?";
	try (PreparedStatement st = Main.conn.prepareStatement(sql) ){
		
		st.setString(1, name);
		st.setString(2, phone);
		st.setString(3, email);
		st.setString(4, address);
		st.setInt(5, cId);
		st.executeUpdate();
		
	}catch (Exception e) {
		return;
	}
	setName(name);
	setPhone(phone);
	setEmail(email);
	setAddress(address);
}
public int getCId() {
	return cId;
}
public void setcId(int cId) {
	this.cId = cId;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getPhone() {
	return phone;
}
public void setPhone(String phone) {
	this.phone = phone;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getAddress() {
	return address;
}
public void setAddress(String address) {
	this.address = address;
}


}
