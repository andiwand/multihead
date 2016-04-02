package at.stefl.multihead;

public class PciDevice {

	private final PciPath pciPath;
	private final DeviceDescriptor descriptor;
	private final DeviceDescriptor subsystemDescriptor;
	private final String name;

	public PciDevice(PciPath pciPath, DeviceDescriptor descriptor, DeviceDescriptor subsystemDescriptor, String name) {
		this.pciPath = pciPath;
		this.descriptor = descriptor;
		this.subsystemDescriptor = subsystemDescriptor;
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

	public String getName() {
		return name;
	}

}
