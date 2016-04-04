package at.stefl.multihead;

public class PciDevice {

	private final PciPath pciPath;
	private final DeviceDescriptor descriptor;
	private final DeviceDescriptor subsystemDescriptor;
	private final DeviceClass deviceClass;
	private final String name;

	public PciDevice(PciPath pciPath, DeviceDescriptor descriptor, DeviceDescriptor subsystemDescriptor,
			DeviceClass deviceClass, String name) {
		this.pciPath = pciPath;
		this.descriptor = descriptor;
		this.subsystemDescriptor = subsystemDescriptor;
		this.deviceClass = deviceClass;
		this.name = name;
	}

	public PciPath getPciPath() {
		return pciPath;
	}

	public DeviceDescriptor getDescriptor() {
		return descriptor;
	}

	public DeviceDescriptor getSubsystemDescriptor() {
		return subsystemDescriptor;
	}

	public DeviceClass getDeviceClass() {
		return deviceClass;
	}

	public String getName() {
		return name;
	}

}
