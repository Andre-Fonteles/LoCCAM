package br.ufc.great.syssu.test;

import java.io.IOException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import br.ufc.great.syssu.R;
import br.ufc.great.syssu.base.Pattern;
import br.ufc.great.syssu.base.Tuple;
import br.ufc.great.syssu.base.TupleField;
import br.ufc.great.syssu.base.TupleSpaceException;
import br.ufc.great.syssu.base.TupleSpaceSecurityException;
import br.ufc.great.syssu.base.interfaces.IDomain;
import br.ufc.great.syssu.base.interfaces.IFilter;
import br.ufc.great.syssu.base.interfaces.ILocalDomain;
import br.ufc.great.syssu.ubibroker.LocalUbiBroker;
import br.ufc.great.syssu.ubibroker.UbiBroker;
import br.ufc.great.syssu.ubicentre.CustomLogFormatter;
import br.ufc.great.syssu.ubicentre.UbiCentreProcess;

public class SysSUTester extends Activity {

	private static int port = 9091;
	private static String logfile;
	private static TextView txtviewTupleSpace;
	private static UbiBroker remoteBroker;
	private static LocalUbiBroker localBroker;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.syssu_mobile);

		txtviewTupleSpace = (TextView) findViewById(R.id.textView_main);
		txtviewTupleSpace.setMovementMethod(new ScrollingMovementMethod());

		Logger logger = Logger.getLogger("UbiCentre");
		logger.addHandler(new StreamHandler());

		if (logfile != null) {
			try {
				FileHandler fileHandler = new FileHandler(logfile, true);
				fileHandler.setFormatter(new CustomLogFormatter());
				logger.addHandler(fileHandler);
			} catch (Exception ex) {
				logger.severe("Error in starting UbiCentre. Invalid log file.");
				System.exit(1);
			}
		}

		try {
			Thread t = new Thread(new UbiCentreProcess(port), "UbiCentre Process");
			t.start();
			logger.info("UbiCentre started and listening in port " + port + ".");
			//t.join();
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Error in starting UbiCentre.", ex);
			System.exit(1);
		}


		try {
			// Create remote broker
			remoteBroker = UbiBroker.createUbibroker("localhost", port, 9092);			
			// Create local broker
			localBroker = LocalUbiBroker.createUbibroker();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	


//		testarPutRead();
//		testarPutTake();
//		testarDomain();
		testarReaction();	
	}

	public static void testarPutRead() {
		try {
			txtviewTupleSpace.append("\n -- testarPutRead -- ");
			System.out.println("\n -- testarPutRead -- \n");
			// Create tuples
			Tuple tuple1 = (Tuple) new Tuple().addField("user", "a1");
			Tuple tuple2 = (Tuple) new Tuple().addField("user", "b1");

			// Get a domain (tuple space subset) from remote broker
			IDomain remoteDomain = remoteBroker.getDomain("great");

			// Remote Tuple insert
			remoteDomain.put(tuple1, null);
			System.out.println("(Remote) Put tupla -> " + tuple1.getField(0).getValue());

			// Get a domain (tuple space subset) from local broker
			IDomain	localDomain = localBroker.getDomain("great");

			// Local Tuple insert
			System.out.println("(Local) Put tupla -> " + tuple2.getField(0).getValue());
			localDomain.put(tuple2, null);

			// Read Local Tuples	
			Pattern pattern = (Pattern) new Pattern().addField("user", "?string");
			List<Tuple> tuples = localBroker.getDomain("great").read(
					pattern,
					"", //function filter(tuple) {return (tuple.user.indexOf('a') !=-1)}
					"");
			System.out.println("Qtd de tuplas lidas: " + tuples.size());

			// Writes tuples in tupleSpace EditText
			printTuples(tuples, "Read Tuplas");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void testarPutTake() {
		try {
			txtviewTupleSpace.append("\n -- testarPutTake -- \n");
			System.out.println("\n -- testarPutTake -- \n");
			Tuple tuple1 = (Tuple)new Tuple().addField("user", "BN").addField("location", "RECIFE");
			Tuple tuple2 = (Tuple) new Tuple().addField("user", "NB").addField("location", "MANAUS");

			IDomain localDomain = localBroker.getDomain("brasil");

			localDomain.put(tuple1, null);
			localDomain.put(tuple2, null);

			Pattern pattern = (Pattern)new Pattern().addField("?", "?").addField("location", "?string");

			List<Tuple> tuples = localBroker.getDomain("brasil").take( 
					pattern,
					"function filter(tuple) {return (tuple.location.indexOf('REC') >= 0)}", //function filter(tuple) {return (tuple.location.indexOf('REC') !=-1)}
					"");
			
//			List<Tuple> tuples = ((ILocalDomain)localBroker.getDomain("brasil")).take(
//					pattern,
//					new IFilter() {
//						
//						@Override
//						public boolean filter(Tuple tuple) {
//							for (int i = 0; i < tuple.size(); i++) {
//								if (tuple.getField(i).getName().equals("location")) {
//
//									String value = (String)tuple.getField(i).getValue();
//
//									if(value.indexOf("REC") >= 0) {
//										System.out.println("ENTROU EBAAA");
//										
//										return true;
//										
//									}
//								}
//							}
//							
//							return false;
//						}
//					},
//					"");

			System.out.println("Qtd de tuplas removidas -> " + tuples.size());

			printTuples(tuples, "Take Tuplas ");

			tuples = localBroker.getDomain("brasil").read(pattern,"","");
			System.out.println("Qtd de tuplas remanecentes -> " + tuples.size());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void testarDomain() {
		try {
			txtviewTupleSpace.append("\n -- testarDomain -- \n");
			System.out.println("\n -- testarDomain -- \n");
			// Create tuples
			Tuple tuple1 = (Tuple) new Tuple().addField("user", "user1").addField("age", 20);
			Tuple tuple2 = (Tuple) new Tuple().addField("user", "user2").addField("age", 21);
			Tuple tuple3 = (Tuple) new Tuple().addField("user", "user3").addField("age", 22);


			// Create remote Domain
			IDomain remoteDomain1 = remoteBroker.getDomain("great");
			IDomain remoteDomain2= remoteBroker.getDomain("great.lab1");
			IDomain remoteDomain3 = remoteBroker.getDomain("great.lab2");

			// Tuple insert
			remoteDomain1.put(tuple1, null);
			System.out.println("Put tupla1 -> " + tuple1.getField(0).getValue());
			remoteDomain2.put(tuple2, null);
			System.out.println("Put tupla2 -> " + tuple2.getField(0).getValue());
			remoteDomain3.put(tuple3, null);
			System.out.println("Put tupla3 -> " + tuple3.getField(0).getValue());

			// Read Tuples	
			Pattern pattern = (Pattern) new Pattern().addField("user", "?string");

			List<Tuple> tuplesDomain1 = remoteDomain1.read(pattern,	"",	"");
			System.out.println("Qtd de tuplas D1: " + tuplesDomain1.size());

			List<Tuple> tuplesDomain2 = remoteDomain2.read(pattern,	"",	"");
			System.out.println("Qtd de tuplas D2: " + tuplesDomain2.size());

			List<Tuple> tuplesDomain3 = remoteDomain3.read(pattern,	"",	"");
			System.out.println("Qtd de tuplas D3: " + tuplesDomain3.size());

			printTuples(tuplesDomain1, "great");
			printTuples(tuplesDomain2, "great.lab1");
			printTuples(tuplesDomain3, "great.lab2");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void testarReaction() {
		txtviewTupleSpace.append("\n -- testarReaction -- \n");
		System.out.println("\n -- testarReaction -- \n");
		// Create tuples
		Tuple tuple1 = (Tuple) new Tuple().addField("user", "user1").addField("age", 20);
		Tuple tuple2 = (Tuple) new Tuple().addField("user", "user2").addField("age", 21);
		Tuple tuple3 = (Tuple) new Tuple().addField("user", "user3").addField("age", 22);

		try {
			// Create remote Domain
//			IDomain remoteDomain1 = remoteBroker.getDomain("great");
			IDomain remoteDomain1 = localBroker.getDomain("great");
			//Subscription for listen all "put" events on great domain
			ReactionImplementation reaction1;
			reaction1 = new ReactionImplementation("PUT");
			reaction1.setId(remoteDomain1.subscribe(reaction1, "put", ""));

			//Subscription for listen all "readOne" events on great domain			
			ReactionImplementation reaction2;
			reaction2 = new ReactionImplementation("READ ONE");
			reaction2.setId(remoteDomain1.subscribe(reaction2, "readone", ""));

			//Subscription for listen all "read" events on great domain
			ReactionImplementation reaction3;
			reaction3 = new ReactionImplementation("READ");
			reaction3.setId(remoteDomain1.subscribe(reaction3, "read", ""));

			//Subscription for listen all "take" events on great domain
			ReactionImplementation reaction4;
			reaction4 = new ReactionImplementation("TAKE");
			reaction4.setId(remoteDomain1.subscribe(reaction4, "take", ""));


			//Tuple insert
			System.out.println("(Remote) Put tupla1 -> " + tuple1.getField(0).getValue());
			remoteDomain1.put(tuple1, null);
			System.out.println("(Remote) Put tupla2 -> " + tuple2.getField(0).getValue());	
			remoteDomain1.put(tuple2, null);
			System.out.println("(Remote) Put tupla3 -> " + tuple3.getField(0).getValue());
			remoteDomain1.put(tuple3, null);

			// Define tuple's template (pattern)  to be handled
			Pattern pattern = (Pattern) new Pattern().addField("user", "?string");

			// Read one tuple (random)
			txtviewTupleSpace.append("\n ** Read one tuple ** \n");
			System.out.println("\n ** Read one tuple ** \n");
			Tuple oneTuple = remoteDomain1.readOne(pattern, "", "");

			// Read a set of tuples
			txtviewTupleSpace.append("\n ** Read a set of tuples ** \n");
			System.out.println("\n ** Read a set of tuples ** \n");
			List<Tuple> tuples = remoteDomain1.read(pattern,"",	"");

			// Take a set of tuples
			txtviewTupleSpace.append("\n ** Take a set of tuples ** \n");
			System.out.println("\n ** Take a set of tuples ** \n");
			tuples = remoteDomain1.take(pattern,"",	"");

		} catch (TupleSpaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TupleSpaceSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void printTuples(List<Tuple> tuples, String dsc) {
		// Writes tuples in tupleSpace EditText
		txtviewTupleSpace.append("\n---" + dsc + "---");
		String srtTuple;
		for (Tuple tp : tuples) {
			srtTuple = "{";
			for (TupleField tupleField : tp) {
				srtTuple = srtTuple + "(" + tupleField.getName() +","+ tupleField.getValue() + ")";
			}
			srtTuple = srtTuple + "}";
			txtviewTupleSpace.append("\n" + srtTuple);
			System.out.println("Tuplas: " + srtTuple + "\n");
		}
	}
}