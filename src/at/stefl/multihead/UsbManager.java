package at.stefl.multihead;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UsbManager {

	private static final Path DEVICES_PATH = Paths.get("/sys/bus/usb/devices");
	private static final String FILE_VENDOR = "idVendor";
	private static final String FILE_DEVICE = "idProduct";
	private static final String CONTROLLER_PREFIX = "usb";

	public UsbManager() {
	}

	public void fetch() throws IOException {
		DirectoryStream<Path> directoryStream = Files.newDirectoryStream(DEVICES_PATH);
		for (Path path : directoryStream) {
			this.fetchDevice(path);
		}
	}

	private void fetchDevice(Path path) throws IOException {
		String fileName = path.getFileName().toString();
		if (fileName.startsWith(CONTROLLER_PREFIX))
			return;
		System.out.println(fileName);
		UsbPath usbPath = UsbPath.parse(fileName);
		System.out.println(usbPath);

		if (Files.isRegularFile(path.resolve(FILE_VENDOR))) {
			int vendor = Integer.parseInt(new String(Files.readAllBytes(path.resolve(FILE_VENDOR))).trim(), 16);
			int device = Integer.parseInt(new String(Files.readAllBytes(path.resolve(FILE_DEVICE))).trim(), 16);
			DeviceDescriptor deviceDescriptor = new DeviceDescriptor(vendor, device);
			System.out.println(deviceDescriptor);
		}
	}

	public static void main(String[] args) throws IOException {
		UsbManager manager = new UsbManager();
		manager.fetch();
	}

}
