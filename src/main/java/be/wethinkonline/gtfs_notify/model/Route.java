package be.wethinkonline.gtfs_notify.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the "routes" database table.
 * 
 */
@Entity
@Table(name="\"routes\"")
@NamedQuery(name="Route.findAll", query="SELECT r FROM Route r")
public class Route implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="\"agency_id\"")
	private String agencyId;

	@Column(name="\"route_color\"")
	private String routeColor;

	@Column(name="\"route_desc\"")
	private String routeDesc;

	@Id
	@Column(name="\"route_id\"")
	private String routeId;

	@Column(name="\"route_index\"")
	private int routeIndex;

	@Column(name="\"route_long_name\"")
	private String routeLongName;

	@Column(name="\"route_short_name\"")
	private String routeShortName;

	@Column(name="\"route_text_color\"")
	private String routeTextColor;

	@Column(name="\"route_type\"")
	private int routeType;

	@Column(name="\"route_url\"")
	private String routeUrl;

	public Route() {
	}

	public String getAgencyId() {
		return this.agencyId;
	}

	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}

	public String getRouteColor() {
		return this.routeColor;
	}

	public void setRouteColor(String routeColor) {
		this.routeColor = routeColor;
	}

	public String getRouteDesc() {
		return this.routeDesc;
	}

	public void setRouteDesc(String routeDesc) {
		this.routeDesc = routeDesc;
	}

	public String getRouteId() {
		return this.routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public int getRouteIndex() {
		return this.routeIndex;
	}

	public void setRouteIndex(int routeIndex) {
		this.routeIndex = routeIndex;
	}

	public String getRouteLongName() {
		return this.routeLongName;
	}

	public void setRouteLongName(String routeLongName) {
		this.routeLongName = routeLongName;
	}

	public String getRouteShortName() {
		return this.routeShortName;
	}

	public void setRouteShortName(String routeShortName) {
		this.routeShortName = routeShortName;
	}

	public String getRouteTextColor() {
		return this.routeTextColor;
	}

	public void setRouteTextColor(String routeTextColor) {
		this.routeTextColor = routeTextColor;
	}

	public int getRouteType() {
		return this.routeType;
	}

	public void setRouteType(int routeType) {
		this.routeType = routeType;
	}

	public String getRouteUrl() {
		return this.routeUrl;
	}

	public void setRouteUrl(String routeUrl) {
		this.routeUrl = routeUrl;
	}

}