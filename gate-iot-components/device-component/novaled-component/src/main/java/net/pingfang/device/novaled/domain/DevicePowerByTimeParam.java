package net.pingfang.device.novaled.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DevicePowerByTimeParam {
	private Date start;
	private Date end;

	public DevicePowerByTimeParam(Date start, Date end) {
		this.start = start;
		this.end = end;
	}

	public DevicePowerByTimeParam() {
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	@Override
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		return "DevicePowerByTimeParam [start=" + sdf.format(start) + ", end=" + sdf.format(end) + "]";
	}

}
