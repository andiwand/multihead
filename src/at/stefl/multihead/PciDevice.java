package at.stefl.multihead;

public class PciDevice {

	private final PciPath pciPath;
	private final DeviceDescriptor descriptor;

	public PciDevice(PciPath pciPath, DeviceDescriptor descriptor) {
		this.pciPath = pciPath;
		this.descriptor = descriptor;
	}

	public PciPath getPciPath() {
		return pciPath;
	}

	public DeviceDescriptor getDescriptor() {
		return descriptor;
	}

}
