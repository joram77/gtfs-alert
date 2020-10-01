package be.wethinkonline.gtfs_notify.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the "calendar_dates" database table.
 * 
 */
@Entity
@Table(name="\"calendar_dates\"")
@NamedQuery(name="CalendarDate.findAll", query="SELECT c FROM CalendarDate c")
public class CalendarDate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="\"date\"")
	private String date;

	@Column(name="\"exception_type\"")
	private int exceptionType;

	@Id
	@Column(name="\"service_id\"")
	private String serviceId;

	@Column(name="\"service_index\"")
	private int serviceIndex;

	public CalendarDate() {
	}

	public String getDate() {
		return this.date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getExceptionType() {
		return this.exceptionType;
	}

	public void setExceptionType(int exceptionType) {
		this.exceptionType = exceptionType;
	}

	public String getServiceId() {
		return this.serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public int getServiceIndex() {
		return this.serviceIndex;
	}

	public void setServiceIndex(int serviceIndex) {
		this.serviceIndex = serviceIndex;
	}

}