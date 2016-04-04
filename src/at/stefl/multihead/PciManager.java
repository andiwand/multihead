package at.stefl.multihead;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class PciManager {

	private static final Path DEVICES_PATH = Paths.get("/sys/bus/pci/devices");
	private static final String FILE_VENDOR = "vendor";
	private static final String FILE_DEVICE = "device";
	private static final String FILE_SUBVENDOR = "subsystem_vendor";
	private static final String FILE_SUBDEVICE = "subsystem_device";
	private static final String FILE_CLASS = "class";

	private final Map<PciPath, PciDevice> devices;

	public PciManager() {
		this.devices = new HashMap<>();
	}

	public Map<PciPath, PciDevice> getDevices() {
		return devices;
	}

	public void fetch() throws IOException {
		this.devices.clear();

		DirectoryStream<Path> directoryStream = Files.newDirectoryStream(DEVICES_PATH);
		for (Path path : directoryStream) {
			PciDevice device = this.fetchDevice(path);
			devices.put(device.getPciPath(), device);
		}
	}

	private PciDevice fetchDevice(Path path) throws IOException {
		PciPath pciPath = PciPath.parse(path.getFileName().toString());

		int vendor = Integer.decode(new String(Files.readAllBytes(path.resolve(FILE_VENDOR))).trim());
		int device = Integer.decode(new String(Files.readAllBytes(path.resolve(FILE_DEVICE))).trim());
		DeviceDescriptor descriptor = new DeviceDescriptor(vendor, device);

		vendor = Integer.decode(new String(Files.readAllBytes(path.resolve(FILE_SUBVENDOR))).trim());
		device = Integer.decode(new String(Files.readAllBytes(path.resolve(FILE_SUBDEVICE))).trim());
		DeviceDescriptor subsystemDescriptor = new DeviceDescriptor(vendor, device);

		int deviceClassNum = Integer.decode(new String(Files.readAllBytes(path.resolve(FILE_CLASS))).trim());
		DeviceClass deviceClass = new DeviceClass(deviceClassNum);

		String name = String.join(" ", PciNameService.find(descriptor, subsystemDescriptor));

		return new PciDevice(pciPath, descriptor, subsystemDescriptor, deviceClass, name);
	}

}
