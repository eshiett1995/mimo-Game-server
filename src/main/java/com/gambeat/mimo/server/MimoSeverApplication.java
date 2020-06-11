package com.gambeat.mimo.server;

import com.gambeat.mimo.server.model.GambeatSystem;
import com.gambeat.mimo.server.model.User;
import com.gambeat.mimo.server.model.Wallet;
import com.gambeat.mimo.server.repository.GambeatSystemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.List;

@SpringBootApplication
public class MimoSeverApplication implements CommandLineRunner {

    @Autowired
    GambeatSystemRepository gambeatSystemRepository;

	public static void main(String[] args) {
		SpringApplication.run(MimoSeverApplication.class, args);

		new EchoServer();
	}

	@Override
	public void run(String... args) throws Exception {

        List<GambeatSystem> gambeatSystems = gambeatSystemRepository.findAll();

        if(gambeatSystems.isEmpty()){

            User user = new User();
            user.setEmail("");
            user.setFirstName("");
            user.setLastName("");
            user.setUsername("");
            user.setWallet(new Wallet());

        }

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