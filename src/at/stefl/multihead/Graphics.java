package at.stefl.multihead;

public class Graphics {

	private final PciPath pciPath;
	private final DeviceDescriptor descriptor;

	public Graphics(PciPath pciPath, DeviceDescriptor descriptor) {
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
