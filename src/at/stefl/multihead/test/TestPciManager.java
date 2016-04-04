package at.stefl.multihead.test;

import java.io.IOException;

import at.stefl.multihead.PciDevice;
import at.stefl.multihead.PciManager;

public class TestPciManager {
	
	public static void main(String[] args) throws IOException {
		PciManager manager = new PciManager();
		manager.fetch();
		
		for (PciDevice device : manager.getDevices().values()) {
			System.out.println(device.getPciPath());
			System.out.println(device.getName());
			System.out.println(device.getDeviceClass());
			System.out.println();
		}
	}
	
}
