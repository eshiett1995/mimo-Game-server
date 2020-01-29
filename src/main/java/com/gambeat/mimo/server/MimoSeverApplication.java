package com.gambeat.mimo.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

@SpringBootApplication
public class MimoSeverApplication {

	public static void main(String[] args) {
		SpringApplication.run(MimoSeverApplication.class, args);

		new EchoServer();
	}

}


class EchoServer extends Thread {
     //crisp for me
	private DatagramSocket socket;
	private boolean running;
	private byte[] buf = new byte[256];

	public EchoServer() {
		try {
			socket = new DatagramSocket(4445);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		running = true;

		while (running) {
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}

			InetAddress address = packet.getAddress();
			int port = packet.getPort();
			packet = new DatagramPacket(buf, buf.length, address, port);
			String received = new String(packet.getData(), 0, packet.getLength());

			if (received.equals("end")) {
				running = false;
				continue;
			}
			try {
				socket.send(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		socket.close();
	}
}