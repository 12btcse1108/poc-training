package com.wipro.uuid;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public final class UUIDGenR {

	/**
	 * Scans MAC addresses for good ones.
	 */
	static class HardwareAddressLookup {

		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			String out = null;
			try {
				Enumeration<NetworkInterface> ifs = NetworkInterface.getNetworkInterfaces();
				if (ifs != null) {
					while (ifs.hasMoreElements()) {
						NetworkInterface iface = ifs.nextElement();
						byte[] hardware = iface.getHardwareAddress();
						if (hardware != null && hardware.length == 6 && hardware[1] != (byte) 0xff) {
							out = Hex.append(new StringBuilder(36), hardware).toString();
							break;
						}
					}
				}
			} catch (SocketException ex) {
				// Ignore it.
			}
			return out;
		}

	}

	/**
	 * The current clock and node value.
	 */
	private static long clockSeqAndNode = 0x8000000000000000L;

	/**
	 * The last time value. Used to remove duplicate UUIDs.
	 */
	private static AtomicLong lastTime = new AtomicLong(Long.MIN_VALUE);

	/**
	 * The cached MAC address.
	 */
	private static String macAddress = null;

	static {

		try {
			Class.forName("java.net.InterfaceAddress");
			macAddress = Class.forName("com.wipro.chatbot.uuid.UUIDGenR$HardwareAddressLookup").newInstance()
					.toString();
		} catch (ExceptionInInitializerError err) {
			// Ignored.
		} catch (ClassNotFoundException ex) {
			// Ignored.
		} catch (LinkageError err) {
			// Ignored.
		} catch (IllegalAccessException ex) {
			// Ignored.
		} catch (InstantiationException ex) {
			// Ignored.
		} catch (SecurityException ex) {
			// Ignored.
		}

		if (macAddress == null) {

			Process p = null;
			BufferedReader in = null;

			try {
				String osname = System.getProperty("os.name", ""), osver = System.getProperty("os.version", "");

				if (osname.startsWith("Windows")) {
					p = Runtime.getRuntime().exec(new String[] { "ipconfig", "/all" }, null);
				}

				// Solaris code must appear before the generic code
				else if (osname.startsWith("Solaris") || osname.startsWith("SunOS")) {
					if (osver.startsWith("5.11")) {
						p = Runtime.getRuntime().exec(new String[] { "dladm", "show-phys", "-m" }, null);
					} else {
						String hostName = getFirstLineOfCommand("uname", "-n");
						if (hostName != null) {
							p = Runtime.getRuntime().exec(new String[] { "/usr/sbin/arp", hostName }, null);
						}
					}
				} else if (new File("/usr/sbin/lanscan").exists()) {
					p = Runtime.getRuntime().exec(new String[] { "/usr/sbin/lanscan" }, null);
				} else if (new File("/sbin/ifconfig").exists()) {
					p = Runtime.getRuntime().exec(new String[] { "/sbin/ifconfig", "-a" }, null);
				}

				if (p != null) {
					in = new BufferedReader(new InputStreamReader(p.getInputStream()), 128);
					String l = null;
					while ((l = in.readLine()) != null) {
						macAddress = MACAddressParser.parse(l);
						if (macAddress != null && Hex.parseShort(macAddress) != 0xff) {
							break;
						}
					}
				}

			} catch (SecurityException ex) {
				// Ignore it.
			} catch (IOException ex) {
				// Ignore it.
			} finally {
				if (p != null) {
					try {
						in.close();
						p.getErrorStream().close();
						p.getOutputStream().close();
						p.destroy();
					} catch (Exception e) {
					}
				}
			}

		}

		if (macAddress != null) {
			clockSeqAndNode |= Hex.parseLong(macAddress);
		} else {
			try {
				byte[] local = InetAddress.getLocalHost().getAddress();
				clockSeqAndNode |= (local[0] << 24) & 0xFF000000L;
				clockSeqAndNode |= (local[1] << 16) & 0xFF0000;
				clockSeqAndNode |= (local[2] << 8) & 0xFF00;
				clockSeqAndNode |= local[3] & 0xFF;
			} catch (UnknownHostException ex) {
				clockSeqAndNode |= (long) (Math.random() * 0x7FFFFFFF);
			}
		}

		// Skip the clock sequence generation process and use random instead.

		clockSeqAndNode |= (long) (Math.random() * 0x3FFF) << 48;

	}

	/**
	 * Creates a new time field from the given timestamp. Note that even
	 * identical values of <code>currentTimeMillis</code> will produce different
	 * time fields.
	 * 
	 * @param currentTimeMillis
	 *            the timestamp
	 * @return a new time value
	 * @see UUID#getTime()
	 */
	public static long createTime(long currentTimeMillis) {

		long time;

		// UTC time

		long timeMillis = (currentTimeMillis * 10000) + 0x01B21DD213814000L;

		while (true) {
			long current = lastTime.get();
			if (timeMillis > current) {
				if (lastTime.compareAndSet(current, timeMillis)) {
					break;
				}
			} else {
				if (lastTime.compareAndSet(current, current + 1)) {
					timeMillis = current + 1;
					break;
				}
			}
		}

		// time low

		time = timeMillis << 32;

		// time mid

		time |= (timeMillis & 0xFFFF00000000L) >> 16;

		// time hi and version

		time |= 0x1000 | ((timeMillis >> 48) & 0x0FFF); // version 1

		return time;

	}

	/**
	 * Returns the current clockSeqAndNode value.
	 * 
	 * @return the clockSeqAndNode value
	 * @see UUID#getClockSeqAndNode()
	 */
	public static long getClockSeqAndNode() {
		return clockSeqAndNode;
	}

	/**
	 * Returns the first line of the shell command.
	 * 
	 * @param commands
	 *            the commands to run
	 * @return the first line of the command
	 * @throws IOException
	 */
	static String getFirstLineOfCommand(String... commands) throws IOException {

		Process p = null;
		BufferedReader reader = null;

		try {
			p = Runtime.getRuntime().exec(commands);
			reader = new BufferedReader(new InputStreamReader(p.getInputStream()), 128);

			return reader.readLine();
		} finally {
			if (p != null) {
				reader.close();
				p.getErrorStream().close();
				p.getOutputStream().close();
				p.destroy();
			}
		}

	}

	/**
	 * Returns the MAC address. Not guaranteed to return anything.
	 * 
	 * @return the MAC address, may be <code>null</code>
	 */
	public static String getMACAddress() {
		return macAddress;
	}

	/**
	 * Generates a new time field. Each time field is unique and larger than the
	 * previously generated time field.
	 * 
	 * @return a new time value
	 * @see UUID#getTime()
	 */
	public static long newTime() {
		return createTime(System.currentTimeMillis());
	}

}
