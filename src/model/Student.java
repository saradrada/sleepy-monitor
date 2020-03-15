package model;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Student extends Thread {

	public final static int CHAIRS = 3;

	/**
	 * Id con el que se identifica el estudiante
	 */
	private int id;
	/**
	 * Semáforos utilizados para simular al monitor y la sala de espera
	 */
	private Semaphore sMonitor, sWaitingRoom;
	/**
	 * Número random utilizado para darle tiempo a los procesos
	 */
	private Random random;

	/**
	 * Constructor de la clase
	 * 
	 * @param id,           identificador del estudiante
	 * @param sMonitor,     semáforo del monitor
	 * @param sWaitingRoom, semáforo de la sala de espera
	 * @param random,       número random para dar tiempo a los procesos
	 */
	public Student(int id, Semaphore sMonitor, Semaphore sWaitingRoom, Random random) {
		this.id = id;
		this.sMonitor = sMonitor;
		this.sWaitingRoom = sWaitingRoom;
		this.random = random;
	}

	@Override
	public void run() {
		// El estudiante ha sido creado y se encuentra en la sala de cómputo trabajando
		System.out.println("El estudiante con id " + id + " ha sido creado y se encuentra en la sala de cómputo");

		while (true) {
			try {

				// El hilo duerme entre 30 a 45 segundos, lo que un estudiante se demora demora
				// en pedir la ayuda de un monitor.
				sleep((random.nextInt(16) + 30) * 1000);

				//El estudiante intenta pedir ayuda al monitor.
				System.out.println("Un estudiante va a pedir la ayuda del monitor.");

				// Verifica si el monitor se encuentra ocupado.
				if (sMonitor.availablePermits() == 0) {

					// Si el monitor está ocupado se verifica que haya sillas disponibles en la sala
					// de espera.
					if (sWaitingRoom.availablePermits() == 0) {

						// El monitor se encuentra ocupado y no hay sillas disponibles en sala de
						// espera.
						System.out.println("El monitor está ocupado y no hay sillas en la sala de espera. "
								+ "El estudiante con id " + id
								+ " se devuelve a la sala de cómputo y pedirá ayuda en otro momento");
					} else {
						// El estudiante se sienta en la sala de espera.
						sWaitingRoom.acquire();

						System.out.println("El monitor está ocupado. El estudiante con id " + id
								+ " toma una silla en la sala de espera");

						// Cantidad de sillas disponibles después de que el estudiante entra a la sala
						// de espera y se sienta.
						System.out.println("Sillas disponibles: " + sWaitingRoom.availablePermits() + "\n");

						// El estudiante espera a que el monitor lo atienda.
						sMonitor.acquire();

						// El estudiante desocupa la silla porque esta siendo atendido por el monintor.
						sWaitingRoom.release();

						System.out.println("El estudiante con id " + id + " es ayudado por el monitor");

						// Sillas disponibles después de que el estudiante entra a la sala del monitor.
						System.out.println("Hay " + sWaitingRoom.availablePermits() + " sillas disponibles" + "\n");

						// El hilo duerme entre 15 a 25 segundos, que es lo que se demora el monitor
						// atendiendo al estudiante.

						int r = (random.nextInt(11) + 15) * 1000;
						sleep(r);

						// El monitor termina de atender al estudiante.
						sMonitor.release();

						System.out.println("El monitor terminó de ayudar al estudiante con id " + id + ". Despues de "
								+ (r / 1000) + " segundos");

					}
				} else {
					// Si el monitor está desocupado, se encuentra dormido.
					System.out.println("El monitor está dormido. El estudiante con id " + id + " lo despierta");

					// El estudiante despierta al monitor para pedir ayuda.
					sMonitor.acquire();

					System.out.println("El monitor ayuda al estudiante con id " + id);

					// El hilo duerme entre 15 a 25 segundos, lo que se demora el monitor en atender
					// al estudiante.

					int r = (random.nextInt(11) + 15) * 1000;
					sleep(r);

					// Una vez el monitor termina de ayudar al estudiante lo libera
					// para que otra persona pueda ser ayudada.
					sMonitor.release();

					System.out.println(
							"El monitor terminó de ayudar a un estudiante despues de " + (r / 1000) + " segundos");
				}

				// El monitor se duerme si terminó de ayudar a un estudiante y no hay nadie en
				// la sala de espera.
				if (sMonitor.availablePermits() == 1 && sWaitingRoom.availablePermits() == CHAIRS) {
					System.out.println("El monitor está durmiendo" + "\n");
				}

			//Atrapa cualquier exception que pueda surgir durante el proceso. 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
