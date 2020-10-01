package be.wethinkonline.gtfs_notify.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the "alert_updates_processed" database table.
 * 
 */
@Entity
@Table(name="\"alert_updates_processed\"")
@NamedQueries({
@NamedQuery(name="AlertUpdatesProcessed.findAll", query="SELECT a FROM AlertUpdatesProcessed a"),
@NamedQuery(name="AlertUpdatesProcessed.findWithUpdateId", query="SELECT a FROM AlertUpdatesProcessed a WHERE a.updateId = :updateId AND a.tripDate=:tripDate")
,@NamedQuery(name="AlertUpdatesProcessed.deleteAll",query="DELETE FROM AlertUpdatesProcessed")
})
public class AlertUpdatesProcessed implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id


@GeneratedValue(generator="sqlite")
@TableGenerator(name="sqlite", table="sqlite_sequence",
    pkColumnName="name", valueColumnName="seq",
    pkColumnValue="alert_updates_processed")
	@Column(name="\"processed_id\"")
	private int processedId;

	@Column(name="\"trip_date\"")
	private String tripDate;

	@Column(name="\"update_id\"")
	private String updateId;

	public AlertUpdatesProcessed() {
	}

	public int getProcessedId() {
		return this.processedId;
	}

	public void setProcessedId(int processedId) {
		this.processedId = processedId;
	}

	public String getTripDate() {
		return this.tripDate;
	}

	public void setTripDate(String tripDate) {
		this.tripDate = tripDate;
	}

	public String getUpdateId() {
		return this.updateId;
	}

	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}

}