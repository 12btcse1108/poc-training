package com.wipro.uuid;

import java.util.Date;
import java.util.UUID;

public class UUIDGen {

	public static UUID getUniqueId() {
		TimestampToUUIDConverter tstuc = new TimestampToUUIDConverter();
		return tstuc.convert(new Date());
	}
}
