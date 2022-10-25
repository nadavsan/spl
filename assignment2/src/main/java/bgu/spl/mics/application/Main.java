package bgu.spl.mics.application;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

import bgu.spl.mics.Initiated;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */
public class Main {

	public static void main(String[] args) {
		Input input;
		Gson gson = new Gson();

   if (args.length != 2) {
      System.out.println("ERROR: illegal number of parameters");
      return;
   }
		String inp = new String(args[0]);
//		File inp = new File ("C:\\Users\\nadav\\Education\\BGU\\3rd Semester\\SPL\\Assignment2\\SPL211\\SPL211\\src\\main\\java\\bgu\\spl\\mics\\application\\input.json");
		System.out.println(inp);
		try (Reader reader = new FileReader(inp)) {
			input = gson.fromJson(reader, Input.class);
			createGame(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String output = new String(args[1]);
//		String output = "C:\\Users\\nadav\\Education\\BGU\\3rd Semester\\SPL\\Assignment2\\SPL211\\SPL211\\src\\main\\java\\bgu\\spl\\mics\\application\\output.json" ;
		try{
			FileWriter write= new FileWriter(output);
			FileWriter fw = new FileWriter(output);
			gson.toJson(Diary.getInstance(), fw);
			fw.flush();
			fw.close();
		}catch (Exception ex) {}


	}


	public static void createGame(Input input) {
		MessageBusImpl messageBus = MessageBusImpl.getInstance();
		Initiated initiated = Initiated.getInstance(4);

		//Ewoks creation
		int numEwoks = input.getEwoks();
		Ewoks ewoks = Ewoks.getInstance(numEwoks);


		//Array of attacks creation
		Attack[] arr = input.getAttacks();
		for (int i = 0; i < arr.length; i++) {
			List<Integer> serials = arrSort(arr[i]);//sorting every attack serial list
			Attack temp = new Attack(serials,arr[i].getDuration());
			arr[i]=temp;
		}

		// create all MicroServices
		HanSoloMicroservice han = new HanSoloMicroservice();
		C3POMicroservice c3po = new C3POMicroservice();
		LandoMicroservice lando = new LandoMicroservice(input.getLando());
		LeiaMicroservice leia = new LeiaMicroservice(arr);
		R2D2Microservice r2d2 = new R2D2Microservice(input.getR2D2());

		//registering all MicroServices to the messageBus
		messageBus.register(han);
		messageBus.register(c3po);
		messageBus.register(lando);
		messageBus.register(leia);
		messageBus.register(r2d2);

		// creating all threads
		Thread tHanSolo = new Thread(han);
		Thread tC3PO = new Thread(c3po);
		Thread tR2D2 = new Thread(r2d2);
		Thread tLeia = new Thread(leia);
		Thread tLando = new Thread(lando);

		//starting all threads
		tHanSolo.start();
		tC3PO.start();
		tR2D2.start();
		tLando.start();

		//initializing all threads except for Leia
		try{
			initiated.getCountDownLatch().await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//starting Leia after initialization of all other threads
		tLeia.start();

		//waiting for all the threads to finish
		try {
			tHanSolo.join();
			tC3PO.join();
			tR2D2.join();
			tLando.join();
			tLeia.join();
		} catch (InterruptedException ignored) {}

		//unregistering all MicroServices from the messageBus
		messageBus.unregister(han);
		messageBus.unregister(c3po);
		messageBus.unregister(lando);
		messageBus.unregister(leia);
		messageBus.unregister(r2d2);
	}

	public static List<Integer> arrSort(Attack arr) {
		List<Integer> serials = new LinkedList<>();
		// making copy
		for (int i = 0; i < arr.getSerials().size(); i++) {
			serials.add(i,arr.getSerials().get(i));
		}
		// sorting the copy
		for (int i = 0; i < arr.getSerials().size(); i++) {
			int max = i;
			for (int j = 0; j < i; j++) {
				if(serials.get(j)  > serials.get(max))
					max=j;
			}
			int temp = serials.get(i);
			serials.set(i ,serials.get(max));
			serials.set(max ,temp);
		}
		// return the copy
		return serials;
	}
}
