package model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * 
 * @author Sara Ortiz Drada
 * @author Daniela Llano
 *
 */
public class Main {

	public static void main(String[] args) {

		try {

			// Semáforo que simula al monitor. Sólo con 1 permiso porque atiene un estuiante
			// a la vez.
			Semaphore sMonitor = new Semaphore(1, true);

			// Semáforo que simula la sala de espera. Con 3 permisos porque tiene 3 sillas.
			Semaphore sWaitingRoom = new Semaphore(Student.CHAIRS, true);

			// Número random para darle tiempo a los procesos.
			Random random = new Random(257L);

			// Se pide al usuario que ingrese la cantidad de estudiantes
			System.out.println("Ingrese el número de estudiantes");

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			// Flag que se utiliza para seguir pidiendo la cantidad al usuario, si ha
			// ingresado un valor inválido.
			boolean flag = false;

			// Cantidad de estudiantes
			int nStudents = 0;
			while (!flag) {
				try {
					nStudents = Integer.parseInt(br.readLine());
					flag = true;
				} catch (Exception e) {
					System.out.println("Por favor ingrese una cantidad válida de estudiantes");
				}
			}

			// Se inicializa la lista de estudiantes
			ArrayList<Student> students = new ArrayList<Student>();

			// Se crean los estudiantes y se inicializa un hilo por estudiante
			for (int i = 0; i < nStudents; i++) {
				Student student = new Student(i, sMonitor, sWaitingRoom, random);
				students.add(student);
				students.get(i).start();

				System.out.println("El proceso ha iniciado...");
			}

			// Atrapa cualquier exception que pueda surgir durante el proceso.
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
