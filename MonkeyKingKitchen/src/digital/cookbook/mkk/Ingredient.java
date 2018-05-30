package digital.cookbook.mkk;

import java.io.Serializable;
import java.sql.*;

/**
 * The entity class of Ingredient
 * 
 * @author Jisheng, Zifeng Zhang
 * @version 1.0 26/5/2018
 * @version 2.0 27/5/2018
 */

public class Ingredient implements Serializable {
	// ingredient attributes
	private String name;
	private String unit;
	private String processMethod;
	private double amount;

	/**
	 * Constructor without process method of the ingredient
	 * 
	 * @param name
	 * @param amount
	 * @param unit
	 */
	public Ingredient(String name, double amount, String unit) {
		this.name = name;
		this.amount = amount;
		this.unit = unit;
	}

	/**
	 * Constructor with process method of the ingredient
	 * 
	 * @param name
	 * @param amount
	 * @param unit
	 * @param processMethod
	 */
	public Ingredient(String name, double amount, String unit, String processMethod) {
		this.name = name;
		this.amount = amount;
		this.unit = unit;
		this.processMethod = processMethod;
	}

	// getters and setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getProcessMethod() {
		return processMethod;
	}

	public void setProcessMethod(String processMethod) {
		this.processMethod = processMethod;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}
