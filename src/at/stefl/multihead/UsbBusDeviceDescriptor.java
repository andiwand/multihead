package at.stefl.multihead;

public class UsbBusDeviceDescriptor {

	public static final int BUS_MIN = 0;
	public static final int DEVICE_MIN = 0;

	private static final String FORMAT = "Bus %03d Device %03d";

	private final int bus;
	private final int device;

	public UsbBusDeviceDescriptor(int bus, int device) {
		if (bus < BUS_MIN)
			throw new IllegalArgumentException("bus not in definition");
		if (device < DEVICE_MIN)
			throw new IllegalArgumentException("device not in definition");

		this.bus = bus;
		this.device = device;
	}

	public int getBus() {
		return this.bus;
	}

	public int getDevice() {
		return this.device;
	}

	@Override
	public String toString() {
		return String.format(FORMAT, this.bus, this.device);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.bus;
		result = prime * result + this.device;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UsbBusDeviceDescriptor other = (UsbBusDeviceDescriptor) obj;
		if (this.bus != other.bus)
			return false;
		if (this.device != other.device)
			return false;
		return true;
	}

}
