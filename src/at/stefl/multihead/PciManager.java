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

	private final Map<PciPath, PciDevice> devices;

	public PciManager() {
		this.devices = new HashMap<>();
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
		DeviceDescriptor deviceDescriptor = new DeviceDescriptor(vendor, device);

		return new PciDevice(pciPath, deviceDescriptor);
	}

}
